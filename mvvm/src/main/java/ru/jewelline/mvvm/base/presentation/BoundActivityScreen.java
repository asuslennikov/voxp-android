package ru.jewelline.mvvm.base.presentation;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import ru.jewelline.mvvm.interfaces.presentation.Effect;
import ru.jewelline.mvvm.interfaces.presentation.Screen;
import ru.jewelline.mvvm.interfaces.presentation.State;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;

/**
 * Реализация {@link Screen}, связывающая его с конкретной {@link ViewModel}
 * и основанная на активити ({@link AppCompatActivity}). Базовая отрисовка экрана
 * происходит с помощью технологии
 * <a href="https://developer.android.com/topic/libraries/data-binding">data-binding</a>.
 *
 * @param <STATE> Класс, описывающий состояние данного экрана (см. {@link State})
 * @param <VM>    Класс, описывающий поведение экрана (см. {@link ViewModel})
 * @param <B>     Класс биндинга
 */
public abstract class BoundActivityScreen<STATE extends State, VM extends ViewModel<STATE>,
        B extends ViewDataBinding> extends ActivityScreen<STATE, VM> {

    private B binding;

    /**
     * Метод возвращает  идентификатор файла разметки.
     *
     * @return идентификатор файла разметки
     */
    @LayoutRes
    protected abstract int getLayoutResourceId();

    /**
     * Возвращает идентификатор переменной биндинга для состояния экрана. Необходимо для корректной работы
     * метода {@link ViewDataBinding#setVariable(int, Object)}
     *
     * @return идентификатор переменной биндинга для состояния экрана
     * @see #render(State)
     */
    protected abstract int getBindingStateVariableId();

    /**
     * Возвращает идентификатор переменной биндинга для обработчика экрана. Необходимо для корректной работы
     * метода {@link ViewDataBinding#setVariable(int, Object)}
     *
     * @return идентификатор переменной биндинга для обработчика экрана
     */
    protected abstract int getBindingViewModelVariableId();

    /**
     * Метод возвращает экземпляр data-binding'a для данного экрана.
     *
     * @return экземпляр data-binding'a
     */
    @NonNull
    protected B getBinding() {
        if (binding == null) {
            throw new IllegalStateException("You called the #getBinding() before #onCreate(Bundle)");
        }
        return binding;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, getLayoutResourceId());
        binding.setVariable(getBindingViewModelVariableId(), getViewModel());
    }

    @Override
    public void render(@NonNull STATE screenState) {
        binding.setVariable(getBindingStateVariableId(), screenState);
    }

    @Override
    public void applyEffect(@NonNull Effect screenEffect) {
        // no effects for this screen
    }
}
