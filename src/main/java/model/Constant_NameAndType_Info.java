package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_NameAndType_Info extends Constant_Info {

    // tag: 12

    // 指向该字段或方法名称常量项的索引
    private U2 name_index;

    // index索引的具体值，该字段或方法名称常量项
    private Constant_Utf8_Info valueof_name_index;

    // 指向该字段或方法描述符常量项的索引
    private U2 descriptor_index;

    // index索引的具体值，该字段或方法描述符常量项
    private Constant_Utf8_Info valueof_descriptor_index;

    public static Constant_NameAndType_Info create(InputStream is, U1 tag) throws IOException {
        Constant_NameAndType_Info ci = new Constant_NameAndType_Info();
        ci.tag = tag;
        ci.name_index = U2.create(is);
        ci.descriptor_index = U2.create(is);
        ci.newBytes();
        return ci;
    }

    public Constant_Info fill(Constant_Info[] constant_infos) {
        fillForException(name_index, constant_infos, Constant_Utf8_Info.class);
        valueof_name_index = (Constant_Utf8_Info) constant_infos[name_index.getValue()];
        fillForException(descriptor_index, constant_infos, Constant_Utf8_Info.class);
        valueof_descriptor_index = (Constant_Utf8_Info) constant_infos[descriptor_index.getValue()];
        return this;
    }
}
