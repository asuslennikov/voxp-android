package ru.voxp.android.presentation.core

import androidx.databinding.ViewDataBinding
import ru.jewelline.mvvm.base.presentation.BoundFragmentScreen
import ru.jewelline.mvvm.interfaces.presentation.State
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import ru.voxp.android.BR
import ru.voxp.android.VoxpApplication

abstract class Fragment<STATE : State, VM : ViewModel<STATE>, B : ViewDataBinding>(
    private val layoutResourceId: Int,
    private val viewModelClass: Class<VM>
) : BoundFragmentScreen<STATE, VM, B>() {

    override fun createViewModel(): VM {
        val viewModelFactory = (context!!.applicationContext as VoxpApplication).viewModelFactory
        return viewModelFactory.getViewModel(this, viewModelClass)
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
