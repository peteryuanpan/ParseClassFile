package model;

import java.io.IOException;
import java.io.InputStream;

public class Constant_InvokeDynamic_Info extends Constant_Info {

    // tag: 18

    // 值必须是对当前CLASS文件中引导方法表的bootstrap_methods[]数组的有效索引
    private U2 index_bootstrap_methods;

    // private bootstrap_methods[] value_bootstrap_methods;

    // 值必须是对当前常量池的有效索引，常量池在该索引处的项必须是CONSTANT_NameAndType_info结构，表示方法名和方法描述符
    private U2 name_and_type_index;

    // index2索引的具体值
    private Constant_NameAndType_Info valueof_name_and_type_index;

    public static Constant_InvokeDynamic_Info create(InputStream is, U1 tag) throws IOException {
        Constant_InvokeDynamic_Info ci = new Constant_InvokeDynamic_Info();
        ci.tag = tag;
        ci.index_bootstrap_methods = U2.create(is);
        ci.name_and_type_index = U2.create(is);
        ci.newBytes();
        return ci;
    }

    public Constant_Info fill(Constant_Info[] constant_infos) {
        fillForException(name_and_type_index, constant_infos, Constant_NameAndType_Info.class);
        valueof_name_and_type_index = (Constant_NameAndType_Info) constant_infos[name_and_type_index.getValue()];
        return this;
    }
}
