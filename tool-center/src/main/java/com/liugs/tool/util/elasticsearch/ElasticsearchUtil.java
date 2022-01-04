package com.liugs.tool.util.elasticsearch;

import com.liugs.tool.base.Console;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ElasticsearchUtil
 * @Description es
 * @Author liugs
 * @Date 2021/3/18 17:23:35
 */
public class ElasticsearchUtil {

    private static String clusterNodes = "101.200.41.184:9200";
    private static String username = "elastic";
    private static String password = "1qaz2wsx";
    private static String whetherEncryption = "1";

    private static final String IS_ENCRYPTION = "1";

    public static void main(String[] args) {
        testQuery();
    }

    public static void testQuery() {
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
//        sourceBuilder.query(QueryBuilders.matchAllQuery());
        sourceBuilder.query();
        sourceBuilder.from(0);
        sourceBuilder.size(3);
        //按评分倒序排序
//        sourceBuilder.sort(new ScoreSortBuilder().order(SortOrder.DESC));
        //按指定字段排序
        sourceBuilder.sort(new FieldSortBuilder("age").order(SortOrder.ASC));
        sourceBuilder.timeout(new TimeValue(6000, TimeUnit.MILLISECONDS));
        //完全关闭_source检索
//        sourceBuilder.fetchSource(false);

        //指定字段检索
        String[] includeFields = new String[] {"name", "age"};
        String[] excludeFields = new String[] {"description"};
        sourceBuilder.fetchSource(includeFields, excludeFields);

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices("index_test");
        searchRequest.indicesOptions();
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = null;
        RestHighLevelClient restHighLevelClient = buildClient();
        try {
            searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Console.show(searchResponse);

        SearchHits hits = searchResponse.getHits();
        Console.show("numHits：" + hits.getTotalHits().value);
        Console.show("maxScore ：" + hits.getMaxScore());
        Console.show("====================================");

        for (SearchHit hit :hits) {
            Console.show("index：" + hit.getIndex());
            Console.show("id：" + hit.getId());
            Console.show("score：" + hit.getScore());
            Console.show("sourceString：" + hit.getSourceAsString());
            Console.show("sourceAsMap：" + hit.getSourceAsMap());
            Console.show("====================================");
        }

        try {
            restHighLevelClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static RestHighLevelClient buildClient() {
        RestClientBuilder restClientBuilder;

        if (StringUtils.isEmpty(clusterNodes)) {
            return null;
        }
        List<String> nodeList = new ArrayList<>(Arrays.asList(clusterNodes.split(",")));
        HttpHost httpHost;
        HttpHost[] httpHosts = new HttpHost[nodeList.size()];
        for (int i = 0;  i <  nodeList.size(); i ++){
            httpHost = new HttpHost( nodeList.get(i).split(":")[0],
                    Integer.valueOf( nodeList.get(i).split(":")[1]),
                    "http");
            httpHosts[i] = httpHost;
        }

        restClientBuilder = RestClient.builder(httpHosts);

        if (IS_ENCRYPTION.equals(whetherEncryption)) {
            final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(username, password));
            restClientBuilder.setHttpClientConfigCallback(httpAsyncClientBuilder ->
                    httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
        }

        return new RestHighLevelClient(restClientBuilder);
    }
}
