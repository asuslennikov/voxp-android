package ru.voxp.android.domain.usecase

import io.reactivex.functions.Consumer
import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.UseCaseExecution
import ru.jewelline.mvvm.interfaces.domain.UseCaseInput
import ru.voxp.android.domain.api.model.Law
import ru.voxp.android.domain.api.network.NetworkManager
import ru.voxp.android.domain.api.remote.RemoteRepository
import java.util.*
import javax.inject.Inject

class FetchLastLawsUseCaseOutput : AbstractUseCaseOutput() {
    var connectionAvailable: Boolean = true
        internal set
    var laws: List<Law> = Collections.emptyList()
        internal set
    var total: Long = 0
        internal set
}

data class FetchLastLawsUseCaseInput(
    val start: Int,
    val count: Int
) : UseCaseInput

class FetchLastLawsUseCase @Inject constructor(
    private val networkManager: NetworkManager,
    private val remoteRepository: RemoteRepository
) : AbstractUseCase<FetchLastLawsUseCaseInput, FetchLastLawsUseCaseOutput>() {

    private companion object {
        const val NETWORK_MONITORING_TASK: String = "networkMonitoring"
        const val FETCH_LAST_LAWS_TASK: String = "fetchLastLaws"
    }

    override fun getUseCaseOutput(): FetchLastLawsUseCaseOutput {
        return FetchLastLawsUseCaseOutput()
    }

    override fun doExecute(
        useCaseInput: FetchLastLawsUseCaseInput,
        execution: UseCaseExecution<FetchLastLawsUseCaseOutput>
    ) {
        val networkAvailability = networkManager.isConnectionAvailable()
        execution.notifyProgress(useCaseOutput.apply {
            connectionAvailable = networkAvailability
        })
        if (networkAvailability) {
            fetchLawsFromServer(useCaseInput, execution)
        } else {
            awaitNetworkConnection(useCaseInput, execution)
        }
    }

    private fun awaitNetworkConnection(
        useCaseInput: FetchLastLawsUseCaseInput,
        execution: UseCaseExecution<FetchLastLawsUseCaseOutput>
    ) {
        execution.joinTask(
            NETWORK_MONITORING_TASK,
            networkManager.connectionAvailability()
                .filter { networkAvailability -> networkAvailability }
                .observeOn(useCaseScheduler)
                .take(1)
                .subscribe(Consumer {
                    execution.notifyProgress(useCaseOutput.apply {
                        connectionAvailable = true
                    })
                    execution.cancelTask(NETWORK_MONITORING_TASK)
                    fetchLawsFromServer(useCaseInput, execution)
                }, execution.notifyFailureOnError())
        )
    }

    private fun fetchLawsFromServer(
        useCaseInput: FetchLastLawsUseCaseInput,
        execution: UseCaseExecution<FetchLastLawsUseCaseOutput>
    ) {
        execution.joinTask(
            FETCH_LAST_LAWS_TASK,
            remoteRepository.getLastLaws(useCaseInput.start / useCaseInput.count + 1, useCaseInput.count)
                .subscribe(
                    { response ->
                        execution.notifySuccess(useCaseOutput.apply {
                            laws = response.laws ?: Collections.emptyList()
                            total = response.count ?: laws.size.toLong()
                        })
                        execution.completeExecution(true)
                    },
                    { throwable -> handleFetchLawsFailure(throwable, execution) }
                )
        )
    }

    private fun handleFetchLawsFailure(throwable: Throwable, execution: UseCaseExecution<FetchLastLawsUseCaseOutput>) {
        execution.notifyFailure(useCaseOutput.apply {
            setException(throwable)
        })
    }
}