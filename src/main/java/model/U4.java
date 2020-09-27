package model;

import java.io.IOException;
import java.io.InputStream;

public class U4 extends Unsigned {

    // 字节数组的整型值（4个字节）
    private int value;

    public int getValue() {
        return value;
    }

    protected void newBytes() {
        this.bytes = new byte[4];
    }

    public static U4 create(InputStream is) throws IOException {
        U4 u4 = new U4();
        u4.newBytes();
        u4.value = (int) u4.read(is, u4.bytes);
        return u4;
    }

    public String toString() {
        return value + "(" + parseBytesToHexString() + ")";
    }
}
