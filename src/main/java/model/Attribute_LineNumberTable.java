package model;

import java.io.InputStream;

public class Attribute_LineNumberTable extends Attribute_Info {

    private U2 table_length;

    private Line_Number_Info[] table;

    public static Attribute_LineNumberTable create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws Exception
    {
        Attribute_LineNumberTable attr = new Attribute_LineNumberTable();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.table_length = U2.create(is);
        attr.table = new Line_Number_Info[attr.table_length.getValue()];
        for (int i = 0; i < attr.table_length.getValue(); i ++) {
            attr.table[i] = Line_Number_Info.create(is);
        }
        attr.newBytes();
        return attr;
    }
}
