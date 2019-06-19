package ru.voxp.android.presentation.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import ru.jewelline.mvvm.base.presentation.AbstractViewModel
import ru.jewelline.mvvm.interfaces.presentation.Effect
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import ru.voxp.android.InitializationStatus
import ru.voxp.android.R
import ru.voxp.android.VoxpApplication
import java.util.concurrent.TimeUnit.MILLISECONDS

// It's delegate class to avoid using our injecting factory (which can be still in process of initialization)
// by default view-model factory from Androidx Jetpack
class SplashViewModel(application: Application) : AndroidViewModel(application), ViewModel<SplashState> {

    private val delegate: ViewModel<SplashState> =
        RealSplashViewModel(application as VoxpApplication)

    override fun getState(savedState: SplashState?): Observable<SplashState> = delegate.getState(savedState)

    override fun getEffect(): Observable<Effect> = delegate.getEffect()
}

internal class RealSplashViewModel(application: VoxpApplication) : AbstractViewModel<SplashState>() {

    private val disposable: Disposable

    init {
        val animationDurationMillis = application.resources?.getInteger(R.integer.bullhorn_animation_duration) ?: 0
        val initializationObservable = application.getInitializationStatus()
            .filter { InitializationStatus.COMPLETE == it }
        val minAnimationDurationObservable = Observable.timer(animationDurationMillis.toLong(), MILLISECONDS)

        disposable = collectDisposable(Observable.combineLatest(
            initializationObservable,
            minAnimationDurationObservable,
            BiFunction { f: InitializationStatus, s: Long -> f })
            .subscribe { navigateFromSplash() })
    }

    override fun buildInitialState(): SplashState =
        SplashState(true)

    private fun navigateFromSplash() {
        sendState(SplashState(false))
        disposable.dispose()
    }
}