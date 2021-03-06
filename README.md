# ParseClassFile
深入理解JAVA虚拟机，类文件结构解析

- [项目背景](#项目背景)
- [成果展示](#成果展示)
- [浅析类文件结构](#浅析类文件结构)
- [浅析数据结构](#浅析数据结构)
  - [Class_File](#Class_File)
  - [Unsigned](#Unsigned)
  - [U1U2U4U8](#U1U2U4U8)
  - [UString](#UString)
  - [Constant_Info](#Constant_Info)
  - [Constant_Class_Info](#Constant_Class_Info)
  - [Class_Access_Flag](#Class_Access_Flag)
  - [](#)
- [关键技术点](#关键技术点)
  - [利用反射实现常量池实例初始化](#利用反射实现常量池实例初始化)
  - [利用继承加反射实现统一格式输出](#利用继承加反射实现统一格式输出)

### 项目背景

我在学习JAVA虚拟机，其中有一个关键环节是学习类文件的结构

JAVA程序或文件，需要编译成.class文件，然后交于虚拟机去加载、运行

对类文件结构的理解程度，决定了对虚拟机原理的理解深度

我在 [深入理解JAVA虚拟机](https://github.com/peteryuanpan/notebook/blob/master/深入理解JAVA虚拟机) 中写了好多篇笔记，都是关于虚拟机原理的，其中 [第2章类文件结构与字节码指令](https://github.com/peteryuanpan/notebook/tree/master/深入理解JAVA虚拟机#第2章类文件结构与字节码指令) 是类文件结构解析的章节，该项目是第2章理论的具体实现项目，第2章是该项目的理论文档

我还参考了周志明的《深入理解JAVA虚拟机》第二版，其中第6章就是专门用于分析类文件结构的

我希望写一个简单的项目，核心功能是：**读入一份类文件，将每一个字节的含义按照特有的格式输出**

### 成果展示

类文件、源文件、解析结果都在 [src/main/resources/classfile](src/main/resources/classfile) 下

其中类文件是二进制文件，无法直接打开查看，可以用SublimeText查看十六进制格式，或者IDEA查看反编译源码

源文件是.java格式的，解析结果是.txt格式的，可以直接打开查看

|类文件|源文件|解析结果|说明|
|--|--|--|--|
|[Object.class](src/main/resources/classfile/Object.class)|[Object.java](src/main/resources/classfile/Object.java)|[ResultObject.txt](src/main/resources/classfile/ResultObject.txt)|JDK中常用的类|
|[VerySimpleClassFile.java](src/main/resources/classfile/VerySimpleClassFile.java)|[VerySimpleClassFile.class](src/main/resources/classfile/VerySimpleClassFile.class)|[ResultVerySimpleClassFile.txt](src/main/resources/classfile/ResultVerySimpleClassFile.txt)|一个最简单的类|
|[VeryUsefulClassFile.java](src/main/resources/classfile/VeryUsefulClassFile.java)|[VeryUsefulClassFile.class](src/main/resources/classfile/VeryUsefulClassFile.class)|[ResultVeryUsefulClassFile.txt](src/main/resources/classfile/ResultVeryUsefulClassFile.txt)|一个非常有用的类|
|[TryCatchExceptionDemo.class](src/main/resources/classfile/TryCatchExceptionDemo.class)|[TryCatchExceptionDemo.java](src/main/resources/classfile/TryCatchExceptionDemo.java)|[ResultTryCatchExceptionDemo.txt](src/main/resources/classfile/ResultTryCatchExceptionDemo.txt)|一个用于解释异常处理的类|
|[IntegerNumberDemo.class](src/main/resources/classfile/IntegerNumberDemo.class)|[IntegerNumberDemo.java](src/main/resources/classfile/IntegerNumberDemo.java)|[ResultIntegerNumberDemo.txt](src/main/resources/classfile/ResultIntegerNumberDemo.txt)|一个用于解释整型变量的类|

### 浅析类文件结构

简单来说，假设有下面这一份很普通的JAVA类

```java
package classfile;

public class VerySimpleClassFile {
}
```

它的class文件用sublimeText打开是这样的

```
cafe babe 0000 0034 000d 0a00 0300 0a07
000b 0700 0c01 0006 3c69 6e69 743e 0100
0328 2956 0100 0443 6f64 6501 000f 4c69
6e65 4e75 6d62 6572 5461 626c 6501 000a
536f 7572 6365 4669 6c65 0100 1856 6572
7953 696d 706c 6543 6c61 7373 4669 6c65
2e6a 6176 610c 0004 0005 0100 1d63 6c61
7373 6669 6c65 2f56 6572 7953 696d 706c
6543 6c61 7373 4669 6c65 0100 106a 6176
612f 6c61 6e67 2f4f 626a 6563 7400 2100
0200 0300 0000 0000 0100 0100 0400 0500
0100 0600 0000 1d00 0100 0100 0000 052a
b700 01b1 0000 0001 0007 0000 0006 0001
0000 0003 0001 0008 0000 0002 0009 
```

引用自《深入理解JAVA虚拟机》第二版6.3节
- Class文件是一组以8位字节为基础单位的二进制流，各个数据项目严格按照顺序紧凑地排列在Class文件之中，中间没有添加任何分隔符，这使得整个Class文件中存储的内容几乎全部是程序运行的必要数据，没有空隙存在。当遇到需要占用8位字节以上空间的数据项时，会按照高位在前（Big-Endian）的方式分割成若干个8位字节进行存储
- 根据Java虚拟机规范的规定，Class文件格式采用一种类似于C语言结构体的伪结构来存储数据，这种伪结构中只有两种数据类型：无符号数和表，后面的解析都要以这两种数据类型为基础
- 无符号数属于基本数据类型，以u1、u2、u4、u8来分别代表1个字节、2个字节、4个字节、8个字节的无符号数，无符号数可以用来描述数字、索引引用、数量值或者按照UTF-8编码构成字符串值
- 表示由多个无符号数或者其他表作为数据项构成的复合数据类型，所有表都习惯地以_info结尾，表用于描述有层次关系的复合结构的数据，整个Class文件本质上就是一张表

根据上面对无符号数以及表的描述，我们可以简单的得到以下结论
- 类文件中数据只有两种：无符号数、表
- 无符号数有4种，分别是u1、u2、u4、u8，表示4种字节
- 表示由无符号数或者表生成的复合数据类型
- Class文件也是一张表

Class文件最外层的格式如下

|序号|类型|名称|数量|
|--|--|--|--|
|1|U4|magic|1|
|2|U2|minor_version|1|
|3|U2|major_version|1|
|4|U2|costant_pool_count|1|
|5|cp_info|costant_pool|costant_pool_count - 1|
|6|U2|access_flags|1|
|7|U2|this_class|1|
|8|U2|super_class|1|
|9|U2|interfaces_count|1|
|10|U2|interfaces|interfaces_count|
|11|U2|fields_count|1|
|12|field_info|field|fields_count|
|13|U2|methods_count|1|
|14|method_info|method|methods_count|
|15|U2|attributes_count|1|
|16|attribute_info|attributes|attributes_count|

这样我们就可以去定义数据结构了

在定义数据结构之前，我们先简单说明一下解析类文件结构主类

可以看出，解析类文件结构主类也是按照类文件最外层结构来设计的，每个函数对应一种类型，分别是**魔数、版本号、常量池、访问标志、本类父类接口、字段表、方法表、属性表**

[ParseClassFile.java](src/main/java/parse/ParseClassFile.java)
```java
public class ParseClassFile {

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
```

### 浅析数据结构

全部定义完后继承关系如下（未展开）

![image](https://user-images.githubusercontent.com/10209135/95332987-bbb69880-08de-11eb-83e6-486daeef639f.png)

#### Class_File

根据Class文件最外层结构，可以定义Class_File类，它是继承Table类的

[Class_File.java](src/main/java/model/Class_File.java)
```java
public class Class_File extends Table {

    public U4 magic;

    public U2 minor_version;

    public U2 major_version;

    public U2 constant_pool_count;

    public Constant_Info[] constant_infos;

    public Class_Access_Flag class_access_flag;

    public Class_Interface_Info this_class;

    public Class_Interface_Info super_class;

    public U2 interfaces_count;

    public Class_Interface_Info[] interfaces;

    public U2 fields_count;

    public Field_Info[] field_infos;

    public U2 methods_count;

    public Method_Info[] method_infos;

    public U2 attributes_count;

    public Attribute_Info[] attribute_infos;
}
```

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

#### U1U2U4U8

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

来看一下toString()方法，返回的结果是 value(parseBytesToHexString) 的形式，比如主版本的输出结果例子：major_version: 52(0x0034)，它表示JDK8

#### UString

我还额外定义了一个UString基本类型，它没有在规范中

[UString.java](src/main/java/model/UString.java)
```java
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

#### Table

接下来定义表

表示由无符号数或者表生成的复合数据类型

[Table.java](src/main/java/model/Table.java)
```java
public abstract class Table extends Unsigned {

    /**
     * 实现字节数组bytes的new动作
     * @return
     */
    protected void newBytes() {
        List<byte[]> list_bytes = getListByteOfDeclaredFields();
        this.bytes = ArrayUtils.newarray(list_bytes);
    }
```

Table类中最关键的是toString方法，它定义了输出输出每一种表类型的结构，这一部分我们放到后面单独展开
```java
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
```

### Constant_Info

Constant_Info类继承的是Table类，它是所有常量池项的父类，它也重写了toString方法，与Table类的toString方法略有不同，多了输出tag标志

[Constant_Info.java](src/main/java/model/Constant_Info.java)
```java
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
```

JDK8中常量池有17种类型，这里就不一一列举出来了

```java
    /**
     * 常量池项的类型，JDK8中有17种（不算CONSTANT_UNKNOW_info）
     */
    public enum TYPE {

        Constant_Unknow_Info((byte)0),

        Constant_Utf8_Info((byte)1),

        Constant_Integer_Info((byte)3),
...
```

这里展开一下Constant_Info在继承关系树中的子树，规范中一共是17种常量池项，我多定义了一种Constant_Large_Numeric_Continued_Info，它是Constant_Double_Info和Constant_Long_Info之后必存在的一个常量池项

![image](https://user-images.githubusercontent.com/10209135/95333512-747cd780-08df-11eb-8257-14cafc574ca5.png)

#### Constant_Class_Info

关于常量池项，我们就举一个例子，其他的常量池项就不展开一一说明了，可以看代码

Constant_Class_Info表示类后接口的符号引用，它有两个子项，一个是tag（U1类型），一个是name_index（U2类型）

U1类型的tag是每个常量池项必有的字段，因此放到Constant_Info类中定义了

name_index是一个指向Constant_Utf8_Info类型的常量池的标志数

常量池前有一个U2变量constant_pool_count，表示常量池的个数，常量池下标是从1开始的（比较特殊，其他的字段表、方法表都是从0），1,2,3,...,constant_pool_count-1 是所有常量池的标志（注意，是-1）

那么name_index也就是 1,2,3,...,constant_pool_count-1 中的一个数，它指向的常量池项是Constant_Utf8_Info类型的（为了保证它，我加了fill方法来校验，就不展开说了）

同时，为了存储和输出更具体的数据，我在许多常量池类中都加了具体index值指向的结果，比如 Constant_Info 类中的 Constant_Utf8_Info valueof_name_index

[Constant_Class_Info.java](src/main/java/model/Constant_Class_Info.java)
```java
public class Constant_Class_Info extends Constant_Info {

    // tag: 7

    // 指向全限定名常量项的索引
    private U2 name_index;

    // index索引的具体值
    private Constant_Utf8_Info valueof_name_index;

    public Constant_Utf8_Info getValueofNameIndex() {
        return valueof_name_index;
    }

    public static Constant_Class_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Class_Info ci = new Constant_Class_Info();
        ci.tag = tag;
        ci.name_index = U2.create(is);
        ci.newBytes();
        return ci;
    }

    public Constant_Class_Info fill(Constant_Info[] constant_infos) {
        fillForException(name_index, constant_infos, Constant_Utf8_Info.class);
        valueof_name_index = (Constant_Utf8_Info) constant_infos[name_index.getValue()];
        return this;
    }
}
```

#### Class_Access_Flag

Access_Flag有4种：Class_Access_Flag、Field_Access_Flag、Method_Access_Flag、Inner_Class_Access_Flag

这个类相互之间都非常相似，内部都有一个枚举类，枚举类表示的字段含义很相近

这里只以Class_Access_Flag举例子，其它类不讨论

Class_Access_Flag表示类的访问标志，必须一提的是，Access_Flag系列表示的都是状态，因此都有一个U2 value值

理解二进制的朋友一定很容易理解这个概念，比如ACC_PUBLIC表示0x0001，ACC_SUPER表示0x0020，类含只有这2个属性，那么value = 0x0001 | 0x0020 = 0x0021

注意Class_Access_Flag与Inner_Class_Access_Flag不同，后者是内部类，内部类外层一个有一个类，而前者表示的类没有被任何类包含

一个类，有许多种属性，下面列举部分
- ACC_PUBLIC：是否是为public
- ACC_FINAL：是否为final
- ACC_INTERFACE：是否是接口
- ACC_ENUM：是否是枚举类

[Class_Access_Flag.java](src/main/java/model/Class_Access_Flag.java)
```java
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
```

......

### 关键技术点

#### 利用反射实现常量池实例初始化

小标题可能比较抽象，我以具体例子来说明

就拿常量池来说，注意到在 [ParseClassFile.java#L27](src/main/java/parse/ParseClassFile.java#L27) 中，有一段如下的函数

```java
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
```

Constant_Info类是常量池项的公共父类，Table类是Constant_Info类的父类

parseConstantPool方法中，要做的事情是
- 读取tag的值
- 根据tag值查对应哪个Constant_Info
- 初始化具体Constnat_Info的实例，并存入常量池数组中
- 给每一个Constant_Info实例填充数据

Constant_Info类有18个子类，如下图

![image](https://user-images.githubusercontent.com/10209135/95333512-747cd780-08df-11eb-8257-14cafc574ca5.png)

难道我们要每一个子类都写一个create方法语句吗，诸如
```java
U1 tag = U1.create(is);
Constant_Info.TYPE type = Constant_Info.TYPE.getTYPE(tag.getValue());
switch(type) {
  case Constant_Class_Info:
    Constant_Class_Info constant_class_info = Constant_Class_Info.create(is, tag);
    break;
  case Constant_Double_Info:
    Constant_Double_Info constant_double_info = Constant_Double_Info.create(is, tag);
    break;
  case Constant_Dynamic_Info:
    Constant_Dynamic_Info constant_dynamic_info = Constant_Dynamic_Info.create(is, tag);
    break;
...写18个case...
}
constant_infos[i] = constant_info;
```

这样肯定是非常不合适的，它不满足设计模式六大原则之一：开闭原则（Open Close Principle）

> 开闭原则的意思是：对扩展开放，对修改关闭。在程序需要进行拓展的时候，不能去修改原有的代码，实现一个热插拔的效果。简言之，是为了使程序的扩展性好，易于维护和升级。

如果后面还有新的Constant_Info类添加进来（对于常量池来说，数量相对有限，但对于Attribute_Info来说，可不一定了），我还要去修改swtich.. case.. 中的代码

如果我中间某个逻辑错了，或者希望添加逻辑（比如create后再干一件事），我需要去添加18行代码...

总之，这样做是很麻烦，不聪明的

取而代之的，使用反射的方法来实现，会比较合适

```java
// 利用反射机制获取到类，调用create方法生成Constant_Info的派生类
Constant_Info constant_info = (Constant_Info) Class.forName("model." + type.name())
        .getMethod("create", InputStream.class, U1.class)
        .invoke(null, is, tag);
```

上面这段代码巧妙运用了反射机制，会去加载model.typeName的类

将Constant_Info.TYPE中的每个字段名字与Constant_Info每个子类名设计成一样，且Constant_Info每个子类名都有一个static修饰的create方法，且形参都是InputStream.class及U1.class

比如Constant_Class_Info
```java
public class Constant_Class_Info extends Constant_Info {
    public static Constant_Class_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Class_Info ci = new Constant_Class_Info();
        ci.tag = tag;
        ci.name_index = U2.create(is);
        ci.newBytes();
        return ci;
    }
}
```

比如Constant_Double_Info
```java
public class Constant_Double_Info extends Constant_Info {
    public static Constant_Double_Info create(InputStream is, U1 tag) throws IOException {
        Constant_Double_Info ci = new Constant_Double_Info();
        ci.tag = tag;
        ci.value = U8.create(is);
        ci.newBytes();
        return ci;
    }
}
```

这样的话，如果有一个新的常量池需要添加，我不需要去修改parseConstantPool中的代码，只需要新增一个Constant_Info的子类，实现好成员字段，静态create方法等即可了

同样的，在Attribute_Info中也有类似的技术

类、字段表、方法表都含有属性表的属性，Attribute_Info中提供一个方法统一实现属性表的创建（逐一读入name_index、length，然后初始化每一个Attribute_对象）

[Attribute_Info.java#L51](src/main/java/model/Attribute_Info.java#L51)
```java
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
```

#### 利用继承加反射实现统一格式输出

看过成果展示的朋友，会不会好奇，解析结果中的输出格式是怎么样的，具体是如何实现的

[ResultClassFileDemo0.txt](src/main/resources/classfile/ResultClassFileDemo0.txt)

```
--------Begin Constant Pool--------
constant_pool_count: 38(0x0026)
[1] Constant_Methodref_Info(0x0a00070018)
tag: [10(0x0a)]
class_index: [7(0x0007)]
valueof_class_index: [Constant_Class_Info(0x07001f), tag: [7(0x07)], name_index: [31(0x001f)], valueof_name_index: [Constant_Utf8_Info(0x0100106a6176612f6c616e672f4f626a656374), tag: [1(0x01)], length_string: [16(0x0010)], value_string: [java/lang/Object(0x6a6176612f6c616e672f4f626a656374)]]]
name_and_type_index: [24(0x0018)]
valueof_name_and_type_index: [Constant_NameAndType_Info(0x0c000a000b), tag: [12(0x0c)], name_index: [10(0x000a)], valueof_name_index: [Constant_Utf8_Info(0x0100063c696e69743e), tag: [1(0x01)], length_string: [6(0x0006)], value_string: [<init>(0x3c696e69743e)]], descriptor_index: [11(0x000b)], valueof_descriptor_index: [Constant_Utf8_Info(0x010003282956), tag: [1(0x01)], length_string: [3(0x0003)], value_string: [()V(0x282956)]]]
...

--------Begin Class & Interfaces--------
[this_class] Class_Interface_Info(0x0003)
class_index: [3(0x0003)]
valueof_class_index: [Constant_Class_Info(0x07001a), tag: [7(0x07)], name_index: [26(0x001a)], valueof_name_index: [Constant_Utf8_Info(0x010018636c61737366696c652f436c61737346696c6544656d6f30), tag: [1(0x01)], length_string: [24(0x0018)], value_string: [classfile/ClassFileDemo0(0x636c61737366696c652f436c61737346696c6544656d6f30)]]]
valueof_name_index: [Constant_Utf8_Info(0x010018636c61737366696c652f436c61737346696c6544656d6f30), tag: [1(0x01)], length_string: [24(0x0018)], value_string: [classfile/ClassFileDemo0(0x636c61737366696c652f436c61737346696c6544656d6f30)]]
...

--------Begin Fields--------
fields_count: 1(0x0001)
[0] Field_Info(0x0000000800090000)
access_flag: [Field_Access_Flag(0x0000), value: [0(0x0000)], FLAGs: []]
name_index: [8(0x0008)]
valueof_name_index: [Constant_Utf8_Info(0x01000161), tag: [1(0x01)], length_string: [1(0x0001)], value_string: [a(0x61)]]
descriptor_index: [9(0x0009)]
valueof_descriptor_index: [Constant_Utf8_Info(0x01000149), tag: [1(0x01)], length_string: [1(0x0001)], value_string: [I(0x49)]]
attributes_count: [0(0x0000)]
attributes: []
--------End Fields--------
...
```

仔细观察的朋友，应该能看出一点规律，对于一个变量T t，它的输出规则是**KEY: [VALUE]**

其中VALUE分几种情况
- 如果它是U1、U2、U4、U8 或者 UString，则**VALUE = value(parseBytesToHexString())**
```java
    public String toString() {
        return value + "(" + parseBytesToHexString() + ")";
    }
```
- 剩下的情况，都是Table类的子类，按照toString()格式输出
```java
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
```
- 对于Constant_Info类与Attribute_Info类要特殊一些，与Table类的toString()类似
```java
public abstract class Constant_Info extends Table {
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
}
```
```java
public abstract class Attribute_Info extends Table {
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
}
```

下面我们来重点说一下Table类的toString()方法

[Table.java#L120](src/main/java/model/Table.java#L120)

```java
public abstract class Table extends Unsigned {
...
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
```

可以看到toString方法实际上是分成了两个方法：toStringClass、toStringDeclaredFields

toStringClass方法，**VALUE = ClassSimpleName(parseBytesToHexString())**

toStringDeclaredFields方法，利用了反射的机制，获取this对象的DeclaredFields

枚举每个Field，**VALUE = FieldName: [VALUE(Field)]"**

VALUE(Field) 由 toStringDeclaredObject方法来解决，它会这么做
- 前提，obj非空
- 判断，obj是不是属于数组对象，obj.getClass().isArray()
- 若属于数组对象，获取数组的长度，枚举每一个元素，递归的调用toStringDeclaredObject方法
- 若非数组对象，按照toString()方法返回

可以看出来，实际上toString()的逻辑，与虚拟机对类文件数据类型的规范是对应的

我们再来看一下类文件的数据类型规范
- 类文件中数据只有两种：无符号数、表
- 无符号数有4种，分别是u1、u2、u4、u8，表示4种字节
- 表示由无符号数或者表生成的复合数据类型
- Class文件也是一张表

类似的，toString()的逻辑
- 类文件中数据只有两种：无符号数、表
- 类文件中每个数据都以**KEY: [VALUE]** 格式返回
- 无符号数有4种，分别是u1、u2、u4、u8，分别按照**VALUE = value(parseBytesToHexString())** 格式返回
- 表示由无符号数或者表生成的复合数据类型
- 枚举表的每个字段，若是数组对象，枚举数组的每个元素递归处理，否则，按照tostring()返回
- Class文件特殊处理

对于倒数第二点，表是一定含有具体字段的，按照tostring()返回，实际上是在嵌套地处理，最终一定会走到一个无符号数，按照**VALUE = value(parseBytesToHexString())** 格式返回

上面用到了“嵌套”两个字，我们在输出结果当中，可以看到大量“嵌套”的例子，比如如下

[ResultClassFileDemo1.txt#L1729](src/main/resources/classfile/ResultClassFileDemo1.txt#L1729)

```
[20] Field_Info(0x00100091008d0001009200000002009303000003db)
access_flag: [Field_Access_Flag(0x0010), value: [16(0x0010)], FLAGs: [[ACC_FINAL]]]
name_index: [145(0x0091)]
valueof_name_index: [Constant_Utf8_Info(0x010004696e7435), tag: [1(0x01)], length_string: [4(0x0004)], value_string: [int5(0x696e7435)]]
descriptor_index: [141(0x008d)]
valueof_descriptor_index: [Constant_Utf8_Info(0x01000149), tag: [1(0x01)], length_string: [1(0x0001)], value_string: [I(0x49)]]
attributes_count: [1(0x0001)]
attributes: [[Attribute_ConstantValue(0x009200000002009303000003db), name_index: [146(0x0092)], valueof_name_index: [Constant_Utf8_Info(0x01000d436f6e7374616e7456616c7565), tag: [1(0x01)], length_string: [13(0x000d)], value_string: [ConstantValue(0x436f6e7374616e7456616c7565)]], length: [2(0x00000002)], constant_index: [147(0x0093)], valueof_constant_index: [Constant_Integer_Info(0x03000003db), tag: [3(0x03)], value: [987(0x000003db)]], constant_value_clazzs: [[class model.Constant_Long_Info], [class model.Constant_Float_Info], [class model.Constant_Double_Info], [class model.Constant_Integer_Info], [class model.Constant_String_Info]]]]
```
