package ru.voxp.android.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val application: Context) {

    @Provides
    internal fun providesContext(): Context {
        return application
    }
}
