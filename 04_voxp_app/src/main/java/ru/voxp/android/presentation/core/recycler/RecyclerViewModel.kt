package ru.voxp.android.presentation.core.recycler

import androidx.annotation.CallSuper
import com.github.asuslennikov.mvvm.api.presentation.Effect
import com.github.asuslennikov.mvvm.api.presentation.Screen
import com.github.asuslennikov.mvvm.api.presentation.State
import com.github.asuslennikov.mvvm.api.presentation.ViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class RecyclerViewModel<STATE : State> : androidx.lifecycle.ViewModel(), ViewModel<STATE> {
    private val stateMapping: MutableMap<Any, BehaviorSubject<STATE>> = HashMap()
    private val effectMapping: MutableMap<Any, Subject<Effect>> = HashMap()
    private val compositeDisposable = CompositeDisposable()

    private fun checkScreenHasState(screen: Screen<STATE>) {
        if (screen.savedState == null) {
            throw IllegalArgumentException("Screen MUST have initial state when used with accessor of RecyclerViewModel")
        }
    }

    private fun getScreenStateKey(screen: Screen<STATE>): Any {
        checkScreenHasState(screen)
        // TODO change, state can be replaced by another instance
        return screen.savedState?.hashCode() ?: 0
    }

    override fun getState(screen: Screen<STATE>): Observable<STATE> {
        val key = getScreenStateKey(screen)
        var stream = stateMapping[key]
        if (stream == null) {
            stream = BehaviorSubject.createDefault(screen.savedState)
            stateMapping[key] = stream
            onFirstScreenAttach(screen)
        }
        return stream
    }

    override fun getEffect(screen: Screen<STATE>): Observable<Effect> {
        val key = getScreenStateKey(screen)
        var stream = effectMapping[key]
        if (stream == null) {
            stream = PublishSubject.create()
            effectMapping[key] = stream
        }
        return stream
    }

    /**
     * Метод вызывается когда [ViewHolder] впервые подключается к данной viewModel
     */
    @CallSuper
    protected open fun onFirstScreenAttach(screen: Screen<STATE>) {
        // do nothing by default
    }

    protected fun collectDisposable(disposable: Disposable): Disposable {
        compositeDisposable.add(disposable)
        return disposable
    }

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        for (stateSubject in stateMapping.values) {
            stateSubject.onComplete()
        }
        for (effectSubject in effectMapping.values) {
            effectSubject.onComplete()
        }
        compositeDisposable.clear()
    }

    private fun getStateSubjectByScreen(screen: Screen<STATE>): BehaviorSubject<STATE> {
        checkScreenHasState(screen)
        return stateMapping[getScreenStateKey(screen)]
            ?: throw RuntimeException("Screen is not attached to this viewModel")
    }

    protected fun sendState(screen: Screen<STATE>, newState: STATE) {
        getStateSubjectByScreen(screen).onNext(newState)
    }

    protected fun getCurrentState(screen: Screen<STATE>): STATE {
        return getStateSubjectByScreen(screen).value!!
    }

    private fun getEffectSubjectByScreen(screen: Screen<STATE>): Subject<Effect> {
        checkScreenHasState(screen)
        return effectMapping[getScreenStateKey(screen)]
            ?: throw RuntimeException("Screen is not attached to this viewModel")
    }

    protected fun sendEffect(screen: Screen<STATE>, screenEffect: Effect) {
        getEffectSubjectByScreen(screen).onNext(screenEffect)
    }
}