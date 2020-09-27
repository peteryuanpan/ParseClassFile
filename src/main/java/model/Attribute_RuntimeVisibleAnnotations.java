package model;

import java.io.InputStream;

public class Attribute_RuntimeVisibleAnnotations extends Attribute_Info {

    private U2 num_annotations;

    private Annotation[] annotations;

    public static Attribute_RuntimeVisibleAnnotations create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws Exception
    {
        Attribute_RuntimeVisibleAnnotations attr = new Attribute_RuntimeVisibleAnnotations();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.num_annotations = U2.create(is);
        attr.annotations = new Annotation[attr.num_annotations.getValue()];
        for (int i = 0; i < attr.num_annotations.getValue(); i ++) {
            attr.annotations[i] = Annotation.create(is);
        }
        attr.newBytes();
        return attr;
    }
}
