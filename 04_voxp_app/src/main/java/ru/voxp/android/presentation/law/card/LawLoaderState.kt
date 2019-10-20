package ru.voxp.android.presentation.law.card

import ru.voxp.android.presentation.HasKey
import ru.voxp.android.presentation.error.ErrorPanelState

class LawLoaderState(private val searchRequestKey: String) : ErrorPanelState(), HasKey<String> {
    var showAnimation = false

    override fun getKey(): String = searchRequestKey

    companion object {
        fun loading(searchRequestKey: String): LawLoaderState {
            return LawLoaderState(searchRequestKey).apply {
                showAnimation = true
            }
        }

        fun completed(searchRequestKey: String): LawLoaderState {
            return LawLoaderState(searchRequestKey).apply {
                showAnimation = false
            }
        }

        fun noInternet(searchRequestKey: String): LawLoaderState {
            return noInternet(LawLoaderState(searchRequestKey))
        }

        fun connectionError(searchRequestKey: String): LawLoaderState {
            return connectionError(LawLoaderState(searchRequestKey))
        }

        fun serverError(searchRequestKey: String): LawLoaderState {
            return serverError(LawLoaderState(searchRequestKey))
        }

        fun deviceError(searchRequestKey: String): LawLoaderState {
            return deviceError(LawLoaderState(searchRequestKey))
        }
    }
}