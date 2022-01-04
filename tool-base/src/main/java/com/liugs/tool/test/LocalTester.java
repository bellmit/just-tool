package com.liugs.tool.test;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liugs.tool.base.Console;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LocalTester
 * @Description
 * @Author liugs
 * @Date 2021/6/1 11:21:58
 */
public class LocalTester {

    private static final String IP_HOST = "http://127.0.0.1:8088/fsc/test/";
//    private static final String IP_HOST = "http://59.110.230.30:10140/fsc/test/";

//    private static final String IP_HOST = "http://59.110.230.30:10130/dyc/common/";
    private static final String TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOjEsImlhdCI6MTYyMzIyNzY4NzY0OCwibG9naW5Tb3VyY2UiOiJwYy13ZWIifQ._DA0Tz7D-TZ6XaTrDM4sp38dfvHhvApW3W74ZZ8OfFw";

    private static final String DEAL_TYPE = "CASHIE_EDIT";

    public static void main(String[] args) {
        OPERATE_TYPE operate_type = OPERATE_TYPE.valueOf(DEAL_TYPE);
        switch (operate_type) {
            case CREATE_MERCHANT:
                createMerchant();
                break;
            case EDIT_PAY_CHANNEL:
                editPayChannel();
                break;
            case PROTOCAL_MERCHANT:
                protocolMerchant();
                break;
            case EDIT_PAYEE_INFO:
                editPayeeInfo();
                break;
            case QUERY_MERCHANT_CONFIG:
                queryMerchantConfig();
                break;
            case DELETE_MERCHANT:
                deleteMerchant();
                break;
            case EDIT_MERCHANT:
                editMerchant();
                break;
            case QUERY_SKU_CATEGORY:
                querySkuCategory();
                break;
            case QUERY_MERCHANT_LIST:
                queryMerchantList();
                break;
            case QUERY_MERCHANT_DETAIL:
                queryMerchantDetail();
                break;
            case CASHIER_QRY_PAYMENT_INS_LIST:
                queryPaymentInsList();
                break;
            case CASHIER_QRY_PAYMENT_PAGE:
                queryPaymentPage();
                break;
            case CASHIER_QRY_PAGE:
                queryCashierPage();
                break;
            case CASHIE_EDIT:
                editCashierBaseInfo();
                break;
            case CASHIE_DETAIL_QUERY:
                queryCashierDetail();
                break;
            case CASHIER_EDIT_PAY_METHOD:
                editCashierPayMethod();
                break;
            case QUERY_PAYMENT_DETAIL:
                queryPaymentDetail();
                break;
            case EDIT_PAY_METHOD_ICON:
                editPayMethodIcon();
                break;
            default:
                finalMerchant();
                break;
        }
    }

    /**
     * 查询商户详情
     */
    private static void queryMerchantDetail() {
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("merchantId", "604349176349589504");
        doPost(paramMap, "qryMerchantDetail");
    }

    /**
     * 编辑支付方式图标
     */
    private static void editPayMethodIcon() {
        Map<String, String> item = new HashMap<>(1);
        item.put("payMethod", "21");
        item.put("payMethodIcon", "图标1");
        List<Object> rels = new ArrayList<>();
        rels.add(item);
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("relPayMethodIcons", item);
        doPost(paramMap, "editPayMethodIcon");
    }

    /**
     * 查询支付机构详情
     */
    private static void queryPaymentDetail() {
        Map<String, Object> paraMap = new HashMap<>(1);
        paraMap.put("paymentInsId", 19L);
        doPost(paraMap, "queryPaymentDetail");
    }

    /**
     * 编辑收银台支付方式
     */
    private static void editCashierPayMethod() {
        Map<String, Object> paraMap = new HashMap<>(1);
        /**操作类型 1：添加 2：删除*/
        paraMap.put("operateType", "1");
        paraMap.put("cashierTemplate", "1001");
        paraMap.put("payMethod", "999");
        paraMap.put("relId", "");
        paraMap.put("name", "admin");
        doPost(paraMap, "editCashierPayMethod");
    }

