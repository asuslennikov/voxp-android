package ru.jewelline.mvvm.base.presentation;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelStoreOwner;
import ru.jewelline.mvvm.interfaces.presentation.Screen;
import ru.jewelline.mvvm.interfaces.presentation.State;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;

/**
 * Компонент, отвечающий за создание экземпляра {@link ViewModel}, а также за его связываение с жизненным циклом экрана ({@link Screen}).
 */
@FunctionalInterface
public interface ViewModelProvider {

    @NonNull
    <STATE extends State, VM extends ViewModel<STATE>> VM getViewModel(@NonNull ViewModelStoreOwner storeOwner, @NonNull Class<VM> viewModelClass);

    default Linked linkWithStore(@NonNull ViewModelStoreOwner storeOwner) {
        return new Linked() {
            @Override
            public <STATE extends State, VM extends ViewModel<STATE>> VM getViewModel(@NonNull Class<VM> viewModelClass) {
                return ViewModelProvider.this.getViewModel(storeOwner, viewModelClass);
            }
        };
    }

    @FunctionalInterface
    interface Linked {
        <STATE extends State, VM extends ViewModel<STATE>> VM getViewModel(@NonNull Class<VM> viewModelClass);
    }
}
