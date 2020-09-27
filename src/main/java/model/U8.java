package model;

import java.io.IOException;
import java.io.InputStream;

public class U8 extends Unsigned {

    // 字节数组的整型值（8个字节）
    private long value;

    public long getValue() {
        return value;
    }

    protected void newBytes() {
        this.bytes = new byte[8];
    }

    public static U8 create(InputStream is) throws IOException {
        U8 u8 = new U8();
        u8.newBytes();
        u8.value = u8.read(is, u8.bytes);
        return u8;
    }

    public String toString() {
        return value + "(" + parseBytesToHexString() + ")";
    }
}
