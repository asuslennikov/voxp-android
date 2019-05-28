package ru.voxp.android.di.domain;

import javax.inject.Scope;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Scope
@Retention(RetentionPolicy.SOURCE)
@interface UseCaseScope {
}
