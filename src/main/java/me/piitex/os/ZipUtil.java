package me.piitex.os;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    public static void unzipFile(File zip, File outDirectory) throws IOException {
        if (!outDirectory.exists()) {
            outDirectory.mkdir();
        }
        try (ZipInputStream zipIn = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)))) {
            ZipEntry entry = zipIn.getNextEntry();
            // iterates over entries in the zip file
            while (entry != null) {
                if (!entry.isDirectory()) {
                    // if the entry is a file, extract it
                    extractFile(entry.getName(), zipIn, outDirectory);
                } else {
                    // if the entry is a directory, create the directory
                    File dir = new File(outDirectory, entry.getName());
                    dir.mkdir();
                }
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }

    private static void extractFile(String name, ZipInputStream zipIn, File output) throws IOException {
        File newFile = new File(output, name);
        newFile.getParentFile().mkdirs();
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(newFile))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }

    public static void zipDirectory(File zipFile, File directory) {
        if (!directory.isDirectory()) {
            return;
        }

        if (zipFile.exists()) {
            throw new RuntimeException("Zip file already exists! Cannot create.");
        }

        try {
            if (zipFile.createNewFile()) {
                logger.info("Created zip file '{}'", zipFile.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFile));

            int zipped = 0;
            for (File file : directory.listFiles()) {
                if (!file.isFile()) {
                    continue;
                }
                zipFile(file, outputStream, zipped);
            }

            outputStream.close();
            logger.info("Zipped '{}' files", zipped);
        } catch (IOException e) {
            logger.error("Could not create zip file!", e);
        }

    }

    private static void zipFile(File file, OutputStream outputStream, int processed) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        try (FileInputStream fileInputStream = new FileInputStream(file)) {

            // 3. Read the file content in chunks and write to the ZipOutputStream
            while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
                processed++;
            }
        } catch (IOException e) {
            logger.error("Could not zip '{}'", file.getAbsolutePath(), e);
        }
    }
}
