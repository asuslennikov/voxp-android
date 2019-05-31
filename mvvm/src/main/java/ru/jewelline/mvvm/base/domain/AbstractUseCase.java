package ru.jewelline.mvvm.base.domain;


import android.util.Log;
import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import ru.jewelline.mvvm.BuildConfig;
import ru.jewelline.mvvm.interfaces.domain.UseCase;
import ru.jewelline.mvvm.interfaces.domain.UseCaseInput;
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput;

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
        return Observable.create(new ObservableOnUseCaseSubscribe<>(useCaseInput, this))
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

    protected abstract void doExecute(@NonNull IN useCaseInput, @NonNull Communicator<OUT> communicator);

    private static final class ObservableOnUseCaseSubscribe<IN extends UseCaseInput,
            OUT extends AbstractUseCaseOutput> implements ObservableOnSubscribe<OUT> {

        private final IN useCaseInput;
        private final AbstractUseCase<IN, OUT> useCase;

        private ObservableOnUseCaseSubscribe(IN useCaseInput, AbstractUseCase<IN, OUT> useCase) {
            this.useCaseInput = useCaseInput;
            this.useCase = useCase;
        }

        @Override
        public void subscribe(ObservableEmitter<OUT> emitter) throws Exception {
            Communicator<OUT> communicator = new Communicator<>(useCase, emitter);
            try {
                notifyUseCaseStarted(emitter);
                useCase.doExecute(useCaseInput, communicator);
                if (!communicator.hasSendNotifications()) {
                    communicator.notifySuccess();
                }
            } catch (Throwable t) {
                if (BuildConfig.DEBUG) {
                    Log.d("UseCaseInterceptor", "Exception during use case execution: ", t);
                }
                communicator.notifyFailure(t);
            }
            if (!emitter.isDisposed()) {
                emitter.onComplete();
            }
        }

        private void notifyUseCaseStarted(ObservableEmitter<OUT> emitter) {
            // do not use communicator here or we will not send success result on complete.
            OUT useCaseOutput = useCase.getUseCaseOutput();
            useCaseOutput.setStatus(UseCaseOutput.Status.IN_PROGRESS);
            if (!emitter.isDisposed()) {
                emitter.onNext(useCaseOutput);
            }
        }
    }

    /**
     * Класс осуществляющий взаимодействие между слушателем и конкретным запуском сценария.
     *
     * @param <OUT> тип результата работы (наследник {@link AbstractUseCaseOutput})
     */
    public static final class Communicator<OUT extends AbstractUseCaseOutput> {
        private final AbstractUseCase<?, OUT> useCase;
        private final ObservableEmitter<OUT> emitter;
        private boolean hasSendNotifications = false;

        private Communicator(AbstractUseCase<?, OUT> useCase, ObservableEmitter<OUT> emitter) {
            this.useCase = useCase;
            this.emitter = emitter;
        }

        private OUT getUseCaseOutput() {
            return useCase.getUseCaseOutput();
        }

        boolean hasSendNotifications() {
            return hasSendNotifications;
        }

        /**
         * Метод оповещает слушателей о результате работы сценария в том виде, как он был передан
         *
         * @param useCaseOutput результат работы
         */
        public void notify(@NonNull OUT useCaseOutput) {
            if (!emitter.isDisposed()) {
                emitter.onNext(useCaseOutput);
                hasSendNotifications = true;
            }
        }

        /**
         * Метод оповещает слушателей об успешном завершении работы сценария
         * (устанавливает статус {@link UseCaseOutput.Status#SUCCESS}).
         */
        public void notifySuccess() {
            OUT useCaseOutput = getUseCaseOutput();
            useCaseOutput.setStatus(UseCaseOutput.Status.SUCCESS);
            notify(useCaseOutput);
        }

        /**
         * Метод оповещает слушателей об ошибочном завершении работы сценария
         * (устанавливает статус {@link UseCaseOutput.Status#FAILURE}).
         */
        public void notifyFailure() {
            OUT useCaseOutput = getUseCaseOutput();
            useCaseOutput.setStatus(UseCaseOutput.Status.FAILURE);
            notify(useCaseOutput);
        }

        /**
         * Метод оповещает слушателей об ошибочном завершении работы сценария
         * (устанавливает статус {@link UseCaseOutput.Status#FAILURE})
         * и указывает на конкретную ошибку.
         *
         * @param ex произошедшая ошибка
         */
        public void notifyFailure(@NonNull Throwable ex) {
            OUT useCaseOutput = getUseCaseOutput();
            useCaseOutput.setStatus(UseCaseOutput.Status.FAILURE);
            useCaseOutput.setException(ex);
            notify(useCaseOutput);
        }

        /**
         * Метод позволяет узнать, необходимо ли выполнять задачу. Может применяться для затратных по
         * времени операций, когда получатель результата уже может в нем не нуждаться.
         *
         * @return {@code true} если результат работы не будет никем получен
         */
        public boolean isCancelled() {
            return emitter.isDisposed();
        }
    }
}
