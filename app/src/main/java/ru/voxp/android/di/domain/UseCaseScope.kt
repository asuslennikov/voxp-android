package ru.voxp.android.di.domain

import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.SOURCE

@Scope
@Retention(SOURCE)
internal annotation class UseCaseScope
