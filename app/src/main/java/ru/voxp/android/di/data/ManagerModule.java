package ru.voxp.android.di.data;

import android.content.Context;
import android.content.SharedPreferences;
import dagger.Module;
import dagger.Provides;

@Module
abstract class ManagerModule {

    @Provides
    @ManagerScope
    static SharedPreferences sharedPreferences(Context context) {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }
}
