package ru.voxp.android.presentation.law.last

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.State
import androidx.transition.*
import ru.voxp.android.R
import ru.voxp.android.databinding.LastLawsFragmentBinding
import ru.voxp.android.presentation.core.Fragment
import ru.voxp.android.presentation.law.card.LawCardAdapter


class LastLawsFragment : Fragment<LastLawsState, LastLawsViewModel, LastLawsFragmentBinding>(
    R.layout.last_laws_fragment,
    LastLawsViewModel::class.java
) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        configureFragmentAnimation(view)
        configureLawsList()
        return view
    }

    private fun configureFragmentAnimation(view: View?) {
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
    }

    private fun configureLawsList() {
        binding.lastLawsFragmentList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: State
            ) {
                val padding = resources.getDimension(R.dimen.element_default_padding).toInt()
                with(outRect) {
                    if (parent.getChildAdapterPosition(view) == 0) {
                        top = padding
                    }
                    left = padding
                    right = padding
                    bottom = padding
                }
            }
        })
        val layoutManager = LinearLayoutManager(context)
        binding.lastLawsFragmentList.layoutManager = layoutManager
        binding.lastLawsFragmentList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            val itemCountBeforeNextPageRequest = 5
            var nextPageTriggered = true
            var previousItemCount = 0

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0) {
                    // Если скроллим вверх или влево-вправо - игнориуем
                    return
                }
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemIndex = layoutManager.findFirstVisibleItemPosition()

                if (nextPageTriggered) {
                    if (totalItemCount > previousItemCount) {
                        previousItemCount = totalItemCount
                        nextPageTriggered = false
                    }
                } else if ((totalItemCount - visibleItemCount) <= (firstVisibleItemIndex + itemCountBeforeNextPageRequest)) {
                    nextPageTriggered = true
                    viewModel.triggerNextPageLoading()
                }
            }
        })
        binding.lastLawsFragmentList.adapter =
            LawCardAdapter(getViewModelProvider().linkWithStore(this))
    }

    /**
     * clipChildren отключен в файле разметки чтобы отрисовать элемент вне пределов родительского элемента во время анимации.
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
        val searchPreviouslyExpanded = savedState?.searchExpanded ?: false
        super.render(screenState)
        (binding.lastLawsFragmentList.adapter as LawCardAdapter).submitList(screenState.laws)
        changeViewVisibility(screenState.loaderVisible, binding.lastLawsFragmentLoaderContainer)
        changeViewVisibility(
            screenState.errorPanelVisible,
            binding.lastLawsFragmentErrorPanel.errorPanelContainer
        )
        changeViewVisibility(screenState.lawsVisible, binding.lastLawsFragmentSwipeRefresh)
        binding.lastLawsFragmentSwipeRefresh.isRefreshing = false
        if (searchPreviouslyExpanded != screenState.searchExpanded) {
            renderToolbarSearchStateSwitch(screenState.searchExpanded)
        }
    }

    private fun getLong(resId: Int): Long {
        return context?.resources?.getInteger(resId)?.toLong() ?: 0L
    }

    private fun changeViewVisibility(targetVisible: Boolean, targetView: View) {
        if ((if (targetVisible) VISIBLE else GONE) != targetView.visibility) {
            if (targetVisible) {
                targetView.alpha = 0f
                targetView.visibility = VISIBLE
                targetView.animate()
                    .alpha(1f)
                    .setDuration(getLong(R.integer.last_law_fragment_visibility_fade_duration))
                    .start()
            } else {
                targetView.visibility = GONE
            }
        }
    }

    private fun renderToolbarSearchStateSwitch(searchExpanded: Boolean) {
        val currentConstraints = ConstraintSet()
        currentConstraints.clone(binding.lastLawsFragmentToolbar)
        val targetConstraints = ConstraintSet()
        targetConstraints.clone(binding.lastLawsFragmentToolbar)
        val transition = AutoTransition().apply {
            duration = getLong(R.integer.last_law_fragment_toolbar_expansion_duration)
        }
        TransitionManager.beginDelayedTransition(binding.lastLawsFragmentToolbar, transition)
        targetConstraints.setVisibility(
            R.id.last_laws_fragment_toolbar_icon,
            if (searchExpanded) GONE else VISIBLE
        )
        targetConstraints.setVisibility(
            R.id.last_laws_fragment_header,
            if (searchExpanded) GONE else VISIBLE
        )
        targetConstraints.setVisibility(
            R.id.last_laws_fragment_toolbar_search,
            if (searchExpanded) VISIBLE else GONE
        )
        if (searchExpanded) {
            binding.lastLawsFragmentToolbarSearch.requestFocus()
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(binding.lastLawsFragmentToolbarSearch, InputMethodManager.SHOW_FORCED)
        } else {
            val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.lastLawsFragmentToolbarSearch.windowToken, 0)
        }
        val searchIconId = if (searchExpanded) R.drawable.ic_search_to_close else R.drawable.ic_close_to_search
        val searchIcon = ResourcesCompat.getDrawable(context!!.resources, searchIconId, null)
        binding.lastLawsFragmentToolbarSearchIcon.setImageDrawable(searchIcon)
        if (searchIcon is Animatable) {
            searchIcon.start()
        }
        targetConstraints.applyTo(binding.lastLawsFragmentToolbar)
    }
}
