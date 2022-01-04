package com.liugs.tool.test;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.liugs.tool.base.Console;
import com.tydic.payment.pay.sdk.DefaultPayCenterClient;
import com.tydic.payment.pay.sdk.PayCenterClient;
import com.tydic.payment.pay.sdk.PayCenterSdkException;
import com.tydic.payment.pay.sdk.PayCenterUtils;
import com.tydic.payment.pay.sdk.vo.*;

/**
 * 标题: com.liugs.tool.test.ZzPayCenterTest
 * 说明: 测试类
 * 时间: 2020/12/11 17:36:48
 * 作者: liugs
 */
public class ZzPayCenterTest {

    private static Integer DEAL_TYPE;
    private static String MERCHANT_ID = "1000000001";
    private static String ORDER_ID;
    private static String OUT_ORDER_ID;
    private static String PAY_METHOD;
    private static String OUT_REFUND_ORDER_ID;
    private static String OUT_REAL_PAY_ORDER_ID;
    private static String BILL_URL;

    public static void main(String[] args) {
        /** 1.支付 2.退款 3.查询支付状态 4.查询退款状态 5.对账 6.实付 7.线下支付回调模拟 */
        DEAL_TYPE = 1;

//        PAY_METHOD = "170";
        //支付单
        ORDER_ID ="2021091701";
        OUT_ORDER_ID = "LPay" + ORDER_ID;

        //退款单
        ORDER_ID = "2021071201";
        OUT_REFUND_ORDER_ID = "LRefund"+ ORDER_ID;

        //实付单
        ORDER_ID = "2021070101";
        OUT_REAL_PAY_ORDER_ID = "LPayReal" + ORDER_ID;

        switch (DEAL_TYPE) {
            case 1:
                payTest(OUT_ORDER_ID, MERCHANT_ID, PAY_METHOD);
                break;
            case 2:
                payCenterRefund(OUT_ORDER_ID, OUT_REFUND_ORDER_ID);
                break;
            case 3:
                queryOrderInfo(OUT_ORDER_ID);
                break;
            case 4:
                queryRefundInfo(OUT_REFUND_ORDER_ID);
                break;
            case 5:
                triggerBill();
                break;
            case 6:
                realPay(OUT_ORDER_ID, OUT_REAL_PAY_ORDER_ID);
                break;
            case 7:
                dealOfflineNotify();
            default:
                testDecode();
                break;
        }

    }

