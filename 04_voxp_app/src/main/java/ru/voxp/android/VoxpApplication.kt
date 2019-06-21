package ru.voxp.android

import android.app.Application
import android.os.Handler
import android.os.Looper
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import ru.jewelline.mvvm.base.presentation.ViewModelFactory
import ru.voxp.android.InitializationStatus.COMPLETE

class VoxpApplication : Application() {
    lateinit var viewModelFactory: ViewModelFactory
    private val initializationSubject: Subject<InitializationStatus> =
        BehaviorSubject.createDefault(InitializationStatus.NOT_READY)

    override fun onCreate() {
        super.onCreate()
        Handler(Looper.getMainLooper()).post {
            initializeComponents()
        }
    }

    protected fun initializeComponents() {
        val registry = ComponentRegistry(this)
        viewModelFactory = registry.viewModelFactory
        initializationSubject.onNext(COMPLETE)
    }

    fun getInitializationStatus(): Observable<InitializationStatus> = initializationSubject
}

enum class InitializationStatus {
    NOT_READY,
    COMPLETE
}