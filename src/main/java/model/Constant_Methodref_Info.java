package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_Methodref_Info extends Constant_Info {

    // tag: 10

    // 指向声明方法的类描述符CONSTANT_Class_info的索引项
    private U2 class_index;

    // index1索引的具体值
    private Constant_Class_Info valueof_class_index;

    // 指向名称及类型描述符CONSTANT_NameAndType的索引项
    private U2 name_and_type_index;

    // index2索引的具体值
    private Constant_NameAndType_Info valueof_name_and_type_index;

    public static Constant_Methodref_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Methodref_Info ci = new Constant_Methodref_Info();
        ci.tag = tag;
        ci.class_index = U2.create(is);
        ci.name_and_type_index = U2.create(is);
        ci.newBytes();
        return ci;
    }

    public Constant_Info fill(Constant_Info[] constant_infos) {
        fillForException(class_index, constant_infos, Constant_Class_Info.class);
        valueof_class_index = (Constant_Class_Info) constant_infos[class_index.getValue()];
        fillForException(name_and_type_index, constant_infos, Constant_NameAndType_Info.class);
        valueof_name_and_type_index = (Constant_NameAndType_Info) constant_infos[name_and_type_index.getValue()];
        return this;
    }
}
