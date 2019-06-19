package ru.voxp.android.presentation.splash

import android.graphics.drawable.Animatable
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import ru.voxp.android.presentation.core.Fragment
import ru.voxp.android.R
import ru.voxp.android.databinding.SplashFragmentBinding

class SplashFragment : Fragment<SplashState, SplashViewModel, SplashFragmentBinding>(
    R.layout.splash_fragment,
    SplashViewModel::class.java
) {

    override fun createViewModel(): SplashViewModel {
        return ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    override fun render(screenState: SplashState) {
        super.render(screenState)
        if (screenState.animationGoing) {
            val logoDrawable = binding.splashFragmentLogo.drawable
            if (logoDrawable is Animatable) {
                logoDrawable.start()
            }
        } else {
            navigateAway()
        }
    }

    private fun navigateAway() {
        val extras = FragmentNavigatorExtras(binding.splashFragmentLogo to "last_laws_fragment_toolbar_icon")
        view!!.findNavController().navigate(R.id.action_splashFragment_to_lastLawsFragment, null, null, extras)
    }
}
