package com.yuri.cnbeta.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Gavin Kwok on 16/3/18.
 * description:文件操作工具类
 */
public class FileUtils {

    private FileUtils(){}

    /**
     * 删除整个文件
     * @param filePath 文件路径
     */
    public static void deleteFile(String filePath){
        if (TextUtils.isEmpty(filePath))
            return;
        deleteFile(new File(filePath));
    }

    /**
     * 删除整个文件
     * @param file 文件对象
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void deleteFile(File file){
        if (file != null && file.exists()){
            if (file.isDirectory()){
                File[] files = file.listFiles();
                for (File childFile : files){
                    childFile.delete();
                }
            }
            file.delete();
        }
    }

    /**
     * 清空文件夹
     * @param path 文件夹路径
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void cleanDir(String path){
        File file = new File(path);
        if (file.exists() && file.isDirectory()){
            File[] files = file.listFiles();
            for (File childFile : files){
                childFile.delete();
            }
        }
    }

    /**
     * 文件是否有子文件
     * @param file 文件对象
     */
    public static boolean fileHasChild(File file){
        if (file != null){
            if (file.exists() && file.isDirectory()){
                File[] files = file.listFiles();
                return files != null && files.length > 0;
            }
        }
        return false;
    }

    /**
     * 文件夹是否有子文件
     * @param filePath 文件路径
     */
    public static boolean fileHasChild(String filePath) {
        return !TextUtils.isEmpty(filePath) && fileHasChild(new File(filePath));
    }

    /**
     * file拷贝
     * @param src 拷贝源
     * @param dst 拷贝地址
     * @throws IOException
     */
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        out.flush();
        in.close();
        out.close();
    }
}
