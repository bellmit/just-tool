package com.liugs.tool.test;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.liugs.tool.base.Console;

/**
 * @ClassName ZNotifyCenterTest
 * @Description 通知中心测试
 * @Author liugs
 * @Date 2021/1/13 10:37:58
 */
public class ZNotifyCenterTest {

    public static void main(String[] args) {
//        sendEmail();
//        dingdangMessage();
        //发送站内信
//        sendInnerMessage();
        //查询站内信
//        queryInnerMessage();
        //中核发短信
        sendZHMessage();
    }

    private static void sendEmail() {
        String url = "http://101.200.57.239:8081/notifyApi/sendEmail";
        url = "http://localhost:8091/notifyApi/sendEmail";
        //叮当测试
        url = "http://39.98.46.250:9005/notifyApi/sendEmail";
        //叮当UAT
//        url = "http://47.92.109.10:9005/notifyApi/sendEmail";

        JSONObject reqJson = new JSONObject();
        reqJson.put("to", "lgschn@qq.com");
        reqJson.put("content", "你好！邮件测试！");
        reqJson.put("subject", "测试邮件");
        String result = HttpUtil.post(url, reqJson.toJSONString());
        Console.show(result);
    }

    private static void dingdangMessage() {

        //本机地址
        String url = "http://localhost:8091/notifyApi/sendDingDangMessage";
        //叮当开发环境
        url = "http://39.98.241.232:9015/notifyApi/sendDingDangMessage";
        //叮当测试环境
        url = "http://39.98.46.250:9005/notifyApi/sendDingDangMessage";
        //叮当UAT环境
//        url = "http://47.92.109.10:9005/notifyApi/sendDingDangMessage";

        url = "http://101.200.150.17:9005/notifyApi/sendDingDangMessage";

        JSONObject reqJson = new JSONObject();
        reqJson.put("phoneNumbers", "18523310756");
        reqJson.put("templateCode", "SMS_209162326");

        JSONObject paramJson = new JSONObject();
        paramJson.put("code", "23432423");

        reqJson.put("templateParam", paramJson.toJSONString());

        Console.show(reqJson);

        String result = HttpUtil.post(url, reqJson.toJSONString());

        Console.show(result);
    }

    /**
     * 描述 发送站内信
     * @return void
     * @author liugs
     * @date 2021/3/11 15:13:06
     */
    private static void sendInnerMessage() {
        //迪易采测试环境
        String url = "http://39.105.66.173:9005/message/sendInnerMessage";
//        url = "http://59.110.230.30:9005/message/sendInnerMessage";

//        url = "http://59.110.230.30:9005/message/sendInnerMessage";

        //本机地址
        url = "http://localhost:8091/message/sendInnerMessage";

//        url = "http://101.200.150.17:9005/message/sendInnerMessage";

        JSONObject reqJson = new JSONObject();
        reqJson.put("sendid", "2");
        reqJson.put("recid", "1");
        reqJson.put("appid", "1");
        reqJson.put("titel", "测试通知");
        reqJson.put("text", "今天天气很好哦！");

        Console.show(reqJson);

        String result = HttpUtil.post(url, reqJson);

        Console.show(result);
    }

    /**
     * 描述 查询站内信
     * @param
     * @return void
     * @author liugs
     * @date 2021/3/11 15:13:33
     */
    private static void queryInnerMessage() {
        //迪易采测试环境
        String url = "http://39.105.66.173:9005/message/selectMessage";
        //开发环境
        url = "http://39.106.107.161:9005/message/selectMessage";

        //本机测试
//        url = "http://localhost:8090/message/selectMessage";

        JSONObject reqJson = new JSONObject();

        String result = HttpRequest.post(url)
                .header("auth-token", "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTYxNTc5OTUyMTIxOCwibG9naW5Tb3VyY2UiOiJwYy13ZWIifQ.uMQ_SSVPbM78w5BGoOWJ8OOM-SNOfGDychiuT87pS5I")
                .body(reqJson.toJSONString())
                .execute().body();

        Console.show(result);
    }

    private static void sendZHMessage() {
        String url = "http://172.16.100.64:9005/notifyApi/sendZHMessage";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("content", "");
        jsonObject.put("mobile", "");

        String result = HttpUtil.post(url, jsonObject);
        Console.show(result);
    }
}
