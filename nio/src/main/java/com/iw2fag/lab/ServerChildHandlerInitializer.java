package com.iw2fag.lab;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * @author Henry
 * @create 2018-10-29 3:41 PM
 **/
public class ServerChildHandlerInitializer extends ChannelInitializer<SocketChannel> {

    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("Init Channel Handler");
        ch.pipeline().addLast(new LoggingHandler());
        ch.pipeline().addLast(new HttpServerCodec());
        ch.pipeline().addLast(new HttpObjectAggregator(1024 * 10));
        ch.pipeline().addLast(new ChunkedWriteHandler());
    }
}
