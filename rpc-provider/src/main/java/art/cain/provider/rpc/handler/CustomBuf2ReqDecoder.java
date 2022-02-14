package art.cain.provider.rpc.handler;

import art.cain.common.rpc.models.Header;
import art.cain.common.rpc.models.Request;
import art.cain.common.rpc.models.RequestPackage;
import art.cain.common.utils.CodecUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 二进制解码，封装为Message，解决粘包问题。
 */
public class CustomBuf2ReqDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        while (complete(buf)) {
//            System.out.println("thread: " + Thread.currentThread().getName() + " data length: " + dataLength + " readable: " + buf.readableBytes());
            Header header = Header.build(buf);
//            System.out.println("header-- flag: " + header.getFlag() + " request id: " + header.getRequestId() + " data length: " + header.getDataLength());
//            System.out.println("thread: " + Thread.currentThread().getName() + " header data length: " + header.getDataLength() + " cap: " + buf.maxCapacity());
            byte[] bytes = new byte[header.getDataLength()];
            buf.readBytes(bytes);
//            System.out.println("thread: " + Thread.currentThread().getName() + " buf left: " + buf.readableBytes());
            Request request = CodecUtils.getObject(bytes, Request.class);
//            System.out.println("request-- class name: " + request.getClassName() + " method name: " + request.getMethodName() + " parameters: " + request.getParameters());
            out.add(new RequestPackage(header, request));
        }
    }

    /**
     * 如果当前的缓冲区的可读大小，已经累积成一个完整的包了，那么说明准备完成。
     *
     * @param buf Byte buf.
     * @return Complete.
     */
    private boolean complete(ByteBuf buf) {
        if (buf.readableBytes() <= Header.BYTES_HEADER) {
            return false;
        }
        int current = buf.readerIndex();
        int dataLength = buf.getInt(current + Header.BYTES_FLAG + Header.BYTES_REQUEST_ID);
//        System.out.println("thread: " + Thread.currentThread().getName() + " dataLength: " + dataLength);
        return buf.readableBytes() >= dataLength + Header.BYTES_HEADER;
    }
}
