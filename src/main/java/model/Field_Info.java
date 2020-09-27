package model;

import java.io.InputStream;

public class Field_Info extends Table {

    private Field_Access_Flag access_flag;

    private U2 name_index;

    private Constant_Utf8_Info valueof_name_index;

    private U2 descriptor_index;

    private Constant_Utf8_Info valueof_descriptor_index;

    private U2 attributes_count;

    private Attribute_Info[] attributes;

    public static Field_Info create(InputStream is, Constant_Info[] constant_infos) throws Exception {
        Field_Info fi = new Field_Info();
        fi.access_flag = Field_Access_Flag.create(is);
        fi.name_index = U2.create(is);
        fi.descriptor_index = U2.create(is);
        fi.attributes_count = U2.create(is);
        fi.attributes = Attribute_Info.createAttributes(is, fi.attributes_count.getValue(), constant_infos);
        fi.newBytes();
        fi.fill(constant_infos);
        return fi;
    }

    public Field_Info fill(Constant_Info[] constant_infos) {
        fillForException(name_index, constant_infos, Constant_Utf8_Info.class);
        valueof_name_index = (Constant_Utf8_Info) constant_infos[name_index.getValue()];
        fillForException(descriptor_index, constant_infos, Constant_Utf8_Info.class);
        valueof_descriptor_index = (Constant_Utf8_Info) constant_infos[descriptor_index.getValue()];
        return this;
    }
}
