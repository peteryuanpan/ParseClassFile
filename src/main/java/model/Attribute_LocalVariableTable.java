package model;

import java.io.InputStream;

public class Attribute_LocalVariableTable extends Attribute_Info {

    private U2 table_length;

    private Local_Variable_Info[] table;

    public static Attribute_LocalVariableTable create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws Exception
    {
        Attribute_LocalVariableTable attr = new Attribute_LocalVariableTable();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.table_length = U2.create(is);
        attr.table = new Local_Variable_Info[attr.table_length.getValue()];
        for (int i = 0; i < attr.table_length.getValue(); i ++) {
            attr.table[i] = Local_Variable_Info.create(is);
        }
        attr.newBytes();
        return attr;
    }
}
