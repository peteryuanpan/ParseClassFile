# ParseClassFile
深入理解JAVA虚拟机，类文件结构解析

- [项目背景](#项目背景)
- [浅析类文件结构](#浅析类文件结构)
- [浅析数据结构](#浅析数据结构)
  - [Unsigned](#Unsigned)
  - [U1U2U4U8及UString](#U1U2U4U8及UString)
  - [](#)

### 项目背景

我在学习JAVA虚拟机，其中有一个关键环节是学习类文件的结构

JAVA程序或文件，需要编译成.class文件，然后交于虚拟机去加载、运行

对类文件结构的理解程度，决定了对虚拟机原理的理解深度

我在 [深入理解JAVA虚拟机-第一至三层](https://github.com/peteryuanpan/notebook/blob/master/深入理解JAVA虚拟机-第一至三层) 中写了好多篇文章，关于虚拟机原理的，其中 [第2章类文件结构与字节码指令](https://github.com/peteryuanpan/notebook/tree/master/深入理解JAVA虚拟机-第一至三层#第2章类文件结构与字节码指令) 是类文件结构解析的章节，该项目是第2章理论的具体实现项目，第2章是该项目的理论文档

我还参考了周志明的《深入理解JAVA虚拟机》第二版，其中第6章就是专门用于分析类文件结构的

我希望写一个简单的项目，能从头到尾读取一份简单的类文件，并按一定易懂的格式输出其结果，这就是本项目最简单的目的（如果有拓展新功能，会在下面补充新的HEADER)

### 浅析类文件结构

简单来说，假设有下面这一份很普通的JAVA类

```java
package classfile;

public class ClassFileDemo0 {

    int a = 0;

    public static void main(String[] args) {
        ClassFileDemo0 demo = new ClassFileDemo0();
        System.out.println(demo.a);
    }
}
```

它的class文件用sublimeText打开是这样的

```
cafe babe 0000 0034 0026 0a00 0700 1809
0003 0019 0700 1a0a 0003 0018 0900 1b00
1c0a 001d 001e 0700 1f01 0001 6101 0001
4901 0006 3c69 6e69 743e 0100 0328 2956
0100 0443 6f64 6501 000f 4c69 6e65 4e75
6d62 6572 5461 626c 6501 0012 4c6f 6361
6c56 6172 6961 626c 6554 6162 6c65 0100
0474 6869 7301 001a 4c63 6c61 7373 6669
6c65 2f43 6c61 7373 4669 6c65 4465 6d6f
303b 0100 046d 6169 6e01 0016 285b 4c6a
6176 612f 6c61 6e67 2f53 7472 696e 673b
2956 0100 0461 7267 7301 0013 5b4c 6a61
7661 2f6c 616e 672f 5374 7269 6e67 3b01
0004 6465 6d6f 0100 0a53 6f75 7263 6546
696c 6501 0013 436c 6173 7346 696c 6544
656d 6f30 2e6a 6176 610c 000a 000b 0c00
0800 0901 0018 636c 6173 7366 696c 652f
436c 6173 7346 696c 6544 656d 6f30 0700
200c 0021 0022 0700 230c 0024 0025 0100
106a 6176 612f 6c61 6e67 2f4f 626a 6563
7401 0010 6a61 7661 2f6c 616e 672f 5379
7374 656d 0100 036f 7574 0100 154c 6a61
7661 2f69 6f2f 5072 696e 7453 7472 6561
6d3b 0100 136a 6176 612f 696f 2f50 7269
6e74 5374 7265 616d 0100 0770 7269 6e74
6c6e 0100 0428 4929 5600 2100 0300 0700
0000 0100 0000 0800 0900 0000 0200 0100
0a00 0b00 0100 0c00 0000 3800 0200 0100
0000 0a2a b700 012a 03b5 0002 b100 0000
0200 0d00 0000 0a00 0200 0000 0300 0400
0500 0e00 0000 0c00 0100 0000 0a00 0f00
1000 0000 0900 1100 1200 0100 0c00 0000
4f00 0200 0200 0000 13bb 0003 59b7 0004
4cb2 0005 2bb4 0002 b600 06b1 0000 0002
000d 0000 000e 0003 0000 0008 0008 0009
0012 000a 000e 0000 0016 0002 0000 0013
0013 0014 0000 0008 000b 0015 0010 0001
0001 0016 0000 0002 0017 
```

引用自《深入理解JAVA虚拟机》第二版6.3节
- Class文件是一组以8位字节为基础单位的二进制流，各个数据项目严格按照顺序紧凑地排列在Class文件之中，中间没有添加任何分隔符，这使得整个Class文件中存储的内容几乎全部是程序运行的必要数据，没有空隙存在。当遇到需要占用8位字节以上空间的数据项时，会按照高位在前（Big-Endian）的方式分割成若干个8位字节进行存储
- 根据Java虚拟机规范的规定，Class文件格式采用一种类似于C语言结构体的伪结构来存储数据，这种伪结构中只有两种数据类型：无符号数和表，后面的解析都要以这两种数据类型为基础
- 无符号数属于基本数据类型，以u1、u2、u4、u8来分别代表1个字节、2个字节、4个字节、8个字节的无符号数，无符号数可以用来描述数字、索引引用、数量值或者按照UTF-8编码构成字符串值
- 表示由多个无符号数或者其他表作为数据项构成的复合数据类型，所有表都习惯地以_info结尾，表用于描述有层次关系的复合结构的数据，整个Class文件本质上就是一张表

### 浅析数据结构

根据上面对无符号数以及表的描述，我们可以简单的得到以下结论
- 类文件中数据只有两种：无符号数、表
- 无符号数有4种，分别是u1、u2、u4、u8，表示4种字节
- 表示由无符号数或者表生成的复合数据类型
- Class文件也是一张表

那么，我们就可以定义数据结构了

#### Unsigned

定义抽象父类

[Unsigned.java](src/main/java/model/Unsigned.java)
```java
public abstract class Unsigned {

    // 无符号数或表的字节数组
    byte[] bytes;

    public byte[] getBytes() {
        return bytes;
    }

    /**
     * 派生类需要重写一个方法，然后实现字节数组bytes的new动作
     * @return
     */
    abstract protected void newBytes();
```

它有一个关键的成员数组变量 byte[] bytes，用于存储Unsigned类的字节数组，所有的无符号数，表，包括常量池、字段表、方法表、属性表，包括类文件，都是Unsigned类的子类

对于Unsigned类，还需要介绍两个方法，一个是read方法，一个是parseBytesToHexString方法

read方法用于从InputStream中读取bytes.length长度的字节，并存储于bytes数组中

parseBytesToHexString方法将bytes数组转为可读的16进制形式的字符串，在子类的toString方法中大量被使用到

```java
    /**
     * 通过 inputStream 读取 bytes数组长度 的字节 到 bytes数组中，并用 Big-Endian 顺序计算整型值
     * @param is
     * @param bytes
     * @return
     * @throws IOException
     */
    protected long read(InputStream is, byte[] bytes) throws IOException {
        if (is == null)
            throw new NullPointerException("InputStream is null");
        int len = is.read(bytes);
        if (len == -1 || len != bytes.length)
            throw new RuntimeException("len " + len + " is not equal bytes.length " + bytes.length);
        long num = 0;
        for (int i = 0; i < bytes.length; i++) {
            num <<= 8;
            num |= bytes[i] & 0xff;
        }
        return num;
    }

    /**
     * 用 Big-Endian 顺序将字节数组转为字符串（十六进制表达式）
     * @return
     */
    public String parseBytesToHexString() {
        StringBuilder sb = new StringBuilder();
        if (bytes == null)
            throw new RuntimeException("bytes is null");
        sb.append("0x");
        for (int i = 0; i < bytes.length; i ++) {
            sb.append(Character.forDigit((bytes[i] & 0xff) / 16, 16));
            sb.append(Character.forDigit((bytes[i] & 0xff) % 16, 16));
        }
        return sb.toString();
    }
}
```

#### U1U2U4U8及UString

接下来定义4种无符号数

这里只以U1为例子说明，其他无符号数类似

U1中有一个create方法，外部函数如果需要生成一个U1，只需要使用类似于 U1 u1 = U1.create(inputStream); 的方法即可

[U1.java](src/main/java/model/U1.java)
```java
public class U1 extends Unsigned {

    // 字节数组的整型值（1个字节）
    private byte value;

    public byte getValue() {
        return value;
    }

    protected void newBytes() {
        this.bytes = new byte[1];
    }

    public static U1 create(InputStream is) throws IOException {
        U1 u1 = new U1();
        u1.newBytes();
        u1.value = (byte) u1.read(is, u1.bytes);
        return u1;
    }

    public String toString() {
        return value + "(" + parseBytesToHexString() + ")";
    }
}
```

U1中有一个成员变量 byte value;

byte类型在JAVA中占1个字节，U1表示的是1个字节的无符号数

想一想，byte是有符号数，而U1表示的是无符号数，能用byte类型来表示U1吗？实际上是可以的，这里我就不展开了，熟悉二进制的朋友一定能自己理解

类似的，U2、U4、U8都有一个成员变量，类型分别是short、int、long

我还额外定义了一个UString基本类型，它没有在规范中

[UString.java](src/main/java/model/UString.java)
```
public class UString extends Unsigned {

    // 字节数组的字符串的长度
    private int length;

    // 字节数组的字符串
    private String value;

    public int getLength() {
        return length;
    }

    public String getValue() {
        return value;
    }

    protected void newBytes() {
        this.bytes = new byte[this.length];
    }

    public static UString create(InputStream is, int length) throws IOException {
        UString ustring = new UString();
        ustring.length = length;
        ustring.newBytes();
        ustring.read(is, ustring.bytes);
        ustring.value = new String(ustring.bytes);
        return ustring;
    }

    public String toString() {
        return value + "(" + parseBytesToHexString() + ")";
    }
}
```
