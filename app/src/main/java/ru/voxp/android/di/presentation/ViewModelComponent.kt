package ru.voxp.android.di.presentation

import dagger.Component
import ru.voxp.android.di.ApplicationProvider
import ru.voxp.android.di.data.ManagerProvider
import ru.voxp.android.di.domain.UseCaseProvider

@ViewModelScope
@Component(
    dependencies = [UseCaseProvider::class, ManagerProvider::class, ApplicationProvider::class],
    modules = [ViewModelModule::class]
)
internal interface ViewModelComponent : ViewModelProvider
// no methods, it's workaround for dagger multi-dependency multi-scope issue
