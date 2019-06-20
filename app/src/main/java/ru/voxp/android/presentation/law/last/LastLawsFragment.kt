package ru.voxp.android.presentation.law.last

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = getLong(R.integer.last_law_fragment_shared_element_transition_duration)
        }
        binding.lastLawsFragmentList.layoutManager = LinearLayoutManager(context)
        binding.lastLawsFragmentList.adapter = LawCardAdapter(viewModel.lawCardViewModelRegistry)
        return view
    }

    override fun render(screenState: LastLawsState) {
        super.render(screenState)
        (binding.lastLawsFragmentList.adapter as LawCardAdapter).setData(screenState.laws)
        changeViewVisibility(screenState.lawsVisible, binding.lastLawsFragmentList)
        changeViewVisibility(screenState.loaderVisible, binding.lastLawsFragmentLoaderContainer)
        changeViewVisibility(screenState.noInternetVisible, binding.lastLawsFragmentNoInternetContainer)
    }

    private fun getLong(resId: Int): Long {
        return context?.resources?.getInteger(resId)?.toLong() ?: 0L
    }

    private fun changeViewVisibility(targetVisible: Boolean, targetView: View) {
        val targetVisibilityState = if (targetVisible) View.VISIBLE else View.GONE
        val currentVisibilityState = targetView.visibility
        if (targetVisibilityState != currentVisibilityState) {
            targetView.animate()
                .alpha(if (targetVisible) 0f else 1.0f)
                .setDuration(getLong(R.integer.last_law_fragment_visibility_fade_duration))
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        if (isAdded) {
                            targetView.visibility = targetVisibilityState
                            targetView.alpha = if (targetVisible) 1.0f else 0f
                        }
                    }
                })
                .start()
        }
    }
}
