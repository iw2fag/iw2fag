package com.iw2fag.lab.utils;

import com.google.common.base.Preconditions;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.*;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public static boolean saveInputToFile(String content, String folder, String fileName) {
        if (content == null) {
            System.out.println("Cannot save null content!");
            return false;
        }
        verifyAndEnsureFolderExist(folder);
        FileOutputStream fop = null;
        File file;
        String fullPath = folder + File.separator + fileName;
        try {

            file = new File(fullPath);
            //if the file already exist , keep the value as is, do not override it with the new data
            if (file.exists()) {
                System.out.println("### enrypted.properties file already exists, cannot override existing value: " + fullPath);
                return false;
            }

            if (!file.exists()) {
                fop = new FileOutputStream(file);
            }
            // if file doesn't exists, then create it
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    System.out.println("Failed to create file " + fullPath);
                    return false;
                }
            }

            // get the content in bytes
            byte[] contentInBytes = (content + "\n").getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();
            fop = null;
            return true;

        } catch (IOException e) {
            System.err.println("Exception while saving file " + fullPath + ", " + e);
            return false;
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                System.err.println("Exception " + e);
            }
        }

    }

    /**
     * Verify given folder is present on file system, if not, create it
     *
     * @param targetFolder
     * @return
     */
    public static boolean verifyAndEnsureFolderExist(String targetFolder) {
        if (targetFolder != null && !targetFolder.isEmpty()) {
            File file = new File(targetFolder);
            if (file.exists()) {
                return true;
            }
            if (file.mkdirs()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Verify given directory is present on file system
     *
     * @param targetDir
     * @return
     */
    public static boolean verifyDirExist(String targetDir) {
        if (targetDir != null && !targetDir.trim().isEmpty()) {
            File file = new File(targetDir);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    public static String getKeyValue(String filePath, String keyName) {
        Properties properties = getPropertiesFile(filePath);
        return properties.getProperty(keyName);
    }

    public static Properties getPropertiesFile(String filePath) {
        Properties result = new Properties();
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(filePath);
            result.load(fileReader);
        } catch (IOException e) {
            System.err.println("Failed to close BufferedReader for file: " + filePath + " ," + e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    // Do nothing here
                }
            }
        }
        return result;
    }

    public static String setKeyValue(String filePath, String keyName, String value) {
        FileOutputStream out = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(filePath);
            Properties props = new Properties();
            props.load(in);
            in.close();

            out = new FileOutputStream(filePath);
            Object oldValObj = props.setProperty(keyName, value);
            String oldValue = oldValObj != null ? oldValObj.toString() : null;
            props.store(out, null);
            return oldValue;
        } catch (IOException e) {
            System.err.println("Exception while updating properties file " + filePath + ", " + e);
            return null;
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    // Do nothing in this case
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // Do nothing in this case
                }
            }

        }
    }

    /**
     * @param fullPath
     */
    public static void deleteDirRecursively(final String fullPath) {
        try {
            if (null == fullPath) {
                return;
            }
            File dir = new File(fullPath);
            if (dir.exists()) {
                deleteRecursively(dir);
            } else {
                logger.debug("Directory '{}' already deleted.", fullPath);
            }
        } catch (IOException e) {
            logger.error("Failed to deleteDir {}, Message: {}", fullPath, e.getMessage());
        }
    }

    public static void deleteDirectory(File dir) throws IOException {
        org.apache.commons.io.FileUtils.deleteDirectory(dir);
    }

    private static void deleteDirectoryContents(File directory) throws IOException {
        Preconditions.checkArgument(directory.isDirectory(), "Not a directory: %s", directory);
        if (directory.getCanonicalPath().equalsIgnoreCase(directory.getAbsolutePath())) {
            File[] files = directory.listFiles();
            if (files == null) {
                throw new IOException("Error listing files for " + directory);
            } else {
                File[] arr$ = files;
                int len$ = files.length;

                for (int i$ = 0; i$ < len$; ++i$) {
                    File file = arr$[i$];
                    deleteRecursively(file);
                }
            }
        }
    }

    private static void deleteRecursively(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectoryContents(file);
        }

        if (!file.delete()) {
            throw new IOException("Failed to delete " + file);
        }
    }

    /**
     * Delete file by full path, assume the fullPath represents a file
     *
     * @param fullPath
     */
    public static void deleteFile(String fullPath) {
        try {
            if (null == fullPath) {
                return;
            }
            Path file = Paths.get(fullPath);
            Files.deleteIfExists(file);
            logger.debug("File '{}' already deleted.", fullPath);
        } catch (IOException e) {
            logger.error("Failed to deleteFile {}, Message: {}", fullPath, e.getMessage());
        }
    }

    public static boolean isInSubDirectory(File dir, File file) {
        if (file == null) {
            return false;
        }

        if (file.equals(dir)) {
            return true;
        }

        return isInSubDirectory(dir, file.getParentFile());
    }

    public static boolean saveBase64(String data, String name, String targetFolder) {
        String fileFullPath = targetFolder + File.separator + name;
        if (verifyAndEnsureFolderExist(targetFolder)) {
            FileOutputStream fileOutputStream = null;
            try {
                byte[] byteArray = Base64.decodeBase64(data.getBytes());
                fileOutputStream = new FileOutputStream(fileFullPath);
                fileOutputStream.write(byteArray);
                fileOutputStream.close();
                return true;
            } catch (Exception e) {
                logger.error("failed to save data to " + fileFullPath, e);
                return false;
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        logger.error("Failed to close output stream", e);
                    }
                }
            }
        } else {
            logger.debug("failed to save data to {}", fileFullPath);
            return false;
        }
    }

    public static boolean createZip(String zipFile, String screenshotsDir, List<String> fileList) {

        byte[] buffer = new byte[1024];
        ZipOutputStream zos = null;
        FileInputStream in = null;
        try {

            FileOutputStream fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            logger.debug("Output to Zip : " + zipFile);

            for (String file : fileList) {

                logger.debug("File Added : " + file);
                ZipEntry ze = new ZipEntry(file);
                zos.putNextEntry(ze);

                in = new FileInputStream(screenshotsDir + File.separator + file);

                int len;
                while ((len = in.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
                }

                in.close();
            }

            zos.closeEntry();
            // remember close it
            zos.close();

            logger.debug("Zip Folder Done");
            return true;
        } catch (Exception ex) {
            logger.error("Zip failed for folder " + screenshotsDir);
            return false;
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
        }
    }

    /**
     * Traverse a directory and get all files, and add the file into fileList
     *
     * @param node           file or directory
     * @param fileList       the list that is being built
     * @param screenshotsDir the screenshot directory
     */
    public static void generateFileList(File node, List<String> fileList, String screenshotsDir) {
        if (node == null) {
            return;
        }
        // add file only
        if (node.isFile()) {
            fileList.add(generateZipEntry(node.getAbsoluteFile().toString(), screenshotsDir));
        }

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote) {
                generateFileList(new File(node, filename), fileList, screenshotsDir);
            }
        }

    }


    /**
     * Format the file path for zip
     *
     * @param file          file path
     * @param screenshotDir the screenshot directory
     * @return Formatted file path
     */
    private static String generateZipEntry(String file, String screenshotDir) {
        return file.substring(screenshotDir.length() + 1, file.length());
    }

    public static boolean verifyAndEnsureFileExist(String targetFolder, String targetFileName) {
        if (verifyAndEnsureFolderExist(targetFolder)) {
            File file = new File(targetFolder, targetFileName);
            if (!file.exists()) {
                try {
                    if (file.createNewFile()) {
                        return true;
                    }
                } catch (IOException e) {
                    logger.error("verifyAndEnsureFileExist failed", e);
                    return false;
                }
            } else {
                // file exists
                return true;
            }
        }
        logger.error("verifyAndEnsureFileExist failed");
        return false;
    }


    /**
     * @param targetFolder
     * @param targetFileName
     * @return
     */
    public static OutputStream createFileOutputStream(String targetFolder, String targetFileName) {
        if (verifyAndEnsureFileExist(targetFolder, targetFileName)) {
            File file = new File(targetFolder, targetFileName);
            try {
                FileOutputStream fos = new FileOutputStream(file, true);
                // as a default we create a more efficient output stream
                return new BufferedOutputStream(fos);
            } catch (IOException e) {
                logger.error("createFileOutputStream failed", e);
            }
        } else {
            logger.error("createFileOutputStream failed");
        }
        return null;
    }


    public static boolean upload(InputStream inputStream, String fileName, String targetFolder) {
        String fullPath = targetFolder + File.separator + fileName;
        OutputStream out = null;
        try {
            //TODO: check performance
            if (verifyAndEnsureFolderExist(targetFolder)) {

                File targetFile = new File(fullPath);
                // write the inputStream to a FileOutputStream
                out = new FileOutputStream(targetFile);

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }

                inputStream.close();
                out.flush();
                out.close();
                out = null;
                if (logger.isDebugEnabled()) {
                    logger.debug("New file created! " + targetFile.getAbsolutePath());
                }
                return true;

            } else {
                logger.error(String.format("%s does not exist and file '%s' cannot be saved to the filesystem in that location.", targetFolder, fileName));
            }
        } catch (IOException e) {
            logger.error("Failed to save file " + fullPath, e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                    // should not happen
                    logger.error("Exception", e);
                }
            }
        }
        return false;
    }


    public static byte[] getFileBytes(String dir, String fileName) {
        Path path = FileSystems.getDefault().getPath(dir, fileName);
        try {
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            logger.error(String.format("Failed to load filename:%s dir:%s", fileName, dir), ex);
            return null;
        }
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {
        IOUtils.copy(in, out);
    }

    public static String binaryFileToBase64(String fullPath) {
        InputStream inputStream = null;
        String appAsBase64String = null;
        try {
            File file = new File(fullPath);
            if (file.exists()) {
                inputStream = new FileInputStream(file);
                byte[] bytes = IOUtils.toByteArray(inputStream);
                byte[] byteASBase64 = Base64.encodeBase64(bytes);
                appAsBase64String = new String(byteASBase64);
            } else {
                logger.error("File not exist in path " + fullPath);
                return null;
            }
        } catch (Exception e) {
            logger.error("Failed to set File as base64", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("Failed to close base64 inputStream");
                }
            }
        }
        return appAsBase64String;
    }

    public byte[] fileToByteArray(String fullPath) throws IOException {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(fullPath));
            return IOUtils.toByteArray(inputStream);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    public static Path moveFile(Path src, Path target, CopyOption... options) {
        if (src != null && src.toFile().exists() && target != null) {
            try {
                logger.debug("Moving file from: " + src.toString() + " to: " + target.toString());
                return Files.move(src, target, options);
            } catch (IOException ioException) {
                logger.error("Failed to move file: " + src.toString() + " to: " + target.toString());
            }
        }
        return null;
    }

    public static boolean copyDir(String srcDir, String destDir) {
        if (srcDir == null || destDir == null) {
            logger.debug("One or more of the given arguments is null");
            return false;
        }

        File src = new File(srcDir);
        File dest = new File(destDir);
        if (!src.exists() || !dest.exists()) {
            logger.debug("One or more of the given arguments is not exists");
            return false;
        }

        try {
            org.apache.commons.io.FileUtils.copyDirectory(src, dest);
            return true;
        } catch (Exception exception) {
            logger.error("Runtime error occur during copy directory", exception);
            return false;
        }

    }
}