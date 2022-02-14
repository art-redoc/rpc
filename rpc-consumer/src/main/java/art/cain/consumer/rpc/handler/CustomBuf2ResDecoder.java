package art.cain.consumer.rpc.handler;

import art.cain.common.rpc.models.Header;
import art.cain.common.rpc.models.ResponsePackage;
import art.cain.common.utils.CodecUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 二进制解码，封装为Message，解决粘包问题。
 */
public class CustomBuf2ResDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buf, List<Object> out) throws Exception {
        while (complete(buf)) {
//            System.out.println("thread: " + Thread.currentThread().getName() + " data length: " + dataLength + " readable: " + buf.readableBytes());
            Header header = Header.build(buf);
//            System.out.println("header-- flag: " + header.getFlag() + " request id: " + header.getRequestId() + " data length: " + header.getDataLength());
//            System.out.println("header data length: "+header.getDataLength());
            byte[] bytes = new byte[header.getDataLength()];
            buf.readBytes(bytes);
//            System.out.println("thread: " + Thread.currentThread().getName() + " buf left: " + buf.readableBytes());
            Object response = CodecUtils.getObject(bytes, Object.class);
//            System.out.println("request-- class name: " + request.getClassName() + " method name: " + request.getMethodName() + " parameters: " + request.getParameters());
            out.add(new ResponsePackage(header, response));
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
        return buf.readableBytes() >= dataLength + Header.BYTES_HEADER;
    }
}
