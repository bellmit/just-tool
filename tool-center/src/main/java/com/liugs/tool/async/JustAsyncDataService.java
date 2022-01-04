package com.liugs.tool.async;

import com.liugs.tool.ability.bo.JustSyncDataServiceReqBo;
import com.liugs.tool.util.PropertiesManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName JustAsyncDataService
 * @Description 数据同步
 * @Author liugs
 * @Date 2021/6/15 15:28:45
 */
@Slf4j
@Component
public class JustAsyncDataService {

    private static List<String> merchantIds = new ArrayList<>();

    @Value("${data.sync.shard:10}")
    private int numberOfShard;

    @Autowired
    private PropertiesManager propertiesManager;



    @Async("ToolAsyncExecutor")
    public void async(JustSyncDataServiceReqBo reqBo) {

        if (StringUtils.isEmpty(reqBo.getMerchantId())) {
            merchantIds = getAllMerchantId();
        } else {
            Collections.addAll(merchantIds, reqBo.getMerchantId().split(","));
        }

        if (CollectionUtils.isEmpty(merchantIds)) {
            log.info("商户列表为空，同步结束");
            return;
        }

        if (reqBo.getIsMultiThread()) {
            syncByMultiThread();
            return;
        }

        syncBySingleThread();
    }

    /**
     * 描述 多线程同步
     * @return void
     * @author liugs
     * @date 2021/7/15 14:36:28
     */
    private void syncByMultiThread() {
        //遍历商户ID
        for (String merchantId : merchantIds) {
            //根据商户ID获取到总记录数
//            int totalOfRecord = getAllDataByMerchantId();
            //根据分片书获取每片的大小
//            int cycle = totalOfRecord % numberOfShard == 0 ? numberOfShard : numberOfShard + 1;

            for (int i = 0; i < numberOfShard; i ++) {

            }


        }

    }

    /**
     * 描述 单线程同步
     * @return void
     * @author liugs
     * @date 2021/7/15 14:36:44
     */
    private void syncBySingleThread() {

    }

    /**
     * 描述 获取所有的商户ID
     * @param
     * @return java.util.List<java.lang.String>
     * @author liugs
     * @date 2021/7/14 15:49:42
     */
    private List<String> getAllMerchantId() {

        return null;
    }
}
