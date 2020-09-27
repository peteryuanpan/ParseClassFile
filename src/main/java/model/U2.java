package model;

import java.io.IOException;
import java.io.InputStream;

public class U2 extends Unsigned {

    // 字节数组的整型值（2个字节）
    private short value;

    public short getValue() {
        return value;
    }

    protected void newBytes() {
        this.bytes = new byte[2];
    }

    public static U2 create(InputStream is) throws IOException {
        U2 u2 = new U2();
        u2.newBytes();
        u2.value = (short) u2.read(is, u2.bytes);
        return u2;
    }

    public String toString() {
        return value + "(" + parseBytesToHexString() + ")";
    }

}
