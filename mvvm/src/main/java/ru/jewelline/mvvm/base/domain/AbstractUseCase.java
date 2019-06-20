package ru.jewelline.mvvm.base.domain;


import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.jewelline.mvvm.interfaces.domain.UseCase;
import ru.jewelline.mvvm.interfaces.domain.UseCaseInput;

/**
 * Базовая реализация {@link UseCase} с использованием реактивных стримов и LCE шаблона предоставления данных.
 *
 * @param <IN>  Представление входных данных
 * @param <OUT> Представление результата работы
 */
public abstract class AbstractUseCase<IN extends UseCaseInput,
        OUT extends AbstractUseCaseOutput> implements UseCase<IN, OUT> {

    /**
     * Метод возвращает объект типа {@link Scheduler}, который определяет в каком потоке
     * будет выполняться данный сценарий бизнес-логики.
     *
     * @return объект, распределяющий выполнение задач по потокам
     */
    @NonNull
    protected Scheduler getUseCaseScheduler() {
        return Schedulers.io();
    }

    @Override
    public Observable<OUT> execute(@NonNull IN useCaseInput) {
        UseCaseObservableOnSubscribe<IN, OUT> useCaseObservable = new UseCaseObservableOnSubscribe<>(useCaseInput, this);
        return Observable.create(useCaseObservable)
                .doOnDispose(useCaseObservable::onObservableDisposed)
                .doOnError(useCaseObservable::onObservableError)
                .subscribeOn(getUseCaseScheduler());
    }

    /**
     * Метод создает объект для хранения результатов работы бизнес-сценария.
     * Может быть вызван более, чем один раз.
     *
     * @return объект для хранения результатов работы
     */
    @NonNull
    protected abstract OUT getUseCaseOutput();

    protected abstract void doExecute(@NonNull IN useCaseInput, @NonNull UseCaseExecution<OUT> execution);
}
