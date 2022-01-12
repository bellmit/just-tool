package com.liugs.tool.util.fastdfs;

import org.apache.commons.io.FilenameUtils;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @ClassName FastDFSUtil
 * @Description
 * @Author liugs
 * @Date 2022/1/11 15:08
 */
public class FastDFSUtil {

    private StorageClient storageClient;

    private int defaultSize;

    public String[] uploadBreakpointFile(String group_name, String fileName, NameValuePair[] meta) {
        String[] results = null;
        File file = new File(fileName);
        long originalFileSize = file.length();

        byte[] file_buff;

        //NameValuePair[] vars = new NameValuePair[]{new NameValuePair("fileName", fileName), new NameValuePair("fileSize", String.valueOf(originalFileSize))};
        int number = (int) (originalFileSize / defaultSize), leftLength;
        number = originalFileSize % defaultSize == 0 ? number : number + 1;
        byte[] bytes;
        try {
            InputStream input = new FileInputStream(file);
            file_buff = new byte[input.available()];
            input.read(file_buff);

            if (originalFileSize > defaultSize) {
                // 如果文件块大，则实现分块上传，需要准备一个空的文件
                for (int i = 0; i < number; i++) {
                    // 采用追加的方式
                    if (originalFileSize - (i) * defaultSize < defaultSize) {
                        leftLength = (int) (originalFileSize - (i) * defaultSize);
                        leftLength = leftLength < 0 ? (int) originalFileSize : leftLength;
                        bytes = new byte[leftLength];
                    } else {
                        bytes = new byte[(int) defaultSize];
                        leftLength = (int) defaultSize;
                    }

                    if (i == 0) {
                        results = storageClient.upload_appender_file(group_name, bytes, 0, leftLength, FilenameUtils.getExtension(fileName), meta);
                    } else {
                        // 采用追加的方式
                        storageClient.append_file(results[0], results[1], bytes, 0, leftLength);
                    }
                }
                // 写入内容
                storageClient.modify_file(results[0], results[1], 0, file_buff, 0, file_buff.length);
            } else {
                // 如果文件比默认的文件要小，则直接上传
                results = storageClient.upload_file(group_name, file_buff, FilenameUtils.getExtension(fileName), meta);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }
}
