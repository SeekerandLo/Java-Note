## 反射

### 什么是反射

- 在运行状态中，根据指定类名获取类的各种信息

### 草稿

### 重要的类
`ClassLoader`、`Method`、`Class`、`Constructor`

### Constructor 类构造函数的反射类

```java
DeleteNode c = (DeleteNode) Class.forName("com.liy.enhance.testcase.list.DeleteNode").getConstructor().newInstance();
```

### Method 类方法的反射类
```java
Method[] methods = Class.forName("com.liy.enhance.testcase.list.DeleteNode").getMethods();

// 获取指定方法
Method method = Class.forName("com.liy.enhance.testcase.list.RemoveNthFromEndTestCase").getDeclaredMethod("testCaseCreate", int.class);
// name 方法名字，parameterTypes 参数类型
public Method getDeclaredMethod(String name, Class<?>... parameterTypes){

}

// 
method.invoke();
// obj 类的对象，即调用方法的对象，args 参数
public Object invoke(Object obj, Object... args){

}
```
### Field 类的成员变量的反射类

```java
// 获取 public 的属性
Field[] fields = Class.forName("com.liy.enhance.testcase.list.RemoveNthFromEndTestCase").getFields();

// 
@CallerSensitive
public Field[] getFields() throws SecurityException {
    checkMemberAccess(Member.PUBLIC, Reflection.getCallerClass(), true);
    return copyFields(privateGetPublicFields(null));
}
```