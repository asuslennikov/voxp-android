package ru.jewelline.mvvm.base.presentation;

import androidx.annotation.Nullable;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;

/**
 * Фабрика по созданию и инициализации новых экземпляров {@link ViewModel}
 */
public interface ViewModelFactory {
    /**
     * Создание инстанса viewModel по его классу и его инициализация. Если фабрика не может создать экземпляр для переданного класса, то возвращается {@code null}.
     *
     * @param viewModelClass
     * @return проинициализированный экземпляр объекта viewModel, либо {@code null}
     */
    @Nullable
    <VM extends ViewModel<?>> VM create(Class<VM> viewModelClass);
}
