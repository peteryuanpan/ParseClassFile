package classfile;

import java.util.ArrayList;
import java.util.List;

public class TryCatchExceptionDemo {

    public int test1() {
        int x;
        try {
            x = 1;
            //throw new Exception();
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
            //return x;
        }
    }

    public String test2() {
        String x;
        try {
            x = "1";
            //throw new Exception();
            return x;
        } catch (Exception e) {
            x = "2";
            return x;
        } finally {
            x = "3";
            //return x;
        }
    }

    public List<Integer> test3() {
        List<Integer> x = new ArrayList<>();
        try {
            x.add(1);
            return x;
        } catch (Exception e) {
            x.add(2);
            return x;
        } finally {
            x.add(3);
            //return x;
        }
    }

    static class T1 {
        int a;
        public T1(int a) {
            this.a = a;
        }
        public String toString() {return String.valueOf(a);}
    }

    public T1 test4() {
        T1 t;
        try {
            t = new T1(1);
            return t;
        } catch (Exception e) {
            t = new T1(2);
            return t;
        } finally {
            t = new T1(3);
            //return x;
        }
    }

    public static void main(String[] args) {
        TryCatchExceptionDemo a = new TryCatchExceptionDemo();
        System.out.println(a.test3());
    }
}
