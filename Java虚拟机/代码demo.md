## 
- 
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

    // 此时是访问了 Child 类中的静态变量，加载 Child 时先加载其父类，运行父类的静态代码块，加载完毕后再加载 Child 类中的静态代码块，最后输出
    ```
***
- 
    ```java
    class Test {
        public static String str = "hello world";
        static {
            System.out.println("test static");
        }
    }

    public class StaticClassTest2 {
        public static void main(String[] args) {
            System.out.println(Test.str);
        }
    }

    // 结果
    // test static
    // hello world
    ```    
    ```java
    // ✨将 str 加上 final 后
    class Test {
        public static final String str = "hello world";
        static {
            System.out.println("test static");
        }
    }

    public class StaticClassTestFinal {
        public static void main(String[] args) {
            System.out.println(Test.str);
        }
    }

    // 结果
    // hello world

    // 为什么！？
    // final 修饰的常量在编译阶段会存入到调用这个常量的方法所在的类的常
    // 量池当中，本质上并没有直接引用到定义常量的类，因此不会触发定义常量类的初始化

    // 这里指的是将常量放在 StaticClassTestFinal 的常量池中，之后
    // StaticClassTestFinal 与 Test 再无瓜葛，甚至可以将 Test 的 class 文件删除
    ```    

- 
```java
class FinalParent {
    public static final String str = UUID.randomUUID().toString();
    static {
        System.out.println("FinalParent static");
    }
}

public class FinalStaticClassTest {
    public static void main(String[] args) {
        System.out.println(FinalParent.str);
    }
}

// 结果
// FinalParent static
// 74e3602a-2b34-4b10-9268-9bcfaa7f2a78  <-(随机的UUID)

// 为什么在这个测试中输出了类的静态代码块？
// 当一个常量的值在编译阶段不能确定时，这个常量不会被放到调用类的常量池中，
// 这时在程序运行时，会导致主动使用这个类的常量所在的类，导致类的初始化
```    
***

- 
```java
class ArrayTemp {
    static {
        System.out.println("ArrayTemp static");
    }
}

public class ArrayClassTest {
    public static void main(String[] args) {
        ArrayTemp[] arrayTemps = new ArrayTemp[1];
    }
}

// 结果没有输出 ArrayTemp static

// 为什么？
// ✨对于数组实例来说，其类型是JVM在运行期间动态生成的，表示为 
// ✨[L包名.包名.包名.类名; 注意这个 [L
// 动态生成的类型，其父类型是 Object

// 对于数组来说，JavaDoc 经常将构成数组的元素称为 Component，实际上是将数组降低一个维度后的类型
```
   - 基本类型的数组的类  
     class [I  : int[]  
     class [Ljava.lang.Integer; : Integer[]   
     class [C  : char[]
     ...

***
- 关于接口的
```java
// 一个接口在初始化时不要求其父接口全部完成初始化
// 只有在真正使用到父接口的时候，（如引用接口所定义的常量时）才会初始化

interface InterfaceTestParent {
    public static final String a = UUID.randomUUID().toString();
}

interface InterfaceTestChild extends InterfaceTestParent{
    public static final int b = 2;
}



public class InterfaceStaticTest {
    public static void main(String[] args) {
        System.out.println(InterfaceTestChild.a);
    }
}

// 按如上代码测试，在第一次编译后删除编译结果文件中的 InterfaceTestParent.class
// 再次运行会出现错误，ClassNotFoundException，因为 a 的结果是在程序运行时产生的
```

