package model;

import java.io.IOException;
import java.io.InputStream;

public abstract class Unsigned {

    // 无符号数或表的字节数组
    byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    /**
     * 派生类需要重写一个方法，然后实现字节数组bytes的new动作
     * @return
     */
    abstract protected void newBytes();

    /**
     * 通过 inputStream 读取 bytes数组长度 的字节 到 bytes数组中，并用 Big-Endian 顺序计算整型值
     * @param is
     * @param bytes
     * @return
     * @throws IOException
     */
    protected long read(InputStream is, byte[] bytes) throws IOException {
        if (is == null)
            throw new NullPointerException("InputStream is null");
        int len = is.read(bytes);
        if (len == -1 || len != bytes.length)
            throw new RuntimeException("len " + len + " is not equal bytes.length " + bytes.length);
        long num = 0;
        for (int i = 0; i < bytes.length; i++) {
            num <<= 8;
            num |= bytes[i] & 0xff;
        }
        return num;
    }

    /**
     * 用 Big-Endian 顺序将字节数组转为字符串（十六进制表达式）
     * @return
     */
    public String parseBytesToHexString() {
        StringBuilder sb = new StringBuilder();
        if (bytes == null)
            throw new RuntimeException("bytes is null");
        sb.append("0x");
        for (int i = 0; i < bytes.length; i ++) {
            sb.append(Character.forDigit((bytes[i] & 0xff) / 16, 16));
            sb.append(Character.forDigit((bytes[i] & 0xff) % 16, 16));
        }
        return sb.toString();
    }
}
