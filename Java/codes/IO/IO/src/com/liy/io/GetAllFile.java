package com.liy.io;

import java.io.File;
import java.util.Objects;

/**
 * File类 递归获取
 * data: 2019/4/4 22:16
 **/

public class GetAllFile {

    // 递归
    private static void getFile(File dir){
        if (dir == null || !dir.exists()) {
            return;
        }
        if (dir.isFile()) {
            System.out.println(dir.getName());
            return;
        }
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            getFile(file);
        }
    }

    public static void main(String[] args) {
        // file对象初始化需要指定路径
        // File file = new File("path");
        File file = new File("D:\\BootStrap");
        getFile(file);
    }

}
