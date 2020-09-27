package model;

import java.io.IOException;
import java.io.InputStream;

public class Inner_Classes_Info extends Attribute_Extend {

    private U2 inner_class_index;

    private Constant_Class_Info valueof_inner_class_index;

    private U2 outer_class_index;

    private Constant_Class_Info valueof_outer_class_index;

    private U2 inner_name_index;

    private Constant_Utf8_Info valueof_inner_name_index;

    private Inner_Class_Access_Flag access_flag;

    public static Inner_Classes_Info create(InputStream is, Constant_Info[] constant_infos) throws IOException {
        Inner_Classes_Info in = new Inner_Classes_Info();
        in.inner_class_index = U2.create(is);
        in.outer_class_index = U2.create(is);
        in.inner_name_index = U2.create(is);
        in.access_flag = Inner_Class_Access_Flag.create(is);
        in.newBytes();
        in.fill(constant_infos);
        return in;
    }

    /**
     * outer_class_index 及 inner_name_index 可能为 0
     * @param constant_infos
     * @return
     */
    public Table fill(Constant_Info[] constant_infos) {
        fillForException(inner_class_index, constant_infos, Constant_Class_Info.class);
        valueof_inner_class_index = (Constant_Class_Info) constant_infos[inner_class_index.getValue()];
        if (outer_class_index.getValue() != 0) {
            fillForException(outer_class_index, constant_infos, Constant_Class_Info.class);
            valueof_outer_class_index = (Constant_Class_Info) constant_infos[outer_class_index.getValue()];
        }
        if (inner_name_index.getValue() != 0) {
            fillForException(inner_name_index, constant_infos, Constant_Utf8_Info.class);
            valueof_inner_name_index = (Constant_Utf8_Info) constant_infos[inner_name_index.getValue()];
        }
        return this;
    }
}
