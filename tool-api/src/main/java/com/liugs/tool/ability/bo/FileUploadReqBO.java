package com.liugs.tool.ability.bo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * @ClassName FileUploadReqBO
 * @Description 文件上传入参
 * @Author liugs
 * @Date 2021/12/30 17:48
 */
@Data
public class FileUploadReqBO implements Serializable {

    private static final long serialVersionUID = 2742013617395924016L;

    /** 总分片数 */
    private Integer chunks;

    /** 当前分片序号 */
    private Integer currentNum;

    /** 文件名称 */
    private String fileName;

    /** md5标记 */
    private String md5Mark;

    /** 分片文件对象 */
    private MultipartFile multipartFile;

}
