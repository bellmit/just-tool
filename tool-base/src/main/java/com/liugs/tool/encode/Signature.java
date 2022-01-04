package com.liugs.tool.encode;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @ClassName: Signature
 * @Description：签名 + 报文体拼装
 * @Author: liugs
 * @Date: 2019/6/10 14:07:10
 */
public class Signature {

    public static String sign(Map<String, Object> params, String appsecret) {
        //将参数排序 用于签名
        String content = getSortParams(params) + appsecret;
        System.out.println("签名时，排序的字符串为：" + content);
        //获取签名 并将生成的sign添加到Map中
        String signStr = doSign(content, "UTF-8");
        System.out.println("签名为：" + signStr);
        params.put("sign", signStr);
        //拼接参数
        String result = combString(params);

        return result;
    }

    /**
     * 获取签名
     */
    public static String getSign(Map<String, Object> params, String appsecret) {
        //将参数排序 用于签名
        String content = getSortParams(params) + appsecret;
        System.out.println("签名时，排序的字符串为：" + content);
        //获取签名
        String signStr = doSign(content, "UTF-8");
        return signStr;
    }

    /**
     * 将除sign外的参数排序
     * @param params
     * @return
     */
    public static String getSortParams(Map<String, Object> params) {
        params.remove("sign");
        String contnt = "";
        Set<String> keySet = params.keySet();
        List<String> keyList = new ArrayList<>();
        //这个接口做sign签名时，值为空的参数也传
        for (String key : keySet) {
            String value = (String) params.get(key);
            // 将值为空的参数排除
			if (StringUtils.isEmpty(value)) {
//				keyList.add(key);
                continue;
			}
            keyList.add(key);
        }
        Collections.sort(keyList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int length = Math.min(o1.length(), o2.length());
                for (int i = 0; i < length; i++) {
                    char c1 = o1.charAt(i);
                    char c2 = o2.charAt(i);
                    int r = c1 - c2;
                    if (r != 0) {
                        // char值小的排前边
                        return r;
                    }
                }
                // 2个字符串关系是str1.startsWith(str2)==true
                // 取str2排前边
                return o1.length() - o2.length();
            }
        });
        //将参数和参数值按照排序顺序拼装成字符串
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            contnt += key + params.get(key);
        }
        return contnt;
    }

    /**
     * 使用排序后的字符串生成签名
     * @param content
     * @param charset
     * @return
     */
    public static String doSign(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            charset = "UTF-8";
        }
        String sign = "";
        try {
//            String contentBytes = new String(content.getBytes(charset), charset);
            //SHA-256
            MessageDigest md5 = MessageDigest.getInstance("SHA-256");
            sign = new BASE64Encoder().encode(md5.digest(content.getBytes(charset)));
            System.out.println("sign:" + sign);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 按接口要求拼接字符串
     * @param params
     * @return
     */
    public static String combString(Map<String, Object> params) {
        String content = "";
        Set<String> keySet = params.keySet();
        List<String> keyList = new ArrayList<String>();
        //这个接口做sign签名时，值为空的参数也传
        for (String key : keySet) {
            String value = (String) params.get(key);
            // 将值为空的参数排除
			if (StringUtils.isEmpty(value)) {
//                keyList.add(key);
                continue;
			}
            keyList.add(key);
        }

        //将参数和参数值按照排序顺序拼装成字符串
        for (int i = 0; i < keyList.size(); i++) {
            String key = keyList.get(i);
            if (i == keyList.size() - 1) {
                content += key + "=" + params.get(key);
                return content;
            }
            content += key + "=" + params.get(key) + "&";

        }
        return content;
    }
}
