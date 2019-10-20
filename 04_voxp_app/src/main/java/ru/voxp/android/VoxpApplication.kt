package ru.voxp.android

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.github.asuslennikov.mvvm.presentation.ViewModelProvider
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import ru.voxp.android.InitializationStatus.COMPLETE

class VoxpApplication : Application() {
    lateinit var viewModelProvider: ViewModelProvider
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
        viewModelProvider = registry.viewModelProvider
        initializationSubject.onNext(COMPLETE)
    }

    fun getInitializationStatus(): Observable<InitializationStatus> = initializationSubject
}

enum class InitializationStatus {
    NOT_READY,
    COMPLETE
}