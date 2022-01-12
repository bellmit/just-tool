package com.liugs.tool.util.oss;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 描述  分片上传
     * @param targetPath
     * @param fileName
     * @param filePath
     * @return java.lang.String
     * @author liugs
     * @date 2022/1/11 14:21
     */
    public String partialUpload(String targetPath, String fileName, String filePath) throws IOException {
        String path = getFilePath(targetPath, fileName);

        // 创建InitiateMultipartUploadRequest对象。
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, path);

        // 如果需要在初始化分片时设置请求头，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // 指定该Object的网页缓存行为。
        // metadata.setCacheControl("no-cache");
        // 指定该Object被下载时的名称。
        // metadata.setContentDisposition("attachment;filename=oss_MultipartUpload.txt");
        // 指定该Object的内容编码格式。
        // metadata.setContentEncoding(OSSConstants.DEFAULT_CHARSET_NAME);
        // 指定过期时间，单位为毫秒。
        // metadata.setHeader(HttpHeaders.EXPIRES, "1000");
        // 指定初始化分片上传时是否覆盖同名Object。此处设置为true，表示禁止覆盖同名Object。
        // metadata.setHeader("x-oss-forbid-overwrite", "true");
        // 指定上传该Object的每个part时使用的服务器端加密方式。
        // metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION, ObjectMetadata.KMS_SERVER_SIDE_ENCRYPTION);
        // 指定Object的加密算法。如果未指定此选项，表明Object使用AES256加密算法。
        // metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_DATA_ENCRYPTION, ObjectMetadata.KMS_SERVER_SIDE_ENCRYPTION);
        // 指定KMS托管的用户主密钥。
        // metadata.setHeader(OSSHeaders.OSS_SERVER_SIDE_ENCRYPTION_KEY_ID, "9468da86-3509-4f8d-a61e-6eab1eac****");
        // 指定Object的存储类型。
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard);
        // 指定Object的对象标签，可同时设置多个标签。
        // metadata.setHeader(OSSHeaders.OSS_TAGGING, "a:1");
        // request.setObjectMetadata(metadata);

        // 初始化分片。
        InitiateMultipartUploadResult upresult = ossClient.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识。您可以根据该uploadId发起相关的操作，例如取消分片上传、查询分片上传等。
        String uploadId = upresult.getUploadId();

        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags = new ArrayList<>();
        // 每个分片的大小，用于计算文件有多少个分片。单位为字节。
        final long partSize = 1 * 1024 * 1024L;

        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        final File sampleFile = new File(filePath);
        long fileLength = sampleFile.length();

        // 分片总数计算
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount ++;
        }

        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            InputStream inStream = new FileInputStream(sampleFile);
            // 跳过已经上传的分片。
            inStream.skip(startPos);
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(path);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(inStream);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
            uploadPartRequest.setPartSize(curPartSize);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出此范围，OSS将返回InvalidArgument错误码。
            uploadPartRequest.setPartNumber( i + 1);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
            partETags.add(uploadPartResult.getPartETag());
        }

        // 创建CompleteMultipartUploadRequest对象。
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, path, uploadId, partETags);

        // 如果需要在完成分片上传的同时设置文件访问权限，请参考以下示例代码。
        // completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.Private);
        // 指定是否列举当前UploadId已上传的所有Part。如果通过服务端List分片数据来合并完整文件时，以上CompleteMultipartUploadRequest中的partETags可为null。
        // Map<String, String> headers = new HashMap<String, String>();
        // 如果指定了x-oss-complete-all:yes，则OSS会列举当前UploadId已上传的所有Part，然后按照PartNumber的序号排序并执行CompleteMultipartUpload操作。
        // 如果指定了x-oss-complete-all:yes，则不允许继续指定body，否则报错。
        // headers.put("x-oss-complete-all","yes");
        // completeMultipartUploadRequest.setHeaders(headers);

        // 完成分片上传。
        CompleteMultipartUploadResult completeMultipartUploadResult = ossClient.completeMultipartUpload(completeMultipartUploadRequest);
        System.out.println(completeMultipartUploadResult.getETag());
        // 关闭OSSClient。
        ossClient.shutdown();

        return path;
    }

    /**
     * 描述 断点续传
     * @param targetPath
     * @param fileName
     * @param filePath
     * @return java.lang.String
     * @author liugs
     * @date 2022/1/11 14:38
     */
    public String breakpoint(String targetPath, String fileName, String filePath) throws Throwable {
        String path = getFilePath(targetPath, fileName);
        ObjectMetadata meta = new ObjectMetadata();
        // 指定上传的内容类型。
        meta.setContentType("text/plain");

        // 通过UploadFileRequest设置多个参数。
        // 填写Bucket名称和Object完整路径。Object完整路径中不能包含Bucket名称。
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, path);

        // 通过UploadFileRequest设置单个参数。
        // 填写Bucket名称。
        //uploadFileRequest.setBucketName("examplebucket");
        // 填写Object完整路径。Object完整路径中不能包含Bucket名称。
        //uploadFileRequest.setKey("exampleobject.txt");
        // 填写本地文件的完整路径。如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件。
        uploadFileRequest.setUploadFile(filePath);
        // 指定上传并发线程数，默认值为1。
        uploadFileRequest.setTaskNum(5);
        // 指定上传的分片大小。
        uploadFileRequest.setPartSize(1 * 1024 * 1024);
        // 开启断点续传，默认关闭。
        uploadFileRequest.setEnableCheckpoint(true);
        // 记录本地分片上传结果的文件。上传过程中的进度信息会保存在该文件中。
//        uploadFileRequest.setCheckpointFile("yourCheckpointFile");
        // 文件的元数据。
        uploadFileRequest.setObjectMetadata(meta);
        // 设置上传成功回调，参数为Callback类型。
        //uploadFileRequest.setCallback("yourCallbackEvent");

        // 断点续传上传。
        ossClient.uploadFile(uploadFileRequest);

        // 关闭OSSClient。
        ossClient.shutdown();

        return path;
    }
}