package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_Class_Info extends Constant_Info {

    // tag: 7

    // 指向全限定名常量项的索引
    private U2 name_index;

    // index索引的具体值
    private Constant_Utf8_Info valueof_name_index;

    public Constant_Utf8_Info getValueofNameIndex() {
        return valueof_name_index;
    }

    public static Constant_Class_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Class_Info ci = new Constant_Class_Info();
        ci.tag = tag;
        ci.name_index = U2.create(is);
        ci.newBytes();
        return ci;
    }

    public Constant_Class_Info fill(Constant_Info[] constant_infos) {
        fillForException(name_index, constant_infos, Constant_Utf8_Info.class);
        valueof_name_index = (Constant_Utf8_Info) constant_infos[name_index.getValue()];
        return this;
    }
}
