package art.cain.common.rpc.models;

import io.netty.buffer.ByteBuf;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.nio.ByteBuffer;

@Getter
@Setter
public class Header implements Serializable {

    private static final long serialVersionUID = 7920360565792689208L;
    public static final byte FLAG_REQUEST = (byte) 0x80;
    public static final byte FLAG_RESPONSE = (byte) 0x81;
    public static final int BYTES_FLAG = Byte.BYTES;
    public static final int BYTES_REQUEST_ID = Long.BYTES;
    public static final int BYTES_DATA_LENGTH = Integer.BYTES;
    public static final int BYTES_HEADER = BYTES_FLAG + BYTES_REQUEST_ID + BYTES_DATA_LENGTH;
    private byte flag;
    private long requestId;
    private int dataLength;

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(BYTES_HEADER);
        buffer.put(flag);
        buffer.putLong(requestId);
        buffer.putInt(dataLength);
        return buffer.array();
    }

    public static Header build(ByteBuf bytes) {
        Header header = new Header();
        header.setFlag(bytes.readByte());
        header.setRequestId(bytes.readLong());
        header.setDataLength(bytes.readInt());
        return header;
    }
}
