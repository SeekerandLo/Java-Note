## 类的加载、连接、初始化

从磁盘到内存的过程

- **加载**：查找并加载类的二进制数据，在内存中生成一个代表这个类的 class 对象

- **连接**：
    - **验证**：确保被加载类的正确性

    - **准备**：为类的 **静态变量** 分配内存，并将其初始化为 **默认值**
        ```java
        public class Test{
            private static int a = 1;
        }
        ```
       int 类型的默认值 0
    - **解析**：*把类中的 **符号引用** 转化为 **直接引用***（解释）

- **初始化：为类的静态变量赋予正确的初始值，执行静态代码块**
    ```java
    // 将上面的 a 赋值 1
    ```
<!-- TODO 画个图 -->
```java
|—— —— ——|    |—— —— —— —— ——|
|        | -> |  |—— —— ——|  |
|—— —— ——|    |  |        |  |        
              |  |—— —— ——|  |
              |      |       |
              |      ↓       |
              |  |—— —— ——|  |
              |  |        |  | 
              |  |—— —— ——|  | 
              |      |       |
              |      ↓       |
              |  |—— —— ——|  |
              |  |        |  |    |—— —— ——|              
              |  |—— —— ——|  | -> |        |
              |—— —— —— —— ——|    |—— —— ——|                
```

- Java 对类的使用方式分为两种
  
    - [主动使用](#主动使用)有七种

    - [被动使用](#被动使用)

- 所有的 Java 虚拟机实现必须在每个类或接口被 Java 程序 **首次[主动使用](主动使用)** 时才 **初始化** 它们

## 类的加载

- 类的加载指的是将类的`.class`文件中的二进制数据读入到内存中，将其放在**运行时数据区**的**方法区**内，然后在内存中创建一个 java.lang.Class 对象（规范并未说明 Class 对象位于哪里，HotSpot 虚拟机将其放在了方法区中）  
 [✨用来封装类在方法区内的数据结构]()

- 加载`.class`文件的方式
    - 从本地系统中直接加载

    - 通过网络下载 `.class` 文件
    
    - 从 `zip、jar` 等归档文件中加载 `.class` 文件
    
    - 从专有数据库中提取 `.class` 文件
    
    - **将Java源文件动态编译为 `.class` 文件**

- **类的加载的最终产品是位于内存中的 Class 对象**

- **Class 对象封装了类在方法区内的数据结构**，并且向 Java 程序员提供了访问方法区内的数据结构接口

## 类的连接

### 类的验证
- 类被加载后，就进入到 **连接** 阶段。连接就是将已经读入到内存的类的二进制数据 **合并到** 虚拟机的运行环境中去

- 类的验证内容
    - 类文件的结构检查
    
    - 语义检查

    - 字节码检查

    - 二进制兼容性的验证

### 类的准备
```java
public class Smaple{
    private static int a = 1;

    private static long b;

    static {
        b = 2;
    }
}
```
在准备阶段，Java 虚拟机为类的静态变量分配内存，并设置默认初始值，例如，在上述代码中，在 Sample 类的准备阶段，虚拟机为 int 类型的静态变量 a 分配 4 个字节的内存空间，并且赋默认值为 0。为 long 类型的静态变量 b 分配 8 个字节的内存空间，并且赋默认值为 0。

### 类的解析

## 类的初始化

- 初始化步骤
    - 假如这个类还有被 **加载** 和 **连接**，那么就先进行加载和连接

    - 假如类存在直接父类，并且这个类还有被初始化，那么先 **初始化** 其父类

    - 假如类中存在初始化语句，那么依次执行这些初始化语句

- Java 虚拟机初始化一个类时，要求它的所有父类都已经被初始化，但是这条规则不适用于接口
    - 在初始化一个类时，并不会先初始化它所实现的接口
    
    - 在初始化一个接口时，并不会先初始化它的父接口

    因此，一个父接口并不会因为它的子接口或实现类被初始化而初始化，只有当程序在首次使用该接口的静态变量时，才会导致该接口的初始化

- 只有当程序访问的静态变量或静态方法确实在当前类或当前接口中定义时，才可以认为是对类或接口的主动使用

- 调用 ClassLoader 类的 loadClass 方法加载一个类时，并不是对类的主动使用，不会导致类的初始化

- 
    ```java
    public class Smaple{
        private static int a = 1; // 在声明处初始化

        private static long b;

        private static long c;

        static {
            b = 2;                // 在静态代码块中初始化
        }
    }
    ```
    在类的初始化阶段，Java 虚拟机执行类的初始化语句，为类的静态变量 **赋予初始值**。在程序中，静态变量的初始化有两种途径：1. 在静态变量声明处进行初始化；2. 在静态代码块中初始化。如上述代码，静态变量 a 和 b 都进行了显式的初始化，而静态变量 c 没有显示初始化，它还是默认值 0

- 
    ```java
    public class Smaple{
        private static int a = 1; 

        static {
            a = 2;                
        }
    static {
            a = 4;                
        }
    
    public static void main(String[] args){
            System.out.print(a); // a = 4
        }
    }
    ```
    静态变量的声明语句和静态代码块都被看作类的初始化语句，Java 虚拟机会按照初始化语句在类文件中先后顺序来依次执行它们，例如，当上述类 Sample 被初始化后，它的静态变量 a 的值为 4



### 主动使用

- **创建类的实例**

- **访问某个类或者接口的静态变量，或者对该静态变量赋值**
    ```java
    /**
     * ✨对于静态字段来说，只有直接定义了该字段的类才会被初始化
     * ✨当一个类在初始化时，要求其父类全部已经初始化完毕
     */
    class Parent {
        public static String str = "hello Parent";
        static {
            System.out.println("Parent static");
        }
    }

    class Child extends Parent {
        public static String str2 = "hello Child";
        static {
            System.out.println("Child static");
        }
    }

    public class StaticClassTest {
        public static void main(String[] args) {
            System.out.println(Child.str);
        }
    }
    // 结果
    // Parent static
    // hello Parent
    
    // 可能会疑问，为什么没有输出 Child 中的 static 代码块呢
    // 因为，通过 Child 访问 Parent 中的 str，只访问了 Parent，所以初始化了 Parent 类，加载类中的静态代码块
    ```

    ```java
    // 将上述代码中的主方法中改为下面
    public class StaticClassTest {
        public static void main(String[] args) {
            System.out.println(Child.str2);
        }
    }

    // 结果
    // Parent static
    // Child static
    // hello Child

    // 此时是访问了 Child 类中的静态变量，加载 Child 时先加载其父类，运行父类的静态代码块，
    // 加载完毕后再加载 Child 类中的静态代码块，最后输出
    ```


- **调用类的静态方法**

- **反射 如 Class.forName("com.liy.Test");**

- **初始化一个类的子类时**

- **Java 虚拟机启动时被标明为启动类的类（Java Test) 如包含了 main 方法**

- JDK1.7 提供的动态语言支持，java.lang.invoke.MethodHandle 实例的解析结果 `REF_getStatic`、`REF_putStatic`、`REF_invokeStatic` **句柄对应的类没有初始化时则初始化**


### 被动使用
除了上述 **7** 种情况外，**其他都是被动使用**，都不会导致类的 **初始化**(三个步骤中的第三个)
