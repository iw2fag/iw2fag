package com.iw2fag.lab.undertow;

import org.xnio.*;

import java.nio.ByteBuffer;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 12/27/2017
 * Time: 4:46 PM
 */
public class HelloWorldServer2 {

    public static void main(String[] args) throws Exception {
        Xnio xnio = Xnio.getInstance();

        XnioWorker worker = xnio.createWorker(OptionMap.builder()
                .set(Options.WORKER_IO_THREADS, 5)
                .set(Options.WORKER_TASK_CORE_THREADS, 5)
                .set(Options.WORKER_TASK_MAX_THREADS, 10)
                .set(Options.TCP_NODELAY, true)
                .getMap());

        OptionMap socketOptions = OptionMap.builder()
                .set(Options.WORKER_IO_THREADS, 5)
                .set(Options.TCP_NODELAY, true)
                .set(Options.REUSE_ADDRESSES, true)
                .getMap();


        Pool<ByteBuffer> buffers = new ByteBufferSlicePool(BufferAllocator.DIRECT_BYTE_BUFFER_ALLOCATOR, 1024, 1024 * 10);


    }

}
