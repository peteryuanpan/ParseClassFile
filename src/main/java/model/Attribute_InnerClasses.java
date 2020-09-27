package model;

import java.io.InputStream;

public class Attribute_InnerClasses extends Attribute_Info {

    private U2 number_of_classes;

    private Inner_Classes_Info[] inner_classes;

    public static Attribute_InnerClasses create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws Exception
    {
        Attribute_InnerClasses attr = new Attribute_InnerClasses();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.number_of_classes = U2.create(is);
        attr.inner_classes = new Inner_Classes_Info[attr.number_of_classes.getValue()];
        for (int i = 0; i < attr.number_of_classes.getValue(); i ++) {
            attr.inner_classes[i] = Inner_Classes_Info.create(is, constant_infos);
        }
        attr.newBytes();
        return attr;
    }
}
