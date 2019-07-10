package ru.voxp.android.domain.usecase

import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.UseCaseExecution
import ru.jewelline.mvvm.interfaces.domain.UseCaseInput
import ru.voxp.android.domain.api.ExceptionType.CONNECTION
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.api.model.Law
import ru.voxp.android.domain.api.network.NetworkManager
import ru.voxp.android.domain.api.remote.RemoteRepository
import java.util.*
import javax.inject.Inject

class FetchLawsUseCaseOutput : AbstractUseCaseOutput() {
    var connectionAvailable: Boolean = true
        internal set
    var laws: List<Law> = Collections.emptyList()
        internal set
    var total: Int = 0
        internal set
}

data class FetchLawsUseCaseInput(
    val start: Int,
    val count: Int
) : UseCaseInput

class FetchLawsUseCase @Inject constructor(
    private val networkManager: NetworkManager,
    private val remoteRepository: RemoteRepository
) : AbstractUseCase<FetchLawsUseCaseInput, FetchLawsUseCaseOutput>() {

    private companion object {
        const val FETCH_LAWS_TASK: String = "fetchLaws"
    }

    override fun getUseCaseOutput(): FetchLawsUseCaseOutput {
        return FetchLawsUseCaseOutput()
    }

    override fun doExecute(useCaseInput: FetchLawsUseCaseInput, execution: UseCaseExecution<FetchLawsUseCaseOutput>) {
        if (networkManager.isConnectionAvailable()) {
            execution.notifyProgress(useCaseOutput.apply {
                connectionAvailable = true
            })
            fetchLawsFromServer(useCaseInput, execution)
        } else {
            execution.notifyFailure(useCaseOutput.apply {
                connectionAvailable = false
                exception = VoxpException(CONNECTION, "No internet connection")
            })
        }
    }

    private fun fetchLawsFromServer(
        useCaseInput: FetchLawsUseCaseInput,
        execution: UseCaseExecution<FetchLawsUseCaseOutput>
    ) {
        execution.joinTask(
            FETCH_LAWS_TASK,
            remoteRepository.getLastLaws(useCaseInput.start / useCaseInput.count + 1, useCaseInput.count)
                .subscribe(
                    { response ->
                        execution.notifySuccess(useCaseOutput.apply {
                            laws = response.laws ?: Collections.emptyList()
                            total = response.count ?: laws.size
                        })
                        execution.completeExecution(true)
                    },
                    { throwable ->
                        execution.notifyFailure(useCaseOutput.apply {
                            setException(throwable)
                        })
                    }
                )
        )
    }
}