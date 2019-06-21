package ru.jewelline.mvvm.base.domain;

import androidx.annotation.NonNull;
import io.reactivex.ObservableEmitter;
import io.reactivex.disposables.Disposable;
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс осуществляющий взаимодействие между подписчиком и конкретным запуском сценария.
 *
 * @param <OUT> тип результата работы (наследник {@link AbstractUseCaseOutput})
 */
public final class UseCaseExecution<OUT extends AbstractUseCaseOutput> {
    private final AbstractUseCase<?, OUT> useCase;
    private final ObservableEmitter<OUT> emitter;
    private List<Disposable> disposables;

    UseCaseExecution(AbstractUseCase<?, OUT> useCase, ObservableEmitter<OUT> emitter) {
        this.useCase = useCase;
        this.emitter = emitter;
    }

    private OUT getUseCaseOutput() {
        return useCase.getUseCaseOutput();
    }

    /**
     * Метод оповещает слушателей о результате работы сценария в том виде, как он был передан
     *
     * @param useCaseOutput результат работы
     */
    public void notify(@NonNull OUT useCaseOutput) {
        if (!isCancelled()) {
            emitter.onNext(useCaseOutput);
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

    private boolean hasJoinedTasks() {
        if (disposables != null && !disposables.isEmpty()) {
            for (Disposable disposable : disposables) {
                if (!disposable.isDisposed()) {
                    return true;
                }
            }
        }
        return false;
    }

    void terminateJoinedTasks() {
        if (disposables != null && !disposables.isEmpty()) {
            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
        }
    }

    /**
     * Метод позволяет текущему запуску сценария дождаться завершения указанной задачи.
     *
     * @param disposable внутренняя задача, завершения которой необходимо дождаться данному запуску сценария
     */
    public void joinTask(Disposable disposable) {
        if (disposables == null) {
            disposables = new ArrayList<>();
        }
        disposables.add(disposable);
    }

    /**
     * Метод позволяет в подать сигнал о завершении работы текущего запуска сценария. После этого подписчику
     * выполнения данного сценария никакие сообщения не будут доставлены.
     *
     * @param terminateJoinedTasks определяет нужно ли завершить ожидаемые задачи
     */
    public void completeExecution(boolean terminateJoinedTasks) {
        if (!isCancelled() && (!hasJoinedTasks() || terminateJoinedTasks)) {
            emitter.onComplete();
        }
        if (terminateJoinedTasks) {
            terminateJoinedTasks();
        }
    }
}
