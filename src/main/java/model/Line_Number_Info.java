package model;

import java.io.IOException;
import java.io.InputStream;

public class Line_Number_Info extends Attribute_Extend {

    private U2 start_pc;

    private U2 line_number;

    public static Line_Number_Info create(InputStream is) throws IOException {
        Line_Number_Info lni = new Line_Number_Info();
        lni.start_pc = U2.create(is);
        lni.line_number = U2.create(is);
        lni.newBytes();
        return lni;
    }
}
