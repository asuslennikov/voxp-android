package ru.voxp.android.presentation.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import ru.jewelline.mvvm.base.di.AndroidXInjectableViewModelFactory
import ru.jewelline.mvvm.base.presentation.ViewModelFactory
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import ru.voxp.android.presentation.law.card.LawCardViewModel
import ru.voxp.android.presentation.law.last.LastLawsViewModel
import javax.inject.Provider

@Module
internal abstract class ViewModelModule {

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
    abstract fun bindsLawsViewModel(instance: LawCardViewModel): ViewModel<*>

    @Module
    companion object {
        @JvmStatic
        @Provides
        @ViewModelScope
        fun viewModelFactory(instances: @JvmSuppressWildcards Map<Class<out ViewModel<*>>, Provider<ViewModel<*>>>): ViewModelFactory {
            return AndroidXInjectableViewModelFactory(instances)
        }
    }
}
