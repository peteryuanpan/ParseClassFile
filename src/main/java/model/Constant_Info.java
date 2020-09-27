package model;

import util.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class Constant_Info extends Table {

    // 常量项必有的标志，每个标志代表一个常量项
    U1 tag;

    public U1 getTag() {
        return tag;
    }

    protected void newBytes() {
        List<byte[]> list_bytes = new ArrayList<>();
        list_bytes.addAll(getListByteOfDeclaredObject(tag));
        list_bytes.addAll(getListByteOfDeclaredFields());
        this.bytes = ArrayUtils.newarray(list_bytes);
    }

    /**
     * 对tag进行特殊处理
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toStringClass());
        sb.append("\n").append("tag: [").append(tag).append("]");
        sb.append(toStringDeclaredFields());
        return sb.toString();
    }

    /**
     * 常量池项的类型，JDK8中有17种（不算CONSTANT_UNKNOW_info）
     */
    public enum TYPE {

        Constant_Unknow_Info((byte)0),

        Constant_Utf8_Info((byte)1),

        Constant_Integer_Info((byte)3),

        Constant_Float_Info((byte)4),

        Constant_Long_Info((byte)5),

        Constant_Double_Info((byte)6),

        Constant_Class_Info((byte)7),

        Constant_String_Info((byte)8),

        Constant_Fieldref_Info((byte)9),

        Constant_Methodref_Info((byte)10),

        Constant_InterfaceMethodref_Info((byte)11),

        Constant_NameAndType_Info((byte)12),

        Constant_MethodHandle_Info((byte)15),

        Constant_MethodType_Info((byte)16),

        Constant_Dynamic_Info((byte)17),

        Constant_InvokeDynamic_Info((byte)18),

        Constant_Module_Info((byte)19),

        Constant_Package_Info((byte)20);

        private byte tag;

        public byte getTag() {
            return tag;
        }

        TYPE (byte tag) {
            this.tag = tag;
        }

        public static TYPE getTYPE(byte tag) {
            for (TYPE a : TYPE.values()) {
                if (a.getTag() == tag) return a;
            }
            return Constant_Unknow_Info;
        }
    }
}
