package model;

import java.io.IOException;
import java.io.InputStream;

public class Attribute_ConstantValue extends Attribute_Info {

    // 指向常量池中一个字面量常量的运用
    private U2 constant_index;

    // 字面量常量的引用类型是以下5种情况的一种
    // Constant_Long_Info
    // Constant_Float_Info
    // Constant_Double_Info
    // Constant_Integer_Info
    // Constant_String_Info
    private Constant_Info valueof_constant_index;

    static final Class[] constant_value_clazzs = new Class[5];
    static {
        constant_value_clazzs[0] = Constant_Long_Info.class;
        constant_value_clazzs[1] = Constant_Float_Info.class;
        constant_value_clazzs[2] = Constant_Double_Info.class;
        constant_value_clazzs[3] = Constant_Integer_Info.class;
        constant_value_clazzs[4] = Constant_String_Info.class;
    }

    public static Attribute_ConstantValue create(
            InputStream is,
            U2 name_index,
            Constant_Utf8_Info valueof_name_index,
            U4 length,
            Constant_Info[] constant_infos) throws IOException
    {
        Attribute_ConstantValue attr = new Attribute_ConstantValue();
        attr.name_index = name_index;
        attr.valueof_name_index = valueof_name_index;
        attr.length = length;
        attr.constant_index = U2.create(is);
        attr.fillForException(attr.constant_index, constant_infos, constant_value_clazzs);
        attr.valueof_constant_index = constant_infos[attr.constant_index.getValue()];
        attr.newBytes();
        return attr;
    }
}
