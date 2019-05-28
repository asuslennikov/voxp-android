package ru.voxp.android;

import android.content.Context;
import ru.jewelline.mvvm.base.presentation.ViewModelFactory;
import ru.voxp.android.di.ApplicationModule;
import ru.voxp.android.di.ApplicationProvider;
import ru.voxp.android.di.DaggerApplicationComponent;
import ru.voxp.android.di.data.DaggerManagerComponent;
import ru.voxp.android.di.data.ManagerProvider;
import ru.voxp.android.di.domain.DaggerUseCaseComponent;
import ru.voxp.android.di.domain.UseCaseProvider;
import ru.voxp.android.di.presentation.DaggerViewModelComponent;
import ru.voxp.android.di.presentation.ViewModelProvider;

class ComponentRegistry {
    private ViewModelProvider viewModelProvider;

    ComponentRegistry(Context context) {
        initializeRegistry(context);
    }

    private void initializeRegistry(Context context) {
        ApplicationProvider applicationProvider = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(context))
                .build();
        ManagerProvider managerProvider = DaggerManagerComponent.builder()
                .applicationProvider(applicationProvider)
                .build();
        UseCaseProvider useCaseProvider = DaggerUseCaseComponent.builder()
                .managerProvider(managerProvider)
                .build();
        viewModelProvider = DaggerViewModelComponent.builder()
                .applicationProvider(applicationProvider)
                .managerProvider(managerProvider)
                .useCaseProvider(useCaseProvider)
                .build();
    }

    ViewModelFactory getViewModelFactory() {
        return viewModelProvider.getFactory();
    }
}
