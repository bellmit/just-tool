//package com.liugs.tool.config.elasticsearch;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//
///**
// * @ClassName ElasticsearchClient
// * @Description test
// * @Author liugs
// * @Date 2021/3/18 16:21:21
// */
//@Slf4j
//@Configuration
//public class ElasticsearchClient {
//
//    @Value("${es.cluster.address}")
//    private String clusterNodes;
//
//    @Bean("restHighLevelClient")
//    public RestHighLevelClient buildClient() {
//        RestClientBuilder restClientBuilder;
//
//        if (StringUtils.isEmpty(clusterNodes)) {
//            return null;
//        }
//        List<String> nodeList = new ArrayList<>(Arrays.asList(clusterNodes.split(",")));
//        HttpHost httpHost;
//        HttpHost[] httpHosts = new HttpHost[nodeList.size()];
//        for (int i = 0;  i <  nodeList.size(); i ++){
//            httpHost = new HttpHost( nodeList.get(i).split(":")[0],
//                    Integer.valueOf( nodeList.get(i).split(":")[1]),
//                    "http");
//            httpHosts[i] = httpHost;
//        }
//        restClientBuilder = RestClient.builder(httpHosts);
//
//        return new RestHighLevelClient(restClientBuilder);
//    }
//
//}
