package ru.jewelline.mvvm.base.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import ru.jewelline.mvvm.interfaces.presentation.Effect;
import ru.jewelline.mvvm.interfaces.presentation.Screen;
import ru.jewelline.mvvm.interfaces.presentation.State;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;

/**
 * Класс инкапсулирует в себе логику публикации состояния и эффектов экрана.
 *
 * @param <STATE> конкретный тип состояния экрана
 */
public abstract class AbstractViewModel<STATE extends State> extends androidx.lifecycle.ViewModel implements ViewModel<STATE> {
    private final BehaviorSubject<STATE> statePublisher;
    private final PublishSubject<Effect> eventPublisher;
    private STATE currentState;

    public AbstractViewModel() {
        statePublisher = BehaviorSubject.create();
        eventPublisher = PublishSubject.create();
    }

    /**
     * Метод предоставляет начальное значение состояния экрана ({@link State}).
     *
     * @return начальное состояние экрана
     */
    @NonNull
    protected abstract STATE buildInitialState();

    /**
     * Предоставляет доступ к текущему значению состояния экрана.
     *
     * @return текущее состояние экрана
     */
    @NonNull
    protected final STATE getState() {
        if (currentState == null) {
            currentState = buildInitialState();
        }
        return currentState;
    }

    @NonNull
    @Override
    public Observable<STATE> getState(@Nullable STATE savedState) {
        STATE state = getState();
        if (savedState != null) {
            state = mergeState(state, savedState);
        }
        sendState(state);
        return statePublisher;
    }

    /**
     * Метод объединяет значения из текущего состояния и сохраненного значения.
     * Стандартная реализация всегда возвращает {@code savedState}.
     *
     * @param currentState текущее значение (значение из данного {@code ViewModel})
     * @param savedState   сохраненной значение (внешнее значение)
     * @return результат объединения двух состояний
     */
    @NonNull
    protected STATE mergeState(@NonNull STATE currentState, @NonNull STATE savedState) {
        return savedState;
    }

    /**
     * Метод запоминает текущее состояние экрана ({@link State}) и оповещает подписчиков
     * (как правило {@link Screen}) о его изменении.
     *
     * @param state новое состояние экрана
     */
    protected final void sendState(@Nullable STATE state) {
        if (state != null) {
            currentState = state;
            statePublisher.onNext(currentState);
        }
    }

    @NonNull
    @Override
    public Observable<Effect> getEffect() {
        return eventPublisher;
    }

    /**
     * Метод оповещает подписчиков (как правило {@link Screen})
     * о необходимости применить указанный эффект.
     *
     * @param effect эффект, который необходимо применить к экрану
     */
    protected final void sendEffect(@Nullable Effect effect) {
        if (effect != null) {
            eventPublisher.onNext(effect);
        }
    }
}
