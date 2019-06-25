package ru.voxp.android.presentation.law.last

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.*
import ru.voxp.android.R
import ru.voxp.android.databinding.LastLawsFragmentBinding
import ru.voxp.android.presentation.core.Fragment
import ru.voxp.android.presentation.law.card.LawCardAdapter

class LastLawsFragment : Fragment<LastLawsState, LastLawsViewModel, LastLawsFragmentBinding>(
    R.layout.last_laws_fragment,
    LastLawsViewModel::class.java
) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        sharedElementEnterTransition = TransitionSet()
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())
            .addListener(object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition) {
                    enableChildClippingInHierarchy(binding.lastLawsFragmentToolbarIcon, view)
                }

                override fun onTransitionCancel(transition: Transition) {
                    enableChildClippingInHierarchy(binding.lastLawsFragmentToolbarIcon, view)
                }
            })
            .apply {
                duration = getLong(R.integer.last_law_fragment_shared_element_transition_duration)
            }
        binding.lastLawsFragmentList.layoutManager = LinearLayoutManager(context)
        binding.lastLawsFragmentList.adapter = LawCardAdapter(viewModel.lawCardViewModelRegistry)
        return view
    }

    /**
     * clipChildren отключен в файле разметки  чтобы отрисовать элемент вне пределов родительского элемента во время анимации.
     * После анимации перехода нам это уже не нужно. Решение обсуждается здесь:
     * https://stackoverflow.com/questions/37512891/scene-transition-with-nested-shared-element
     */
    private fun enableChildClippingInHierarchy(child: View, root: View?) {
        var parent = child.parent
        while (parent != null) {
            if (parent is ViewGroup) {
                parent.clipChildren = true
            }
            if (parent != root) {
                parent = parent.parent
            } else {
                parent = null
            }
        }
    }

    override fun render(screenState: LastLawsState) {
        super.render(screenState)
        (binding.lastLawsFragmentList.adapter as LawCardAdapter).setData(screenState.laws)
        changeViewVisibility(screenState.loaderVisible, binding.lastLawsFragmentLoaderContainer)
        changeViewVisibility(
            screenState.errorPanelVisible,
            binding.lastLawsFragmentErrorPanelContainer.errorPanelContainer
        )
        changeViewVisibility(screenState.lawsVisible, binding.lastLawsFragmentList)
    }

    private fun getLong(resId: Int): Long {
        return context?.resources?.getInteger(resId)?.toLong() ?: 0L
    }

    private fun changeViewVisibility(targetVisible: Boolean, targetView: View) {
        if ((if (targetVisible) View.VISIBLE else View.GONE) != targetView.visibility) {
            if (targetVisible) {
                targetView.alpha = 0f
                targetView.visibility = View.VISIBLE
                targetView.animate()
                    .alpha(1f)
                    .setDuration(getLong(R.integer.last_law_fragment_visibility_fade_duration))
                    .start()
            } else {
                targetView.visibility = View.GONE
            }
        }
    }
}
