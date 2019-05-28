package ru.voxp.android;

import android.app.Application;
import ru.jewelline.mvvm.base.presentation.ViewModelFactory;

public class VoxpApplication extends Application {
    protected ViewModelFactory viewModelFactory;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeComponents();
    }

    protected void initializeComponents() {
        ComponentRegistry registry = new ComponentRegistry(this);
        viewModelFactory = registry.getViewModelFactory();
    }

    public ViewModelFactory getViewModelFactory() {
        return viewModelFactory;
    }
}
