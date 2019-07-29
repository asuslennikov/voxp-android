package ru.jewelline.mvvm.base.di;

import ru.jewelline.mvvm.base.presentation.ViewModelFactory;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;

/**
 * Фабрика по созданию объектов {@link ViewModel} с помощью инжекции зависимостей (dagger 2).
 */
public final class InjectableViewModelFactory implements ViewModelFactory {
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> instances;

    @Inject
    public InjectableViewModelFactory(Map<Class<? extends ViewModel>, Provider<ViewModel>> instances) {
        this.instances = instances;
    }

    @Override
    public <VM extends ViewModel<?>> VM create(Class<VM> viewModelClass) {
        Provider<ViewModel> provider = instances.get(viewModelClass);
        if (provider == null) {
            return null;
        }
        return viewModelClass.cast(provider.get());
    }
}
