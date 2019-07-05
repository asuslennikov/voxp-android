package ru.voxp.android.presentation.core.recycler

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.reactivex.disposables.CompositeDisposable
import ru.jewelline.mvvm.interfaces.presentation.Effect
import ru.jewelline.mvvm.interfaces.presentation.Screen
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import ru.voxp.android.BR

open class BoundRecyclerViewHolder<STATE : State, VM : ViewModel<STATE>>(itemView: View) : ViewHolder(itemView),
    Screen<STATE> {
    private val binding: ViewDataBinding? = DataBindingUtil.bind(itemView)
    private val disposable: CompositeDisposable = CompositeDisposable()

    internal fun bindHolder(bindingInformation: Pair<STATE, VM>?) {
        disposable.clear()
        if (bindingInformation != null) {
            binding?.setVariable(getBindingViewModelVariableId(), bindingInformation.second)
            disposable.add(bindingInformation.second
                .getState(bindingInformation.first)
                .subscribe { this.render(it) })
            if (holderSupportsEffects()) {
                disposable.addAll(bindingInformation.second
                    .getEffect()
                    .subscribe { this.applyEffect(it) })
            }
        }
    }

    /**
     * Возвращает идентификатор переменной биндинга для состояния экрана. Необходимо для корректной работы
     * метода [ViewDataBinding#setVariable(int, Object)]
     *
     * @return идентификатор переменной биндинга для состояния экрана
     * @see #render(STATE)
     */
    protected fun getBindingStateVariableId() = BR.state

    /**
     * Возвращает идентификатор переменной биндинга для обработчика экрана. Необходимо для корректной работы
     * метода [ViewDataBinding#setVariable(int, Object)]
     *
     * @return идентификатор переменной биндинга для обработчика экрана
     */
    protected fun getBindingViewModelVariableId() = BR.viewModel

    /**
     * Метод определяет, есть ли у холдера возможность обрабатывать эффекты. Если данный метод возвращает `true`, то
     * подписка на получение эффектов от модели осуществляться не будет, тем самым можем сэкономить по одному инстансу на каждый холдер.
     *
     * @return `true` если холдер умеет обрабатывать эффекты
     */
    protected fun holderSupportsEffects(): Boolean = false

    override fun render(screenState: STATE) {
        binding?.setVariable(getBindingStateVariableId(), screenState)
    }

    override fun applyEffect(screenEffect: Effect) {
        // do nothing by default
    }
}