package ru.jewelline.mvvm.base.presentation;

import androidx.annotation.NonNull;
import ru.jewelline.mvvm.interfaces.presentation.Screen;
import ru.jewelline.mvvm.interfaces.presentation.State;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;

/**
 * Компонент, отвечающий за создание экземпляра {@link ViewModel}, а также за его связываение с жизненным циклом экрана ({@link Screen}).
 */
public interface ViewModelProvider {

    @NonNull
    <STATE extends State, VM extends ViewModel<STATE>> VM getViewModel(@NonNull Screen<STATE> screen, @NonNull Class<VM> viewModelClass);
}
