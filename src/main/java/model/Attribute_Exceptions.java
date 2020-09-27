package model;

import java.io.IOException;
import java.io.InputStream;

public class Attribute_Exceptions extends Attribute_Info {

    private U2 number_of_exceptions;

    private U2[] exception_index_table;

    public static Attribute_Exceptions create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws IOException
    {
        Attribute_Exceptions attr = new Attribute_Exceptions();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.number_of_exceptions = U2.create(is);
        attr.exception_index_table = new U2[attr.number_of_exceptions.getValue()];
        for (int i = 0; i < attr.number_of_exceptions.getValue(); i ++) {
            attr.exception_index_table[i] = U2.create(is);
        }
        attr.newBytes();
        return attr;
    }
}
