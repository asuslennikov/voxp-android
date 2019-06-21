package ru.voxp.android.di

import dagger.Component

@UseCaseScope
@Component(dependencies = [], modules = [UseCaseModule::class])
interface UseCaseComponent : UseCaseProvider
// no methods, it's workaround for dagger multi-dependency multi-scope issue
