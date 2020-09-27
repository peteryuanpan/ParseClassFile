package model;

import java.io.IOException;
import java.io.InputStream;

public class Exception_Info extends Attribute_Extend {

    private U2 start_pc;

    private U2 end_pc;

    private U2 handler_pc;

    private U2 catch_type;

    public static Exception_Info create(InputStream is) throws IOException {
        Exception_Info ex = new Exception_Info();
        ex.start_pc = U2.create(is);
        ex.end_pc = U2.create(is);
        ex.handler_pc = U2.create(is);
        ex.catch_type = U2.create(is);
        ex.newBytes();
        return ex;
    }
}
