## Java 流

- 根据处理数据类型不同分为字符流，字节流

- 根据数据流向不同分为输入流，输出流


### 字符流

- 操作 char 类型数据

- Reader 输入流
```java
// FileReader
public static void main(String[] args) throws IOException {
    File file = new File("D:\\test.txt");
    FileReader fileReader = new FileReader(file);
    int n;
    StringBuilder stringBuffer = new StringBuilder();
    while ((n = fileReader.read()) != -1) {
        stringBuffer.append((char) n);
    }
    System.out.println(stringBuffer.toString());
    fileReader.close();
}
```

- Writer 输出流
```java
// FileWriter
public static void main(String[] args) throws IOException {
    File file = new File("D:\\test2.txt");
    String str = "qwewqeqweqweqw778787";
    FileWriter fileWriter = new FileWriter(file);
    for (int i = 0; i < str.length(); i++) {
        fileWriter.write((int) str.charAt(i));
    }
    fileWriter.close();
}
```



### 字节流

- 操纵 byte 类型数据，一字节等于8位

- InputStream 输入流
```java
// FileInputStream
 public static void main(String[] args) throws IOException {
    String path = "D:\\test.txt";
    File file = new File(path);
    FileInputStream inputStream = new FileInputStream(file);
    int n;
    StringBuilder stringBuffer = new StringBuilder();
    while ((n = inputStream.read()) != -1) {
        stringBuffer.append((char) n);
    }
    System.out.println(stringBuffer.toString());
    inputStream.close();
}
```

- OutputStream 输出流
```java
// FileOutputStream
public static void main(String[] args) throws IOException {
    File file = new File("D:\\test2.txt");
    FileOutputStream fileOutputStream = new FileOutputStream(file);

    String str = "qweqweqe123";
    for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        fileOutputStream.write((int) c);
    }
    fileOutputStream.close();
}
```


### 基本数据类型的长度

- byte 1 字节

- short 2 字节

- int 4 字节

- long 8 字节

- double 8 字节

- float 4 字节

- boolean

- char 2 字节