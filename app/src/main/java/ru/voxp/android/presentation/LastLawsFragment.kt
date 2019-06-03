package ru.voxp.android.presentation

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import ru.voxp.android.Fragment
import ru.voxp.android.R
import ru.voxp.android.databinding.LastLawsFragmentBinding

class LastLawsFragment : Fragment<LastLawsState, LastLawsViewModel, LastLawsFragmentBinding>(
    R.layout.last_laws_fragment,
    LastLawsViewModel::class.java
) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 250L
        }
        binding.lastLawsFragmentList.layoutManager = LinearLayoutManager(context)
        binding.lastLawsFragmentList.adapter = LastLawsAdapter()
        return view
    }

    override fun render(screenState: LastLawsState) {
        super.render(screenState)
        (binding.lastLawsFragmentList.adapter as LastLawsAdapter).setItems(screenState.laws)
    }
}
