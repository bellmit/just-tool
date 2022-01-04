package com.liugs.tool.ability.bo;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MultipartFileParam {

    /** 总分片数量 */
    private int chunks;

    /** 当前为第几块分片 */
    private int chunk;

    /** 文件名 */
    private String name;

    /** 分片对象 */
    private MultipartFile file;

    /** MD5 */
    private String md5;
}
