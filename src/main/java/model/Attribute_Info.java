package model;

import util.ArrayUtils;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class Attribute_Info extends Table {

    // 属性名，指向常量池中的一个Constant_Utf8_Info
    U2 name_index;

    // 属性名的具体值
    Constant_Utf8_Info valueof_name_index;

    // 属性值所占用的字节数
    U4 length;

    protected void newBytes() {
        List<byte[]> list_bytes = new ArrayList<>();
        list_bytes.addAll(getListByteOfDeclaredObject(name_index));
        list_bytes.addAll(getListByteOfDeclaredObject(length));
        list_bytes.addAll(getListByteOfDeclaredFields());
        this.bytes = ArrayUtils.newarray(list_bytes);
    }

    /**
     * 对name_index、valueof_name_index、length进行特殊处理
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toStringClass());
        sb.append("\n").append("name_index: [").append(name_index).append("]");
        sb.append("\n").append("valueof_name_index: [").append(valueof_name_index).append("]");
        sb.append("\n").append("length: [").append(length).append("]");
        sb.append(toStringDeclaredFields());
        return sb.toString();
    }

    /**
     * 用于创建属性表，在类、字段表、方法表中都含有属性表，甚至Code属性中也有，因此写到这里并声明为public、static
     * @param is
     * @param length_attributes
     * @param constant_infos
     * @return
     * @throws Exception
     */
    public static Attribute_Info[] createAttributes(InputStream is, int length_attributes, Constant_Info[] constant_infos) throws Exception {
        // 创建属性表数组
        Attribute_Info[] attribute_infos = null;
        // 属性表size大于0才new，否则为null
        if (length_attributes > 0) {
            // 实例化属性表数组
            attribute_infos = new Attribute_Info[length_attributes];
            for (int i = 0; i < length_attributes; i ++) {
                // 属性表结构第一项：name_index，指向常量池中的一个Constant_Utf8_Info，表示属性名
                U2 name_index = U2.create(is);
                // 获取属性名的具体值
                fillForException(name_index, constant_infos, Constant_Utf8_Info.class);
                Constant_Utf8_Info valueof_name_index = (Constant_Utf8_Info) constant_infos[name_index.getValue()];
                // 属性表结构第二项：length，表示属性值所占用的字节数
                U4 length = U4.create(is);
                // 属性名有很多，比如 Code、Exceptions、ConstantValue、InnerClasses等
                // 每一个属性类，名字都加上了一个前缀Attribute_，并且属于package model下
                // 使用反射的方法获取到具体属性类的class对象
                Class clazz = Class.forName("model.Attribute_" + valueof_name_index.getValueString().getValue());
                // 每一个派生属性类都应该定义一个create方法，如下
                // public static create(InputStream is, U2 index_attribute_name, Constant_Utf8_Info value_attribute_name, U4 attribute_length, Constant_Info[] constant_infos)
                // 通过class对象获取到method
                Method method = clazz.getMethod("create", InputStream.class, U2.class, Constant_Utf8_Info.class, U4.class, Constant_Info[].class);
                // 调用create方法获取到具体的属性对象
                // 由于create方法是static的，因此invoke方法第一个参数为null
                Attribute_Info attribute_info = (Attribute_Info) method.invoke(null, is, name_index, valueof_name_index, length, constant_infos);
                // 将属性对象传入属性表数组
                attribute_infos[i] = attribute_info;
            }
        }
        return attribute_infos;
    }
}
