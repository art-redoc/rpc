package art.cain.consumer.rpc.spring;

import art.cain.consumer.rpc.RpcProxy;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Proxy;

public class RpcFactoryBean<T> implements FactoryBean<T> {

    private Class<T> interfaceType;
    @Autowired
    private RpcProxy handler;

    public RpcFactoryBean(Class<T> interfaceType) {
        this.interfaceType = interfaceType;
    }

    @Override
    public T getObject() {
        return (T) Proxy.newProxyInstance(
                interfaceType.getClassLoader(),
                new Class[]{interfaceType},
                handler);
    }

    @Override
    public Class<T> getObjectType() {
        return interfaceType;
    }
}
