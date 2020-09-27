package model;

import java.io.IOException;
import java.io.InputStream;

public class Element_Value_Pair extends Attribute_Extend {

    private U2 key_index;

    private U1 value_tag;

    private U2 value_index;

    public static Element_Value_Pair create(InputStream is) throws IOException {
        Element_Value_Pair el = new Element_Value_Pair();
        el.key_index = U2.create(is);
        el.value_tag = U1.create(is);
        el.value_index = U2.create(is);
        el.newBytes();
        return el;
    }
}
