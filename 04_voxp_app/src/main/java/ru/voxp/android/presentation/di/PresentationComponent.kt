package ru.voxp.android.presentation.di

import dagger.Component
import ru.voxp.android.data.di.ManagerProvider
import ru.voxp.android.di.ApplicationProvider
import ru.voxp.android.domain.di.UseCaseProvider

@PresentationScope
@Component(
    dependencies = [UseCaseProvider::class, ManagerProvider::class, ApplicationProvider::class],
    modules = [PresentationModule::class]
)
internal interface PresentationComponent : PresentationProvider
// no methods, it's workaround for dagger multi-dependency multi-scope issue
