package model;

import java.io.IOException;
import java.io.InputStream;

public class Stack_Map_Frame extends Attribute_Extend {

    private int length;

    protected void newBytes() {
        this.bytes = new byte[length];
    }

    public static Stack_Map_Frame create(InputStream is, int length) throws IOException {
        Stack_Map_Frame smf = new Stack_Map_Frame();
        smf.length = length;
        smf.newBytes();
        smf.read(is, smf.bytes);
        return smf;
    }
}
