package com.liugs.tool.util.oss;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.InputStream;

/**
 * @ClassName OssUtil
 * @Description oss上传
 * @Author liugs
 * @Date 2021/3/5 22:06:55
 */
@Slf4j
public class OssUtil {
    private static final String BACKSLASH = "/";

    @Value("${oss.endpoint:}")
    private String endpoint;

    @Value("${oss.access.key.id:}")
    private String accessKeyId;

    @Value("${oss.access.key.secret:}")
    private String accessKeySecret;

    @Value("${oss.bucket.name:}")
    private String bucketName;

    @Value("${oss.access.url:}")
    private String accessUrl;

    private OSS ossClient;

    /**
     * 描述 获取oss操作客户端
     * @return com.aliyun.oss.OSS
     * @author liugs
     * @date 2021/3/5 22:16:40
     */

    public OssUtil() {
        if(this.ossClient == null) {
            ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
            conf.setSupportCname(false);
            this.ossClient = (new OSSClientBuilder()).build(endpoint, accessKeyId, accessKeySecret);
        }
    }


    /**
     * 描述 上传文件到指定目录，返回路径
     * @param targetPath
     * @param fileName
     * @param file
     * @return java.lang.String
     * @author liugs
     * @date 2021/3/6 8:32:12
     */
    public  String  uploadFile(String targetPath, String fileName, InputStream file) {
        String filePath = getFilePath(targetPath, fileName);
        ossClient.putObject(bucketName, targetPath, file);
        return accessUrl + BACKSLASH + filePath;
    }

    private String getFilePath(String targetPath, String fileName) {
        if (!targetPath.endsWith(BACKSLASH)) {
            targetPath = targetPath + BACKSLASH;
        }
        return targetPath + fileName;
    }
}
