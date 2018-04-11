package com.iw2fag.lab.redis.mq;

public interface Callback {
    public void onMessage(String message);
}
