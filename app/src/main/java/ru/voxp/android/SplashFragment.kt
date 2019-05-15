package ru.voxp.android

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val ivLogo = view?.findViewById<ImageView>(R.id.splash_fragment_logo)
        val logoDrawable = ivLogo?.drawable
        if (logoDrawable is Animatable) {
            logoDrawable.start()
        }

    }
}
