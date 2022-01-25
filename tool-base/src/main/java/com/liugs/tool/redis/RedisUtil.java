package com.liugs.tool.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName RedisUtil
 * @Description
 * @Author liugs
 * @Date 2022/1/21 17:16
 */
public class RedisUtil {

    private String redisIp = "xxx.xx.xx.xxx";
    private int redisPort = 6879;
    private String redisAuth = "xxxxxx";
    private int timeout = 30000;

    private Jedis jedis;

    public RedisUtil() {
        jedis = new Jedis(redisIp, redisPort);
        jedis.auth(redisAuth);
    }

    /**
     * 描述  使用连接池
     * @param
     * @return void
     * @author liugs
     * @date 2022/1/25 11:37
     */
    private void Pool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxTotal(20);
        JedisPool pool = new JedisPool(config, redisIp, redisPort, timeout, redisAuth);
        jedis = pool.getResource();

        // 使用完后，将连接还给连接池，使用连接池的情况下，是不会关闭连接的，而是将连接返回到连接池，可以看代码。
        jedis.close();
    }

    // 高可用连接，可以在Redis Sentinel主从切换时候，通知应用，连接到新的master
    // Jedis版本必须2.4.2或更新版本
    /**
     * 描述 高可用实例
     * @param
     * @return void
     * @author liugs
     * @date 2022/1/25 11:40
     */
    private void highAvailability() {
        Set<String> sentinels = new HashSet<>();
        sentinels.add("ip:port");
        sentinels.add("ip:port");

        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxTotal(20);
        JedisSentinelPool sentinelPool = new JedisSentinelPool("master", sentinels, config);

        jedis = sentinelPool.getResource();

        // 使用完后，将连接还给连接池，使用连接池的情况下，是不会关闭连接的，而是将连接返回到连接池，可以看代码。
        jedis.close();
    }



}
