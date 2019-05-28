package ru.voxp.android.presentation

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.voxp.android.R.layout

class LastLawsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layout.last_laws_fragment, container, false)
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 250L
        }
        return view
    }
}
