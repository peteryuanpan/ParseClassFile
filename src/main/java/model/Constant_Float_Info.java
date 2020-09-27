package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_Float_Info extends Constant_Info {

    // tag: 4

    // 按照高位在前存储的float值
    private U4 value;

    public static Constant_Float_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Float_Info ci = new Constant_Float_Info();
        ci.tag = tag;
        ci.value = U4.create(is);
        ci.newBytes();
        return ci;
    }
}
