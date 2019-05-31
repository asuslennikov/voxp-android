package ru.voxp.android.data

import ru.jewelline.mvvm.base.domain.AbstractUseCase
import ru.jewelline.mvvm.base.domain.AbstractUseCaseOutput
import ru.jewelline.mvvm.base.domain.EmptyUseCaseInput
import java.util.*
import javax.inject.Inject

class GetLastLawsUseCaseOutput : AbstractUseCaseOutput() {
    var laws: List<*>? = null
}

class GetLastLawsUseCase @Inject constructor() : AbstractUseCase<EmptyUseCaseInput, GetLastLawsUseCaseOutput>() {
    override fun getUseCaseOutput(): GetLastLawsUseCaseOutput = GetLastLawsUseCaseOutput()

    override fun doExecute(useCaseInput: EmptyUseCaseInput, communicator: Communicator<GetLastLawsUseCaseOutput>) {
        communicator.notify(GetLastLawsUseCaseOutput().apply {
            laws = Arrays.asList("1", "2")
        })
    }
}