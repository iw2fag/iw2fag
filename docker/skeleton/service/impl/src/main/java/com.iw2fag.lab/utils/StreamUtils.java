package com.iw2fag.lab.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * This class is intended to centralize InputStreamReader creation using UTF-8 as default
 * or the file.encoding property based on a env. property that is set to OFF be default.
 * in addition you can create an InputStreamReader by explicitly passing the charset.
 */
public final class StreamUtils {

    private static final Logger logger = LoggerFactory.getLogger(StreamUtils.class);
    /**
     * private cons. to avoid instances
     */
    private StreamUtils() {
    }

    /**
     * Creates a BufferedReader for the Process.getInputStream() using the logic used in createInputReader(InputStream inputStream) method.
     * @param process the Process with the InputStream to wrap
     * @return a BufferedReader wrapping the InputStream
     */
    public static BufferedReader createInputReader(Process process) {
        return createInputReader(process.getInputStream());
    }

    /**
     * Creates a BufferedReader using UTF-8 as default or file.encoding property based on an env. variable.
     * @param inputStream the stream to wrap
     * @return a BufferedReader wrapping the InputStream
     */
    public static BufferedReader createInputReader(InputStream inputStream) {
        String useDefault = System.getProperty("USE_DEFAULT_CHARSET_INPUT_READER", Boolean.FALSE.toString());
        if ("true".equalsIgnoreCase(useDefault)) {
            return new BufferedReader(new InputStreamReader(inputStream));
        }
        return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
    }

    /**
     * Creates a BufferedReader using the passed in charset as default or the logic used in createInputReader(InputStream inputStream) method.
     * @param inputStream the stream to wrap
     * @param charsetName the charset to use
     * @return a BufferedReader wrapping the InputStream
     */
    public static BufferedReader createInputReader(InputStream inputStream, String charsetName) {
        if (charsetName == null || charsetName.isEmpty()) {
            return createInputReader(inputStream);
        }
        Charset charset = Charset.forName(charsetName);
        return new BufferedReader(new InputStreamReader(inputStream, charset));

    }

    public static boolean saveStream(InputStream inputStream, String fileName, String targetFolder) {
        OutputStream out = null;
        try {
            if (FileUtils.verifyAndEnsureFolderExist(targetFolder)) {

                File targetFile = new File(targetFolder + File.separator + fileName);
                // write the inputStream to a FileOutputStream
                out = new FileOutputStream(targetFile);

                int read;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                inputStream.close();
                out.flush();
                out.close();
                if (logger.isDebugEnabled()) {
                    logger.debug("New file created! " + targetFile.getAbsolutePath());
                }
                return true;

            } else {
                logger.error(targetFolder + " does not exist and file cannot be saved to the filesystem.");
            }
        } catch (IOException e) {
            logger.error("Failed to save file", e);
        } finally {
            if(out!=null){
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
        return false;
    }

}
