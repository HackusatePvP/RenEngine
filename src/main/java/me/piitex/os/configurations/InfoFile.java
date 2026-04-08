package me.piitex.os.configurations;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.IllegalBlockSizeException;
import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class InfoFile {
    private final File file;
    private final boolean encrypt;
    private final boolean dev = false; // When set to true it disables encryption even if encryption is true.

    private static final Logger logger = LoggerFactory.getLogger(InfoFile.class);

    private Map<String, String> entryMap = new HashMap<>();

    /**
     * Constructs a new {@code InfoFile} instance for storing data in memory.
     * <p>
     * This constructor does not create or associate with a physical file.
     */
    public InfoFile() {
        this.file = null;
        this.encrypt = false;
    }

    /**
     * Constructs a new {@code InfoFile} instance and associates it with a physical file.
     * <p>
     * If the file does not exist, it will be created. If the file exists, its contents
     * will be loaded into memory via a binary stream. The file's contents will be decrypted if
     * encryption is enabled.
     *
     * @param file    the file to read from and write to.
     * @param encrypt a boolean indicating whether the file should be encrypted.
     * @throws RuntimeException if an {@link IOException} or {@link FileNotFoundException} occurs during file handling.
     */
    public InfoFile(File file, boolean encrypt) {
        this.file = file;
        this.encrypt = encrypt;
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    logger.warn("Unable to create file '{}'", file.getAbsolutePath());
                }
            } catch (IOException e) {
                logger.error("IO exception occurred while creating info file!", e);
            }
        } else {
            File output = null;
            try {
                output = Files.createTempFile(null, ".info").toFile();
                File targetFile = file;

                if (encrypt && !dev) {
                    try {
                        FileCrypter.decryptFile(file, output);
                        targetFile = output;
                    } catch (IllegalBlockSizeException | IOException ignored) {
                        // Fallback or ignore if decryption fails
                    }
                }

                // Safeguard: Only attempt to read if the file actually has data
                if (targetFile.length() > 0) {
                    try (DataInputStream dis = new DataInputStream(new FileInputStream(targetFile))) {
                        int mapSize = dis.readInt();
                        for (int i = 0; i < mapSize; i++) {
                            String key = dis.readUTF();
                            String value = dis.readUTF();
                            entryMap.put(key, value);
                        }
                    } catch (EOFException e) {
                        logger.warn("Reached EOF while reading info file '{}'", file.getName());
                    } catch (UTFDataFormatException e) {
                        logger.error("Data format error in info file '{}'. The file may be an old text version. Please delete it.", file.getName(), e);
                    }
                }
            } catch (IOException e) {
                logger.error("An error occurred during the initial read process.", e);
            } finally {
                if (output != null && output.exists()) {
                    if (!output.delete()) {
                        forceDelete(output);
                    }
                }
            }
        }
    }

    private void forceDelete(File file) {
        logger.info("Attempting a forceful deletion of '{}'", file.getAbsolutePath());
        try {
            FileUtils.forceDelete(file);
        } catch (IOException e) {
            logger.error("Could not forcefully delete '{}'", file.getAbsolutePath(), e);
        }
    }

    /**
     * Checks if the file is configured to be encrypted.
     *
     * @return true if encryption is enabled, false otherwise.
     */
    public boolean isEncrypted() {
        return encrypt;
    }

    /**
     * Returns the file associated with this {@code InfoFile} instance.
     *
     * @return the {@link File} object.
     */
    public File getFile() {
        return file;
    }

    /**
     * Retrieves the string value associated with the specified key.
     *
     * @param key the key of the value to get.
     * @return the string value, or null if the key is not found.
     */
    public String get(String key) {
        return entryMap.get(key);
    }

    /**
     * Retrieves the boolean value associated with the specified key.
     *
     * @param key the key of the value to get.
     * @return the boolean value.
     */
    public Boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key));
    }

    /**
     * Retrieves the integer value associated with the specified key.
     *
     * @param key the key of the value to get.
     * @return the integer value.
     */
    public Integer getInteger(String key) {
        return Integer.parseInt(get(key));
    }

    /**
     * Retrieves the double value associated with the specified key.
     *
     * @param key the key of the value to get.
     * @return the double value.
     */
    public Double getDouble(String key) {
        return Double.parseDouble(get(key));
    }

    /**
     * Retrieves the long value associated with the specified key.
     *
     * @param key the key of the value to get.
     * @return the long value.
     */
    public Long getLong(String key) {
        return Long.parseLong(get(key));
    }

    public String getOrDefault(String key, String defaultValue) {
        return entryMap.getOrDefault(key, defaultValue);
    }

    public Boolean getBooleanOrDefault(String key, Boolean defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Boolean.parseBoolean(value);
    }

    public Integer getIntegerOrDefault(String key, Integer defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    public Double getDoubleOrDefault(String key, Double defaultValue) {
        String value = get(key);
        return value == null ? defaultValue : Double.parseDouble(value);
    }

    /**
     * Retrieves a list of strings associated with the specified key.
     * <p>
     * The values in the list are expected to be delimited by "!@!".
     *
     * @param key the key of the value to get.
     * @return a {@link List} of strings.
     */
    public List<String> getList(String key) {
        String value = entryMap.get(key);
        List<String> toReturn = new ArrayList<>();
        if (value == null || !value.contains("!@!")) {
            return toReturn;
        }
        toReturn.addAll(Arrays.asList(value.split("!@!")));
        return toReturn;
    }

    /**
     * Retrieves a linked list of strings associated with the specified key.
     * <p>
     * The values in the linked list are expected to be delimited by "!@!".
     *
     * @param key the key of the value to get.
     * @return a {@link LinkedList} of strings.
     */
    public LinkedList<String> getLinkedList(String key) {
        String value = entryMap.get(key);
        LinkedList<String> toReturn = new LinkedList<>();
        if (value == null || !value.contains("!@!")) {
            return toReturn;
        }
        toReturn.addAll(Arrays.asList(value.split("!@!")));
        return toReturn;
    }

    /**
     * Retrieves a map of strings associated with the specified key.
     * <p>
     * The map is expected to be stored using the "!&'!" and "@!@" delimiters.
     *
     * @param key the key of the map to get.
     * @return a {@link Map} of strings.
     */
    public Map<String, String> getStringMap(String key) {
        Map<String, String> toReturn = new HashMap<>();
        String value = entryMap.get(key);
        if (value == null || !value.contains("!&'!")) {
            return toReturn;
        }
        for (String s : value.split("!&'!")) {
            String[] split = s.split("@!@");
            if (split.length > 1) {
                toReturn.put(split[0], split[1]);
            }
        }
        return toReturn;
    }

    /**
     * Retrieves a treemap of strings associated with the specified key. The map is sorted by the string identifier.
     * <p>
     * The map is expected to be stored using the "!&'!" and "@!@" delimiters.
     *
     * @param key the key of the map to get.
     * @return a {@link Map} of strings.
     */
    public TreeMap<String, String> getSortedStringMap(String key) {
        TreeMap<String, String> toReturn = new TreeMap<>();
        String value = entryMap.get(key);
        if (value == null || !value.contains("!&'!")) {
            return toReturn;
        }
        for (String s : value.split("!&'!")) {
            String[] split = s.split("@!@");
            if (split.length > 1) {
                toReturn.put(split[0], split[1]);
            }
        }
        return toReturn;
    }

    /**
     * Checks if a key exists in the entry map.
     *
     * @param key the key to check for.
     * @return true if the key exists, false otherwise.
     */
    public boolean hasKey(String key) {
        return entryMap.containsKey(key);
    }

    /**
     * Sets a key-value pair.
     * <p>
     * Using binary serialization natively supports newline characters, meaning they no longer need to be delimited.
     *
     * @param key   the key to set.
     * @param value the string value to set.
     */
    public void set(String key, String value) {
        entryMap.put(key, value);
        update();
    }

    /**
     * Sets a key-value pair with a double value.
     *
     * @param key   the key to set.
     * @param value the double value to set.
     */
    public void set(String key, double value) {
        set(key, value + "");
    }

    /**
     * Sets a key-value pair with an integer value.
     *
     * @param key   the key to set.
     * @param value the integer value to set.
     */
    public void set(String key, int value) {
        set(key, value + "");
    }

    /**
     * Sets a key-value pair with a boolean value.
     *
     * @param key   the key to set.
     * @param value the boolean value to set.
     */
    public void set(String key, boolean value) {
        set(key, value + "");
    }

    /**
     * Sets a key-value pair with a long value.
     *
     * @param key   the key to set.
     * @param value the long value to set.
     */
    public void set(String key, long value) {
        set(key, value + "");
    }

    /**
     * Sets a key-value pair with a list of strings.
     * <p>
     * The list will be stored as a single string delimited by "!@!".
     *
     * @param key    the key to set.
     * @param values the list of strings to set.
     */
    public void set(String key, List<String> values) {
        StringBuilder appender = new StringBuilder();
        for (String s : values) {
            appender.append("!@!").append(s);
        }
        set(key, appender.toString());
    }

    /**
     * Sets a key-value pair with a map of strings.
     * <p>
     * The map will be stored as a single string using the "!&'!" and "@!@" delimiters.
     *
     * @param key the key to set.
     * @param map the map of strings to set.
     */
    public void set(String key, Map<String, String> map) {
        StringBuilder appender = new StringBuilder();
        map.forEach((s, s2) -> {
            appender.append("!&'!").append(s).append("@!@").append(s2);
        });
        set(key, appender.toString());
    }

    /**
     * Writes the current entry map to the associated file.
     * <p>
     * This method is called automatically after every {@link #set(String, String)} operation.
     * The file will be written using a binary stream and encrypted if encryption is enabled.
     *
     * @throws RuntimeException if an {@link IOException} occurs during file writing.
     */
    public void update() {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            logger.warn("Failed to write data! '{}' Does not exist!", file.getName());
        }

        File output = null;
        try {
            output = Files.createTempFile(null, ".info").toFile();
            File targetFile = (encrypt && !dev) ? output : file;

            try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(targetFile))) {
                dos.writeInt(entryMap.size());
                for (Map.Entry<String, String> entry : entryMap.entrySet()) {
                    dos.writeUTF(entry.getKey());

                    // Safeguard against null values breaking the binary stream
                    String value = entry.getValue() == null ? "" : entry.getValue();
                    dos.writeUTF(value);
                }
            }

            if (encrypt && !dev) {
                FileCrypter.encryptFile(output, file);
            }
        } catch (IOException e) {
            logger.error("An error occurred during the save process.", e);
        } finally {
            if (output != null && output.exists()) {
                if (!output.delete()) {
                    logger.error("Unable to delete temp file after writing. '{}'", output.getAbsolutePath());
                    forceDelete(output);
                }
            }
        }
    }

    public Map<String, String> getEntryMap() {
        return entryMap;
    }

    public void setEntryMap(Map<String, String> entryMap) {
        this.entryMap = entryMap;
    }

    public static InfoFile copy(InfoFile input) {
        InfoFile infoFile = new InfoFile();
        input.getEntryMap().forEach((s, s2) -> infoFile.getEntryMap().put(s, s2));
        infoFile.update();
        return infoFile;
    }
}