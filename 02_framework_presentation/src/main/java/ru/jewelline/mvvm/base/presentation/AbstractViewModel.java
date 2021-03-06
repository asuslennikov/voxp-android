package ru.jewelline.mvvm.base.presentation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
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
    private PublishSubject<Effect> effectPublisher;
    private final CompositeDisposable disposable = new CompositeDisposable();
    private STATE currentState;

    public AbstractViewModel() {
        statePublisher = BehaviorSubject.create();
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
    protected final STATE getCurrentState() {
        if (currentState == null) {
            currentState = buildInitialState();
        }
        return currentState;
    }

    @NonNull
    @Override
    public Observable<STATE> getState(@NonNull Screen<STATE> screen) {
        STATE state = mergeState(currentState, screen.getSavedState());
        sendState(state);
        return statePublisher;
    }

    /**
     * Метод объединяет значения из текущего состояния и сохраненного значения.
     * Стандартная реализация возвращает {@code savedState} только если {@code currentState} еще не был инициализирован.
     *
     * @param currentState текущее значение (значение из данного {@code ViewModel})
     * @param savedState   сохраненной значение (внешнее значение)
     * @return результат объединения двух состояний
     */
    @NonNull
    protected STATE mergeState(@Nullable STATE currentState, @Nullable STATE savedState) {
        if (currentState == null && savedState != null) {
            return savedState;
        }
        if (currentState == null) {
            currentState = buildInitialState();
        }
        return currentState;
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
    public Observable<Effect> getEffect(@NonNull Screen<STATE> ignoredScreen) {
        if (effectPublisher == null) {
            effectPublisher = PublishSubject.create();
        }
        return effectPublisher;
    }

    /**
     * Метод оповещает подписчиков (как правило {@link Screen})
     * о необходимости применить указанный эффект.
     *
     * @param effect эффект, который необходимо применить к экрану
     */
    protected final void sendEffect(@Nullable Effect effect) {
        if (effect != null && effectPublisher != null) {
            effectPublisher.onNext(effect);
        }
    }

    /**
     * Метод позволяет автоматически очистить disposable при завершении работы данной viewModel.
     *
     * @param disposable объект подписки на событие
     */
    protected Disposable collectDisposable(Disposable disposable) {
        this.disposable.add(disposable);
        return disposable;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        statePublisher.onComplete();
        if (effectPublisher != null) {
            effectPublisher.onComplete();
        }
        disposable.clear();
    }
}
