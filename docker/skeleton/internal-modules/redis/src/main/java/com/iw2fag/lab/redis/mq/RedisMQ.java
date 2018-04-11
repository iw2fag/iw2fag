package com.iw2fag.lab.redis.mq;

import redis.clients.jedis.Jedis;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 3/14/2018
 * Time: 4:42 PM
 */
public class RedisMQ {

    private Jedis jedis;

    public RedisMQ(Jedis jedis) {
        this.jedis = jedis;
    }


    public void produce(String key, String message) {
        jedis.lpush(key, message);
    }

    public String consume(String key) {
        return jedis.rpop(key);
    }

}
