package ru.voxp.android.presentation.core.recycler

import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.asuslennikov.mvvm.api.presentation.Effect
import com.github.asuslennikov.mvvm.api.presentation.Screen
import com.github.asuslennikov.mvvm.api.presentation.State
import com.github.asuslennikov.mvvm.api.presentation.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import ru.voxp.android.BR

open class BoundViewHolder<STATE : State, VM : ViewModel<STATE>>(itemView: View, private val viewModel: VM) :
    ViewHolder(itemView), Screen<STATE> {
    private companion object {
        const val NO_ACTUAL_ID = 0
    }

    private val binding: ViewDataBinding = DataBindingUtil.bind(itemView)!!
    private val disposable: CompositeDisposable = CompositeDisposable()
    private var screenState: STATE? = null

    init {
        binding.setVariable(getBindingViewModelVariableId(), viewModel)
        val screenVariableId = getBindingScreenVariableId()
        if (screenVariableId != NO_ACTUAL_ID) {
            binding.setVariable(screenVariableId, this)
        }
    }

    fun bind(state: STATE) {
        disposable.clear()
        screenState = state
        disposable.add(viewModel
            .getState(this)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { this.render(it) })
        if (holderSupportsEffects()) {
            disposable.addAll(viewModel
                .getEffect(this)
                .observeOn(AndroidSchedulers.mainThread())
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
    protected open fun getBindingStateVariableId() = BR.state

    /**
     * Возвращает идентификатор переменной биндинга для обработчика экрана. Необходимо для корректной работы
     * метода [ViewDataBinding#setVariable(int, Object)]
     *
     * @return идентификатор переменной биндинга для обработчика экрана
     */
    protected open fun getBindingViewModelVariableId() = BR.viewModel

    /**
     * Возвращает идентификатор переменной биндинга для экрана. Необходимо для корректной работы
     * метода [ViewDataBinding.setVariable]. Может потребоваться когда одна [ViewModel]
     * обрабатывает несколько экранов и требуется передать текущий экран в качестве аргумента для обработчика UI события
     *
     * @return идентификатор переменной биндинга для экрана
     */
    protected open fun getBindingScreenVariableId(): Int {
        return NO_ACTUAL_ID
    }

    /**
     * Метод определяет, есть ли у холдера возможность обрабатывать эффекты. Если данный метод возвращает `true`, то
     * подписка на получение эффектов от модели осуществляться не будет, тем самым можем сэкономить по одному инстансу на каждый холдер.
     *
     * @return `true` если холдер умеет обрабатывать эффекты
     */
    protected open fun holderSupportsEffects(): Boolean = false

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