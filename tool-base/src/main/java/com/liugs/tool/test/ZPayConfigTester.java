package com.liugs.tool.test;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liugs.tool.base.Console;

/**
 * @ClassName ZPayConfigTester
 * @Description
 * @Author liugs
 * @Date 2021/4/13 9:45:09
 */
public class ZPayConfigTester {
    private static final String URL = "http://localhost:8081/pay/";

    private static int operateType = 4;


    public static void main(String[] args) {
        switch (operateType) {
            case 1:
                addMerchant();
                break;
            case 2:
                configMerchant();
                break;
            case 3:
                queryPaymentIns();
            default:
                queryMerchantRelPayIns();
                break;
        }
    }

    private static void queryMerchantRelPayIns() {
        String url = "http://127.0.0.1:8084/webjars/payweb/pay/rest/merchantInfoM/query/merchanRelPayInsInfo";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("merchantId", "1000000001");
//        jsonObject.put("paymentInsId", "16");
//        jsonObject.put("payMethod", "21");
        Console.show(JSON.toJSONString(jsonObject));

        Console.show(HttpUtil.post(url, jsonObject));
    }

    /**
     * 描述 配置商户
     * @param
     * @return void
     * @author liugs
     * @date 2021/4/13 9:47:59
     */
    private static void configMerchant() {
        String url = URL + "deleteRelPayPara";

        JSONObject reqJson = new JSONObject();
        reqJson.put("merchantId", "99999");
//        reqJson.put("createOperId", "99999");
//        reqJson.put("createOperName", "liugs");
//        reqJson.put("remark", "API测试");

        JSONObject paymentInsJson = new JSONObject();
//        paymentInsJson.put("paymentInsId", "2");

//        Long[] paymethods = {21L, 20L, 22L};
//        List<Long> paymethodList = Arrays.asList(paymethods);
//        paymentInsJson.put("payMethodList", paymethodList);
//
//        JSONArray paras = new JSONArray();
//
//        JSONObject appid = new JSONObject();
//        appid.put("parameterCode", "appid");
//        appid.put("parameterValue", "12121212");
//
//        JSONObject privateKey = new JSONObject();
//        privateKey.put("parameterCode", "privateKey");
//        privateKey.put("parameterValue", "23423423423423423423423423");
//
//        JSONObject publicKey = new JSONObject();
//        publicKey.put("parameterCode", "publicKey");
//        publicKey.put("parameterValue", "423423423423423423423423423423");
//
//        paras.add(appid);
//        paras.add(privateKey);
//        paras.add(publicKey);
//
//        paymentInsJson.put("payParaList", paras);

        reqJson.put("paymentIns", paymentInsJson);

        String requestStr;
        Console.show(requestStr = JSON.toJSONString(reqJson));

        Console.show(HttpUtil.post(url, requestStr));

    }

    /**
     * 描述 查询支付机构信息
     * @param
     * @return void
     * @author liugs
     * @date 2021/4/13 9:53:08
     */
    private static void queryPaymentIns() {
        String url = URL + "qryPayMethodAndParas";
        JSONObject reqJson = new JSONObject();
//        reqJson.put("paymentInsId", "2");

        String requestStr;
        Console.show(requestStr = JSON.toJSONString(reqJson));

        Console.show(HttpUtil.post(url, requestStr));
    }

    private static void addMerchant() {

    }

}
