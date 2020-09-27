package model;

import util.ArrayUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class Table extends Unsigned {

    /**
     * 实现字节数组bytes的new动作
     * @return
     */
    protected void newBytes() {
        List<byte[]> list_bytes = getListByteOfDeclaredFields();
        this.bytes = ArrayUtils.newarray(list_bytes);
    }

    /**
     * 枚举this对象的全部字段（不包括父类的 且 字段的类继承Unsigned类），将bytes数组合成一个list返回
     * @return
     */
    protected List<byte[]> getListByteOfDeclaredFields() {
        List<byte[]> list_bytes = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object obj = field.get(this);
                list_bytes.addAll(getListByteOfDeclaredObject(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list_bytes;
    }

    /**
     * 对象的类需要继承Unsigned类<br>
     * 若对象非数组类型，直接返回bytes数组转化的list<br>
     * 若对象是数组类型，递归枚举每个元素，合成所有bytes数组转化的list
     * @param obj
     * @return
     */
    protected List<byte[]> getListByteOfDeclaredObject(Object obj) {
        List<byte[]> list_bytes = new ArrayList<>();
        if (obj != null) {
            if (obj.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(obj); i ++) {
                    list_bytes.addAll(getListByteOfDeclaredObject(Array.get(obj, i)));
                }
            } else if (Unsigned.class.isInstance(obj))
                list_bytes.add(((Unsigned) obj).bytes);
        }
        return list_bytes;
    }

    /**
     * 对其他数据进行填充，派生类一般会重写这个方法
     * @param constant_infos
     * @return
     * @throws Exception
     */
    public Table fill(Constant_Info[] constant_infos) {
        if (constant_infos == null)
            throw new NullPointerException("infos is null");
        return this;
    }

    /**
     * 对其他数据进行填充之前，先进行异常判断，派生类一般会调用这个方法
     * @param index
     * @param constant_infos
     * @param clazz
     * @throws Exception
     */
    protected static void fillForException(U2 index, Constant_Info[] constant_infos, Class clazz) {
        if (index == null)
            throw new NullPointerException("index is null");
        if (index.getValue() < 1 || index.getValue() >= constant_infos.length)
            throw new RuntimeException("index " + index.getValue() + " not in range [1, " + constant_infos.length + "]");
        if (!clazz.isInstance(constant_infos[index.getValue()]))
            throw new RuntimeException("not instanceof " + clazz.getName());
    }

    private static String names(Class[] clazzs) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < clazzs.length; i ++) {
            if (i != 0) sb.append(", ");
            sb.append(clazzs[i].getName());
        }
        sb.append("]");
        return sb.toString();
    }

    protected static void fillForException(U2 index, Constant_Info[] constant_infos, Class[] clazzs) {
        if (index == null)
            throw new NullPointerException("index is null");
        if (index.getValue() < 1 || index.getValue() >= constant_infos.length)
            throw new RuntimeException("index " + index.getValue() + " not in range [1, " + constant_infos.length + "]");
        Class clazz0 = null;
        for (Class clazz : clazzs) {
            if (clazz.isInstance(constant_infos[index.getValue()])) {
                clazz0 = clazz;
                break;
            }
        }
        if (clazz0 == null)
            throw new RuntimeException("not instanceof " + names(clazzs));
    }

    /**
     * 按照特有的格式返回<br>
     * 注意：派生类调用toString()方法时，会自动调用该方法，此时this指向的是派生类的对象，非本类的对象
     * @return
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toStringClass());
        sb.append(toStringDeclaredFields());
        return sb.toString();
    }

    /**
     * 返回类名 及 bytes数组的十六进制字符串
     * @return
     */
    protected String toStringClass() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append("(").append(this.parseBytesToHexString()).append(")");
        return sb.toString();
    }

    /**
     * 枚举this对象的全部字段（不包括父类的），以特殊格式返回
     * @return
     */
    protected String toStringDeclaredFields() {
        StringBuilder sb = new StringBuilder();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                sb.append("\n").append(field.getName()).append(": [");
                sb.append(toStringDeclaredObject(field.get(this))).append("]");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 若对象非数组类型，以name: [value1, value2]的格式返回<br>
     * 若对象是数组类型，递归枚举每个元素，直到不是数组类型时，以name: [[value1], [value2]]的格式返回
     * @param obj
     * @return
     */
    private String toStringDeclaredObject(Object obj) {
        StringBuilder sb = new StringBuilder();
        if (obj != null) {
            if (obj.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(obj); i ++) {
                    if (i != 0) sb.append(", ");
                    sb.append("[").append(toStringDeclaredObject(Array.get(obj, i))).append("]");
                }
            } else
                sb.append(obj.toString().replaceAll("\n", ", "));
        }
        return sb.toString();
    }
}
