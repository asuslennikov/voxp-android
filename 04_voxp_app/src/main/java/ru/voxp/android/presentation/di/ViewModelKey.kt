package ru.voxp.android.presentation.di

import dagger.MapKey
import ru.jewelline.mvvm.interfaces.presentation.ViewModel
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.reflect.KClass

@MapKey
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@Retention(SOURCE)
internal annotation class ViewModelKey(val value: KClass<out ViewModel<*>>)