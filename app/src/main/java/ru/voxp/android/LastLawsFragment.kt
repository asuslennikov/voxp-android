package ru.voxp.android

import android.os.Bundle
import android.transition.ChangeBounds
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class LastLawsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.last_laws_fragment, container, false)
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 250L
        }
        return view
    }
}
