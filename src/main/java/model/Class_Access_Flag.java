package model;

import java.io.IOException;
import java.io.InputStream;

public class Class_Access_Flag extends Access_Flag {

    // access_flag（access_flag实际上是一个状态集合，通过或运算得到）
    private U2 value;

    // access_flag数组（枚举每个访问标志，通过与value进行与预算，若结果非0，则进入数组）
    private FLAG[] FLAGs;

    public static Class_Access_Flag create(InputStream is) throws IOException {
        Class_Access_Flag caf = new Class_Access_Flag();
        caf.value = U2.create(is);
        caf.FLAGs = FLAG.get_array(caf.value.getValue());
        caf.newBytes();
        return caf;
    }

    public enum FLAG {

        // 是否声明为public
        ACC_PUBLIC(Short.valueOf("0001", 16)),

        // 这里没有 static，因为class文件名对应的public类不可声明为static
        // 而子类可以声明为 static，是在 Attribues 中 InnerClasses 里

        // 是否声明为protected
        ACC_FINAL(Short.valueOf("0010", 16)),

        // 是否允许使用invokespecial字节码指令的新语意
        // invokespecial指令的语意在JDK1.0.2发生过改变，为了区别这条指令使用哪种语意，JDK1.0.2之后编译出来的类的这个标志都必须为真
        ACC_SUPER(Short.valueOf("0020", 16)),

        // 标识这是一个接口
        ACC_INTERFACE(Short.valueOf("0200", 16)),

        // 是否为声明为abstract
        ACC_ABSTRACT(Short.valueOf("0400", 16)),

        // 标识这个类并非由用户代码产生的
        ACC_SYNTHETIC(Short.valueOf("1000", 16)),

        // 标识这是一个注解
        ACC_ANNOTATION(Short.valueOf("2000", 16)),

        // 标识这是个枚举
        ACC_ENUM(Short.valueOf("4000", 16));

        private short status;

        public short getStatus() {
            return status;
        }

        FLAG(short status) {
            this.status = status;
        }

        static int length(short status) {
            int len = 0;
            for (FLAG flag : FLAG.values()) {
                if ((flag.getStatus() & status) != 0) len += 1;
            }
            return len;
        }

        /**
         * 查找每个枚举，枚举的status与形参的status 与预算的结果 非0
         * @param status
         * @return
         */
        public static FLAG[] get_array(short status) {
            FLAG[] array = new FLAG[length(status)];
            int len = 0;
            for (FLAG flag : FLAG.values()) {
                if ((flag.getStatus() & status) != 0) array[len++] = flag;
            }
            return array;
        }
    }
}
