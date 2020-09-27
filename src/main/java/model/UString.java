package model;

import java.io.IOException;
import java.io.InputStream;

public class UString extends Unsigned {

    // 字节数组的字符串的长度
    private int length;

    // 字节数组的字符串
    private String value;

    public int getLength() {
        return length;
    }

    public String getValue() {
        return value;
    }

    protected void newBytes() {
        this.bytes = new byte[this.length];
    }

    public static UString create(InputStream is, int length) throws IOException {
        UString ustring = new UString();
        ustring.length = length;
        ustring.newBytes();
        ustring.read(is, ustring.bytes);
        ustring.value = new String(ustring.bytes);
        return ustring;
    }

    public String toString() {
        return value + "(" + parseBytesToHexString() + ")";
    }
}
