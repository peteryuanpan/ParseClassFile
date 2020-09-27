package model;

import java.io.IOException;
import java.io.InputStream;

public class U1 extends Unsigned {

    // 字节数组的整型值（1个字节）
    private byte value;

    public byte getValue() {
        return value;
    }

    protected void newBytes() {
        this.bytes = new byte[1];
    }

    public static U1 create(InputStream is) throws IOException {
        U1 u1 = new U1();
        u1.newBytes();
        u1.value = (byte) u1.read(is, u1.bytes);
        return u1;
    }

    public String toString() {
        return value + "(" + parseBytesToHexString() + ")";
    }
}
