package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_Double_Info extends Constant_Info {

    // tag: 6

    // 按照高位在前存储的double值
    private U8 value;

    public static Constant_Double_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Double_Info ci = new Constant_Double_Info();
        ci.tag = tag;
        ci.value = U8.create(is);
        ci.newBytes();
        return ci;
    }
}
