package ru.voxp.android.presentation.core

import androidx.databinding.ViewDataBinding
import com.github.asuslennikov.mvvm.api.presentation.State
import com.github.asuslennikov.mvvm.api.presentation.ViewModel
import com.github.asuslennikov.mvvm.presentation.BoundFragmentScreen
import com.github.asuslennikov.mvvm.presentation.ViewModelProvider
import ru.voxp.android.BR
import ru.voxp.android.VoxpApplication

abstract class Fragment<STATE : State, VM : ViewModel<STATE>, B : ViewDataBinding>(
    private val layoutResourceId: Int,
    private val viewModelClass: Class<VM>
) : BoundFragmentScreen<STATE, VM, B>() {

    protected fun getViewModelProvider(): ViewModelProvider =
        (context!!.applicationContext as VoxpApplication).viewModelProvider

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
