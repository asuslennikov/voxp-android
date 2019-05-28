package ru.jewelline.mvvm.interfaces.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import ru.jewelline.mvvm.interfaces.domain.UseCase;

/**
 * Интерфейс обработчика события для экрана. Отвечает за логику работы экрана, хранит его состояние отдельным объектом.
 * Также осуществляет взаимодействие с domain слоем приложения (запускает {@link UseCase}).
 * <p></p>
 * <b>Зона ответственности:</b> Управление состоянием экрана и реакция на его события
 * <p></p>
 *
 * @param <STATE> Класс, описывающий состояние данного экрана (см. {@link State})
 */
public interface ViewModel<STATE extends State> {

    /**
     * Метод позволяет зарегестрировать слушателя для получения текущего состояния экрана.
     *
     * @param savedState предыдущее состояние, сохраненное у слушателя.
     *                   Реализация <code>ViewModel</code> самостоятельно решает как именно
     *                   использовать переданный экземпляр
     * @return RxJava имплементацию слушателя
     */
    @NonNull
    Observable<STATE> getState(@Nullable STATE savedState);

    /**
     * Метод позволяет зарегестрировать слушателя для получения эффектов экрана.
     *
     * @return RxJava имплементацию слушателя
     */
    @NonNull
    Observable<Effect> getEffect();
}
