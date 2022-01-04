package com.liugs.tool.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liugs.tool.base.Console;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @ClassName JsonTester
 * @Description JOSN测试
 * @Author liugs
 * @Date 2021/7/8 14:52:11
 */
public class JsonTester {

    public static void main(String[] args) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("fieldOne", "1");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(jsonObject);

        List<TestObj> testObjs = JSONObject.parseArray(JSON.toJSONString(jsonArray), TestObj.class);
        for (TestObj obj : testObjs) {
            if (StringUtils.isEmpty(obj.getFieldThree())) {
                Console.show("===========");
            }
        }
    }
}
