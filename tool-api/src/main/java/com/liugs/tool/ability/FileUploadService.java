package com.liugs.tool.ability;

import com.liugs.tool.ability.bo.FileUploadReqBO;
import com.liugs.tool.ability.bo.FileUploadRspBO;

import java.io.IOException;

/**
 * @ClassName FileUploadService
 * @Description 文件上传服务
 * @Author liugs
 * @Date 2022/1/4 10:25
 */
public interface FileUploadService {

    /**
     * 描述 文件上传
     * @param reqBO
     * @return com.liugs.tool.ability.bo.FileUploadRspBO
     * @author liugs
     * @date 2022/1/4 10:26
     */
    FileUploadRspBO upload(FileUploadReqBO reqBO) throws IOException;
}
