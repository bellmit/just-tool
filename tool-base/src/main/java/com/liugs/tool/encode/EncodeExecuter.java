package com.liugs.tool.encode;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

/**
 * @ClassName EncodeExecuter
 * @Description 加密执行
 * @Author liugs
 * @Date 2020/7/8 17:11:42
 */
public class EncodeExecuter {

    /**
     * 描述 公钥加密
     * @param publicKeyStr, content, signKey
     * @return java.lang.String
     * @author liugs
     * @date 2020/7/8 17:13:38
     */
    public static String publicEncode(String publicKeyStr, String content, String signKey) throws Exception {

        //获取签名
        String sign = getSign(content, signKey);

        Map<String,Object> map = RsaEncodeTool.getMapFromJson(content);
        map.put("sign", sign);
        String paramStr = RsaEncodeTool.getJsonFromMap(map);

        //还原公钥
        PublicKey publicKey = RsaEncodeTool.restorePublicKey(publicKeyStr);

        //RSA加密
        byte[] encodedText = RsaEncodeTool.rsaKeyEncode(publicKey, null, paramStr.getBytes(StandardCharsets.UTF_8));

        //转成base64
        String result = Base64.encodeBase64String(encodedText);

        return result;
    }

    /**
     * 描述 私钥加密
     * @param privateStr, content, signKey
     * @return java.lang.String
     * @author liugs
     * @date 2020/7/8 17:46:47
     */
    public static String privateEncode(String privateStr, String content, String signKey) throws Exception {
        //获取签名
        String sign = getSign(content, signKey);

        Map<String,Object> map = RsaEncodeTool.getMapFromJson(content);
        map.put("sign", sign);
        String paramStr = RsaEncodeTool.getJsonFromMap(map);

        //还原公钥
        PrivateKey privateKey = RsaEncodeTool.restorePrivateKey(privateStr);

        //RSA加密
        byte[] encodedText = RsaEncodeTool.rsaKeyEncode(null, privateKey, paramStr.getBytes(StandardCharsets.UTF_8));

        //转成base64
        String result = Base64.encodeBase64String(encodedText);

        return result;
    }

    /**
     * 描述 私钥解密
     * @param encodeStr, privateKey
     * @return java.lang.String
     * @author liugs
     * @date 2020/7/8 17:57:07
     */
    public static String privateDecode(String privateKeyStr, String encodeStr) throws Exception{
        //还原私钥
        PrivateKey privateKey = RsaEncodeTool.restorePrivateKey(privateKeyStr);
        //RSA解密
        byte[] decodeBytes = RsaEncodeTool.rsaPrivateDecode(null, privateKey, Base64.decodeBase64(encodeStr));
        String result = new String(decodeBytes, StandardCharsets.UTF_8);
        return result;
    }

    public static String publicDeCode( String publicKeyStr, String encodeStr) throws Exception {
        //还原公钥
        PublicKey publicKey = RsaEncodeTool.restorePublicKey(publicKeyStr);
        //RSA解密
        byte[] decodeBytes = RsaEncodeTool.rsaPrivateDecode(publicKey, null, Base64.decodeBase64(encodeStr));
        String result = new String(decodeBytes, StandardCharsets.UTF_8);
        return result;
    }


    /**
     * 描述 解密后签名校验
     * @param content, signKey
     * @return boolean
     * @author liugs
     * @date 2020/7/8 18:03:53
     */
    public static boolean validateSign(String content, String signKey) throws Exception{

        Map<String, Object> paramMap = RsaEncodeTool.getMapFromJson(content);

        //获取接收到的签名
        String reqSign = paramMap.get("sign").toString();

        //把签名从数据中移除
        paramMap.remove("sign");

        String paramStr = RsaEncodeTool.getJsonFromMap(paramMap);

        //再进行签名
        String sign = getSign(paramStr, signKey);

        return reqSign.equals(sign);
    }

    /**
     * 描述 获取签名
     * @param signKey
     * @return java.lang.String
     * @author liugs
     * @date 2020/7/8 17:14:54
     */
    private static String getSign(String content, String signKey) {
        Map<String, Object> paramMap = RsaEncodeTool.getMapFromJson(content);
        return Signature.getSign(paramMap, signKey);
    }

}
