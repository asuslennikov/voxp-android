package ru.voxp.android.di.data

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides

@Module
internal object ManagerModule {

    @Provides
    @ManagerScope
    fun sharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    }
}
