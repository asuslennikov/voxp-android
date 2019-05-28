package ru.voxp.android.di.data;

import dagger.Component;
import ru.voxp.android.di.ApplicationProvider;

@ManagerScope
@Component(dependencies = {ApplicationProvider.class}, modules = ManagerModule.class)
interface ManagerComponent extends ManagerProvider {
    // no methods, it's workaround for dagger multi-dependency multi-scope issue
}
