package ru.voxp.android.presentation.core.recycler

import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import ru.jewelline.mvvm.interfaces.presentation.Effect
import ru.jewelline.mvvm.interfaces.presentation.Screen
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel

abstract class RecyclerViewModel<STATE : State> : androidx.lifecycle.ViewModel(), ViewModel<STATE> {
    private val stateMapping: MutableMap<Any, Subject<STATE>> = HashMap()
    private val effectMapping: MutableMap<Any, Subject<Effect>> = HashMap()

    private fun checkScreenHasState(screen: Screen<STATE>) {
        if (screen.savedState == null) {
            throw IllegalArgumentException("Screen MUST have initial state when used with accessor of RecyclerViewModel")
        }
    }

    private fun getScreenStateKey(screen: Screen<STATE>): Any {
        // TODO change, state can be replaced by another instance
        return screen.savedState?.hashCode() ?: 0
    }

    override fun getState(screen: Screen<STATE>): Observable<STATE> {
        checkScreenHasState(screen)
        val key = getScreenStateKey(screen)
        var stream = stateMapping[key]
        if (stream == null) {
            stream = BehaviorSubject.createDefault(screen.savedState)
            stateMapping[key] = stream
        }
        return stream
    }

    override fun getEffect(screen: Screen<STATE>): Observable<Effect> {
        checkScreenHasState(screen)
        val key = getScreenStateKey(screen)
        var stream = effectMapping[key]
        if (stream == null) {
            stream = PublishSubject.create()
            effectMapping[key] = stream
        }
        return stream
    }
}