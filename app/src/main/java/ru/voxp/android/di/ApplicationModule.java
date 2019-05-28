package ru.voxp.android.di;

import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Context application;

    public ApplicationModule(Context application) {
        this.application = application;
    }

    @Provides
    Context providesContext() {
        return application;
    }
}
