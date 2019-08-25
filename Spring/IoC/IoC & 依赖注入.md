## IoC & 依赖注入

### IoC 定义

&emsp;&emsp;IoC 是控制反转，Inverse of Control，将类的选择控制权从调用类中移除，交给第三方来控制，在 Spring 中的体现就是将 Bean 的选择权交给 Spring 容器。由容器根据配置文件去创建和管理 Bean。

&emsp;&emsp;IoC 是通过依赖注入来实现的，即应用程序在运行过程中依赖 Spring 容器动态注入对象所需的资源。

&emsp;&emsp;IoC 注入：构造器注入，属性注入，工厂方法注入

### 依赖注入的方式

- 构造器注入

- 属性注入

- 工厂方法注入
