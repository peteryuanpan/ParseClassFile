package model;

import java.io.InputStream;

public class Attribute_Code extends Attribute_Info {

    private U2 max_statck;

    private U2 max_locals;

    private U4 code_length;

    private ByteCode code;

    private U2 exception_table_length;

    private Exception_Info[] exception_table;

    private U2 attributes_count;

    private Attribute_Info[] attributes;

    public static Attribute_Code create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws Exception
    {
        Attribute_Code attr = new Attribute_Code();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.max_statck = U2.create(is);
        attr.max_locals = U2.create(is);
        attr.code_length = U4.create(is);
        attr.code = ByteCode.create(is, attr.code_length.getValue());
        attr.exception_table_length = U2.create(is);
        attr.exception_table = new Exception_Info[attr.exception_table_length.getValue()];
        for (int i = 0; i < attr.exception_table_length.getValue(); i ++) {
            attr.exception_table[i] = Exception_Info.create(is);
        }
        attr.attributes_count = U2.create(is);
        attr.attributes = Attribute_Info.createAttributes(is, attr.attributes_count.getValue(), constant_infos);
        attr.newBytes();
        return attr;
    }
}
