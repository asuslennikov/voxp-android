package ru.jewelline.mvvm.base.domain;

import ru.jewelline.mvvm.interfaces.domain.UseCaseInput;

public final class EmptyUseCaseInput implements UseCaseInput {
    private static volatile EmptyUseCaseInput instance;

    private EmptyUseCaseInput() {
        // private constructor
    }

    public static EmptyUseCaseInput getInstance() {
        EmptyUseCaseInput localInstance = instance;
        if (localInstance == null) {
            synchronized (EmptyUseCaseInput.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new EmptyUseCaseInput();
                }
            }
        }
        return localInstance;
    }
}
