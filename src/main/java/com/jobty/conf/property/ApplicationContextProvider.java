package com.jobty.conf.property;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Bean 가져오기 위한 클래스
 * <br>
 * example:
 * <pre>
 *     Property property = ApplicationContextProvider.getBean("property", Property.class)
 *     property.getName();
 * </pre>
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 해당하는 빈을 가져온다.
     * @param name 빈 이름
     * @param targetClass 해당하는 클래스
     */
    public static <T> T getBean(String name, Class<T> targetClass){
        return applicationContext.getBean(name, targetClass);
    }
}
