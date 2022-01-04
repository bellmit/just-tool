package com.liugs.tool.service.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liugs.tool.ability.ExportDataService;
import com.liugs.tool.ability.FileUploadService;
import com.liugs.tool.ability.ValidateService;
import com.liugs.tool.ability.bo.*;
import com.liugs.tool.base.Console;
import com.liugs.tool.constants.Constants;
import com.liugs.tool.constants.RespConstants;
import com.liugs.tool.constants.ResultStatus;
import com.liugs.tool.exception.ToolBusinessException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName ToolTestController
 * @Description test controller
 * @Author liugs
 * @Date 2021/2/27 18:12:34
 */
@RestController
@RequestMapping("tool/test")
public class ToolTestController {

    @Autowired
    private ExportDataService exportDataService;

    @RequestMapping(value = "/exportData", method = RequestMethod.POST)
    @ResponseBody
    public Object exportData(ExportDataServiceReqBo reqBo) {
        return exportDataService.exportData(reqBo);
    }

    @Autowired
    private ValidateService validateService;

    @RequestMapping(value = "/validate", method = RequestMethod.POST)
    @ResponseBody
    public Object validate(@RequestBody ValidateServiceReqBo reqBo) {
        return validateService.validate(reqBo);
    }

    @RequestMapping(value = "/validateRule", method = RequestMethod.POST)
    @ResponseBody
    public Object validateRule(@RequestBody ValidateServiceReqBo reqBo) {
        return validateService.validateRule(reqBo);
    }



    @RequestMapping(value = "/download", method = RequestMethod.POST)
    @ResponseBody
    public Object download(@RequestBody ValidateServiceReqBo reqBo) throws InterruptedException {
        Console.show("来了");
        JSONObject json = new JSONObject();

        JSONObject dataJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 200; i ++) {
            JSONObject data = new JSONObject();
            data.put("field_1", "1212");
            data.put("field_2", "1212");
            data.put("field_3", "1212");
            jsonArray.add(data);
        }

        dataJson.put("pageNo", "1");
        dataJson.put("pageSize", "200");
        dataJson.put("recordsTotal", "30000");
        dataJson.put("total", "150");
        dataJson.put("rows", jsonArray);

        json.put("code", "0");
        json.put("data", dataJson);
        json.put("message", "成功");
        Thread.sleep(6000);
        return json;
    }


    @Autowired
    private FileUploadService fileUploadService;

    @RequestMapping(value = "/fileUpload", method = RequestMethod.POST)
    @ResponseBody
    public FileUploadRspBO fileUpload(MultipartFileParam param, HttpServletRequest request) throws IOException {
        if (!ServletFileUpload.isMultipartContent(request)) {
            throw new ToolBusinessException(RespConstants.RESP_CODE_FAILED, "不包含多部分内容");
        }
        FileUploadReqBO reqBO = new FileUploadReqBO();
        reqBO.setChunks(param.getChunks());
        reqBO.setCurrentNum(param.getChunk());
        reqBO.setFileName(param.getName());
        reqBO.setMultipartFile(param.getFile());
        reqBO.setMd5Mark(param.getMd5());

        return fileUploadService.upload(reqBO);
    }


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @RequestMapping(value = "getUpdateProgress", method = RequestMethod.POST)
    @ResponseBody
    public Object checkFileMd5(String md5) throws IOException {
        Object processingObj = stringRedisTemplate.opsForHash().get(Constants.FILE_UPLOAD_STATUS, md5);
        if (processingObj == null) {
            return new ResultVo(ResultStatus.NO_HAVE);
        }
        String processingStr = processingObj.toString();
        boolean processing = Boolean.parseBoolean(processingStr);
        String value = stringRedisTemplate.opsForValue().get(Constants.FILE_MD5_KEY + md5);
        if (processing) {
            return new ResultVo(ResultStatus.IS_HAVE, value);
        } else {
            File confFile = new File(value);
            byte[] completeList = FileUtils.readFileToByteArray(confFile);
            List<String> missChunkList = new LinkedList<>();
            for (int i = 0; i < completeList.length; i++) {
                if (completeList[i] != Byte.MAX_VALUE) {
                    missChunkList.add(i + "");
                }
            }
            return new ResultVo<>(ResultStatus.ING_HAVE, missChunkList);
        }
    }


}
