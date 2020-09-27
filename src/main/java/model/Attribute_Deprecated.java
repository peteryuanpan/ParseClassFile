package model;

import java.io.IOException;
import java.io.InputStream;

public class Attribute_Deprecated extends Attribute_Info {

    // 没有字段

    public static Attribute_Deprecated create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws IOException
    {
        Attribute_Deprecated attr = new Attribute_Deprecated();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.newBytes();
        return attr;
    }
}
