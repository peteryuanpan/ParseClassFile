package model;

import java.io.InputStream;

public class Attribute_Signature extends Attribute_Info {

    private U2 singature_index;

    private Constant_Utf8_Info valueof_singature_index;

    public static Attribute_Signature create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws Exception
    {
        Attribute_Signature attr = new Attribute_Signature();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.singature_index = U2.create(is);
        attr.fillForException(attr.singature_index, constant_infos, Constant_Utf8_Info.class);
        attr.valueof_singature_index = (Constant_Utf8_Info) constant_infos[attr.singature_index.getValue()];
        attr.newBytes();
        return attr;
    }
}
