package com.liugs.tool.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import com.liugs.tool.constants.RespConstants;
import com.liugs.tool.exception.ToolBusinessException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * @ClassName ZipUtil
 * @Description zip工具
 * @Author liugs
 * @Date 2021/8/3 15:26:39
 */
public class ZipUtil {

    public static List<String> zipToList(String filePath) throws IOException {
        List<String> list = new ArrayList<>();
        try {
            ZipFile zipFile = new ZipFile(filePath, CharsetUtil.CHARSET_UTF_8);
            ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(filePath), CharsetUtil.CHARSET_UTF_8);
            ZipEntry zipEntry;
            // 不为空，且不是文件夹
            while ((zipEntry = zipInputStream.getNextEntry()) != null && !zipEntry.isDirectory()) {
                // 判断是否是需要的文件格式
                if (zipEntry.getName().endsWith(".txt")) {
                    // 按行读取，并存放到dataList中
                    IoUtil.readLines(zipFile.getInputStream(zipEntry), CharsetUtil.CHARSET_UTF_8, list);
                }
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
            zipFile.close();
        } catch (Exception e) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "处理解压文件异常：" + e);
        }
        return list;
    }
}
