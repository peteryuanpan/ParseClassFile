package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_Integer_Info extends Constant_Info {

    // tag: 3

    // 按照高位在前存储的int值
    private U4 value;

    public static Constant_Integer_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Integer_Info ci = new Constant_Integer_Info();
        ci.tag = tag;
        ci.value = U4.create(is);
        ci.newBytes();
        return ci;
    }
}
