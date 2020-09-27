package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_MethodHandle_Info extends Constant_Info {

    // tag: 15

    // 值必须在 1 ~ 9 之间（包括1和9），它决定了方法句柄的类型
    // 方法句柄类型的值表示方法句柄的字节码行为
    private U1 reference_kind;

    // 值必须是对常量池的有效索引
    private U2 reference_index;

    public static Constant_MethodHandle_Info create(InputStream is, U1 tag) throws IOException {
        Constant_MethodHandle_Info ci = new Constant_MethodHandle_Info();
        ci.tag = tag;
        ci.reference_kind = U1.create(is);
        ci.reference_index = U2.create(is);
        ci.newBytes();
        return ci;
    }
}
