package art.cain.consumer.rpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@Component
public class RpcProxy<T> implements InvocationHandler {

    @Autowired
    private RpcProxyService rpcProxyService;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
//        System.out.println(proxy.getClass().getName());
//        System.out.println(method.getName());
//        System.out.println(args.length);
        try {
            return rpcProxyService.doProxy(proxy, method, args);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        if (method.getName().equals("getUserName")) {
//            Account account = (Account) args[0];
//            if ("cain".equals(account.getUsername()) && "1111qqqq".equals(account.getPassword())) {
//                return "Hello World";
//            }
//        }
//        if (method.getName().equals("test")) {
//            return args[0];
//        }
        return "Error";
    }
}
