package parse;

import model.*;

import java.io.FileInputStream;
import java.io.InputStream;

public class ParseClassFile {

    private static Class_File parseMagic(Class_File class_file, InputStream is) throws Exception {
        // 魔数 0xcafebabe
        U4 magic = U4.create(is);
        class_file.magic = magic;
        return class_file;
    }

    private static Class_File parseVersion(Class_File class_file, InputStream is) throws Exception {
        // 次版本号
        U2 minor_version = U2.create(is);
        class_file.minor_version = minor_version;
        // 主版本号
        U2 major_version = U2.create(is);
        class_file.major_version = major_version;
        return class_file;
    }

    private static Class_File parseConstantPool(Class_File class_file, InputStream is) throws Exception {
        // 常量池容量计数值
        U2 constant_pool_count = U2.create(is);
        class_file.constant_pool_count = constant_pool_count;
        // 解析每一个常量池项（从1开始，到constant_pool_count-1）
        Constant_Info[] constant_infos = new Constant_Info[constant_pool_count.getValue()];
        class_file.constant_infos = constant_infos;
        // 创建常量池项
        for (int i = 1; i < constant_pool_count.getValue(); i ++) {
            // 常量池项的tag
            U1 tag = U1.create(is);

            Constant_Info.TYPE type = Constant_Info.TYPE.getTYPE(tag.getValue());

            if (type == Constant_Info.TYPE.Constant_Unknow_Info)
                throw new Exception("Constant_Unknow_Info tag: " + tag.getValue());

            // 利用反射机制获取到类，调用create方法生成Constant_Info的派生类
            Constant_Info constant_info = (Constant_Info) Class.forName("model." + type.name())
                    .getMethod("create", InputStream.class, U1.class)
                    .invoke(null, is, tag);

            // 存入常量池数组
            constant_infos[i] = constant_info;

            if (type == Constant_Info.TYPE.Constant_Long_Info || type == Constant_Info.TYPE.Constant_Double_Info)
                constant_infos[++i] = Constant_Large_Numeric_Continued_Info.create(is, null);
        }
        // 填充常量池数据
        for (int i = 1; i < constant_pool_count.getValue(); i ++) {
            if (constant_infos[i] != null)
                constant_infos[i].fill(constant_infos);
        }
        return class_file;
    }

    private static Class_File parseAccessFlag(Class_File class_file, InputStream is) throws Exception {
        // 访问标志，access_flag（access_flag实际上是一个状态集合，通过或运算得到）
        Class_Access_Flag class_access_flag = Class_Access_Flag.create(is);
        class_file.class_access_flag = class_access_flag;
        return class_file;
    }

    private static Class_File parseClassAndInterfaces(Class_File class_file, InputStream is) throws Exception {
        // 本类
        Class_Interface_Info this_class = Class_Interface_Info.create(is, class_file.constant_infos);
        class_file.this_class = this_class;
        // 父类
        Class_Interface_Info super_class = Class_Interface_Info.create(is, class_file.constant_infos);
        class_file.super_class = super_class;
        // 接口计数器
        U2 interfaces_count = U2.create(is);
        class_file.interfaces_count = interfaces_count;
        // 解析每一个接口（从0开始，到interfaces_count-1）
        Class_Interface_Info[] interfaces = new Class_Interface_Info[interfaces_count.getValue()];
        class_file.interfaces = interfaces;
        for (int i = 0; i < interfaces_count.getValue(); i ++) {
            Class_Interface_Info interface_ = Class_Interface_Info.create(is, class_file.constant_infos);
            interfaces[i] = interface_;
        }
        return class_file;
    }

    private static Class_File parseFields(Class_File class_file, InputStream is) throws Exception {
        // 字段计数值
        U2 fields_count = U2.create(is);
        class_file.fields_count = fields_count;
        // 解析每一个字段（从0开始，到fields_count-1）
        Field_Info[] field_infos = new Field_Info[fields_count.getValue()];
        class_file.field_infos = field_infos;
        // 创建字段
        for (int i = 0; i < fields_count.getValue(); i ++) {
            Field_Info field_info = Field_Info.create(is, class_file.constant_infos);
            field_infos[i] = field_info;
        }
        return class_file;
    }

    private static Class_File parseMethods(Class_File class_file, InputStream is) throws Exception {
        // 方法计数值
        U2 methods_count = U2.create(is);
        class_file.methods_count = methods_count;
        // 解析每一个方法（从0开始，到methods_count-1）
        Method_Info[] method_infos = new Method_Info[methods_count.getValue()];
        class_file.method_infos = method_infos;
        // 创建方法
        for (int i = 0; i < methods_count.getValue(); i ++) {
            Method_Info method_info = Method_Info.create(is, class_file.constant_infos);
            method_infos[i] = method_info;
        }
        return class_file;
    }

    private static Class_File parseAttributes(Class_File class_file, InputStream is) throws Exception {
        // 属性计数值
        U2 attributes_count = U2.create(is);
        class_file.attributes_count = attributes_count;
        // 创建属性表
        Attribute_Info[] attribute_infos = Attribute_Info.createAttributes(is, attributes_count.getValue(), class_file.constant_infos);
        class_file.attribute_infos = attribute_infos;
        return class_file;
    }

    public static Class_File parse(String class_file_path) throws Exception {
        Class_File class_file = new Class_File();
        class_file.class_file_path = class_file_path;
        // 构造InputStream
        InputStream is = new FileInputStream(class_file_path);
        // 解析魔数
        parseMagic(class_file, is);
        // 解析版本号
        parseVersion(class_file, is);
        // 解析常量池
        parseConstantPool(class_file, is);
        // 解析访问标志
        parseAccessFlag(class_file, is);
        // 解析类与接口
        parseClassAndInterfaces(class_file, is);
        // 解析字段
        parseFields(class_file, is);
        // 解析方法
        parseMethods(class_file, is);
        // 解析属性
        parseAttributes(class_file, is);
        // 确认无字节可读
        byte[] bytes = new byte[1];
        if (is.read(bytes) != -1) {
            throw new RuntimeException("class file has bytes remained");
        }
        // 构建类文件的字节数组
        class_file.newBytes();
        // 解析类文件结束
        is.close();
        return class_file;
    }
}
