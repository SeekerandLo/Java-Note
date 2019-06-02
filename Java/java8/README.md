## Java 8 特性
1. [lamda表达式](#lamda-表达式)
2. [方法引用](#方法引用)

### lamda 表达式
- Lamda 表达式又称闭包，允许将函数作为参数，可以使代码更加紧凑
- 语法格式  () -> {}
 
> - 可选类型声明：不需要声明参数类型，编译器可以统一识别参数值。  
> - 可选的参数圆括号：一个参数无需定义圆括号，但多个参数需要定义圆括号。  
> - 可选的大括号：如果主体包含了一个语句，就不需要使用大括号。  
> - 可选的返回关键字：如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定明表达式返回了一个数值。

#### 函数式接口
- 使用 @FunctionInterface 接口声明，在接口只能声明一个抽象方法，再使用时可为方法定义函数体
```java
@FunctionalInterface
public interface StartInterface {
    int add(int x, int y);
}

public class Start  {
    public static void main(String[] args) {
        StartInterface start = (x, y) -> x + y;
        System.out.println(start.add(1, 2));
    }
}
```
#### 变量作用域
- lambda 表达式只能引用标记了 final 的外层局部变量，这就是说不能在 lambda 内部修改定义在域外的局部变量，否则会编译错误。
- lambda 表达式的局部变量可以不用声明为 final，但是必须不可被后面的代码修改（即隐性的具有 final 的语义）
- 在 Lambda 表达式当中不允许声明一个与局部变量同名的参数或者局部变量

### 方法引用
> - 方法引用是用来直接访问类或者实例的已经存在的方法或者构造方法。方法引用提供了一种引用而不执行方法的方式，它需要由兼容的函数式接口构成的目标类型上下文。计算时，方法引用会创建函数式接口的一个实例。
> - 当Lambda表达式中只是执行一个方法调用时，不用Lambda表达式，直接通过方法引用的形式可读性更高一些。方法引用是一种更简洁易懂的Lambda表达式。
> - 注意方法引用是一个Lambda表达式，其中方法引用的操作符是双冒号"::"

#### 引用形式

 类型 | 示例    
 ---- | ---- 
 引用静态方法	 | ContainingClass::staticMethodName 
 引用某个对象的实例方法	 |containingObject::instanceMethodName 
 引用某个类型的任意对象的实例方法 | ContainingType::methodName 
 引用构造方法	| ClassName::new

- 示例1.引用静态方法
```java
// ----------定义函数式接口--------------
@FunctionalInterface
public interface StaticMethodInterface {
    void out(String a);
}
// ----------定义静态方法----------------
public class StaticMethod {
    public static void out(String a) {
        System.out.println(a);
    }
}
// -------------------------------------
public class Start {
    private static void sayOut(StaticMethodInterface s, String a) {
        s.out(a);
    }

    public static void main(String[] args) {
        sayOut(StaticMethod::out, "引用静态方法");
    }
}
```
- 示例2.引用某个对象的实例方法，与引用静态方法类似，只是不再使用类名调用
```java
// ----------定义函数式接口--------------
@FunctionalInterface
public interface StaticMethodInterface {
    void in(String a);
}
// ----------定义对象方法----------------
public class StaticMethod {
    public void in(String a) {
        System.out.println(a);
    }
}
// -------------------------------------
public class Start {
    private static void sayIn(StaticMethodInterface s, String a) {
        s.in(a);
    }

    public static void main(String[] args) {
        StaticMethod staticMethod = new StaticMethod();
        sayIn(staticMethod::in, "引用实例方法");
    }
}
```
