package model;

import java.io.IOException;
import java.io.InputStream;

public class ByteCode extends Attribute_Extend {

    // 字节数组长度
    private int length;

    // 字节数组转化后的字节码字符串
    private String value;

    protected void newBytes() {
        this.bytes = new byte[length];
    }

    public static ByteCode create(InputStream is, int length) throws IOException {
        ByteCode bc = new ByteCode();
        bc.length = length;
        bc.newBytes();
        bc.read(is, bc.bytes);
        bc.value = transformByteCodeFromBytesToString(bc.bytes);
        return bc;
    }

    /**
     * 将字节数组转为字节码字符串
     */
    public static String transformByteCodeFromBytesToString(byte[] bytes) {
        // TODO
        return "";
    }
}
