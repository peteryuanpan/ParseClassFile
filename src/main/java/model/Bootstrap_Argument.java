package model;

import java.io.IOException;
import java.io.InputStream;

public class Bootstrap_Argument extends Attribute_Extend {

    // 指向的常量池必须是以下结构之一
    // Constant_String_Info、Constant_Class_Info、Constant_Integer_Info、Constant_Long_Info
    // Constant_Float_Info、Constant_Double_Info、Constant_MethodHandle_Info、Constant_MethodType_Info
    private U2 constant_index;

    private Constant_Info valueof_constant_index;

    private static final Class[] constant_value_clazzs = new Class[8];

    static {
        constant_value_clazzs[0] = Constant_String_Info.class;
        constant_value_clazzs[1] = Constant_Class_Info.class;
        constant_value_clazzs[2] = Constant_Integer_Info.class;
        constant_value_clazzs[3] = Constant_Long_Info.class;
        constant_value_clazzs[4] = Constant_Float_Info.class;
        constant_value_clazzs[5] = Constant_Double_Info.class;
        constant_value_clazzs[6] = Constant_MethodHandle_Info.class;
        constant_value_clazzs[7] = Constant_MethodType_Info.class;
    }

    public static Bootstrap_Argument create(InputStream is, Constant_Info[] constant_infos) throws IOException {
        Bootstrap_Argument ba = new Bootstrap_Argument();
        ba.constant_index = U2.create(is);
        fillForException(ba.constant_index, constant_infos, constant_value_clazzs);
        ba.valueof_constant_index = constant_infos[ba.constant_index.getValue()];
        ba.newBytes();
        return ba;
    }
}
