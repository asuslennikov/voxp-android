package ru.voxp.android.domain

import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import ru.voxp.android.data.VoxpApi
import java.util.*
import javax.inject.Inject

class GetLastLawsUseCaseOutput : AbstractUseCaseOutput() {
    var laws: List<*>? = null
}

class GetLastLawsUseCase @Inject constructor(private val voxpApi: VoxpApi) :
    AbstractUseCase<EmptyUseCaseInput, GetLastLawsUseCaseOutput>() {

    override fun getUseCaseOutput(): GetLastLawsUseCaseOutput =
        GetLastLawsUseCaseOutput()

    override fun doExecute(useCaseInput: EmptyUseCaseInput, communicator: Communicator<GetLastLawsUseCaseOutput>) {
        val lastLaws = voxpApi.getLastLaws()
        communicator.notify(useCaseOutput.apply {
            laws = Arrays.asList("1", "2")
        })
    }
}