package ru.voxp.android.presentation.di

import com.github.asuslennikov.mvvm.api.presentation.ViewModel
import com.github.asuslennikov.mvvm.presentation.ViewModelFactory
import com.github.asuslennikov.mvvm.presentation.ViewModelProvider
import com.github.asuslennikov.mvvm.presentation.di.AndroidXViewModelProvider
import com.github.asuslennikov.mvvm.presentation.di.InjectableViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.voxp.android.presentation.law.card.LawCardViewModel
import ru.voxp.android.presentation.law.card.LawLoaderViewModel
import ru.voxp.android.presentation.law.last.LastLawsViewModel
import javax.inject.Provider

@Module
internal abstract class PresentationModule {

    /*
    @Binds
    @IntoMap
    @ViewModelKey(::class)
    abstract fun binds(instance: ): ViewModel<*>
    */

    @Binds
    @IntoMap
    @ViewModelKey(LastLawsViewModel::class)
    abstract fun bindsLastLawsViewModel(instance: LastLawsViewModel): ViewModel<*>

    @Binds
    @IntoMap
    @ViewModelKey(LawCardViewModel::class)
    abstract fun bindsLawCardViewModel(instance: LawCardViewModel): ViewModel<*>

    @Binds
    @IntoMap
    @ViewModelKey(LawLoaderViewModel::class)
    abstract fun bindsLawLoaderViewModel(instance: LawLoaderViewModel): ViewModel<*>

    @Module
    companion object {

        @JvmStatic
        @Provides
        @PresentationScope
        fun viewModelFactory(instances: @JvmSuppressWildcards Map<Class<out ViewModel<*>>, Provider<ViewModel<*>>>): ViewModelFactory {
            return InjectableViewModelFactory(instances)
        }

        @JvmStatic
        @Provides
        @PresentationScope
        fun viewModelProvider(viewModelFactory: ViewModelFactory): ViewModelProvider {
            return AndroidXViewModelProvider(viewModelFactory)
        }
    }
}
