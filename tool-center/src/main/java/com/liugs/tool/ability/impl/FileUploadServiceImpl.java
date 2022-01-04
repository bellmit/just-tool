package com.liugs.tool.ability.impl;

import com.liugs.tool.ability.FileUploadService;
import com.liugs.tool.ability.bo.FileUploadReqBO;
import com.liugs.tool.ability.bo.FileUploadRspBO;
import com.liugs.tool.constants.Constants;
import com.liugs.tool.constants.RespConstants;
import com.liugs.tool.exception.ToolBusinessException;
import com.liugs.tool.util.FileUploadUtil;
import com.liugs.tool.util.ToolRspUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName FileUploadServiceImpl
 * @Description 文件上传 实现类
 * @Author liugs
 * @Date 2022/1/4 10:27
 */
@Slf4j
@Service("fileUploadService")
public class FileUploadServiceImpl implements FileUploadService {

    private static final String TEMP_FILE_SUFFIX = "_tmp";


    @Value("${upload.temp.folder:1}")
    private String tempFolder;

    @Value("${upload.chunk.size:5 242 880}")
    private long chunkSize;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public FileUploadRspBO upload(FileUploadReqBO reqBO) throws IOException {
        // 入参校验
        validateReqArgs(reqBO);

        FileUploadRspBO retBO = ToolRspUtil.getRspBo(RespConstants.RESP_CODE_FAILED, RespConstants.RESP_DESC_FAILED, FileUploadRspBO.class);

        // 临时文件夹
        String tempDir = tempFolder + File.separator + reqBO.getMd5Mark();
        // 临时文件名
        String tempFileName = reqBO.getFileName() + TEMP_FILE_SUFFIX;
        // 临时文件夹
        File tempDirFile = new File(tempDir);
        // 临时文件存储路径
        File tempFile = new File(tempDir, tempFileName);

        try {
            FileUtils.forceMkdir(tempDirFile);
        } catch (IOException e) {
            log.error("创建临时文件夹失败：{}", e.getMessage());
            retBO.setRespDesc("创建临时文件夹失败：" + e.getMessage());
            return retBO;
        }

        // 随机访问文件对象，设置访问模式为：开放阅读和写作
        RandomAccessFile tempRaf = new RandomAccessFile(tempFile, "rw");
        // 获取文件通道
        FileChannel fileChannel = tempRaf.getChannel();

        // 写入本次分片的数据
        long offSet = chunkSize * reqBO.getCurrentNum();
        byte[] fileData = reqBO.getMultipartFile().getBytes();
        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, offSet, fileData.length);
        mappedByteBuffer.put(fileData);

        // 释放缓冲区
        FileUploadUtil.freedMappedByteBuffer(mappedByteBuffer);

        // 关闭通道
        fileChannel.close();

        // 检查并设置上传进度
        boolean isFinish = checkAndSetUploadProgress(reqBO, tempDir);

        if (isFinish) {
            boolean flag = renameFile(tempFile, reqBO.getFileName());
            log.info("Upload complete !!" + flag + " name=" + reqBO.getFileName());
        }
        return retBO;
    }

    /**
     * 描述 将临时文件重命名
     * @param tempFile
     * @param fileName
     * @return boolean
     * @author liugs
     * @date 2022/1/4 15:29
     */
    private boolean renameFile(File tempFile, String fileName) {
        //检查要重命名的文件是否存在，是否是文件
        if (!tempFile.exists() || tempFile.isDirectory()) {
            log.error("File does not exist: " + tempFile.getName());
            return false;
        }
        String p = tempFile.getParent();
        File newFile = new File(p + File.separatorChar + fileName);

        //修改文件名
        return tempFile.renameTo(newFile);
    }

    /**
     * 描述 检查并设置上传进度
     * @param reqBO
     * @param tempDir
     * @return boolean
     * @author liugs
     * @date 2022/1/4 15:07
     */
    private boolean checkAndSetUploadProgress(FileUploadReqBO reqBO, String tempDir) throws IOException {
        String fileName = reqBO.getFileName();
        File confFile = new File(tempDir, fileName + ".conf");
        RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");

        // 把该分段标记为 true 表示完成
        log.info("Set part " + reqBO.getCurrentNum() + " complete");
        accessConfFile.setLength(reqBO.getChunks());
        accessConfFile.seek(reqBO.getCurrentNum());
        accessConfFile.write(Byte.MAX_VALUE);

        //completeList 检查是否全部完成，如果数组里，全部都是(全部分片都成功上传)
        byte[] completeList = FileUtils.readFileToByteArray(confFile);
        byte isComplete = Byte.MAX_VALUE;
        for (int i = 0; i < completeList.length && isComplete == Byte.MAX_VALUE; i++) {
            //与运算, 如果有部分没有完成则 isComplete 不是 Byte.MAX_VALUE
            isComplete = (byte) (isComplete & completeList[i]);
            log.info("Check part " + i + " complete?:" + completeList[i]);
        }

        accessConfFile.close();
        if (isComplete == Byte.MAX_VALUE) {
            stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, reqBO.getMd5Mark(), "true");
            stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + reqBO.getMd5Mark(), tempDir + "/" + fileName);
            return true;
        } else {
            if (!stringRedisTemplate.opsForHash().hasKey(Constants.FILE_UPLOAD_STATUS, reqBO.getMd5Mark())) {
                stringRedisTemplate.opsForHash().put(Constants.FILE_UPLOAD_STATUS, reqBO.getMd5Mark(), "false");
            }
            if (stringRedisTemplate.hasKey(Constants.FILE_MD5_KEY + reqBO.getMd5Mark())) {
                stringRedisTemplate.opsForValue().set(Constants.FILE_MD5_KEY + reqBO.getMd5Mark(), tempDir + "/" + fileName + ".conf");
            }
            return false;
        }
    }

    /**
     * 描述 入参校验
     * @param reqBO
     * @return void
     * @author liugs
     * @date 2022/1/4 10:29
     */
    private void validateReqArgs(FileUploadReqBO reqBO) {
        if (null == reqBO) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "入参对象不能为空");
        }
        if (null == reqBO.getChunks()) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "总分片数不能为空");
        }
        if (null == reqBO.getCurrentNum()) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "当前分片序号不能为空");
        }
        if (StringUtils.isEmpty(reqBO.getFileName())) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "文件名称不能为空");
        }
        if (StringUtils.isEmpty(reqBO.getMd5Mark())) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "md5标记不能为空");
        }
        if (null == reqBO.getMultipartFile()) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "分片文件对象不能为空");
        }
    }
}
