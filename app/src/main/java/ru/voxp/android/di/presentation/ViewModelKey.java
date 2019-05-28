package ru.voxp.android.di.presentation;

import dagger.MapKey;
import ru.jewelline.mvvm.interfaces.presentation.ViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@MapKey
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
@interface ViewModelKey {
    Class<? extends ViewModel> value();
}
