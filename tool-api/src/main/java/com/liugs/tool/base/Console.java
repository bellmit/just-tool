package com.liugs.tool.base;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName Console
 * @Description console
 * @Author liugs
 * @Date 2020/7/13 14:17:24
 */
public class Console {

    private static String timeStr;

    /**
     * 描述 打印
     * @param msg
     * @return void
     * @author liugs
     * @date 2020/7/13 14:22:49
     */
    public static void show(Object msg) {
        getTime();
        System.out.println(timeStr + "：" + msg);
    }

    /**
     * 描述 打印后空行，windows
     * @param msg
     * @return void
     * @author liugs
     * @date 2020/7/13 14:22:19
     */
    public static void showBl(Object msg) {
        getTime();
        System.out.println(timeStr + "：" + msg + "\r\n");
    }

    /**
     * 描述 获取当前时间
     * @return void
     * @author liugs
     * @date 2020/7/13 14:27:05
     */
    private static void getTime() {
        Date nowTime = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
        timeStr = dateFormat.format(nowTime);
    }
}
