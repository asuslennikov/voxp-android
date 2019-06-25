package ru.voxp.android.domain.usecase

import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.domain.UseCaseExecution
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.SUCCESS
import ru.voxp.android.domain.api.network.NetworkManager
import ru.voxp.android.domain.di.UseCaseScope
import javax.inject.Inject

class NetworkSwitchUseCaseOutput(val connectionAvailable: Boolean) : AbstractUseCaseOutput()

@UseCaseScope
class NetworkSwitchUseCase @Inject constructor(
    private val networkManager: NetworkManager
) : AbstractUseCase<EmptyUseCaseInput, NetworkSwitchUseCaseOutput>() {

    override fun getUseCaseOutput(): NetworkSwitchUseCaseOutput {
        return NetworkSwitchUseCaseOutput(networkManager.isConnectionAvailable())
    }

    override fun getUseCaseScheduler(): Scheduler = Schedulers.computation()

    override fun doExecute(
        useCaseInput: EmptyUseCaseInput,
        execution: UseCaseExecution<NetworkSwitchUseCaseOutput>
    ) {
        val currentAvailability = networkManager.isConnectionAvailable()
        execution.joinTask(networkManager.connectionAvailability()
            .observeOn(useCaseScheduler)
            .filter { availability -> availability != currentAvailability }
            .take(1)
            .subscribe(
                Consumer { availability ->
                    execution.notify(NetworkSwitchUseCaseOutput(availability).apply {
                        status = SUCCESS
                    })
                    execution.completeExecution(true)
                },
                execution.notifyFailureOnError()
            )
        )
    }
}