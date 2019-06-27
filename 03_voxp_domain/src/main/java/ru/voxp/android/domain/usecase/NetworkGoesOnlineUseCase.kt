package ru.voxp.android.domain.usecase

import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.domain.UseCaseExecution
import ru.voxp.android.domain.api.network.NetworkManager
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class NetworkGoesOnlineUseCaseOutput : AbstractUseCaseOutput() {
    var networkAvailable: Boolean = true
        internal set
}

class NetworkGoesOnlineUseCase @Inject constructor(
    private val networkManager: NetworkManager
) : AbstractUseCase<EmptyUseCaseInput, NetworkGoesOnlineUseCaseOutput>() {

    private companion object {
        const val NETWORK_MONITORING_TASK: String = "networkMonitoring"
    }

    override fun getUseCaseOutput(): NetworkGoesOnlineUseCaseOutput = NetworkGoesOnlineUseCaseOutput()

    override fun getUseCaseScheduler(): Scheduler = Schedulers.computation()

    override fun doExecute(
        useCaseInput: EmptyUseCaseInput,
        execution: UseCaseExecution<NetworkGoesOnlineUseCaseOutput>
    ) {
        val networkAvailable = AtomicBoolean(networkManager.isConnectionAvailable())
        execution.joinTask(
            NETWORK_MONITORING_TASK,
            networkManager.connectionAvailability()
                .observeOn(useCaseScheduler)
                .subscribe(
                    Consumer { connectionAvailable ->
                        if (!connectionAvailable) {
                            execution.notifyProgress(useCaseOutput.apply {
                                this.networkAvailable = false
                            })
                            networkAvailable.set(false)
                        } else if (networkAvailable.get() != connectionAvailable) {
                            execution.notifySuccess(useCaseOutput.apply {
                                this.networkAvailable = true
                            })
                            execution.completeExecution(true)
                        }
                    },
                    execution.notifyFailureOnError()
                )
        )
    }
}