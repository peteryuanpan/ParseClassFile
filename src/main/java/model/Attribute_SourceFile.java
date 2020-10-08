package model;

import java.io.InputStream;

public class Attribute_SourceFile extends Attribute_Info {

    private U2 sourcefile_name_index;

    private Constant_Utf8_Info valueof_sourcefile_name_index;

    public static Attribute_SourceFile create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws Exception
    {
        Attribute_SourceFile attr = new Attribute_SourceFile();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.sourcefile_name_index = U2.create(is);
        attr.fillForException(attr.sourcefile_name_index, constant_infos, Constant_Utf8_Info.class);
        attr.valueof_sourcefile_name_index = (Constant_Utf8_Info) constant_infos[attr.sourcefile_name_index.getValue()];
        attr.newBytes();
        return attr;
    }
}
