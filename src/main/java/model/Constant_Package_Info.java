package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_Package_Info extends Constant_Info {

    // tag: 20

    // 值必须是对常量池的有效索引，常量池在该索引处的项必须是CONSTANT_Utf8_info结构，表示包名字
    private U2 name_index;

    // index索引的具体值
    private Constant_Utf8_Info valueof_name_index;

    public static Constant_Package_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Package_Info ci = new Constant_Package_Info();
        ci.tag = tag;
        ci.name_index = U2.create(is);
        ci.newBytes();
        return ci;
    }

    public Constant_Package_Info fill(Constant_Info[] constant_infos) {
        fillForException(name_index, constant_infos, Constant_Utf8_Info.class);
        valueof_name_index = (Constant_Utf8_Info) constant_infos[name_index.getValue()];
        return this;
    }
}