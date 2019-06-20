package ru.voxp.android.domain

import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.domain.UseCaseExecution
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.IN_PROGRESS
import ru.voxp.android.data.api.VoxpManager
import ru.voxp.android.data.api.model.Law
import ru.voxp.android.domain.api.network.NetworkManager
import java.util.*
import javax.inject.Inject

class GetLastLawsUseCaseOutput : AbstractUseCaseOutput() {
    var connectionAvailable: Boolean = true
    var laws: List<Law> = Collections.emptyList()
}

class GetLastLawsUseCase @Inject constructor(
    private val networkManager: NetworkManager,
    private val voxpManager: VoxpManager
) :
    AbstractUseCase<EmptyUseCaseInput, GetLastLawsUseCaseOutput>() {

    override fun getUseCaseOutput(): GetLastLawsUseCaseOutput {
        if (!networkManager.isConnectionAvailable()) {
            return GetLastLawsUseCaseOutput().apply {
                connectionAvailable = false
            }
        }
        return GetLastLawsUseCaseOutput()
    }

    override fun doExecute(useCaseInput: EmptyUseCaseInput, execution: UseCaseExecution<GetLastLawsUseCaseOutput>) {
        if (networkManager.isConnectionAvailable()) {
            loadFromServerAndNotify(execution)
        } else {
            execution.joinTask(
                networkManager.connectionAvailability()
                    .filter { it }
                    .observeOn(useCaseScheduler)
                    .take(1)
                    .subscribe {
                        execution.notify(useCaseOutput.apply {
                            status = IN_PROGRESS
                        })
                        loadFromServerAndNotify(execution)
                    }
            )
        }
    }

    private fun loadFromServerAndNotify(execution: UseCaseExecution<GetLastLawsUseCaseOutput>) {
        execution.notify(useCaseOutput.apply {
            laws = voxpManager.getLastLaws().execute().body()?.laws ?: Collections.emptyList()
        })
        execution.completeExecution(true)
    }
}