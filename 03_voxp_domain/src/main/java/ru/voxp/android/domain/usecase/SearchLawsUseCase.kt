package ru.voxp.android.domain.usecase

import com.github.asuslennikov.mvvm.api.domain.UseCase
import com.github.asuslennikov.mvvm.api.domain.UseCaseInput
import com.github.asuslennikov.mvvm.api.domain.UseCaseOutput
import com.github.asuslennikov.mvvm.domain.AbstractUseCaseOutput
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import ru.voxp.android.domain.api.ExceptionType
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.api.model.Law
import ru.voxp.android.domain.api.model.SearchRequest
import ru.voxp.android.domain.api.network.NetworkManager
import ru.voxp.android.domain.api.remote.RemoteRepository
import ru.voxp.android.domain.di.UseCaseScope
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

data class SearchLawsInput(
    val key: String? = null,
    val name: String? = null,
    val fetchCount: Int = 20
) : UseCaseInput

interface SearchLawsOutput : UseCaseOutput {
    fun getKey(): String
    fun getTotal(): Int
    fun getData(): List<Law>
    fun getStatus(): UseCaseOutput.Status
    fun getException(): Throwable?
}

private class SearchLawsOutputImpl(
    private val key: String,
    internal val disposable: CompositeDisposable
) : AbstractUseCaseOutput(), SearchLawsOutput {
    private var total: Int = 0
    private var data: List<Law> = Collections.emptyList()

    override fun getKey(): String = key

    override fun getTotal(): Int = total

    fun setTotal(value: Int) {
        total = value
    }

    override fun getData(): List<Law> = Collections.unmodifiableList(data)

    fun setData(value: List<Law>) {
        data = value
    }

    public override fun setStatus(status: UseCaseOutput.Status) {
        super.setStatus(status)
    }
}

@UseCaseScope
class SearchLawsUseCase @Inject constructor(
    private val networkManager: NetworkManager,
    private val remoteRepository: RemoteRepository
) : UseCase<SearchLawsInput, SearchLawsOutput> {
    private val searches: MutableMap<String, BehaviorSubject<SearchLawsOutputImpl>> = WeakHashMap()

    private fun generateKey(): String = UUID.randomUUID().toString()

    private fun generateOutput(key: String): SearchLawsOutputImpl {
        return SearchLawsOutputImpl(key, CompositeDisposable()).apply {
            status = UseCaseOutput.Status.IN_PROGRESS
        }
    }

    private fun cloneOutput(output: SearchLawsOutputImpl): SearchLawsOutputImpl {
        return SearchLawsOutputImpl(output.getKey(), output.disposable).apply {
            status = output.status
            exception = output.exception
            setData(output.getData())
            setTotal(output.getTotal())
        }
    }

    private fun cleanByKey(key: String) {
        val subject = searches[key]
        if (subject != null) {
            searches.remove(key)
            subject.value?.disposable?.clear()
        }
    }

    private fun getScheduler(): Scheduler = Schedulers.computation()

    private fun mapToObservable(key: String, subject: Subject<SearchLawsOutputImpl>): Observable<SearchLawsOutput> {
        // todo dispose breaks broadcast logic
        return subject.doOnComplete { cleanByKey(key) }
            .doOnError { cleanByKey(key) }
            .doOnDispose { cleanByKey(key) }
            .subscribeOn(getScheduler())
            .map { it }
    }

    private fun handleFailure(outputSubject: BehaviorSubject<SearchLawsOutputImpl>): Consumer<Throwable> {
        return Consumer { throwable ->
            val output = outputSubject.value!!
            outputSubject.onNext(cloneOutput(output).apply {
                status = UseCaseOutput.Status.FAILURE
                exception = throwable
            })
        }
    }

    override fun execute(input: SearchLawsInput): Observable<SearchLawsOutput> {
        val key: String = input.key ?: generateKey()
        if (input.key != null) {
            val subject = searches[key]
            if (subject != null) {
                return mapToObservable(key, subject)
            }
        }
        val output = generateOutput(key)
        val subject = BehaviorSubject.createDefault(output)
        searches[key] = subject
        doExecute(input, subject)
        return mapToObservable(key, subject)
    }

    fun triggerNextPageLoading(input: SearchLawsInput) {
        if (input.key != null) {
            val subject = searches[input.key]
            if (subject != null && subject.value!!.status != UseCaseOutput.Status.IN_PROGRESS) {
                doExecute(input, subject)
            }
        }
    }

    private fun doExecute(input: SearchLawsInput, outputSubject: BehaviorSubject<SearchLawsOutputImpl>) {
        val networkAvailability = networkManager.isConnectionAvailable()
        if (networkAvailability) {
            fetchLawsFromServer(input, outputSubject)
        } else {
            handleFailure(outputSubject).accept(
                VoxpException(ExceptionType.NO_CONNECTION_AVAILABLE, "Internet connection is not available")
            )
            awaitNetworkConnection(input, outputSubject)
        }
    }

    private fun awaitNetworkConnection(input: SearchLawsInput, outputSubject: BehaviorSubject<SearchLawsOutputImpl>) {
        val output = outputSubject.value!!
        output.disposable.add(
            networkManager.connectionAvailability()
                .filter { networkAvailability -> networkAvailability }
                .observeOn(getScheduler())
                .take(1)
                .subscribe(
                    Consumer { fetchLawsFromServer(input, outputSubject) },
                    handleFailure(outputSubject)
                )
        )
    }

    private fun fetchLawsFromServer(input: SearchLawsInput, outputSubject: BehaviorSubject<SearchLawsOutputImpl>) {
        val output = outputSubject.value!!
        outputSubject.onNext(cloneOutput(output).apply {
            status = UseCaseOutput.Status.IN_PROGRESS
            exception = null
        })
        val start: Int = output.getData().size
        output.disposable.add(
            remoteRepository.getLastLaws(SearchRequest().apply { name = input.name }, start / input.fetchCount + 1, input.fetchCount)
                .observeOn(getScheduler())
                .subscribe(
                    Consumer { response ->
                        outputSubject.onNext(SearchLawsOutputImpl(output.getKey(), output.disposable).apply {
                            status = UseCaseOutput.Status.SUCCESS
                            exception = null
                            val data = ArrayList(output.getData())
                            data.addAll(response.laws ?: Collections.emptyList())
                            setData(data)
                            setTotal(response.count ?: getData().size)
                        })
                    },
                    handleFailure(outputSubject)
                )
        )
    }
}