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
    // final 修饰的常量在编译阶段会存入到调用这个常量的方法所在的类的常量池当中，本质上并没有直接引用到定义常量的类，因此不会触发定义常量类的初始化

    // 这里指的是将常量放在 StaticClassTestFinal 的常量池中，之后 StaticClassTestFinal 与 Test 再无瓜葛，甚至可以将 Test 的 class 文件删除
    ```    