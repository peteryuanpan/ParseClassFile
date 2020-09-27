package model;

import java.io.IOException;
import java.io.InputStream;

public class Inner_Class_Access_Flag extends Access_Flag {

    // access_flag（access_flag实际上是一个状态集合，通过或运算得到）
    private U2 value;

    // access_flag数组（枚举每个访问标志，通过与value进行与预算，若结果非0，则进入数组）
    private FLAG[] FLAGs;

    public static Inner_Class_Access_Flag create(InputStream is) throws IOException {
        Inner_Class_Access_Flag in = new Inner_Class_Access_Flag();
        in.value = U2.create(is);
        in.FLAGs = FLAG.get_array(in.value.getValue());
        in.newBytes();
        return in;
    }

    public enum FLAG {

        // 内部类是否声明为public
        ACC_PUBLIC(Short.valueOf("0001", 16)),

        // 内部类是否声明为private
        ACC_PRIVATE(Short.valueOf("0002", 16)),

        // 内部类是否声明为protected
        ACC_PROTECTED(Short.valueOf("0004", 16)),

        // 内部类是否声明为static
        ACC_STATIC(Short.valueOf("0008", 16)),

        // 内部类是否声明为final
        ACC_FINAL(Short.valueOf("0010", 16)),

        // 内部类是否为接口
        ACC_INTERFACE(Short.valueOf("0020", 16)),

        // 内部类为声明为abstract
        ACC_ABSTRACT(Short.valueOf("0400", 16)),

        // 内部类是否由用户代码产生的
        ACC_SYNTHETIC(Short.valueOf("1000", 16)),

        // 内部类是否是一个注解
        ACC_ANNOTATION(Short.valueOf("2000", 16)),

        // 内部类是否是一个枚举
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