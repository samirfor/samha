package com.samha.infraestructure;

import com.samha.commons.IUseCaseManager;
import com.samha.commons.UseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;

@Component
public class UseCaseManager implements IUseCaseManager, BeanFactoryAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(UseCaseManager.class);

    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void prepare(UseCase useCase) {
        this.beanFactory.autowireBean(useCase);
    }

    @Override
    public void destroy(UseCase useCase) {
        try{
            this.beanFactory.destroyBean(useCase);
        }catch (Exception ex){
            LOGGER.warn("Failed to destroy UseCase: "+ ex.getClass() + "ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (AutowireCapableBeanFactory) beanFactory;
    }
}
