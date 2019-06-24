package ru.voxp.android.domain.usecase

import io.reactivex.functions.Consumer
import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.jewelline.mvvm.base.domain.UseCaseExecution
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput.Status.IN_PROGRESS
import ru.voxp.android.domain.api.model.Law
import ru.voxp.android.domain.api.network.NetworkManager
import ru.voxp.android.domain.api.remote.RemoteRepository
import java.util.*
import javax.inject.Inject

class GetLastLawsUseCaseOutput : AbstractUseCaseOutput() {
    var connectionAvailable: Boolean = true
    var laws: List<Law> = Collections.emptyList()
}

class GetLastLawsUseCase @Inject constructor(
    private val networkManager: NetworkManager,
    private val remoteRepository: RemoteRepository
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
            getLawsFromServer(execution)
        } else {
            awaitNetworkConnection(execution)
        }
    }

    private fun getLawsFromServer(execution: UseCaseExecution<GetLastLawsUseCaseOutput>) {
        execution.joinTask(
            remoteRepository.getLastLaws()
                .subscribe(Consumer { response ->
                    execution.notify(useCaseOutput.apply {
                        laws = response.laws ?: Collections.emptyList()
                    })
                    execution.completeExecution(true)
                }, execution.notifyFailureOnError())
        )
    }

    private fun awaitNetworkConnection(execution: UseCaseExecution<GetLastLawsUseCaseOutput>) {
        execution.joinTask(
            networkManager.connectionAvailability()
                .filter { it }
                .observeOn(useCaseScheduler)
                .take(1)
                .subscribe(Consumer {
                    execution.notify(useCaseOutput.apply {
                        status = IN_PROGRESS
                    })
                    getLawsFromServer(execution)
                }, execution.notifyFailureOnError())
        )
    }
}