package model;

import java.io.InputStream;

public class Constant_Utf8_Info extends Constant_Info {

    // tag: 1

    // UTF-8编码的字符占用的字节数
    private U2 length_string;

    // 长度为length的UTF-8编码的字符串
    private UString value_string;

    public U2 getLengthString() {
        return length_string;
    }

    public UString getValueString() {
        return value_string;
    }

    public static Constant_Utf8_Info create(InputStream is, U1 tag) throws Exception {
        Constant_Utf8_Info ci = new Constant_Utf8_Info();
        ci.tag = tag;
        ci.length_string = U2.create(is);
        ci.value_string = UString.create(is, ci.length_string.getValue());
        ci.newBytes();
        return ci;
    }
}
