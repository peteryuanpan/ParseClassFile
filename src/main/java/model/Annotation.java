package model;

import java.io.IOException;
import java.io.InputStream;

public class Annotation extends Attribute_Extend {

    private U2 type_index;

    private U2 num_element_value_pairs;

    private Element_Value_Pair[] element_value_pairs;

    public static Annotation create(InputStream is) throws IOException {
        Annotation an = new Annotation();
        an.type_index = U2.create(is);
        an.num_element_value_pairs = U2.create(is);
        an.element_value_pairs = new Element_Value_Pair[an.num_element_value_pairs.getValue()];
        for (int i = 0; i < an.num_element_value_pairs.getValue(); i ++) {
            an.element_value_pairs[i] = Element_Value_Pair.create(is);
        }
        an.newBytes();
        return an;
    }
}