    private static void payTest(String orderId, String merchantId, String payMethod) {
        //本机测试地址
        String serverUrl = "http://localhost:8081/pay/rest/payPro/uniOrderEncrypt";
//        merchantId = "1296659250633871378";

        //本机测试亚朵，中核生产参数商户
//        merchantId = "1296659250633871361";
        /**=================================亚          朵============================================*/
        //亚朵开发环境 下单地址
//        serverUrl = "http://39.105.201.15:10040/pay/rest/payPro/uniOrderEncrypt";
        //B2C
//        merchantId = "1296659250633871362";
        //B2B
//        merchantId = "1296659250633871361";
        //亚朵测试环境 下单地址
//        serverUrl = "http://39.106.87.128:10040/pay/rest/payPro/uniOrderEncrypt";
        //亚朵生产环境 下单地址
//        serverUrl = "http://118.31.166.138:30013/pay/rest/payPro/uniOrderEncrypt";
        /**=================================中核（一期）测试===========================================*/
        //中核一期测试地址  中核一期测试商户
//        serverUrl = "http://172.16.100.32:10040/pay/rest/payPro/uniOrderEncrypt";
        //中核一期测试商户 活期账户
//        merchantId = "1296659250633871361";
        //中核一期测试商户 实际收款（银行转账）
//        merchantId = "350993442164748293";
        /**=================================中核（一期）开发===========================================*/
        //中核一期开发地址 中核一期开发商户
//        serverUrl = "http://172.16.100.22:10040/pay/rest/payPro/uniOrderEncrypt";
//        merchantId = "1296659250633871361";
        /**==========================================================================================*/
        /**=================================中核（二期）开发===========================================*/
//        serverUrl = "http://172.16.100.52:10040/pay/rest/payPro/uniOrderEncrypt";
//        merchantId = "1296659250633871361";
        /**=================================中核（二期）测试===========================================*/
        serverUrl = "http://172.16.100.62:10040/pay/rest/payPro/uniOrderEncrypt";
//        merchantId = "1296659250633871361";
        /**===================================中核生产===============================================*/
//        serverUrl = "https://www.cnncmall.com/pay/rest/payPro/uniOrderEncrypt";
//        merchantId = "1296659250633871361";
//        merchantId = "1296659250633871362";

        /**===================================迪易采测试===============================================*/
//        serverUrl = "http://39.105.66.173:10160/pay/rest/payPro/uniOrderEncrypt";
//        merchantId = "1407226085071540224";

        String busiCode = "D500";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGVZJK5Tyi4Z+ZqS8PNWrzog3vh8BDTEMtF/4HbXY+qW7wVuEVRFxxAV7uR+q9e+ieiLhdJkHhFD9tmzkvQud49BbY9OnTzH/We5cFBiYBm2mg0vqz+FGYMQ3bRZP0jCAYD9LbBYRGz4aVndEzhSLMDbeM08suwarMdTN5iF7eNQIDAQAB";
        String signKey = "lDZ1V4s20zOjiwpCIuB4qkPEm7gU5csr";

        PayCenterClient client = new DefaultPayCenterClient(serverUrl, busiCode, publicKey, signKey);
        PayCenterUniOrderRequest payCenterRequest = new PayCenterUniOrderRequest();
        payCenterRequest.setBusiCode("D500");

        payCenterRequest.setMerchantId(merchantId);
        payCenterRequest.setOutOrderId(orderId);
        payCenterRequest.setTotalFee("1");
        payCenterRequest.setReqWay("1");
        payCenterRequest.setDetailName("支付中心下单测试");
//        payCenterRequest.setPayMethod(payMethod);
        payCenterRequest.setCreateIpAddress("113.204.98.253");
        payCenterRequest.setRemark("支付中心SDK测试");
        payCenterRequest.setCreateOperId("lgs20190822");
        payCenterRequest.setCreateOperIdName("刘贵生");
        payCenterRequest.setRedirectUrl("https://www.jd.com");
        payCenterRequest.setNotifyUrl("http://liugs.test.utools.club/pay/rest/acceptPayCenterCallback");
        payCenterRequest.setUserAccount("21212121");
        payCenterRequest.setUserMobile("18523310756");
        payCenterRequest.setProvinceId("401");
        payCenterRequest.setCityId("120");
        payCenterRequest.setChannelId("191919");
        payCenterRequest.setDistrictId("232323");
        payCenterRequest.setChannelType("3");
        payCenterRequest.setExtTime("10");

        //业务扩展参数
        JSONObject busiReqJson = new JSONObject();
        //指定支付方式
//        busiReqJson.put("payMethods", "21");
        busiReqJson.put("notifyAddress", "http://liugs.test.utools.club/pay/rest/acceptPayCenterCallback");
//        //中核小程序
//        busiReqJson.put("appletAppId", "wx7cfecb25a2403d03");
//        busiReqJson.put("openId", "ohXgx5bSwV8kZ9aR47DkPxsg4yPY");
//        busiReqJson.put("payerAccNo", "1001280909004620106");
//        busiReqJson.put("payerAccName", "壁和患空诬椒魏输移氯");
        busiReqJson.put("offlinePayUrl", "www.baidu.com");

        String busiReqData = busiReqJson.toJSONString();
//        Console.show("busiReqDataStr：" + busiReqData);
        payCenterRequest.setBusiReqData(busiReqData);


        Console.show("==========================================================");
        PayCenterUniOrderResponse rsp = null;
        try {
            rsp = client.execute(payCenterRequest, PayCenterUniOrderResponse.class);
            System.out.println("返回结果：" + JSON.toJSONString(rsp));
        } catch (PayCenterSdkException e) {
            e.printStackTrace();
        }
        assert rsp != null;
        JSONObject jsonObject = JSONObject.parseObject(rsp.getBusiRspData());

        Console.show("返回的参数：" + JSON.toJSONString(jsonObject));
        Console.show("HTML_BODY：" + jsonObject.getString("htmlBody"));
        Console.show("WEB_URL：" + jsonObject.getString("webUrl"));
        Console.show("收银台地址：" + rsp.getUrl());
    }

