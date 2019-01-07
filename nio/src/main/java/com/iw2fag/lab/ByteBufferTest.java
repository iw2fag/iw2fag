package com.iw2fag.lab;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * @author Henry
 * @create 2018-11-12 10:45 AM
 **/
public class ByteBufferTest {
    public static void main(String[] args){
        PooledByteBufAllocator pooledByteBufAllocator = PooledByteBufAllocator.DEFAULT;
        ByteBuf byteBuf = pooledByteBufAllocator.heapBuffer();


    }
}
