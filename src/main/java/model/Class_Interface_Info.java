package model;

import java.io.InputStream;

/**
 * 类索引、父类索引、接口索引的数据结构
 */
public class Class_Interface_Info extends Table {

    // 指向Constant_Class_Info的索引项
    private U2 class_index;

    // index1索引的具体值
    private Constant_Class_Info valueof_class_index;

    // index2索引的具体值
    private Constant_Utf8_Info valueof_name_index;

    /**
     * 创建一个类索引、父类索引、接口索引的数据结构
     * @param is
     * @param constant_infos
     * @return
     * @throws Exception
     */
    public static Class_Interface_Info create(InputStream is, Constant_Info[] constant_infos) throws Exception {
        Class_Interface_Info cir = new Class_Interface_Info();
        cir.class_index = U2.create(is);
        cir.newBytes();
        cir.fill(constant_infos);
        return cir;
    }

    /**
     * 数据填充
     * @param constant_infos
     * @return
     * @throws Exception
     */
    public Class_Interface_Info fill(Constant_Info[] constant_infos) {
        if (class_index.getValue() == 0) // java.lang.Object 没有父类
            return this;
        fillForException(class_index, constant_infos, Constant_Class_Info.class);
        valueof_class_index = (Constant_Class_Info) constant_infos[class_index.getValue()];
        valueof_name_index = valueof_class_index.getValueofNameIndex();
        return this;
    }
}
