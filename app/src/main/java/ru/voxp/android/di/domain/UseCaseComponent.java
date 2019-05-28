package ru.voxp.android.di.domain;

import dagger.Component;
import ru.voxp.android.di.data.ManagerProvider;

@UseCaseScope
@Component(dependencies = {ManagerProvider.class}, modules = UseCaseModule.class)
interface UseCaseComponent extends UseCaseProvider {
    // no methods, it's workaround for dagger multi-dependency multi-scope issue
}
