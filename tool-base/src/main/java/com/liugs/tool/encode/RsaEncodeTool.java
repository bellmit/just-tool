package com.liugs.tool.encode;

import com.alibaba.fastjson.JSONObject;
import com.liugs.tool.constant.ToolConstants;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName RsaEncodeTool
 * @Description RSA加密工具
 * @Author liugs
 * @Date 2020/7/8 16:44:09
 */
public class RsaEncodeTool {
    /**加密算法*/
    private static final String KEY_ALGORITHM = "RSA";

    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**RSA最大加密明文大小*/
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**RSA最大解密密文大小*/
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 描述 生成公钥和私钥 public 公钥(RSAPublicKey) private 私钥(RSAPrivateKey)
     * @param
     * @return java.util.Map<java.lang.String,java.lang.String>
     * @author liugs
     * @date 2020/7/8 16:45:19
     */
    public static Map<String, String> generateRSAKeys() throws NoSuchAlgorithmException {
        Map<String, String> keyMap = new HashMap<>(16);

        //用于生成公钥和私钥
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);

        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024);

        //生成一组密钥，保存至keyPair
        KeyPair keyPair = keyPairGen.generateKeyPair();

        //公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        //私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        //公钥 字符串
        String publicKeyStr = new String(Base64.encodeBase64(publicKey.getEncoded()));
        //私钥 字符串
        String privateKeyStr = new String(Base64.encodeBase64(privateKey.getEncoded()));

        keyMap.put(ToolConstants.EncodeConstants.PUBLIC_KEY, publicKeyStr);
        keyMap.put(ToolConstants.EncodeConstants.PRIVATE_KEY, privateKeyStr);
        return keyMap;
    }

    /**
     * 描述 还原公钥 X509EncodedKeySpec 用于构建公钥的规范
     * @param publicKeyStr
     * @return java.security.PublicKey
     * @author liugs
     * @date 2020/7/8 17:28:41
     */
    public static PublicKey restorePublicKey(String publicKeyStr) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKeyStr));
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    /**
     * 描述 还原私钥 PKCS8EncodedKeySpec 用于构建私钥的规范
     * @param privateStr
     * @return java.security.PublicKey
     * @author liugs
     * @date 2020/7/8 17:47:24
     */
    public static PrivateKey restorePrivateKey(String privateStr) throws Exception {
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateStr));
        KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
        PrivateKey privateKey = factory.generatePrivate(pkcs8EncodedKeySpec);
        return privateKey;
    }

    /**
     * 描述 密钥加密
     * @param publicKey, privateKey, bytes
     * @return byte[]
     * @author liugs
     * @date 2020/7/8 17:48:58
     */
    public static byte[] rsaKeyEncode(PublicKey publicKey, PrivateKey privateKey, byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        if (null != publicKey) {
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        } else if (null != privateKey){
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        } else {
            System.out.println("publicKey and privateKey can`t be empty at the same time");
            return null;
        }
        int inputLen = bytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(bytes, offSet, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(bytes, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        return decryptedData;
    }

    /**
     * 描述 解密
     * @param publicKey, privateKey, decodeBase64
     * @return byte[]
     * @author liugs
     * @date 2020/7/8 17:57:57
     */
    public static byte[] rsaPrivateDecode(PublicKey publicKey, PrivateKey privateKey, byte[] decodeBase64) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        if (null != privateKey) {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } else if (null != publicKey) {
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
        } else {
            System.out.println("publicKey and privateKey can`t be empty at the same time");
            return null;
        }
        int inputLen = decodeBase64.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(decodeBase64, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(decodeBase64, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;
    }

    /**
     * 描述 获取随机字符串
     * @param length
     * @return java.lang.String
     * @author liugs
     * @date 2020/7/9 10:02:54
     */
    public static String getRandomStringByLength(int length) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    /** json转换为map*/
    public static Map<String,Object> getMapFromJson(String json){
        return getMapFromJsonByFastJson(json);
    }

    /** map转为json*/
    public static String getJsonFromMap(Map<String,Object> map){
        return getJsonFromMapByFastJson(map);
    }

    private static String getJsonFromMapByFastJson(Map<String, Object> map) {
        return JSONObject.toJSONString(map);
    }

    private static Map<String,Object> getMapFromJsonByFastJson(String json){
        return JSONObject.parseObject(json,Map.class);
    }
}
