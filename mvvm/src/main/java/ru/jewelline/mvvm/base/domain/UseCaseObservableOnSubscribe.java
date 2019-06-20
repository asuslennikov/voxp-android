package ru.jewelline.mvvm.base.domain;

import android.util.Log;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import ru.jewelline.mvvm.BuildConfig;
import ru.jewelline.mvvm.interfaces.domain.UseCaseInput;
import ru.jewelline.mvvm.interfaces.domain.UseCaseOutput;

final class UseCaseObservableOnSubscribe<IN extends UseCaseInput,
        OUT extends AbstractUseCaseOutput> implements ObservableOnSubscribe<OUT> {

    private final IN useCaseInput;
    private final AbstractUseCase<IN, OUT> useCase;
    private UseCaseExecution<OUT> execution;

    UseCaseObservableOnSubscribe(IN useCaseInput, AbstractUseCase<IN, OUT> useCase) {
        this.useCaseInput = useCaseInput;
        this.useCase = useCase;
    }

    @Override
    public void subscribe(ObservableEmitter<OUT> emitter) throws Exception {
        execution = new UseCaseExecution<>(useCase, emitter);
        try {
            OUT useCaseOutput = useCase.getUseCaseOutput();
            useCaseOutput.setStatus(UseCaseOutput.Status.IN_PROGRESS);
            execution.notify(useCaseOutput);
            useCase.doExecute(useCaseInput, execution);
        } catch (Throwable t) {
            if (BuildConfig.DEBUG) {
                Log.d("UseCaseInterceptor", "Exception during use case execution: ", t);
            }
            execution.notifyFailure(t);
        }
        execution.completeExecution(false);
    }

    /**
     * Необходимо завершать все дочерние задачи (если они есть) для текущего выполнения сценария, если он был отменен
     */
    void onObservableDisposed() {
        if (execution != null) {
            execution.terminateJoinedTasks();
        }
    }

    /**
     * Необходимо завершать все дочерние задачи (если они есть) для текущего выполнения сценария, если он был отменен
     */
    void onObservableError(Throwable ignored) {
        if (execution != null) {
            execution.terminateJoinedTasks();
        }
    }
}
