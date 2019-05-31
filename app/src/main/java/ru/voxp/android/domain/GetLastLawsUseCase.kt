package ru.voxp.android.domain

import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.voxp.android.data.api.VoxpManager
import ru.voxp.android.data.api.model.Law
import java.util.*
import javax.inject.Inject

class GetLastLawsUseCaseOutput : AbstractUseCaseOutput() {
    var laws: List<Law>? = null
}

class GetLastLawsUseCase @Inject constructor(private val voxpManager: VoxpManager) :
    AbstractUseCase<EmptyUseCaseInput, GetLastLawsUseCaseOutput>() {

    override fun getUseCaseOutput(): GetLastLawsUseCaseOutput =
        GetLastLawsUseCaseOutput()

    override fun doExecute(useCaseInput: EmptyUseCaseInput, communicator: Communicator<GetLastLawsUseCaseOutput>) {
        communicator.notify(useCaseOutput.apply {
            laws = voxpManager.getLastLaws().execute().body()?.laws ?: Collections.emptyList()
        })
    }
}