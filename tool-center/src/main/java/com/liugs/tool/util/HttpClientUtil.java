package com.liugs.tool.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @ClassName HttpClientUtil
 * @Description HTTP工具
 * @Author liugs
 * @Date 2021/8/4 14:30:58
 */
@Slf4j
public class HttpClientUtil {

    private static CloseableHttpClient httpClient = null;

    static {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        //总连接池数量
        connectionManager.setMaxTotal(100);
        //每个域名单独设置连接池的数量
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost("")), 20);

        RequestConfig requestConfig = RequestConfig.custom()
                //设置建立连接的超时时间
                .setConnectTimeout(1000)
                //从连接池中拿连接的等待超时时间
                .setConnectionRequestTimeout(2000)
                //发出请求后等待对端应答的超时时间
                .setSocketTimeout(3000)
                .build();
        // 重试处理器，StandardHttpRequestRetryHandler
        HttpRequestRetryHandler retryHandler = new StandardHttpRequestRetryHandler();

        httpClient = HttpClients.custom().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(retryHandler).build();
    }

    /**
     * 描述 GET请求
     * @param url                       地址
     * @param paramMap                  参数map
     * @return java.lang.Object
     * @author liugs
     * @date 2021/8/4 16:22:04
     */
    public static String doHttpGet(String url, Map<String, String> paramMap) {
        CloseableHttpResponse response = null;
        String resultStr = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (!CollectionUtils.isEmpty(paramMap)) {
                List<NameValuePair> list = new ArrayList<>();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    list.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                uriBuilder.setParameters(list);
            }
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            response = httpClient.execute(httpGet);

            int status = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != status) {
                log.error("请求状态：{}", status);
            }
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                resultStr = EntityUtils.toString(entity, "utf-8");
            }
        } catch (URISyntaxException | IOException e) {
            log.error("CloseableHttpClient get 请求异常：", e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultStr;
    }


    /**
     * 描述 post请求
     * @param url
     * @param paramMap
     * @return java.lang.String
     * @author liugs
     * @date 2021/8/4 16:32:38
     */
    public static String doPost(String url, Map<String, String> paramMap) {
        CloseableHttpResponse response = null;
        String resultStr = null;
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            HttpPost httpPost = new HttpPost(uriBuilder.build());
            if (!CollectionUtils.isEmpty(paramMap)) {
                List<NameValuePair> list = new ArrayList<>();
                for (Map.Entry<String, String> param : paramMap.entrySet()) {
                    list.add(new BasicNameValuePair(param.getKey(), param.getValue()));
                }
                HttpEntity httpEntity = new UrlEncodedFormEntity(list, "utf-8");
                httpPost.setEntity(httpEntity);
            }
            response = httpClient.execute(httpPost);
            int status = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != status) {
                log.error("请求状态：{}", status);
            }
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                resultStr = EntityUtils.toString(entity, "utf-8");
            }
        } catch (Exception e) {
            log.error("CloseableHttpClient post 请求异常：", e);
        } finally {
            try {
                if (null != response) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultStr;
    }
}
