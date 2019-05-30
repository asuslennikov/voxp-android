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
import io.reactivex.disposables.Disposable
import ru.voxp.android.InitializationStatus
import ru.voxp.android.R
import ru.voxp.android.R.layout
import ru.voxp.android.VoxpApplication

class SplashFragment : Fragment() {

    private var splashStartTime: Long = 0
    private var disposable: Disposable? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        splashStartTime = System.currentTimeMillis()
        return inflater.inflate(layout.splash_fragment, container, false)
    }

    override fun onStart() {
        super.onStart()
        val ivLogo = view?.findViewById<ImageView>(R.id.splash_fragment_logo)
        val logoDrawable = ivLogo?.drawable
        if (logoDrawable is Animatable) {
            logoDrawable.start()
        }
    }

    override fun onResume() {
        super.onResume()
        disposable = (context?.applicationContext as VoxpApplication)?.getInitializationStatus()
            .filter { InitializationStatus.COMPLETE == it }
            .subscribe { navigateFromSplashIfInTime() }
    }

    override fun onPause() {
        super.onPause()
        disposable?.dispose()
    }

    private fun navigateFromSplashIfInTime() {
        val diffTime = System.currentTimeMillis() - splashStartTime;
        val expectedAnimationTime = context?.resources?.getInteger(R.integer.bullhorn_animation_duration) ?: 0
        if (diffTime >= expectedAnimationTime) {
            navigateFromSplash()
        } else {
            view?.postDelayed({ navigateFromSplash() }, expectedAnimationTime - diffTime)
        }
    }

    private fun navigateFromSplash() {
        if (view != null) {
            val ivLogo = view!!.findViewById<ImageView>(R.id.splash_fragment_logo)
            val extras = FragmentNavigatorExtras(ivLogo to "last_laws_fragment_toolbar_icon")
            view!!.findNavController().navigate(R.id.action_splashFragment_to_lastLawsFragment, null, null, extras)
        }
    }
}
