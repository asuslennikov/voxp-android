package ru.voxp.android.domain.usecase

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.interfaces.domain.UseCase
import ru.jewelline.mvvm.interfaces.domain.UseCaseInput
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput
import ru.voxp.android.domain.api.model.Law
import ru.voxp.android.domain.api.remote.RemoteRepository
import ru.voxp.android.domain.di.UseCaseScope
import java.util.*
import javax.inject.Inject

data class SearchLawsInput(
    val key: String? = null,
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
    private val remoteRepository: RemoteRepository
) : UseCase<SearchLawsInput, SearchLawsOutput> {
    private val searches: MutableMap<String, BehaviorSubject<SearchLawsOutputImpl>> = WeakHashMap()

    private fun generateKey(): String = UUID.randomUUID().toString()

    private fun generateOutput(key: String): SearchLawsOutputImpl {
        return SearchLawsOutputImpl(key, CompositeDisposable()).apply {
            status = UseCaseOutput.Status.IN_PROGRESS
        }
    }

    private fun cleanByKey(key: String) {
        val subject = searches[key]
        if (subject != null) {
            searches.remove(key)
            subject.value?.disposable?.clear()
        }
    }

    private fun mapToObservable(key: String, subject: Subject<SearchLawsOutputImpl>): Observable<SearchLawsOutput> {
        return subject.doOnComplete { cleanByKey(key) }
            .doOnError { cleanByKey(key) }
            .doOnDispose { cleanByKey(key) }
            .map { it }
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
        fetchLawsFromServer(input, subject)
        return mapToObservable(key, subject)
    }

    private fun fetchLawsFromServer(input: SearchLawsInput, outputSubject: BehaviorSubject<SearchLawsOutputImpl>) {
        val output = outputSubject.value!!
        val start: Int = output.getData().size
        output.disposable.add(
            remoteRepository.getLastLaws(start / input.fetchCount + 1, input.fetchCount)
                .subscribe(
                    { response ->
                        outputSubject.onNext(SearchLawsOutputImpl(output.getKey(), output.disposable).apply {
                            status = UseCaseOutput.Status.SUCCESS
                            setData(response.laws ?: Collections.emptyList())
                            setTotal(response.count ?: getData().size)
                        })
                    },
                    { throwable ->
                        outputSubject.onNext(SearchLawsOutputImpl(output.getKey(), output.disposable).apply {
                            status = UseCaseOutput.Status.FAILURE
                            exception = throwable
                        })
                    }
                )
        )
    }
}