package me.piitex.os.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Set;

public class MasterKeyManager {
    private static final File MASTER_KEY_FILE = new File(System.getProperty("user.home"), ".ren.ff12.bin");
    private static final int MASTER_KEY_LENGTH_BYTES = 64; // Use 64 bytes (512 bits) for a very strong master secret

    private static final Logger logger = LoggerFactory.getLogger(MasterKeyManager.class);

    /**
     * Retrieves the persistent master key. Generates a new one if it doesn't exist.
     * @return The persistent master key as a character array (for compatibility with PBEKeySpec).
     */
    public static char[] getPersistentPassKey() {
        if (!MASTER_KEY_FILE.exists()) {
            generateAndSaveMasterKey();
        }
        return loadMasterKey();
    }

    private static void generateAndSaveMasterKey() {
        System.out.println("Generating new persistent master key...");
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[MASTER_KEY_LENGTH_BYTES];
        random.nextBytes(keyBytes);

        try (FileOutputStream outputStream = new FileOutputStream(MASTER_KEY_FILE)) {
            // Write data
            outputStream.write(keyBytes);

            Path filePath = MASTER_KEY_FILE.toPath();
            String os = System.getProperty("os.name").toLowerCase();

            // Setup linux chmod permissions.
             if (os.contains("nix") || os.contains("nux") || os.contains("aix") || os.contains("mac")) {
                try {
                    // "r--------" means Owner: Read | Group: None | Others: None
                    Set<PosixFilePermission> perms = PosixFilePermissions.fromString("r--------");
                    Files.setPosixFilePermissions(filePath, perms);
                } catch (IOException e) {
                    // This can happen if the OS doesn't support POSIX permissions or if the Java process lacks the permission to change them.
                    logger.error("Could not set POSIX permissions (chmod 400): {}", e.getMessage());
                }
            }

            // Set the file to hidden.
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                try {
                    Files.setAttribute(filePath, "dos:hidden", true);
                } catch (IOException e) {
                    logger.error("Could not set hidden attribute on Windows: {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.error("Failed to save master key file.", e);
        } finally {
            // Securely wipe the key from memory
            Arrays.fill(keyBytes, (byte) 0);
        }
    }

    private static char[] loadMasterKey() {
        byte[] keyBytes = new byte[MASTER_KEY_LENGTH_BYTES];
        try (FileInputStream fis = new FileInputStream(MASTER_KEY_FILE)) {
            int bytesRead = fis.read(keyBytes);
            if (bytesRead != MASTER_KEY_LENGTH_BYTES) {
                throw new IOException("Incomplete read of master key file.");
            }
        } catch (IOException e) {
            logger.error("Failed to load master key file.", e);
        }

        // Convert byte array to character array for PBEKeySpec
        char[] keyChars = new char[MASTER_KEY_LENGTH_BYTES];
        for (int i = 0; i < MASTER_KEY_LENGTH_BYTES; i++) {
            keyChars[i] = (char) (keyBytes[i] & 0xFF);
        }

        // Wipe the byte array version of the key after conversion
        Arrays.fill(keyBytes, (byte) 0);
        return keyChars;
    }
}