package ru.voxp.android.domain.usecase

import io.reactivex.functions.Consumer
import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.domain.UseCaseExecution
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.*
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
}

class FetchLastLawsUseCase @Inject constructor(
    private val networkManager: NetworkManager,
    private val remoteRepository: RemoteRepository,
    private val networkGoesOnlineUseCase: NetworkGoesOnlineUseCase
) : AbstractUseCase<EmptyUseCaseInput, FetchLastLawsUseCaseOutput>() {

    private companion object {
        const val NETWORK_MONITORING_TASK: String = "networkMonitoring"
        const val NETWORK_GOES_ONLINE_TASK: String = "networkGoesOnline"
        const val FETCH_LAST_LAWS_TASK: String = "fetchLastLaws"
    }

    override fun getUseCaseOutput(): FetchLastLawsUseCaseOutput {
        return FetchLastLawsUseCaseOutput()
    }

    override fun doExecute(useCaseInput: EmptyUseCaseInput, execution: UseCaseExecution<FetchLastLawsUseCaseOutput>) {
        val networkAvailability = networkManager.isConnectionAvailable()
        execution.notifyProgress(useCaseOutput.apply {
            connectionAvailable = networkAvailability
        })
        if (networkAvailability) {
            fetchLawsFromServer(execution)
        } else {
            awaitNetworkConnection(execution)
        }
    }

    private fun awaitNetworkConnection(execution: UseCaseExecution<FetchLastLawsUseCaseOutput>) {
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
                    fetchLawsFromServer(execution)
                }, execution.notifyFailureOnError())
        )
    }

    /**
     * Если произошла ошибка мы запускаем мониторинг "передергивания" соединения. Когда сеть отключат -
     * отправим нотификацию на показ плашки отсутствия соединения, после восстановления сети -
     * запустим получение законопроектов. Отличие от awaitNetworkConnection в том, что мы ждем когда пользователь сначала
     * отключит, а потом вновь включит сеть.
     */
    private fun awaitNetworkConnectionJuggle(execution: UseCaseExecution<FetchLastLawsUseCaseOutput>) {
        execution.cancelTask(NETWORK_GOES_ONLINE_TASK)
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
                                fetchLawsFromServer(execution)
                            }
                            FAILURE -> {
                                execution.notifyFailure(useCaseOutput.apply {
                                    if (result.exception != null) {
                                        this.setException(result.exception!!)
                                    }
                                })
                                execution.completeExecution(true)
                            }
                        }
                    },
                    execution.notifyFailureOnError()
                )
        )
    }

    private fun fetchLawsFromServer(execution: UseCaseExecution<FetchLastLawsUseCaseOutput>) {
        execution.joinTask(
            FETCH_LAST_LAWS_TASK,
            remoteRepository.getLastLaws()
                .subscribe(
                    { response ->
                        execution.notifySuccess(useCaseOutput.apply {
                            laws = response.laws ?: Collections.emptyList()
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
        awaitNetworkConnectionJuggle(execution)
    }
}