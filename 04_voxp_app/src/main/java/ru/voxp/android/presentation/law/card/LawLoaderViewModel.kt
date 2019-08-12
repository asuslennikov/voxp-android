package ru.voxp.android.presentation.law.card

import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput
import ru.jewelline.mvvm.interfaces.presentation.Screen
import ru.voxp.android.domain.usecase.SearchLawsInput
import ru.voxp.android.domain.usecase.SearchLawsUseCase
import ru.voxp.android.presentation.core.recycler.RecyclerViewModel
import javax.inject.Inject

class LawLoaderViewModel @Inject constructor(
    private val searchLawsUseCase: SearchLawsUseCase
) : RecyclerViewModel<LawLoaderState>() {

    override fun onFirstScreenAttach(screen: Screen<LawLoaderState>) {
        super.onFirstScreenAttach(screen)
        val searchRequestKey = screen.savedState!!.key
        collectDisposable(searchLawsUseCase.execute(SearchLawsInput(searchRequestKey))
            .subscribe { result ->
                when (result.getStatus()) {
                    UseCaseOutput.Status.IN_PROGRESS -> null
                    UseCaseOutput.Status.SUCCESS -> null
                    UseCaseOutput.Status.FAILURE -> {
                        sendState(screen, LawLoaderState(searchRequestKey).apply {
                            errorText = result?.getException()?.message
                        })
                    }
                }
            })
    }

}