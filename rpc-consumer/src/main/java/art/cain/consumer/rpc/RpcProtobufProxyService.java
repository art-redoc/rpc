//package art.cain.consumer.rpc;
//
//import art.cain.common.rpc.proto.RpcProto;
//import art.cain.common.utils.CodecUtils;
//import art.cain.consumer.service.SnowFlakeGeneratorService;
//import com.google.protobuf.ByteString;
//import io.netty.buffer.ByteBuf;
//import io.netty.buffer.Unpooled;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
///**
// * todo Implement by Protobuf.
// */
//@Service
//public class RpcProtobufProxyService {
//
//    @Autowired
//    private SnowFlakeGeneratorService snowFlakeGeneratorService;
//
//    public Object doProxy(Object proxy, Method method, Object[] args) throws InterruptedException {
//
//        RpcProto.Request request = getRequest(proxy.getClass().getInterfaces()[0], method, new ArrayList<>(Arrays.asList(args)));
//        byte[] requestBytes = request.toByteArray();
//        RpcProto.Header header = getHeader(requestBytes.length);
//        byte[] headerBytes = header.toByteArray();
//
//        ByteBuf buf = Unpooled.buffer(requestBytes.length + headerBytes.length);
//        buf.writeBytes(headerBytes).writeBytes(requestBytes);
//        long requestId = header.getRequestId();
//        ResponseRegistry.registerResponse(requestId);
//        // send request
////        System.out.println("send request-- request id: " + requestId + " data length: " + header.getDataLength());
//        ChannelPool.getInstance().getChannel().writeAndFlush(buf).sync();
////        sync.channel().closeFuture().sync();
//        return ResponseRegistry.getAndRemoveResponse(requestId);
////        return ResponseRegistry.getAndRemoveResponse(header.getRequestId());
//    }
//
//    private RpcProto.Header getHeader(int dataLength) {
//        return RpcProto.Header.newBuilder()
//                .setMsgCode(RpcProto.MsgCode.REQUEST)
//                .setRequestId(snowFlakeGeneratorService.generate(1L, 1L))
//                .setDataLength(dataLength).build();
//    }
//
//    private RpcProto.Request getRequest(Class interfaceType, Method method, List<Object> parameterList) {
//
//        RpcProto.Request.Builder builder = RpcProto.Request.newBuilder()
//                .setClassName(interfaceType.getName())
//                .setMethodName(method.getName());
//        for (int i = 0; i < method.getParameterCount(); i++) {
//            String parameterTypeClassName = method.getParameterTypes()[i].getName();
//            Object arg = parameterList.get(i);
//            byte[] bytes = CodecUtils.getBytes(arg);
//            RpcProto.Request.Parameter parameter = RpcProto.Request.Parameter.newBuilder()
//                    .setParameterType(parameterTypeClassName)
//                    .setArg(ByteString.copyFrom(bytes)).build();
//            builder.addParameter(parameter);
//        }
//        return builder.build();
//    }
//}
