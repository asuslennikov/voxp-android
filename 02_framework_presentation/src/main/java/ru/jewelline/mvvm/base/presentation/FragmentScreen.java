package ru.jewelline.mvvm.base.presentation;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import ru.jewelline.mvvm.interfaces.presentation.Effect;
import ru.jewelline.mvvm.interfaces.presentation.Screen;
import ru.jewelline.mvvm.interfaces.presentation.State;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;

import java.util.concurrent.TimeUnit;

/**
 * Реализация {@link Screen}, связывающая его с конкретной {@link ViewModel}
 * и основанная на фрагментах ({@link Fragment}).
 *
 * @param <STATE> Класс, описывающий состояние данного экрана (см. {@link State})
 * @param <VM>    Класс, описывающий поведение экрана (см. {@link ViewModel})
 */
public abstract class FragmentScreen<STATE extends State, VM extends ViewModel<STATE>>
        extends Fragment implements Screen<STATE> {

    private VM viewModel;
    private CompositeDisposable disposable;

    /**
     * Метод отвечает за корректное построение обработчика экрана с учетом всех зависимостей.
     *
     * @return инициализированный обработчик экрана
     */
    @NonNull
    protected abstract VM createViewModel();

    /**
     * Возвращает экземпляр ViewModel для данного экрана.
     *
     * @return экземпляр ViewModel
     */
    @NonNull
    protected VM getViewModel() {
        if (viewModel == null) {
            viewModel = createViewModel();
        }
        return viewModel;
    }

    /**
     * Метод возвращает объект типа {@link Scheduler}, который определяет в каком потоке
     * будет выполняться обработка методов {@link #render(State)} и{@link #applyEffect(Effect)}.
     * Может вызываться несколько раз.
     *
     * @return объект, распределяющий выполнение задач по потокам
     */
    @NonNull
    protected Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }

    /**
     * Позволяет сменить стандартный интревал (в миллисекундах) фильтрации UI событий (чтобы избежать бессмысленной
     * реакции на UI событие, которое уже было заменено на другое). Объяснение в картинках можно найти в документации
     * к {@link Observable#throttleLatest(long, TimeUnit, boolean)}
     *
     * @return интервал фильтрации
     */
    protected long getUiThrottleIntervalInMillis() {
        return 100L;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        disposable = new CompositeDisposable();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewModel == null) {
            throw new IllegalStateException("Handler for this screen is not initialized");
        }
        disposable.add(viewModel.getState(null)
                .throttleLatest(getUiThrottleIntervalInMillis(), TimeUnit.MILLISECONDS, true)
                .observeOn(getScheduler())
                .subscribe(this::render));
        disposable.add(viewModel.getEffect()
                .throttleLatest(getUiThrottleIntervalInMillis(), TimeUnit.MILLISECONDS, true)
                .observeOn(getScheduler())
                .subscribe(this::applyEffect));
    }

    @Override
    public void onPause() {
        super.onPause();
        disposable.clear();
    }
}
