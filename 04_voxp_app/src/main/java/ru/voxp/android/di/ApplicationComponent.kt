package ru.voxp.android.di

import dagger.Component

@ApplicationScope
@Component(modules = [ApplicationModule::class])
internal interface ApplicationComponent : ApplicationProvider
// no methods, it's workaround for dagger multi-dependency multi-scope issue
