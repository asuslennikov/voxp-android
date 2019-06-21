package ru.voxp.android.domain.di

import dagger.Component

@UseCaseScope
@Component(dependencies = [UseCaseDependenciesProvider::class], modules = [UseCaseModule::class])
interface UseCaseComponent : UseCaseProvider
// no methods, it's workaround for dagger multi-dependency multi-scope issue
