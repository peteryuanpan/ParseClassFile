package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_Large_Numeric_Continued_Info extends Constant_Info {

    // tag: null

    // 这个类的对象在 Constant_Long_Info 或 Constant_Double_Info 对象生成后被生成（手动）

    public static Constant_Large_Numeric_Continued_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Large_Numeric_Continued_Info ci = new Constant_Large_Numeric_Continued_Info();
        ci.tag = tag;
        ci.newBytes();
        return ci;
    }
}
