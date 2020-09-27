package model;

import java.io.IOException;
import java.io.InputStream;

public class Local_Variable_Info extends Attribute_Extend {

    private U2 start_pc;

    private U2 length;

    private U2 name_index;

    private U2 descriptor_index;

    private U2 index;

    public static Local_Variable_Info create(InputStream is) throws IOException {
        Local_Variable_Info lvi = new Local_Variable_Info();
        lvi.start_pc = U2.create(is);
        lvi.length = U2.create(is);
        lvi.name_index = U2.create(is);
        lvi.descriptor_index = U2.create(is);
        lvi.index = U2.create(is);
        lvi.newBytes();
        return lvi;
    }
}
