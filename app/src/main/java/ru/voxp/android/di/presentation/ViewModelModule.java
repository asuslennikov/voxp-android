package ru.voxp.android.di.presentation;


import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import ru.jewelline.mvvm.base.di.AndroidXInjectableViewModelFactory;
import ru.jewelline.mvvm.base.presentation.ViewModelFactory;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;
import ru.voxp.android.presentation.LastLawsViewModel;

@Module
abstract class ViewModelModule {

    /*
    @Binds
    @IntoMap
    @ViewModelKey(.class)
    public abstract ViewModel binds( instance);
    */

    @Binds
    @IntoMap
    @ViewModelKey(LastLawsViewModel.class)
    public abstract ViewModel bindsLastLawsViewModel(LastLawsViewModel instance);

    @Binds
    @ViewModelScope
    public abstract ViewModelFactory viewModelFactory(AndroidXInjectableViewModelFactory instance);
}