    /**
     * 查询收银台详情
     */
    private static void queryCashierDetail() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cashierTemplate", "1001");
        doPost(paramMap, "queryCashierDetail");
    }

    /**
     * 编辑收银台基本信息
     */
    private static void editCashierBaseInfo() {
        Map<String, Object> paramMap = new HashMap<>();
        /**1 新增 2修改*/
        paramMap.put("operateType", "2");
        paramMap.put("cashierTemplate", "3333349");
        /**状态 0 停用 1 提交 2 暂存*/
        paramMap.put("flag", "0");
        paramMap.put("cashierCode", "编码007");
        paramMap.put("cashierTemplateName", "名称123");
        paramMap.put("reqWay", "5");
        paramMap.put("cashierTemplateUrl", "http://www.baidu.com");
        paramMap.put("remark", "");
        paramMap.put("name", "操作人");
        doPost(paramMap, "editCashierBaseInfo");
    }

    /**
     * 分页查询收银台
     */
    private static void queryCashierPage() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cashierTemplateName", "");
        paramMap.put("cashierCode", "");
        paramMap.put("flag", "");
        paramMap.put("reqWay", "");
        paramMap.put("createOperName", "");
        paramMap.put("createTImeStart", "");
        paramMap.put("createTimeEnd", "");
        paramMap.put("updateOperName", "");
        paramMap.put("updateTimeStart", "");
        paramMap.put("updateTimeEnd", "");

        doPost(paramMap, "queryCashier");
    }

    /**
     * 支付机构分页查询
     */
    private static void queryPaymentPage() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("paymentInsId", "");
        paramMap.put("createOperName", "");
        paramMap.put("createTimeStart", "2019-09-10 10:22:29");
        paramMap.put("createTimeEnd", "2021-09-10 10:22:29");
        paramMap.put("pageNo", "1");
        paramMap.put("pageSize", "10");
        doPost(paramMap, "qryPagePaymentIns");
    }

    /**
     * 查询支付机构列表
     */
    private static void queryPaymentInsList() {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("paymentInsId", "");
        doPost(paramMap, "queryPaymentIns");
    }

    /**
     * 查询商户列表
     */
    private static void queryMerchantList() {
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("merchantId", "");
        doPost(paramMap, "queryMerchant");
    }

    /**
     * 查询商品类目
     */
    private static void querySkuCategory() {
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("merchantId", "597144223602405376");

        doPost(paramMap, "querySkuCategory");

    }

    /**
     * 创建一个完整的主商户
     */
    private static void finalMerchant() {
        //商户基本信息
        Map<String, Object> merchantBaseInfo = new HashMap<>(1);
        merchantBaseInfo.put("name", "提交人");
        merchantBaseInfo.put("userId", "212121212");

        merchantBaseInfo.put("merchantId", "597148508037804032");
        merchantBaseInfo.put("accountOrgId", "100000000002");
        merchantBaseInfo.put("orgId", "100000000002");
        merchantBaseInfo.put("accountOrgName", "统一创建2-修改提交测试");
        merchantBaseInfo.put("orgName", "统一创建2");
        merchantBaseInfo.put("dealType", "0");
        /** 商户类型 1:纯协议商户 2:非协议商户 3:混合业务商户 */
        merchantBaseInfo.put("merchantType", "2");
        /** 商户类别与会员同步  0:运营单位 1:采购单位 2:供应商 */
        merchantBaseInfo.put("merchantCategory", "2");
        merchantBaseInfo.put("contactName", "刘壮实");
        merchantBaseInfo.put("contactPhone", "18523310756");
        merchantBaseInfo.put("contactPhoneBak", "18523310756");
        merchantBaseInfo.put("remark", "统一创建测试");
        /** 业务场景范围 0:全部 1:电商类采购 2:协议类采购 3:无协议类采购 */
        merchantBaseInfo.put("payBusiSceneRange", "0");
        /** 支付适用平台用户身份 0:全部 1:外部个人 2:外部企业 3:内部个人用户 4:内部企业用户  */
        merchantBaseInfo.put("payUserIdentity", "0");
        /** 支付是否允许例外 1:是 0:否 */
        merchantBaseInfo.put("payAllowExceptionFlag", "1");
        /** 付款方式 1:预付款 2:账期支付 */
        merchantBaseInfo.put("payType", "2");

        /** 账期支付结算规则 1:指定账期日 2:账期起算特定业务节点+账期天数 */
        merchantBaseInfo.put("payRule", "1");
        merchantBaseInfo.put("payCreditAmount", "100000");
        merchantBaseInfo.put("payBreakScale", "3");

        merchantBaseInfo.put("payAccountDay", "30");
        /** 账期日结算订单规则 1:签收发票开始 2:订单验收 3:订单到货 */
        merchantBaseInfo.put("payAccountDayRule", "2");

        merchantBaseInfo.put("payNodeAccountDays", "");
        /** 账期起算特定业务节点 1:签收发票开始计算 2:订单验收开始计算 3:订单到货开始计算*/
        merchantBaseInfo.put("payNodeRule", "");

        /** 模式适用范围 0:全部 1:按合同 2:按商品类目 */
        merchantBaseInfo.put("modelSceneRange", "0");
        /** 模式适用平台用户身份  0:全部 1:外部个人 2:外部企业 3:内部个人用户 4:内部企业用户 */
        merchantBaseInfo.put("modelUserIdentity", "0");
        /**模式是否允许例外 1:是 0:否*/
        merchantBaseInfo.put("modelAllowExceptionFlag", "1");
        /** 结算模式 1:撮合 0:贸易 */
        merchantBaseInfo.put("modelSettle", "0");

        Console.show("=====================商户暂存==========================");
        JSONObject merchantRsp = doPost(merchantBaseInfo, "createMerchant");

//        JSONObject data = (JSONObject) merchantRsp.get("data");
//        Long merchantId = data.getLong("merchantId");
        Long merchantId = merchantRsp.getLong("merchantId");

        //支付渠道信息
        Map<String, Object> channelInfo = new HashMap<>(1);
        /** 操作类型 1、新增 2、修改 3、删除*/
        channelInfo.put("name", "提交人");
        channelInfo.put("userId", "212121212");
        channelInfo.put("operType", "1");
        channelInfo.put("merchantId", merchantId);
        channelInfo.put("payChannel", "2");
        channelInfo.put("payMethod", "20,21,22,24");

        JSONObject appId = new JSONObject();
        appId.put("parameterCode", "appid");
        appId.put("parameterValue", "2018100961643004");
        JSONObject privateKey = new JSONObject();
        privateKey.put("parameterCode", "privateKey");
        privateKey.put("parameterValue", "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC5we+9ZfY95+3b3XkgvfIwYs+wkgFcbVF/XpJtVktlpD1SMtbBCr1Nv8HDAdnckWOvmRnUIoGhnm3WvepZRkSpTFgLM2WohxKxg8USPlLQtpa+Qw69i4qJl/HVjbtZTaBBVSi8AezauM62PoqfanAKzw1v8jFwaAAi2YmkHSO+xNoOdOVtRh96gaT0pJrERjEn9t5pf6HYGP9vAhjSb2SQWjXPjgzuRxs+3omw7ayLHhbMFpoRtGG2nmSvP+sQoxoY7YQPxL+phzLBe/hygZfOgNcvr68l0lOn0YrLszbzHthc3CEmadUm3892PBgsdskhV6BilKUIDLwZDzt7cT+VAgMBAAECggEAZzVu+7+0Ik9D5F5Yl62R6/pqxQNKiE0g8/Da0K8asz2e6JXgpl9OMO2zPaNApgGN7pnVNP17Iw5AnwjkNXgKXDpTzN1gxVNPOpwpGerQ3rAkb1AY1425ipuGvY3DI2oy+TnUqSk0rY3zJGT5ZnugBxFluOZLO9Y9BR/zDuE15B7OffrpoIZFdtT4Ama+DTmYS8zzalm1D90IxsZYhZOWf81BERdlVYOjIyLwSdEr6hL0aNANg/+bOW4CrZTMCS09htcbyZ7AEi/NUK/gF6hat/yuqKou/p6RvFEt7VCb69P47sRjIqMzVipFq1sAS42ZnrFlmOClkhFzGEtmtWc+oQKBgQDt7YArWP1G4Snn3lYsJdo401NHyKfzabnFGj8R2xkTfwkA9aublnidBWrTdjRMMYxR2vp+OIMwz9elLWJKlikg9ZJv+zU2pm3JgcUj28QSJCqyg/kkZk+sqE93Nt7x9GQ6x9Ru5OZ4XjZdjE9AOKbmkuRu9DDZnySB0OBTDWv1uQKBgQDH3fzvJinpg3fKpXK9wP409yylRxOnwVZ01Tbk4g/vKnIfvHkPbGYmRJoNjnCHVszikBJ7VfCQxL2CYV4cdCPb9g3YLsfPJC97tRmxumN8ymDlMoXtxDPm1d4Ie9w58Klp6P4X6Lrsn9iHc192qwbHDurZcz03Hk6MOxMxoA6GvQKBgDrQIoaUkMcckYdHZnWzRaYWVte/ugRpdgpK2pxHVIOFO2yJTco//8CQvj9XUMYk2VLClnjs70D5n4ytE0bsp24GRhR4PEcuDKVq9phOksEJP6SxCrz8rWU4lawaXDHi/4OoMdLXskC+ySGGtXBikJ8eYGks44Zn2FZek5T0FGPBAoGAP4TRSb+7OjdJ7uE94ghfwb18Ntqr6HiNXXwVghBGOCApPOTNJJWysR7ERuKp6JkxbB+VgBUD6z5bbAA9xJU0gEdmhvn3DmHVpV8c11sBGzvGJ5Ey9ymPxSDa+59Bkjhl3v7pIfgYz/LNAd8sy6327XRUNt9k6Qt8sxxjMTZI55ECgYEA3d8lGkHUNr8kbFPKdY0z3etndDq2DiDLilh9eW8RkMkyd2HHu+vAqGWxpLiQn3OAveswRrbO89Tb4Xl4gf/PjKlXJGuV75vf8t/3JCI4j8WDpeURdqsHtOB56DK778SnZfXBHj0u+oogOLRa7wch8vKPvtLuxZZKapgb89Z3opo=");
        JSONObject publicKey = new JSONObject();
        publicKey.put("parameterCode", "publicKey");
        publicKey.put("parameterValue", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAstR/FMQuqfiayPyzjiMGlcTrg8Xo/kR/2y05YU3T8RgVqWakJVfDEFWSJK+YfspqsFJjwV6ztKqJ4zRUidaBx66zJ4S4pZAfuuFcl7H8oCnXphuGtmDYwgDVOSqQAcXnd6GbzES00imutH580aSCE8nMQ4SKKvTOO3WtgTz7jC9/DV46gRpwGUhy9Ksedc/4Hyd8lQp7qEPwWMDM4+7623y2u2Fa6HTRJT+IxOgsP/dvWJ5LeFCmmML8JifUTHX1tvNGdlhcoYtroy8PAJQeavHZYJkb6P0tvwPzTDWyyXKQ7YbNeLb396IfBeBWjrIakBi3sM2J7JrBmAcuqSAEfQIDAQAB");
        JSONArray payJson = new JSONArray();
        payJson.add(appId);
        payJson.add(privateKey);
        payJson.add(publicKey);

        channelInfo.put("payJson", publicKey);

        Console.show("=======================支取渠道提交========================");
        doPost(channelInfo, "editPayChannel");

        //收款账户信息
        Map<String, Object> payeeInfo = new HashMap<>(1);
        payeeInfo.put("operType", "1");
        payeeInfo.put("merchantId", merchantId);
        payeeInfo.put("payeeAccountName", "收款人名称");
        payeeInfo.put("payBankAccount", "622898478374383738");
        payeeInfo.put("payBankName", "中国工商银行光电园支行");

        merchantBaseInfo.put("payeeInfo", payeeInfo);
        merchantBaseInfo.put("orgName", "统一创建2-提交");
        merchantBaseInfo.put("dealType", "0");

        Console.show("========================商户提交=======================");
        doPost(merchantBaseInfo, "editMerchant");
    }

    private static void editMerchant() {
        Map<String, Object> paraMap = new HashMap<>(1);
        paraMap.put("name", "操作人");
        paraMap.put("userId", "3432423");
        paraMap.put("accountOrgId", "566702009197236224");
        paraMap.put("orgId", "566702009197236224");
        paraMap.put("orgName", "重庆核电机构");
        paraMap.put("accountOrgName", "重庆核电机构");
        paraMap.put("dealType", "0");
        paraMap.put("merchantId", "599349660453711872");
        paraMap.put("parentId", "597428775172538368");
        paraMap.put("merchantNo", "");
        /** 商户类型 1:纯协议商户 2:非协议商户 3:混合业务商户 */
        paraMap.put("merchantType", "");
        /** 商户类别与会员同步  0:运营单位 1:采购单位 2:供应商 */
        paraMap.put("merchantCategory", "");
        paraMap.put("contactName", "");
        paraMap.put("contactPhone", "");
        paraMap.put("contactPhoneBak", "");
        paraMap.put("remark", "");

        /** 业务场景范围 0:全部 1:电商类采购 2:协议类采购 3:无协议类采购 *//*
        paraMap.put("payBusiSceneRange", "");
        *//** 支付适用平台用户身份 0:全部 1:外部个人 2:外部企业 3:内部个人用户 4:内部企业用户  *//*
        paraMap.put("payBusiSceneRange", "");
        *//** 支付是否允许例外 1:是 0:否 *//*
        paraMap.put("payAllowExceptionFlag", "");

        paraMap.put("payObjId", "");
        paraMap.put("payObjName", "");
        *//** 付款方式 1:预付款 2:账期支付 *//*
        paraMap.put("payType", "");
        *//** 账期支付结算规则 1:指定账期日 2:账期起算特定业务节点+账期天数 *//*
        paraMap.put("payRule", "");
        paraMap.put("payCreditAmount", "");
        paraMap.put("payBreakScale", "");
        *//** 指定账期日 *//*
        paraMap.put("payAccountDay", "");
        *//** 账期日结算订单规则 1:签收发票开始 2:订单验收 3:订单到货 *//*
        paraMap.put("payAccountDayRule", "");
        *//** 账期天数 *//*
        paraMap.put("payNodeAccountDays", "");
        *//** 账期起算特定业务节点 1:签收发票开始计算 2:订单验收开始计算 3:订单到货开始计算*//*
        paraMap.put("payNodeRule", "");
        *//** 模式适用范围 0:全部 1:按合同 2:按商品类目 *//*
        paraMap.put("modelSceneRange", "");
        paraMap.put("modelContractNo", "");
        *//** 模式适用平台用户身份  0:全部 1:外部个人 2:外部企业 3:内部个人用户 4:内部企业用户 */
        paraMap.put("modelUserIdentity", "");
        paraMap.put("modelObjId", "");
        paraMap.put("modelObjName", "");
        /**模式是否允许例外 1:是 0:否*/
        paraMap.put("modelAllowExceptionFlag", "");
        /** 结算模式 1:撮合 0:贸易 */
        paraMap.put("modelSettle", "");
       /** 例外用户维度 1:按类型 2:按机构/个人*/
        paraMap.put("exceptionUserLatitude", "");
        /** 是否例外配置 1:是 0:否*/
        paraMap.put("exceptionFlag", "");
        /** 例外配置类别 1:结算模式配置 2:支付方式配置 3:混合配置*/
        paraMap.put("exceptionCategory", "");

        doPost(paraMap, "editMerchant");
    }

    /**
     * 删除商户
     */
    private static void deleteMerchant() {
        Map<String, Object> paraMap = new HashMap<>(1);
        paraMap.put("merchantId", "597085729875066880");
        doPost(paraMap, "deleteMerchant");
    }

    /**
     * 查询商户配置
     */
    private static void queryMerchantConfig() {
        Map<String, Object> paraMap = new HashMap<>(1);
        paraMap.put("purOrgId", "10000000000");
        paraMap.put("supId", "22222222");
        doPost(paraMap, "queryMerchantConfig");
    }

    /**
     * 编辑收款账户
     */
    private static void editPayeeInfo() {
        Map<String, Object> payeeInfo = new HashMap<>(1);
        payeeInfo.put("operType", "2");
        payeeInfo.put("merchantId", 597068603271061504L);
        payeeInfo.put("payeeAccountName", "收款方名称1");
        payeeInfo.put("payBankAccount", "1212121212");
        payeeInfo.put("payBankName", "中国银行渝北区支行");
        doPost(payeeInfo, "dealPayeeEdit");

    }

    /**
     * 协议商户提交
     */
    private static void protocolMerchant() {
        Map<String, Object> paraMap = new HashMap<>(1);
        paraMap.put("accountOrgId", 22222222);
        paraMap.put("accountOrgName", "2021-6-01测试");
        paraMap.put("name", "操作人");
        paraMap.put("userId", 1001L);

        /** 处理类型：0.暂存，1.提交 */
        paraMap.put("dealType", 1);
        /** 商户类型 1:纯协议商户 2:非协议商户 3:混合业务商户 */
        paraMap.put("merchantType", 2);
        /** 商户类别与会员同步  0:运营单位 1:采购单位 2:供应商 */
        paraMap.put("merchantCategory", 0);
        paraMap.put("contactName", "刘贵生");
        paraMap.put("contactPhone", "18523310756");
        paraMap.put("contactPhoneBak", "18523310756");
        paraMap.put("remark", "测试");

        //收款信息
        Map<String, Object> payeeInfo = new HashMap<>(1);
        payeeInfo.put("operType", "2");
        payeeInfo.put("merchantId", 597068603271061504L);
        payeeInfo.put("payeeAccountName", "收款方名称");
        payeeInfo.put("payBankAccount", "1212121212");
        payeeInfo.put("payBankName", "中国银行渝北区支行");

        paraMap.put("payeeInfo", payeeInfo);

        doPost(paraMap, "dealMerchantCreate");
    }

    /**
     * 添加支付渠道
     */
    private static void editPayChannel() {
        Map<String, Object> paraMap = new HashMap<>(1);
        paraMap.put("operType", 1);
        paraMap.put("merchantId", 597085729875066880L);
        paraMap.put("payChannel", 3);
        paraMap.put("payMethod", "30");
        paraMap.put("payJson", "[]");
        doPost(paraMap, "editPayChannel");
    }

    /**
     * 描述创建商户
     * @date 2021/6/1 11:34:24
     */
    private static void createMerchant() {
        Map<String, Object> paraMap = new HashMap<>(1);
        paraMap.put("accountOrgId", 1919991991L);
        paraMap.put("accountOrgName", "20210601Test");
        paraMap.put("orgId", "192919291923");
        paraMap.put("orgName", "20210601Test");
        paraMap.put("name", "操作人");
        paraMap.put("userId", 1001L);

        /** 处理类型：0.暂存，1.提交 */
        paraMap.put("dealType", 1);
        /** 商户类型 1:纯协议商户 2:非协议商户 3:混合业务商户 */
        paraMap.put("merchantType", 2);
        /** 商户类别与会员同步  0:运营单位 1:采购单位 2:供应商 */
        paraMap.put("merchantCategory", 0);
        paraMap.put("contactName", "刘贵生");
        paraMap.put("contactPhone", "18523310756");
        paraMap.put("contactPhoneBak", "18523310756");
        paraMap.put("remark", "测试");

//        paraMap.put("payBusiSceneRange", "测试");
//        paraMap.put("remark", "测试");
//        paraMap.put("remark", "测试");
//        paraMap.put("remark", "测试");
//
//        /** 支付适用平台用户身份 0:全部 1:外部个人 2:外部企业 3:内部个人用户 4:内部企业用户  */
//        paraMap.put("payUserIdentity", "0");
//        /** 支付是否允许例外 1:是 0:否 */
//        paraMap.put("payAllowExceptionFlag", "1");

        doPost(paraMap, "createMerchant");
//        doPost(paraMap, "dealMerchantCreate");
    }

    private static JSONObject doPost(Map<String, Object> paraMap, String methodName) {
        Console.show("入参：" + JSON.toJSONString(paraMap));

        String url = IP_HOST + methodName;
        String result = HttpRequest.post(url)
                .header("auth-token", TOKEN)
                .body(JSON.toJSONString(paraMap))
                .timeout(20000)
                .execute().body();

        JSONObject resultJson = JSON.parseObject(result);

        Console.show("出参：" + resultJson.toJSONString());
        return resultJson;
    }

    public enum OPERATE_TYPE {
        CREATE_MERCHANT("CREATE_MERCHANT"),
        PROTOCAL_MERCHANT("PROTOCAL_MERCHANT"),
        EDIT_PAYEE_INFO("EDIT_PAYEE_INFO"),
        QUERY_MERCHANT_CONFIG("QUERY_MERCHANT_CONFIG"),
        DELETE_MERCHANT("DELETE_MERCHANT"),
        EDIT_MERCHANT("EDIT_MERCHANT"),
        QUERY_SKU_CATEGORY("QUERY_SKU_CATEGORY"),
        QUERY_MERCHANT_LIST("QUERY_MERCHANT_LIST"),
        QUERY_MERCHANT_DETAIL("QUERY_MERCHANT_DETAIL"),
        EDIT_PAY_CHANNEL("EDIT_PAY_CHANNEL"),
        CASHIER_QRY_PAYMENT_PAGE("CASHIER_QRY_PAYMENT_PAGE"),
        CASHIER_QRY_PAGE("CASHIER_QRY_PAGE"),
        CASHIE_EDIT("CASHIE_EDIT"),
        CASHIE_DETAIL_QUERY("CASHIE_DETAIL_QUERY"),
        CASHIER_EDIT_PAY_METHOD("CASHIER_EDIT_PAY_METHOD"),
        QUERY_PAYMENT_DETAIL("QUERY_PAYMENT_DETAIL"),
        EDIT_PAY_METHOD_ICON("EDIT_PAY_METHOD_ICON"),

        CASHIER_QRY_PAYMENT_INS_LIST("CASHIER_QRY_PAYMENT_INS_LIST");

        private String value;

        OPERATE_TYPE(String value) {
            this.value = value;
        }
    }
}
