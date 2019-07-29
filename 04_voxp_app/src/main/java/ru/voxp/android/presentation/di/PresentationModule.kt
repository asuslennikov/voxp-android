package ru.voxp.android.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.jewelline.mvvm.base.di.AndroidXViewModelProvider
import ru.jewelline.mvvm.base.di.InjectableViewModelFactory
import ru.jewelline.mvvm.base.presentation.ViewModelFactory
import ru.jewelline.mvvm.base.presentation.ViewModelProvider
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import ru.voxp.android.presentation.law.card.LawCardViewModel
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
