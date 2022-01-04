package com.liugs.tool.util;

import sun.misc.Cleaner;

import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * @ClassName FileUploadUtil
 * @Description
 * @Author liugs
 * @Date 2022/1/4 16:24
 */
public class FileUploadUtil {

    /**
     * 描述 释放MappedByteBuffer
     * 说明 在MappedByteBuffer释放后再对它进行读操作的话就会引发jvm crash，在并发情况下很容易发生
     *      在释放时另一个线程正开始读取，于是crash就发生了。所以为了系统稳定性释放前一般需要检 查是否还有线程在读或写
     * @param mappedByteBuffer
     * @return void
     * @author liugs
     * @date 2022/1/4 14:55
     */
    public static void freedMappedByteBuffer(MappedByteBuffer mappedByteBuffer) {
        try {
            if (mappedByteBuffer == null) {
                return;
            }

            mappedByteBuffer.force();
            AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
                try {
                    Method getCleanerMethod = mappedByteBuffer.getClass().getMethod("cleaner", new Class[0]);
                    getCleanerMethod.setAccessible(true);
                    Cleaner cleaner = (Cleaner) getCleanerMethod.invoke(mappedByteBuffer,
                            new Object[0]);
                    cleaner.clean();
                } catch (Exception e) {
                    System.out.println("clean MappedByteBuffer error!!!" + e);
                }
                System.out.println("clean MappedByteBuffer completed!!!");
                return null;
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
