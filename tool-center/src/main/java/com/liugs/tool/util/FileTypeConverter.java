package com.liugs.tool.util;

import com.aspose.cells.License;
import com.aspose.cells.SaveFormat;
import com.aspose.cells.Workbook;
import org.apache.commons.io.FileUtils;

import java.io.*;

/**
 * @ClassName FileTypeConverter
 * @Description 文件类型转换工具类
 * @Author liugs
 * @Date 2021/1/18 14:26:49
 */
public class FileTypeConverter {

    /**
     * 描述 xml格式转Excel文件
     * @param targetFile
     * @param resourceFile
     * @author liugs
     * @date 2021/1/18 14:44:05
     */
    public static void xmlToExcel(String resourceFile, String targetFile) throws Exception {
        //读取临时文件
        byte[] resourceBytes = getBytesByFile(resourceFile);
        //文件转换
        byte[] targetBytes = xmlToExcelBytes(resourceBytes);
        //写入文件
        generateFileByBytes(targetBytes, targetFile);
    }

    /**
     * 描述 xml转xlsx
     * @param input
     * @return byte[]
     * @author liugs
     * @date 2021/1/18 14:40:22
     */
    private static byte[] xmlToExcelBytes(byte[] input) throws Exception {
        if (!getLicense()) {
            //解决水印问题
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Workbook wb = new Workbook(new ByteArrayInputStream(input));
            wb.save(byteArrayOutputStream, SaveFormat.XLSX);
            return byteArrayOutputStream.toByteArray();
        }finally {
            byteArrayOutputStream.close();
        }
    }

    /**
     * 描述 获取授权
     * @return boolean
     * @author liugs
     * @date 2021/1/18 14:39:55
     */
    private static boolean getLicense() {
        boolean result = false;
        try {
            InputStream is = FileTypeConverter.class.getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 描述 将文件转换成Byte数组
     * @param pathStr
     * @return byte[]
     * @author liugs
     * @date 2021/1/18 14:40:57
     */
    public static byte[] getBytesByFile(String pathStr) throws IOException {
        File file = new File(pathStr);
        FileInputStream fis = new FileInputStream(file);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
        byte[] b = new byte[1000];
        int n;
        while ((n = fis.read(b)) != -1) {
            bos.write(b, 0, n);
        }
        fis.close();
        byte[] data = bos.toByteArray();
        bos.close();
        return data;
    }


    /**
     * 描述 将Byte数组转换成文件
     * @param bytes
     * @param targetFile
     * @return void
     * @author liugs
     * @date 2021/1/18 14:41:11
     */
    public static void generateFileByBytes(byte[] bytes, String targetFile) throws IOException {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file;

        try {
            //获取父目录，判断是否存在，不存在新建
            file = new File(targetFile.substring(0, targetFile.lastIndexOf(File.separator)));
            if (!file.exists()) {
                FileUtils.forceMkdir(file);
            }
            //目标文件
            file = new File(targetFile);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } finally {
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}