package com.iw2fag.lab;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;

/**
 * @author Henry
 * @create 2018-10-29 4:58 PM
 **/
public class DownloadFileHandler extends ChannelInboundHandlerAdapter {

    private AsciiString contentType = HttpHeaderValues.TEXT_PLAIN;

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Registered");
        ctx.fireChannelRegistered();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest httpRequest = (FullHttpRequest) msg;
        String uri = httpRequest.uri();
        System.out.println("Request Uri:" + uri);

        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.wrappedBuffer("HelloWorld".getBytes())); // 2

        HttpHeaders heads = response.headers();
        heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes()); // 3
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);

        ctx.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }


}
