package art.cain.consumer.rpc.spring;

import art.cain.consumer.rpc.RpcClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.stream.Stream;

/**
 * Custom a {@link BeanFactoryPostProcessor} for registering the proxy object that decorated by {@link RpcClient} annotation.
 *
 * @author Cain
 */
@Component
public class RpcBeanFactoryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) registry;
        // Get all exists bean name.
        Iterator<String> beanNamesIterator = beanFactory.getBeanNamesIterator();
        while (beanNamesIterator.hasNext()) {
            String beanName = beanNamesIterator.next();
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            // Get all exists bean class name by its bean definition.
            String className = beanDefinition.getBeanClassName();
            if (StringUtils.isEmpty(className)) {
                return;
            }
            try {
                // Loop all declare fields with RpcClient annotation.
                Class<?> clazz = Class.forName(className);
                Field[] declaredFields = clazz.getDeclaredFields();
                Stream.of(declaredFields).forEach(x -> {
                    RpcClient rpcClient = x.getDeclaredAnnotation(RpcClient.class);
                    if (rpcClient != null) {
                        // Get the interface decorated by RpcClient annotation and do register.
                        Class interfaceClass = x.getType();
                        doRegister(registry, interfaceClass);
                    }
                });
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void doRegister(BeanDefinitionRegistry registry, Class<?> beanClazz) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
        GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
        // Deliver the interface class to factory bean for producing bean.
        definition.getConstructorArgumentValues().addGenericArgumentValue(beanClazz);
        definition.setBeanClass(RpcFactoryBean.class);
        definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);
        registry.registerBeanDefinition(Introspector.decapitalize(beanClazz.getSimpleName()), definition);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
