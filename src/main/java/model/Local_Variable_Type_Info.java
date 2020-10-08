package model;

import java.io.IOException;
import java.io.InputStream;

public class Local_Variable_Type_Info extends Attribute_Extend {

    private U2 start_pc;

    private U2 length;

    private U2 name_index;

    private U2 singature_index;

    private U2 index;

    public static Local_Variable_Type_Info create(InputStream is) throws IOException {
        Local_Variable_Type_Info lvti = new Local_Variable_Type_Info();
        lvti.start_pc = U2.create(is);
        lvti.length = U2.create(is);
        lvti.name_index = U2.create(is);
        lvti.singature_index = U2.create(is);
        lvti.index = U2.create(is);
        lvti.newBytes();
        return lvti;
    }
}