package art.cain.consumer.rpc;

import art.cain.common.rpc.models.Header;
import art.cain.common.rpc.models.Parameter;
import art.cain.common.rpc.models.Request;
import art.cain.common.utils.CodecUtils;
import art.cain.consumer.service.SnowFlakeGeneratorService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RpcProxyService {

    @Autowired
    private SnowFlakeGeneratorService snowFlakeGeneratorService;

    public Object doProxy(Object proxy, Method method, Object[] args) throws InterruptedException {
        // todo ip port zookeeper
        Request request = getRequest(proxy.getClass().getInterfaces()[0], method, new ArrayList<>(Arrays.asList(args)));
        byte[] requestBytes = CodecUtils.getBytes(request);
        Header header = getHeader(requestBytes.length);
        byte[] headerBytes = header.getBytes();

        ByteBuf buf = Unpooled.buffer(requestBytes.length + headerBytes.length);
        buf.writeBytes(headerBytes).writeBytes(requestBytes);
        long requestId = header.getRequestId();
        ResponseRegistry.registerResponse(requestId);
        // send request
//        System.out.println("send request-- request id: " + requestId + " data length: " + header.getDataLength());
        ChannelPool.getInstance().getChannel().writeAndFlush(buf).sync();
//        sync.channel().closeFuture().sync();
        return ResponseRegistry.getAndRemoveResponse(requestId);
//        return ResponseRegistry.getAndRemoveResponse(header.getRequestId());
    }

    private Header getHeader(int dataLength) {
        Header header = new Header();
        header.setFlag(Header.FLAG_REQUEST);
        header.setRequestId(snowFlakeGeneratorService.generate(1L, 1L));
        header.setDataLength(dataLength);
        return header;
    }

    private Request getRequest(Class interfaceType, Method method, List<Object> parameterList) {
        Request request = new Request();
        request.setClassName(interfaceType.getName());
        request.setMethodName(method.getName());
        List<Parameter> parameters = new ArrayList<>();
        for (int i = 0; i < method.getParameterCount(); i++) {
            Parameter parameter = new Parameter();
            String parameterTypeClassName = method.getParameterTypes()[i].getName();
            parameter.setParameterType(parameterTypeClassName);
            parameter.setArg(parameterList.get(i));
            parameters.add(parameter);
        }
        request.setParameters(parameters);
        return request;
    }
}
