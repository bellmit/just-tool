package com.liugs.tool.test;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.liugs.tool.base.Console;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ASearchTest
 * @Description 搜索测试
 * @Author liugs
 * @Date 2021/3/25 9:29:18
 */
public class ASearchTest {

    private static int OPERATE_TYPE;
    private static String URL = "http://localhost:8081/es/elastic/";

    public static void main(String[] args) {
        OPERATE_TYPE = 3;
        switch (OPERATE_TYPE) {
            case 1:
                addDocument();
                break;
            case 2:
                getDocById();
                break;
            case 3:
                syncDataToEs();
                break;
            case 4:
                deleteIndices();
                break;
            case 5:
                operateIndex();
                break;
            case 6:
                createIndex();
                break;
            case 7:
                participleManager();
                break;
            default:
                break;
        }
    }

    private static void participleManager() {
        URL = URL + "dealParticiple";
        JSONObject reqJson = new JSONObject();
        String[] removeWords = {"而我却"};
        String[] addWords = {"七二", "而我却", "到处都是", "委任为", "广泛大概"};

        reqJson.put("removeParticiple", Arrays.asList(removeWords));
        reqJson.put("addParticiple", Arrays.asList(addWords));

        Console.show(HttpUtil.post(URL, reqJson.toJSONString()));
    }