    /**
     * 调用退款接口
     */
    public static void payCenterRefund(String orderId, String refundOrderId) {
        String serverUrl = "http://localhost:8085/demo/refundEncrypt";

        /**=================================亚          朵============================================*/
        //亚朵开发环境 下单地址
//        serverUrl = "http://39.105.201.15:10040/pay/rest/payPro/refundEncrypt";
        //亚朵测试环境 下单地址
//        serverUrl = "http://39.106.87.128:10040/pay/rest/payPro/refundEncrypt";
        //亚朵生产
//        serverUrl = "http://118.31.166.138:30013/pay/rest/payPro/refundEncrypt";

        /**=================================中核（一期）测试===========================================*/
//        serverUrl = "http://172.16.100.32:10040/pay/rest/payPro/refundEncrypt";

        /**=================================中核（一期）开发===========================================*/
//        serverUrl = "http://172.16.100.22:10040/pay/rest/payPro/refundEncrypt";

        /**==========================================================================================*/
        /**=================================中核（二期）开发===========================================*/
//        serverUrl = "http://172.16.100.52:10040/pay/rest/payPro/refundEncrypt";

        /**=================================中核（二期）测试===========================================*/
//        serverUrl = "http://172.16.100.62:10040/pay/rest/payPro/refundEncrypt";

        /**=====================================中核 生产============================================*/
//        serverUrl = "https://www.cnncmall.com/pay/rest/payPro/refundEncrypt";

        String busiCode = "D500";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGVZJK5Tyi4Z+ZqS8PNWrzog3vh8BDTEMtF/4HbXY+qW7wVuEVRFxxAV7uR+q9e+ieiLhdJkHhFD9tmzkvQud49BbY9OnTzH/We5cFBiYBm2mg0vqz+FGYMQ3bRZP0jCAYD9LbBYRGz4aVndEzhSLMDbeM08suwarMdTN5iF7eNQIDAQAB";
        String signKey = "lDZ1V4s20zOjiwpCIuB4qkPEm7gU5csr";

        PayCenterClient client = new DefaultPayCenterClient(serverUrl, busiCode, publicKey, signKey);
        PayCenterRefundRequest payCenterRequest = new PayCenterRefundRequest();

        payCenterRequest.setBusiCode("D500");
        payCenterRequest.setOriOutOrderId("210329ZFD11551");
        payCenterRequest.setRefundFee("108000");
        payCenterRequest.setOrderType("06");
        payCenterRequest.setRefundOutOrderId("830057093495005185");
        payCenterRequest.setRefundReason("门店要求取消订单");
        payCenterRequest.setNotifyUrl("http://192.168.0.183:10230/task/order/refundPay");

        //业务扩展参数
        JSONObject busiReqJson = new JSONObject();

        String busiReqData = busiReqJson.toJSONString();
        Console.show("busiReqDataStr：" + busiReqData);

        payCenterRequest.setBusiReqData(busiReqData);

        try {
            PayCenterRefundResponse rsp = client.execute(payCenterRequest, PayCenterRefundResponse.class);
            System.out.println("返回结果：" + JSON.toJSONString(rsp));
        } catch (PayCenterSdkException e) {
            e.printStackTrace();
        }
    }

    public static void queryOrderInfo(String orderId) {
//        String serverUrl = "http://localhost:8085/demo/qryPayStatus";
        String serverUrl = "http://127.0.0.1:8081/pay/rest/payPro/qryStatusEncrypt";

        /**=================================亚          朵============================================*/
        //亚朵开发环境 下单地址
//        serverUrl = "http://39.105.201.15:10040/pay/rest/payPro/qryStatusEncrypt";
        //亚朵测试环境 下单地址
//        serverUrl = "http://39.106.87.128:10040/pay/rest/payPro/qryStatusEncrypt";

        /**=================================中核（一期）开发===========================================*/
//        serverUrl = "http://172.16.100.22:10040/pay/rest/payPro/qryStatusEncrypt";
        String busiCode = "D500";
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGVZJK5Tyi4Z+ZqS8PNWrzog3vh8BDTEMtF/4HbXY+qW7wVuEVRFxxAV7uR+q9e+ieiLhdJkHhFD9tmzkvQud49BbY9OnTzH/We5cFBiYBm2mg0vqz+FGYMQ3bRZP0jCAYD9LbBYRGz4aVndEzhSLMDbeM08suwarMdTN5iF7eNQIDAQAB";
        String signKey = "lDZ1V4s20zOjiwpCIuB4qkPEm7gU5csr";

        PayCenterClient client = new DefaultPayCenterClient(serverUrl, busiCode, publicKey, signKey);
        PayCenterQryOrderStatusRequest payCenterRequest = new PayCenterQryOrderStatusRequest();

        payCenterRequest.setBusiCode("D500");
        payCenterRequest.setOriOrderId(orderId);
        payCenterRequest.setRealQueryFlag("1");

        try {
            PayCenterQryOrderStatusResponse rsp = client.execute(payCenterRequest, PayCenterQryOrderStatusResponse.class);
            Console.show("返回结果为：" + JSON.toJSONString(rsp));
        } catch (PayCenterSdkException e) {
            e.printStackTrace();
        }
    }

    /**
     * 查询退款状态
     * @param outRefundOrderId
     */
    private static void queryRefundInfo(String outRefundOrderId) {
        String serverUrl = "http://localhost:8085/demo/queryRefundStatus";
        JSONObject reqJson = new JSONObject();
        reqJson.put("busiCode", "D500");
        reqJson.put("oriOrderId", outRefundOrderId);

        String result = HttpUtil.post(serverUrl, reqJson.toJSONString());
        Console.show(result);
    }

