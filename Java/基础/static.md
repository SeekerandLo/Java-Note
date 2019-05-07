## static
- 使用 static 声明的方法或变量可以直接使用类名进行调用

### static变量
- 静态变量被所有的对象共享，在内存中只存在一个，在类初次加载的时候被初始化，在非静态方法中可以调用静态变量，但是在静态方法中不能调用非静态变量


### static方法
- 在静态方法中不能使用 this 关键字，所以不能调用非静态变量，最常见的静态方法是 main 方法
- 静态方法和静态变量都可通过类的对象调用


### static类


### static 代码块
- 只需要进行一次的初始化操作可以放在static代码块中进行，节省空间


### 生命周期
- 

### static 的意义
- 如果给类中的变量设置为 static，那么能确保变量在内存中只有一个，当需要唯一变量的时候可以使用 static 关键字修饰变量
- 当修饰方法时，可以不创建对象直接通过类名调用，方便快捷


### 面试题整理
> 摘自 https://www.cnblogs.com/dolphin0520/p/3799052.html
***
阅读程序，输出结果
```java
public class Test extends Base{
 
    static{
        System.out.println("test static");
    }
     
    public Test(){
        System.out.println("test constructor");
    }
     
    public static void main(String[] args) {
        new Test();
    }
}
 
class Base{
     
    static{
        System.out.println("base static");
    }
     
    public Base(){
        System.out.println("base constructor");
    }
}
```
<details>
    <summary> 点击展开答案 </summary>

```java
base static
test static
base constructor
test constructor
```
&emsp;&emsp;首先寻找 main 方法，需要加载 Test 类，Test 继承自 Base 类，于是转去加载 Base 类，加载时发现有 staic 代码块，加载代码块(输出)，加载完毕后加载 Test 类，发现有 static 代码块，加载代码块(输出)，加载所有类完毕后，开始调用方法，在调用 Test 的构造函数时会先调用父类的构造函数 Base() (输出)
，最后调用 Test() (输出)
</details>

***
阅读程序，输入结果
```java
public class Test {
    Person person = new Person("Test");
    static{
        System.out.println("test static");
    }
     
    public Test() {
        System.out.println("test constructor");
    }
     
    public static void main(String[] args) {
        new MyClass();
    }
}
 
class Person{
    static{
        System.out.println("person static");
    }
    public Person(String str) {
        System.out.println("person "+str);
    }
}
 
 
class MyClass extends Test {
    Person person = new Person("MyClass");
    static{
        System.out.println("myclass static");
    }
     
    public MyClass() {
        System.out.println("myclass constructor");
    }
}
```
<details>
    <summary> 点击展开答案 </summary>

```java
test static
myclass static
person static
person Test
test constructor
person MyClass
myclass constructor
```
&emsp;&emsp;首先寻找 main 方法，调用 MyClass() ，加载 MyClass 类，MyClass 类继承自 Test 类，去加载 Test 类，有 static 代码块(输出)，
然后加载 MyClass 类，有 static 代码块(输出)，类加载完毕，开始执行方法，调用 MyClass() 方法，首先执行父类 Test 中的构造函数，在创建父类对象时会先初始化父类中的成员变量，调用 new Person()，需先加载 Person 类，有 static 代码块(输出)，调用 Person 类构造函数出入 Test(输出)，调用 Test 构造函数(输出)，调用 MyClass 构造函数，初始化 Person 对象，因为已经加载过一次 Person 类中的 static 代码块故不再加载，只执行构造函数，传入MyClass(输出)，最后调用 MyClass 构造函数(输出)
</details>

***
阅读程序，输出结果
```java
public class Test {
     
    static{
        System.out.println("test static 1");
    }
    public static void main(String[] args) {
         
    }
     
    static{
        System.out.println("test static 2");
    }
}
```
<details>
    <summary> 点击展开答案 </summary>

```java
test static 1
test static 2
```
&emsp;&emsp;static块可以出现类中的任何地方（只要不是方法内部，记住，任何方法内部都不行），并且执行是按照static块的顺序执行的。
</details>