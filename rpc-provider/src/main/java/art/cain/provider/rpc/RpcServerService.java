package art.cain.provider.rpc;

import art.cain.common.rpc.models.Header;
import art.cain.common.rpc.models.RequestPackage;
import art.cain.common.rpc.models.Parameter;
import art.cain.common.rpc.models.Request;
import art.cain.common.utils.CodecUtils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RpcServerService {

    static AtomicInteger atomicInteger = new AtomicInteger(0);

    @Autowired
    private ApplicationContext applicationContext;

    public ByteBuf handle(Object msg) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        RequestPackage message = (RequestPackage) msg;
        System.out.println("get new msg " + atomicInteger.incrementAndGet());
        Header header = message.getHeader();
        Request request = message.getRequest();
        String className = request.getClassName();
        String methodName = request.getMethodName();
        List<Parameter> parameters = request.getParameters();
        Class<?> beanClass = Class.forName(className);
        Class[] paramTypes = new Class[parameters.size()];
        Object[] paramValues = new Object[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            Class paramType = Class.forName(parameter.getParameterType());
            paramTypes[i] = paramType;
            Object arg = parameter.getArg();
            paramValues[i] = arg;
        }
        Object bean = applicationContext.getBean(beanClass);
        Method method = beanClass.getMethod(methodName, paramTypes);
        Object response = method.invoke(bean, paramValues);

        // can be replaced by an encoder
        byte[] responseBytes = CodecUtils.getBytes(response);

        header.setFlag(Header.FLAG_RESPONSE);
        header.setRequestId(header.getRequestId());
        header.setDataLength(responseBytes.length);
        byte[] responseHeader = header.getBytes();

        ByteBuf responseBuf = Unpooled.buffer(responseHeader.length + responseBytes.length);
        responseBuf.writeBytes(responseHeader).writeBytes(responseBytes);
        return responseBuf;
    }
}
