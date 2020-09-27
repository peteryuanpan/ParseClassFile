package model;

import java.io.IOException;
import java.io.InputStream;

public class Attribute_StackMapTable extends Attribute_Info {

    private U2 number_of_entries;

    private Stack_Map_Frame stack_map_frame_entries;

    public static Attribute_StackMapTable create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws IOException
    {
        Attribute_StackMapTable attr = new Attribute_StackMapTable();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.number_of_entries = U2.create(is);
        attr.stack_map_frame_entries = Stack_Map_Frame.create(is, length.getValue() - attr.number_of_entries.getValue());
        attr.newBytes();
        return attr;
    }
}
