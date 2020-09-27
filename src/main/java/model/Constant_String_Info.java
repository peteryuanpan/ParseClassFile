package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_String_Info extends Constant_Info {

    // tag: 8

    // 指向字符串字面量的索引
    private U2 value_index;

    // index索引的具体值
    private Constant_Utf8_Info valueof_value_index;

    public static Constant_String_Info create(InputStream is, U1 tag) throws IOException {
        Constant_String_Info ci = new Constant_String_Info();
        ci.tag = tag;
        ci.value_index = U2.create(is);
        ci.newBytes();
        return ci;
    }

    public Constant_String_Info fill(Constant_Info[] constant_infos) {
        fillForException(value_index, constant_infos, Constant_Utf8_Info.class);
        valueof_value_index = (Constant_Utf8_Info) constant_infos[value_index.getValue()];
        return this;
    }
}