    private static void realPay(String outOrderId, String outRealPayOrderId) {
        //本机测试地址
        String serverUrl = "http://localhost:8085/demo/dealRealPay";

        //中核二期测试
//        serverUrl = "http://172.16.100.62:10040/pay/rest/payPro/dealRealPay";

        //中核活期账户 - 二期测试
        String merchantId = "1296659250633871361";

        JSONObject reqJosn = new JSONObject();
        reqJosn.put("busiCode", "D500");
        reqJosn.put("merchantId", merchantId);
//        reqJosn.put("oriOutOrderId", outOrderId);
        reqJosn.put("oriOutOrderId", "Pay2020122801");
        reqJosn.put("outOrderId", outRealPayOrderId);
        reqJosn.put("payMethod", "161");
        reqJosn.put("notifyUrl", "http://liugs.test.utools.club/pay/rest/acceptPayCenterCallback");
        reqJosn.put("remark", "测试");
        reqJosn.put("createOperId", "下单人编号");
        reqJosn.put("createOperIdName", "下单人姓名");
        reqJosn.put("userAccount", "用户账户");
        reqJosn.put("userMobile", "用户手机号");
        reqJosn.put("provinceId", "省分");
        reqJosn.put("cityId", "地市");
        reqJosn.put("channelId", "渠道编码");
        reqJosn.put("districtId", "区县");
        reqJosn.put("channelType", "渠道类型");

        Console.show(reqJosn.toJSONString());
        String result = HttpUtil.post(serverUrl, reqJosn.toJSONString());
        Console.show(result);
    }

    /**
     * 描述 触发对账
     * @param
     * @return void
     * @author liugs
     * @date 2020/10/15 10:41:44
     */
    public static void triggerBill() {
        BILL_URL = "http://172.16.100.62:10040/pay/rest/bill/newBill";
//        BILL_URL = "http://127.0.0.1:8081/pay/rest/bill/newBill";


//        BILL_URL = "http://127.0.0.1:8085/pay/bill/newBill";
        String result = HttpUtil.post(BILL_URL, "111");
        Console.show("BILL_RESULT：" + result);
    }

    private static void testDecode() {
        String content = "UWOmtwbVsLYzGe0NDxrChS0OAF0lyiKlhy0bBuAJ93Ugxngz4ybZqf1c9BrZgEbQvxMwozSMogG42fayAIRXzL6BCdD1OS8d15LHLysNzo4/3F+blAFbwJwvBqu59nuS0dZe2sDrst6EKkxm6ieiFw5mxhGrOn8Oh5xLnERC6s1cP84d1+8iZdmV5V8fne0E/ryCh8G5GVbwP7BtNuP9NPOaDNYzORGYVFn7NbsiH9yxss2J341iXBB6U+aJgOIj9AwkLKPs9MtmU81LWW0sKeUr6HnuDS/NPaF+J1LdkzOW0rGO3ivrYV3Wtoj1/u41XPjQYGWNl1Yn0RNreBE8pCB+T9IKmE2rfD7JYq+Lpj8EKVFmiIpRct60xgE8w93wJgR97eeKY2GyQ9KsseMTYJt27LDyX8WVfyB1eQ6Te78OQeiJ5CAkoIfhAaBFpL4bDsEai+CO7pnx/J8U2WsZX9K8dZC/2BuH6xW7+Ra1pssJpdGncTDPVjqyGes7PRQVXF53fTGs6o2ol58hhMzGqaiTNwL5whKHj1wCYEJo7RKc7D/x50rIMK6noCUbrY54/UUCiqhnmU/aD5fctxznWbtAuGw+xR94OfccAkTlonqtn9UKsmEArPFyBuMafPFY5rpQ5PXnYUtBt94s1KQuFOI397dUIUSMAQ7nP/cG964=";
        //实际使用时，直接传入content和publicKey
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGVZJK5Tyi4Z+ZqS8PNWrzog3vh8BDTEMtF/4HbXY+qW7wVuEVRFxxAV7uR+q9e+ieiLhdJkHhFD9tmzkvQud49BbY9OnTzH/We5cFBiYBm2mg0vqz+FGYMQ3bRZP0jCAYD9LbBYRGz4aVndEzhSLMDbeM08suwarMdTN5iF7eNQIDAQAB";
        //调用解密方法
        String retStr = PayCenterUtils.deCode(content, publicKey);
        System.out.println("使用工具类解密后的数据：" + retStr);
    }

    /**
     * 描述 模拟线下支付回调
     * @param
     * @return void
     * @author liugs
     * @date 2021/3/26 10:26:22
     */
    private static void dealOfflineNotify() {
        String url = "http://localhost:8081/pay/rest/dealOfflinePayNotify";

        JSONObject reqData = new JSONObject();
        reqData.put("payNotifyTransId", "PayDev2345821413376");
        reqData.put("payOrderId", "PayDev2345821413376");
        reqData.put("tradeTime", "20200326103050");

        Console.show(HttpUtil.post(url, reqData));
    }
}

