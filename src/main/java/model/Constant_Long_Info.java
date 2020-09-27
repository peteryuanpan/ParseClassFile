package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_Long_Info extends Constant_Info {

    // tag: 5

    // 按照高位在前存储的long值
    private U8 value;

    public static Constant_Long_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Long_Info ci = new Constant_Long_Info();
        ci.tag = tag;
        ci.value = U8.create(is);
        ci.newBytes();
        return ci;
    }
}
