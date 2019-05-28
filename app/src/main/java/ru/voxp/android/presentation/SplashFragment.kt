package ru.voxp.android.presentation

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.voxp.android.R
import ru.voxp.android.R.layout

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout.splash_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val ivLogo = view?.findViewById<ImageView>(R.id.splash_fragment_logo)
        val logoDrawable = ivLogo?.drawable
        if (logoDrawable is Animatable) {
            logoDrawable.start()
        }
        ivLogo?.postDelayed({
            if (isAdded) {
                val extras = FragmentNavigatorExtras(ivLogo to "last_laws_fragment_toolbar_icon")
                ivLogo.findNavController().navigate(R.id.action_splashFragment_to_lastLawsFragment, null, null, extras)
            }
        }, 4000L)
    }
}
