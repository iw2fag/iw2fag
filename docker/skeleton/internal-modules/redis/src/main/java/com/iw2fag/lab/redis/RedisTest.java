package com.iw2fag.lab.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 3/13/2018
 * Time: 11:22 AM
 */
public class RedisTest {
    public static void main(String[] args) {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxWaitMillis(1000);
        JedisPool pool = new JedisPool(poolConfig, "192.168.99.100", 6379);

        Jedis jedis = pool.getResource();
        jedis.set("name", "zs");
        System.out.println(jedis.exists("name"));
        System.out.println(jedis.get("name"));

    }
}
