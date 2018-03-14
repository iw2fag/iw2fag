package com.iw2fag.lab.redis.lock;


import redis.clients.jedis.Jedis;

import java.util.Collections;

/***
 * https://redis.io/topics/distlock
 *
 */
public class RedisDistributedLock {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * SET resource_name my_random_value NX PX 30000
     * @param jedis
     * @param lockKey
     * @param distributedId
     * @param expirationTime
     * @return
     */
    public boolean tryGetDistributedLock(Jedis jedis, String lockKey, String distributedId, int expirationTime) {

        String returnValue = jedis.set(lockKey, distributedId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expirationTime);
        if (LOCK_SUCCESS.equals(returnValue)) {
            return true;
        }
        return false;
    }

    /**
     *   if redis.call("get",KEYS[1]) == ARGV[1] then
         return redis.call("del",KEYS[1])
         else
         return 0
         end
     * @param jedis
     * @param lockKey
     * @param distributedId
     * @return
     */
    public boolean releaseDistributedLock(Jedis jedis, String lockKey, String distributedId) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(distributedId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

}
