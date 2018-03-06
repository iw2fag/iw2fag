package com.iw2fag.lab.utils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: yanghanx
 * Date: 2/2/2018
 * Time: 10:04 AM
 */
public class ExecuteShellUtils {

    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private ExecuteShellUtils() {
    }

    /**
     * @param platform
     * @param script
     * @param executionDir
     * @param timeout
     * @return
     * @throws IOException
     */
    public static ShellOutput executeScript(Platform platform, String script, File executionDir, Integer timeout) throws IOException {
        File scriptFile = createScriptFile(platform, script);
        try {
            ProcessBuilder pb = new ProcessBuilder();
            pb.command(createCommand(platform, scriptFile));
            pb.directory(executionDir);

            ShellOutput output = startProcess(pb, timeout);
            return output;
        } finally {
            scriptFile.delete();
        }
    }

    private static ShellOutput startProcess(final ProcessBuilder pb, final Integer timeout) throws IOException {
        final ShellOutput output = new ShellOutput();
        Future<Object> future = executorService.submit(new Callable<Object>() {

            Process process;

            @Override
            public Boolean call() throws Exception {
                try {
                    process = pb.start();
                    // Catch outputs
                    RetrieveOutputFromStreamThread retrieveNormalOutputThread = new RetrieveOutputFromStreamThread(process.getInputStream(), output);
                    RetrieveOutputFromStreamThread retrieveErrorOutputThread = new RetrieveOutputFromStreamThread(process.getErrorStream(), output);
                    retrieveNormalOutputThread.start();
                    retrieveErrorOutputThread.start();

                    //wait for process finished and get the output
                    if (timeout != null) {
                        process.waitFor();
                        retrieveNormalOutputThread.join();
                        retrieveErrorOutputThread.join();
                    }
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    process.destroy();
                    output.setExitCode(process.exitValue());
                }
            }
        });

        try {
            if (timeout != null) {
                future.get(timeout.longValue(), TimeUnit.SECONDS);
            } else {
                future.get();
            }
        } catch (TimeoutException e) {
            output.setIsTimeout(true);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            future.cancel(true);
        }

        return output;
    }

    private static class RetrieveOutputFromStreamThread extends Thread {
        private final InputStream in;
        private final ShellOutput output;

        private RetrieveOutputFromStreamThread(InputStream in, ShellOutput output) {
            this.in = in;
            this.output = output;
        }

        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder("");
                String line;
                while ((line = br.readLine()) != null) {
                    synchronized (output) {
                        sb.append(line).append(System.lineSeparator());
                    }
                }
                if (!sb.toString().isEmpty()) {
                    output.setOutput(sb.toString());
                }
                br.close();
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private static List<String> createCommand(Platform platform, File batchFile) throws IOException {
        if (Platform.Windows == platform) {
            return Arrays.asList("cmd.exe", "/c", escapeFilePathBlankSpace(batchFile.getAbsolutePath()));
        }
        return Collections.singletonList(batchFile.getAbsolutePath());
    }

    private static String escapeFilePathBlankSpace(String filename) {
        return "\"" + filename + "\"";
    }

    private static File createScriptFile(Platform platform, String commands) throws IOException {
        File scriptFile = File.createTempFile("script", getScriptExtension(platform));
        scriptFile.deleteOnExit();
        scriptFile.setExecutable(true);
        scriptFile.setWritable(true);

        String content = commands;
        String scriptHeader;
        if (platform == Platform.Windows) {
            scriptHeader = "@echo off";
        } else {
            scriptHeader = "#!/bin/bash";
        }
        content = scriptHeader + System.lineSeparator() + content;
        write(scriptFile, content);
        return scriptFile;
    }

    private static void write(File file, String content) throws IOException {

        if (content == null) {
            throw new IllegalArgumentException("content can not be null");
        }

        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("file must exist");
        }

        if (file.isDirectory()) {
            throw new IllegalArgumentException("file can't be a directory");
        }

        FileOutputStream fos = new FileOutputStream(file, false);
        fos.write(content.getBytes(Charset.defaultCharset()));
        fos.close();
    }

    private static String getScriptExtension(Platform platform) {
        if (platform == Platform.Windows) {
            return ".bat";
        }
        return ".sh";
    }

    public static class ShellOutput {

        private String output;
        private int exitCode;
        private boolean isTimeout = false;

        public void setOutput(String output) {
            this.output = output;
        }

        public void setExitCode(int exitCode) {
            this.exitCode = exitCode;
        }

        public void setIsTimeout(boolean isTimeout) {
            this.isTimeout = isTimeout;
        }

        public String getOutput() {
            return output;
        }

        public int getExitCode() {
            return exitCode;
        }

        public boolean isTimeout() {
            return isTimeout;
        }
    }

    public enum Platform {
        Windows,
        Linux,
        MacOS
    }

}
