package ru.voxp.android.presentation.core

import androidx.databinding.ViewDataBinding
import ru.jewelline.mvvm.base.presentation.BoundActivityScreen
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import ru.voxp.android.BR
import ru.voxp.android.VoxpApplication

abstract class Activity<STATE : State, VM : ViewModel<STATE>, B : ViewDataBinding>(
    private val layoutResourceId: Int,
    private val viewModelClass: Class<VM>
) : BoundActivityScreen<STATE, VM, B>() {


    protected fun getViewModelProvider() = (applicationContext as VoxpApplication).viewModelProvider

    override fun createViewModel(): VM {
        return getViewModelProvider().getViewModel(this, viewModelClass)
    }

    override fun getLayoutResourceId(): Int {
        return layoutResourceId
    }

    override fun getBindingStateVariableId(): Int {
        return BR.state
    }

    override fun getBindingViewModelVariableId(): Int {
        return BR.viewModel
    }
}
