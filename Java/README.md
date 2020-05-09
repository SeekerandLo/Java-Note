# Java 的参数

最近有需求使用到了javaagent，发现之前都没看过java命令的参数，所以总结一下，整理常用用法。

### -d32

### -d64

### -server

### -cp

### classpath

### -D

### -verbose

### -version

### -showversion

### -help

### -X

### -ea[:&lt;packagename&gt;...|:&lt;classname&gt;]  
-enableassertions[:&lt;packagename&gt;...|:&lt;classname&gt;]

### -da[:&lt;packagename&gt;...|:&lt;classname&gt;]
-disableassertions[:&lt;packagename&gt;...|:&lt;classname&gt;]

### -esa |  -enablesystemassertions

### -dsa | -disablesystemassertions

### -agentlib:&lt;libname&gt;[=&lt;选项&gt;]
加载本机代理库 &lt;libname&gt;, 例如 -agentlib:hprof
另请参阅 -agentlib:jdwp=help 和 -agentlib:hprof=help

### -agentpath:&lt;pathname&gt;[=&lt;选项&gt;]
按完整路径名加载本机代理库

### -javaagent:&lt;jarpath&gt;[=&lt;选项&gt;]
加载 Java 编程语言代理, 请参阅 java.lang.instrument
&emsp;&emsp;最近是在工作中使用到了这个参数，和代码覆盖率有关，使用这个参数来启动jacocoagent.jar。


###  -splash:&lt;imagepath&gt;
使用指定的图像显示启动屏幕
