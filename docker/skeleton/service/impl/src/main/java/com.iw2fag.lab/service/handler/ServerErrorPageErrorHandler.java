package com.iw2fag.lab.service.handler;

import org.eclipse.jetty.server.handler.ErrorHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;


public class ServerErrorPageErrorHandler extends ErrorHandler {

    @Override
    protected void writeErrorPageBody(HttpServletRequest request, Writer writer, int code, String message, boolean showStacks) throws IOException {

        String uri = request.getRequestURI();

        writeErrorPageMessage(request, writer, code, message, uri);
        if (showStacks)
            writeErrorPageStacks(request, writer);
        writer.write("<hr><i><small>Powered by Server://</small></i><hr/>\n");
    }
}
