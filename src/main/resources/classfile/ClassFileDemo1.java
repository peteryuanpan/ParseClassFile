package classfile;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Orange(getName = "3333",getValue = "4444")
public class ClassFileDemo1 implements Interface1, Interface2, Interface3 {

    boolean boolean1 = false;
    boolean boolean2 = true;
    boolean boolean3 = Boolean.FALSE;
    boolean boolean4 = Boolean.TRUE;

    byte byte1 = 123;
    byte byte2 = Byte.valueOf("12345");
    byte byte3 = Byte.MAX_VALUE;
    byte byte4 = Byte.MIN_VALUE;

    char char1 = 'A';
    char char2 = '\0';
    char char3 = Character.MAX_VALUE;
    char char4 = Character.MIN_VALUE;

    short short1 = 12345;
    short short2 = Short.valueOf("123456");
    short short3 = Short.MAX_VALUE;
    short short4 = Short.MIN_VALUE;

    int int1 = 123;
    int int2 = 123456789;
    int int3 = Integer.MAX_VALUE;
    int int4 = Integer.MIN_VALUE;

    final int int5 = 987;

    float float1 = 100;
    float float2 = Float.valueOf("1234567");
    float float3 = Float.MAX_VALUE;
    float float4 = Float.MIN_VALUE;

    long long1 = 12345678987654321L;
    long long2 = -12345678987654321L;
    long long3 = Long.valueOf("12345678");
    long long4 = Long.MAX_VALUE;
    long long5 = Long.MIN_VALUE;

    double double1 = 1e3;
    double double2 = 1e100;
    double double3 = Double.valueOf("123456789");
    double double4 = Double.MAX_VALUE;
    double double5 = Double.MIN_VALUE;

    public static String string1 = "string1";
    String string2 = new String("string2");
    String string3 = string1 + string2;

    Interface1 interface1 = new Interface1() {
        public int aaa(int a) { return -1; }
        public double bbb() { return 0.0; }
    };
    int interface1_aaa1 = interface1.aaa(1);
    public double interface1_bbb1 = interface1.bbb();

    public ClassFileDemo1() throws InterruptedException {
        Thread.sleep(100);
        try {
            Thread.sleep(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    public int aaa(int aa) {
        return 0;
    }

    public double bbb() {
        return 0;
    }

    public static void main(String[] args) {
    }

    static class SubClass1 {
        int a;
    }

    class SubClass2 {
        int b;
    }


}

interface Interface1 {
    int aaa(int a);
    double bbb();
}

interface Interface2 {
}

interface Interface3 {
}

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Orange {
    String getName();
    String getValue();
}
