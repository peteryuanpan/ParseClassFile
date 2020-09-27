package model;

import java.io.InputStream;

public class Method_Info extends Table {

    private Method_Access_Flag access_flag;

    private U2 name_index;

    private Constant_Utf8_Info valueof_name_index;

    private U2 descriptor_index;

    private Constant_Utf8_Info valueof_descriptor_index;

    private U2 attributes_count;

    private Attribute_Info[] attributes;

    public static Method_Info create(InputStream is, Constant_Info[] constant_infos) throws Exception {
        Method_Info mi = new Method_Info();
        mi.access_flag = Method_Access_Flag.create(is);
        mi.name_index = U2.create(is);
        mi.descriptor_index = U2.create(is);
        mi.attributes_count = U2.create(is);
        mi.attributes = Attribute_Info.createAttributes(is, mi.attributes_count.getValue(), constant_infos);
        mi.newBytes();
        mi.fill(constant_infos);
        return mi;
    }

    public Method_Info fill(Constant_Info[] constant_infos) {
        fillForException(name_index, constant_infos, Constant_Utf8_Info.class);
        valueof_name_index = (Constant_Utf8_Info) constant_infos[name_index.getValue()];
        fillForException(descriptor_index, constant_infos, Constant_Utf8_Info.class);
        valueof_descriptor_index = (Constant_Utf8_Info) constant_infos[descriptor_index.getValue()];
        return this;
    }
}
