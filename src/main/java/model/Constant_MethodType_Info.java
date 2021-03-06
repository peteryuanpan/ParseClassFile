package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_MethodType_Info extends Constant_Info {

    // tag: 16

    // 值必须是对常量池的有效索引，常量池在该索引处的项必须是CONSTANT_Utf8_info结构，表示方法的描述符
    private U2 descriptor_index;

    // index索引的具体值
    private Constant_Utf8_Info valueof_descriptor_index;

    public static Constant_MethodType_Info create(InputStream is, U1 tag) throws IOException {
        Constant_MethodType_Info ci = new Constant_MethodType_Info();
        ci.tag = tag;
        ci.descriptor_index = U2.create(is);
        ci.newBytes();
        return ci;
    }

    public Constant_MethodType_Info fill(Constant_Info[] constant_infos) {
        fillForException(descriptor_index, constant_infos, Constant_Utf8_Info.class);
        valueof_descriptor_index = (Constant_Utf8_Info) constant_infos[descriptor_index.getValue()];
        return this;
    }
}
