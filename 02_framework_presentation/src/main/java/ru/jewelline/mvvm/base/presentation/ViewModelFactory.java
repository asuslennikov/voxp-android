package ru.jewelline.mvvm.base.presentation;

import androidx.annotation.NonNull;
import ru.jewelline.mvvm.interfaces.presentation.Screen;
import ru.jewelline.mvvm.interfaces.presentation.State;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;

/**
 * Интерфейс описывает фабрику по созданию {@link ViewModel}.
 */
public interface ViewModelFactory {

    @NonNull
    <STATE extends State, VM extends ViewModel<STATE>> VM getViewModel(@NonNull Screen<STATE> screen, @NonNull Class<VM> viewModelClass);
}