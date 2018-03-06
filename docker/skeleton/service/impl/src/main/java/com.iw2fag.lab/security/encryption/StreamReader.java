package com.iw2fag.lab.security.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamReader implements Runnable
{
    private static final Logger logger = LoggerFactory.getLogger(StreamReader.class);

    private final InputStream in;
    private final OutputStream out;

    public StreamReader( InputStream in, OutputStream out ) {
        this.in = in;
        this.out = out;
    }

    @Override
    public void run()
    {
        int c;
        try {
            while( ( c = in.read() ) != -1 ) {
                out.write( c );
            }
            out.flush();
        }
        catch( IOException e ) {
            logger.debug("exception while reading stream", e);
        }
        finally {
            closeStream(in);
            closeStream(out);
        }
    }

    private void closeStream(final Closeable closeableStream) {
        if (closeableStream != null) {
            try {
                closeableStream.close();
            } catch (IOException e) {
                // do nothing here
                logger.debug("failed closing stream", e);
            }
        }
    }
}
