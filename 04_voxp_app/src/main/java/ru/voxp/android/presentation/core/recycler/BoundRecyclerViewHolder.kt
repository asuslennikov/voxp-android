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
    companion object {
        private const val NO_ACTUAL_ID = 0
    }

    private val binding: ViewDataBinding = DataBindingUtil.bind(itemView)!!
    private val disposable: CompositeDisposable = CompositeDisposable()
    private var screenState: STATE? = null

    internal fun bind(state: STATE, viewModel: VM) {
        disposable.clear()
        screenState = state
        val screenVariableId = getBindingScreenVariableId()
        if (NO_ACTUAL_ID != screenVariableId) {
            binding.setVariable(screenVariableId, this)
        }
        binding.setVariable(getBindingViewModelVariableId(), viewModel)
        disposable.add(viewModel
            .getState(this)
            .subscribe { this.render(it) })
        if (holderSupportsEffects()) {
            disposable.addAll(viewModel
                .getEffect(this)
                .subscribe { this.applyEffect(it) })
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
     * Возвращает идентификатор переменной биндинга для экрана. Необходимо для корректной работы
     * метода [ViewDataBinding.setVariable]. Может потребоваться когда одна [ViewModel]
     * обрабатывает несколько экранов и требуется передать текущий экран в качестве аргумента для обработчика UI события
     *
     * @return идентификатор переменной биндинга для экрана
     */
    protected fun getBindingScreenVariableId(): Int {
        return NO_ACTUAL_ID
    }

    /**
     * Метод определяет, есть ли у холдера возможность обрабатывать эффекты. Если данный метод возвращает `true`, то
     * подписка на получение эффектов от модели осуществляться не будет, тем самым можем сэкономить по одному инстансу на каждый холдер.
     *
     * @return `true` если холдер умеет обрабатывать эффекты
     */
    protected fun holderSupportsEffects(): Boolean = false

    override fun getSavedState(): STATE? {
        return screenState
    }

    override fun render(screenState: STATE) {
        binding.setVariable(getBindingStateVariableId(), screenState)
    }

    override fun applyEffect(screenEffect: Effect) {
        // do nothing by default
    }
}