    private static void createIndex() {
        URL = URL + "addIndex";
        JSONObject reqJson = new JSONObject();

        reqJson.put("indexName", "commodity_test");
        reqJson.put("indexJsonStr", "{\n" +
                "    \"settings\": {\n" +
                "        \"number_of_shards\": 5,\n" +
                "        \"number_of_replicas\": 1,\n" +
                "        \"max_result_window\": 2147483647,\n" +
                "        \"analysis\": {\n" +
                "            \"filter\": {\n" +
                "                \"my_synonym_filter\": {\n" +
                "                    \"type\": \"synonym\",\n" +
                "                    \"synonyms_path\": \"analysis/synonyms.txt\"\n" +
                "                }\n" +
                "            },\n" +
                "            \"analyzer\": {\n" +
                "                \"ik_syno\": {\n" +
                "                    \"type\": \"custom\",\n" +
                "                    \"tokenizer\": \"ik_smart\",\n" +
                "                    \"filter\": [\n" +
                "                        \"lowercase\",\n" +
                "                        \"my_synonym_filter\"\n" +
                "                    ]\n" +
                "                },\n" +
                "                \"ik_syno_max\": {\n" +
                "                    \"type\": \"custom\",\n" +
                "                    \"tokenizer\": \"ik_max_word\",\n" +
                "                    \"filter\": [\n" +
                "                        \"lowercase\",\n" +
                "                        \"my_synonym_filter\"\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "        },\n" +
                "        \"index\": {\n" +
                "            \"analysis.analyzer.default.type\": \"ik_max_word\"\n" +
                "        }\n" +
                "    },\n" +
                "    \"mappings\": {\n" +
                "            \"properties\": {\n" +
                "                \"L4mg_category_id\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"L4mg_category_name\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"agreement_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"agreement_price\": {\n" +
                "                    \"type\": \"double\"\n" +
                "                },\n" +
                "                \"analyzer\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"audit_time\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"bookValue\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"brand_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"brand_id_name\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"brand_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"channel_Name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"channel_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"commd_pic_url\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"comment_number\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"commodity_class\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"commodity_code\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"commodity_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"commodity_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"commodity_status\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"contact_id\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"contact_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"create_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"create_time\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"deal_way\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"description\": {\n" +
                "                    \"type\": \"text\"\n" +
                "                },\n" +
                "                \"discounts\": {\n" +
                "                    \"type\": \"double\"\n" +
                "                },\n" +
                "                \"domesticOrImport\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"down_time\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"ext_sku_id\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"factory\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"figure\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"goodsUseStatusDesc\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"insert_time\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"l1_category_id\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"l1_category_name\": {\n" +
                "                    \"type\": \"text\"\n" +
                "                },\n" +
                "                \"l2_category_id\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"l2_category_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    \"analyzer\": \"ik_syno_max\"\n" +
                "                },\n" +
                "                \"l3_category_id\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"l3_category_id_name\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"l3_category_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    \"analyzer\": \"ik_syno_max\"\n" +
                "                },\n" +
                "                \"l4mg_category_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"l4mg_category_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"manufacturer\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"market_price\": {\n" +
                "                    \"type\": \"double\"\n" +
                "                },\n" +
                "                \"materialId\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"materialName\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"materialSourceDesc\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"materialType\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"material_code\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"material_id\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"material_name\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"measure_name\": {\n" +
                "                    \"type\": \"text\"\n" +
                "                },\n" +
                "                \"member_price1\": {\n" +
                "                    \"type\": \"double\"\n" +
                "                },\n" +
                "                \"member_price2\": {\n" +
                "                    \"type\": \"double\"\n" +
                "                },\n" +
                "                \"member_price3\": {\n" +
                "                    \"type\": \"double\"\n" +
                "                },\n" +
                "                \"member_price4\": {\n" +
                "                    \"type\": \"double\"\n" +
                "                },\n" +
                "                \"member_price5\": {\n" +
                "                    \"type\": \"double\"\n" +
                "                },\n" +
                "                \"mfgsku\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"model\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"nuclearSecurityLevel\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"on_shelve_time\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"operId\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"operIds\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"operName\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"orgId\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"orgName\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"origin_brand_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"origin_brand_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"packState\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"param\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"picture_url\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"productTime\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"properties\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    \"analyzer\": \"ik_syno_max\"\n" +
                "                },\n" +
                "                \"push_time\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"qualityAssuranceLevel\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"qualityData\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"rangeOfApplication\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"remainLifetime\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"salePrice\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"sale_price\": {\n" +
                "                    \"type\": \"double\"\n" +
                "                },\n" +
                "                \"search_name\": {\n" +
                "                    \"type\": \"text\"\n" +
                "                },\n" +
                "                \"secondHandStateDesc\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"seven_free\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"ship_way\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"shop_name\": {\n" +
                "                    \"type\": \"text\"\n" +
                "                },\n" +
                "                \"size\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"skuNum\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"skuPhoneDetailChar\": {\n" +
                "                    \"type\": \"text\"\n" +
                "                },\n" +
                "                \"skuPoolIds\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"sku_code\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"sku_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"sku_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    \"analyzer\": \"ik_syno_max\"\n" +
                "                },\n" +
                "                \"sku_pool_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"sku_source\": {\n" +
                "                    \"type\": \"integer\"\n" +
                "                },\n" +
                "                \"sku_status\": {\n" +
                "                    \"type\": \"integer\"\n" +
                "                },\n" +
                "                \"sold_number\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"sourceAssort\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"sourceCost\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"spec\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"stockAge\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"supplierOrgId\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"supplierOrgName\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"supplier_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"supplier_id_name\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"supplier_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    },\n" +
                "                    \"analyzer\": \"ik_syno_max\"\n" +
                "                },\n" +
                "                \"supplier_shop_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"taxCode\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"taxRate\": {\n" +
                "                    \"type\": \"float\"\n" +
                "                },\n" +
                "                \"text\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"texture\": {\n" +
                "                    \"type\": \"text\"\n" +
                "                },\n" +
                "                \"totalPrice\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"type_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"type_name\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"unitAssessPrice\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"unitPrice\": {\n" +
                "                    \"type\": \"float\"\n" +
                "                },\n" +
                "                \"up_time\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"upc\": {\n" +
                "                    \"type\": \"keyword\"\n" +
                "                },\n" +
                "                \"vendor_id\": {\n" +
                "                    \"type\": \"long\"\n" +
                "                },\n" +
                "                \"vendor_name\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\"\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"view_order\": {\n" +
                "                    \"type\": \"integer\"\n" +
                "                },\n" +
                "                \"volume\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                },\n" +
                "                \"weight\": {\n" +
                "                    \"type\": \"text\",\n" +
                "                    \"fields\": {\n" +
                "                        \"keyword\": {\n" +
                "                            \"type\": \"keyword\",\n" +
                "                            \"ignore_above\": 256\n" +
                "                        }\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "    }\n" +
                "}");
        Console.show(HttpUtil.post(URL, reqJson.toJSONString()));
    }

    private static void operateIndex() {
        URL = "http://localhost:8081/es/elastic/deal";
        JSONObject req = new JSONObject();
        req.put("type", 1);
        Console.show(HttpUtil.post(URL, req.toJSONString()));

    }

    /**
     * 描述 同步数据到ES
     * @param
     * @return void
     * @author liugs
     * @date 2021/3/25 14:49:17
     */
    private static void syncDataToEs() {
        URL = "http://39.107.158.51:8089/es/elastic/syncAllData";
//        URL = "http://localhost:8081/es/elastic/syncAllData";

        JSONObject reqData = new JSONObject();
        reqData.put("sheet", 20);

        //指定供应商
        String[] supplierId = {"100062"};
        List<String> supplierIdList = Arrays.asList(supplierId);
        reqData.put("supplierId", supplierIdList);

        Console.show(HttpUtil.post(URL, reqData.toJSONString()));
    }

    /**
     * 描述 根据文章ID查询文章
     * @param
     * @return void
     * @author liugs
     * @date 2021/3/25 14:15:30
     */
    private static void getDocById() {
        URL = URL + "utilTest";
        JSONObject reqData = new JSONObject();
        reqData.put("operateType", "2");
        reqData.put("indexName", "alias_test");
        reqData.put("docId", "2021032501");


        Console.show(reqData.toJSONString());

        String result = HttpUtil.post(URL, reqData.toJSONString());
        Console.show(result);
    }

    /**
     * 描述 新增文章
     * @param
     * @return void
     * @author liugs
     * @date 2021/3/25 9:32:17
     */
    private static void addDocument() {
        URL = URL + "utilTest";

        JSONObject reqData = new JSONObject();
        reqData.put("operateType", "1");
        reqData.put("indexName", "index_test");
        reqData.put("docId", "2021032501");

        JSONObject source = new JSONObject();
        source.put("age", "25");
        source.put("description", "RestClient Api Test");
        source.put("name", "liugs_test1");
        source.put("update_test", "12121");
        source.put("weight", "1111");

        JSONObject nameExtData = new JSONObject();
        nameExtData.put("first_name", "liu");
        nameExtData.put("last_name", "gs");

//        source.put("name_ext", nameExtData.toJSONString());
        reqData.put("source", source.toJSONString());

        Console.show(reqData.toJSONString());

        String result = HttpUtil.post(URL, reqData.toJSONString());
        Console.show(result);
    }


    /**
     * 描述 删除索引
     * @param
     * @return void
     * @author liugs
     * @date 2021/3/30 17:06:19
     */
    private static void deleteIndices() {
        URL = URL + "utilTest";
        JSONObject reqJson = new JSONObject();
        reqJson.put("operateType", "5");
        reqJson.put("indexName", "111");

        Console.show(HttpUtil.post(URL, reqJson.toJSONString()));
    }
}
