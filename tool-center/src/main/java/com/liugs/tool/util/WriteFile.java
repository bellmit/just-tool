package com.liugs.tool.util;


import com.liugs.tool.base.Console;

import java.io.*;
import java.util.List;

/**
 * @ClassName WriteFile
 * @Description 写文件
 * @Author liugs
 * @Date 2021/2/24 16:22:55
 */
public class WriteFile {

    public static int fileNumber = 1;

    public static final String FILE_SUFFIX = ".txt";

    public static void writeFile(List<String> data, String path, int j, Long maxRecord) {

        if (j % maxRecord == 0) {
            fileNumber++;
        }
        path = path.replaceAll(FILE_SUFFIX, fileNumber + FILE_SUFFIX);
        writeFile(data, path);
    }

    /**
     * 写数据到文件
     */
    public static void writeFile(List<String> data, String path) {

        if (null == data || data.size() == 0){
            return;
        }
        Console.show("本次写到文件的数据有：" + data.size() + "条, 文件路径：" + path);

        File file = new File(path);
        FileOutputStream fileOutputStream;
        OutputStreamWriter write;
        BufferedWriter writer;
        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            fileOutputStream = new FileOutputStream(path, true);
            write = new OutputStreamWriter(fileOutputStream, "UTF-8");
            writer = new BufferedWriter(write);
            for (String str : data) {
                writer.write(str);
                writer.flush();
            }
            writer.close();
            write.close();
            fileOutputStream.close();
        } catch (IOException e) {
            Console.show("创建文件失败");
            e.printStackTrace();
        }
    }
}
