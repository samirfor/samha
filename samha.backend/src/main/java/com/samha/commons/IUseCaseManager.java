package com.samha.commons;

public interface IUseCaseManager {

    void prepare(UseCase useCase);

    void destroy(UseCase useCase);
}
