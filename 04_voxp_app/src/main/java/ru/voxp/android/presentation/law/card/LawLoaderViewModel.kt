package ru.voxp.android.presentation.law.card

import com.github.asuslennikov.mvvm.api.domain.UseCaseOutput
import com.github.asuslennikov.mvvm.api.presentation.Screen
import ru.voxp.android.domain.api.ExceptionType
import ru.voxp.android.domain.api.VoxpException
import ru.voxp.android.domain.usecase.SearchLawsInput
import ru.voxp.android.domain.usecase.SearchLawsOutput
import ru.voxp.android.domain.usecase.SearchLawsUseCase
import ru.voxp.android.presentation.core.recycler.RecyclerViewModel
import ru.voxp.android.presentation.error.ErrorPanelViewModel
import javax.inject.Inject

class LawLoaderViewModel @Inject constructor(
    private val searchLawsUseCase: SearchLawsUseCase
) : RecyclerViewModel<LawLoaderState>(), ErrorPanelViewModel {

    private var searchRequestKey: String = ""

    override fun onFirstScreenAttach(screen: Screen<LawLoaderState>) {
        super.onFirstScreenAttach(screen)
        searchRequestKey = screen.savedState!!.key
        triggerNextPageRequest(screen)
    }

    private fun triggerNextPageRequest(screen: Screen<LawLoaderState>) {
        collectDisposable(
            searchLawsUseCase.execute(SearchLawsInput(searchRequestKey))
                .subscribe { result ->
                    when (result.getStatus()) {
                        UseCaseOutput.Status.IN_PROGRESS -> sendState(
                            screen,
                            LawLoaderState.loading(searchRequestKey)
                        )
                        UseCaseOutput.Status.SUCCESS -> sendState(
                            screen,
                            LawLoaderState.completed(searchRequestKey)
                        )
                        UseCaseOutput.Status.FAILURE -> handleSearchFailure(screen, result)
                    }
                })
    }

    private fun handleSearchFailure(screen: Screen<LawLoaderState>, result: SearchLawsOutput) {
        val key = screen.savedState!!.key
        if (result.getException() is VoxpException) {
            sendState(
                screen,
                when ((result.getException() as VoxpException).exceptionType) {
                    ExceptionType.NO_CONNECTION_AVAILABLE -> LawLoaderState.noInternet(key)
                    ExceptionType.CONNECTION -> LawLoaderState.connectionError(key)
                    ExceptionType.SERVER -> LawLoaderState.serverError(key)
                    else -> LawLoaderState.deviceError(key)
                }
            )
        } else {
            sendState(screen, LawLoaderState.deviceError(key))
        }
    }

    override fun errorPanelActionClicked() {
        searchLawsUseCase.triggerNextPageLoading(SearchLawsInput(searchRequestKey))
    }
}