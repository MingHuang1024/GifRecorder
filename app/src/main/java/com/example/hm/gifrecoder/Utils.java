package com.example.hm.gifrecoder;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;

/**
 * @author hm  17-12-21
 */
public class Utils {
    public static boolean createFile(String filePath) {
        boolean result = false;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                result = file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 创建目录
     *
     * @param directory
     * @return
     */
    public static boolean createDirectory(String directory) {
        boolean result = false;
        File file = new File(directory);
        if (!file.exists()) {
            result = file.mkdirs();
        }
        return result;
    }

    /**
     * 递归删除文件夹下面的文件和文件夹
     *
     * @param filePath
     */
    public static void deleteDirectory(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File myfile : files) {
                deleteDirectory(filePath + "/" + myfile.getName());
            }

            file.delete();
        }
    }

    /**
     * 格式化日期（yyyyMMddHHmmss）
     *
     * @param date 日期
     * @return
     */
    public static String formatDate(Date date) {
        return android.text.format.DateFormat.format("yyyyMMddHHmmss", date).toString();
    }
}