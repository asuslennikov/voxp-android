package ru.voxp.android.domain.usecase

import io.reactivex.functions.Consumer
import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.domain.UseCaseExecution
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.*
import ru.voxp.android.domain.api.network.NetworkManager
import javax.inject.Inject

class FetchLawsNetworkAwareUseCase @Inject constructor(
    private val networkManager: NetworkManager,
    private val fetchLawsUseCase: FetchLawsUseCase,
    private val networkGoesOnlineUseCase: NetworkGoesOnlineUseCase
) : AbstractUseCase<FetchLawsUseCaseInput, FetchLawsUseCaseOutput>() {

    private companion object {
        const val NETWORK_MONITORING_TASK: String = "networkMonitoring"
        const val NETWORK_GOES_ONLINE_TASK: String = "networkGoesOnline"
        const val FETCH_LAWS_TASK: String = "fetchLaws"
    }

    override fun getUseCaseOutput(): FetchLawsUseCaseOutput {
        return FetchLawsUseCaseOutput()
    }

    override fun doExecute(
        useCaseInput: FetchLawsUseCaseInput,
        execution: UseCaseExecution<FetchLawsUseCaseOutput>
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
        useCaseInput: FetchLawsUseCaseInput,
        execution: UseCaseExecution<FetchLawsUseCaseOutput>
    ) {
        execution.joinTask(
            NETWORK_MONITORING_TASK,
            networkManager.connectionAvailability()
                .filter { networkAvailability -> networkAvailability }
                .observeOn(useCaseScheduler)
                .take(1)
                .subscribe(
                    Consumer {
                        fetchLawsFromServer(useCaseInput, execution)
                        execution.cancelTask(NETWORK_MONITORING_TASK)
                    },
                    execution.notifyFailureOnError()
                )
        )
    }

    private fun fetchLawsFromServer(
        useCaseInput: FetchLawsUseCaseInput,
        execution: UseCaseExecution<FetchLawsUseCaseOutput>
    ) {
        execution.joinTask(
            FETCH_LAWS_TASK,
            fetchLawsUseCase.execute(useCaseInput)
                .observeOn(useCaseScheduler)
                .subscribe(
                    { result ->
                        when (result.status) {
                            IN_PROGRESS -> execution.notifyProgress(result)
                            SUCCESS -> {
                                execution.notifySuccess(result)
                                execution.completeExecution(true)
                            }
                            FAILURE -> {
                                execution.notifyFailure(result)
                                handleFetchLawsFailure(useCaseInput, execution)
                            }
                        }
                    },
                    { throwable ->
                        execution.notifyFailure(useCaseOutput.apply {
                            exception = throwable
                        })
                        handleFetchLawsFailure(useCaseInput, execution)
                    }
                )
        )
    }

    /**
     * Если произошла ошибка мы запускаем мониторинг "передергивания" соединения. Когда сеть отключат -
     * отправим нотификацию на показ плашки отсутствия соединения, после восстановления сети -
     * запустим получение законопроектов. Отличие в том, что мы ждем когда пользователь сначала
     * отключит, а потом вновь включит сеть.
     */
    private fun handleFetchLawsFailure(
        useCaseInput: FetchLawsUseCaseInput,
        execution: UseCaseExecution<FetchLawsUseCaseOutput>
    ) {
        execution.joinTask(
            NETWORK_GOES_ONLINE_TASK,
            networkGoesOnlineUseCase.execute(EmptyUseCaseInput.getInstance())
                .observeOn(useCaseScheduler)
                .subscribe(
                    Consumer { result ->
                        when (result.status) {
                            IN_PROGRESS -> execution.notifyProgress(useCaseOutput.apply {
                                connectionAvailable = false
                            })
                            SUCCESS -> {
                                execution.notifyProgress(useCaseOutput.apply {
                                    connectionAvailable = true
                                })
                                fetchLawsFromServer(useCaseInput, execution)
                                execution.cancelTask(NETWORK_GOES_ONLINE_TASK)
                            }
                            FAILURE -> {
                                execution.notifyFailure(useCaseOutput.apply {
                                    exception = result.exception
                                })
                                execution.completeExecution(true)
                            }
                        }
                    },
                    execution.notifyFailureOnError()
                )
        )
    }
}