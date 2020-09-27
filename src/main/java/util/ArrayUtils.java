package util;

import java.util.Arrays;
import java.util.List;

public class ArrayUtils {

    public static int length(byte[]... array_bytes) {
        int length = 0;
        for (byte[] bytes : array_bytes) {
            length += bytes.length;
        }
        return length;
    }

    public static int length(List<byte[]> list_bytes) {
        int length = 0;
        for (byte[] bytes : list_bytes) {
            length += bytes.length;
        }
        return length;
    }

    public static byte[] newarray(byte[]... array_bytes) {
        byte[] dest = new byte[length(array_bytes)];
        int length = 0;
        for (byte[] bytes : array_bytes) {
            System.arraycopy(bytes, 0, dest, length, bytes.length);
            length += bytes.length;
        }
        return dest;
    }

    public static byte[] newarray(List<byte[]> list_bytes) {
        byte[] dest = new byte[length(list_bytes)];
        int length = 0;
        for (byte[] bytes : list_bytes) {
            System.arraycopy(bytes, 0, dest, length, bytes.length);
            length += bytes.length;
        }
        return dest;
    }
}
