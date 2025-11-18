package me.piitex.os;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipUtil {

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
        System.out.println("File name: " + name);
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(output, name)))) {
            byte[] bytesIn = new byte[4096];
            int read;
            while ((read = zipIn.read(bytesIn)) != -1) {
                bos.write(bytesIn, 0, read);
            }
        }
    }
}
