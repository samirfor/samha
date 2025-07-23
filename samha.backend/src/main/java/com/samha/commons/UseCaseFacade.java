package com.samha.commons;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AuthorizationServiceException;

import javax.transaction.Transactional;

public class UseCaseFacade {

    private final IUseCaseManager useCaseManager;

    public UseCaseFacade(IUseCaseManager useCaseManager) {
        this.useCaseManager = useCaseManager;
    }

    @Transactional
    public <T> T execute(UseCase<T> useCase){
        return this.internal(useCase);
    }

    public <T> T internal(UseCase<T> useCase){
        useCaseManager.prepare(useCase);

        try{
            for(var child : useCase.getExecuteBefore()) executeChild(child);
            T result = executeAndHandleExceptions(useCase);
            for(var child : useCase.getExecuteAfter()) executeChild(child);

            return result;
        } finally {
            useCaseManager.destroy(useCase);
        }
    }

    private <T> T executeAndHandleExceptions(UseCase<T> useCase){
        try {
            return useCase.execute();
        } catch (BusinessException | AuthorizationServiceException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        } catch (ConstraintViolationException ex) {
            throw new BusinessException(ex.getMessage(), ex);
        } catch (DataIntegrityViolationException ex) {
            throw new BusinessException("Dados inconsistentes ou inválidos! Cheque o seu formulário.", ex);
        } catch (Throwable ex) {
            throw new UnexpectedException("Ocorreu um erro inesperado", ex);
        }
    }

    private void executeChild(UseCase child){
        this.internal(child);
    }
}
