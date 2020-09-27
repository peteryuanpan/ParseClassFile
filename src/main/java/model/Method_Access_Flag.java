package model;

import java.io.IOException;
import java.io.InputStream;

public class Method_Access_Flag extends Access_Flag {

    // access_flag（access_flag实际上是一个状态集合，通过或运算得到）
    private U2 value;

    // access_flag数组（枚举每个访问标志，通过与value进行与预算，若结果非0，则进入数组）
    private FLAG[] FLAGs;

    public static Method_Access_Flag create(InputStream is) throws IOException {
        Method_Access_Flag caf = new Method_Access_Flag();
        caf.value = U2.create(is);
        caf.FLAGs = FLAG.get_array(caf.value.getValue());
        caf.newBytes();
        return caf;
    }

    public enum FLAG {

        // 方法是否声明为public
        ACC_PUBLIC(Short.valueOf("0001", 16)),

        // 方法是否声明为private
        ACC_PRIVATE(Short.valueOf("0002", 16)),

        // 方法是否声明为protected
        ACC_PROTECTED(Short.valueOf("0004", 16)),

        // 方法是否声明为static
        ACC_STATIC(Short.valueOf("0008", 16)),

        // 方法是否声明为final
        ACC_FINAL(Short.valueOf("0010", 16)),

        // 方法是否声明为synchronized
        ACC_SYNCHRONIZED(Short.valueOf("0020", 16)),

        // 方法是否由编译器产生的桥接方法
        ACC_BRIDGE(Short.valueOf("0040", 16)),

        // 方法是否接受不定参数
        ACC_VARARGS(Short.valueOf("0080", 16)),

        // 方法是否声明为native
        ACC_NATIVE(Short.valueOf("0100", 16)),

        // 方法是否为声明为abstract
        ACC_ABSTRACT(Short.valueOf("0400", 16)),

        // 方法是否声明为stictfp
        ACC_STRICTFP(Short.valueOf("0800", 16)),

        // 方法是否由编译器自动产生的
        ACC_SYNTHETIC(Short.valueOf("1000", 16));

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

