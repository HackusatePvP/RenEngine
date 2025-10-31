package me.piitex.os;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class ProcessUtil {
    private static final Logger logger = LoggerFactory.getLogger(ProcessUtil.class);

    private static final boolean VALIDATE_OS = OSUtil.getOS().toLowerCase().contains("window");

    static {
        if (!VALIDATE_OS) {
            logger.error("Unsupported operating system. ProcessUtil will only work with Windows devices.");
        }
    }

    public static boolean isProcessRunning(long pid) {
        if (!VALIDATE_OS) {
            return false;
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "tasklist",
                    "/FI",
                    "PID eq " + pid
            );
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains(" " + pid + " ")) { // Check for PID in the output
                    return true;
                }
            }
            return false;
        } catch (IOException e) {
            logger.error("Unable to check process condition!", e);
            return false;
        }
    }

    public static Optional<ProcessHandle> getRunningProcess(long pid) {
        return ProcessHandle.of(pid);
    }

    /**
     * Gracefully destroys a process, allowing it to clean up.
     */
    public static boolean terminateProcess(long pid) {
        if (!VALIDATE_OS) {
            return false;
        }

        Optional<ProcessHandle> processHandle = getRunningProcess(pid);
        return processHandle.map(ProcessHandle::destroy).orElse(false);
    }

    /**
     * Destroys a process without allowing it to gracefully shutdown.
     * <p>
     *     It is recommended to use first, {@link #terminateProcess(long)}
     * </p>
     */
    public static boolean killProcess(long pid) {
        if (!VALIDATE_OS) {
            return false;
        }

        Optional<ProcessHandle> processHandle = getRunningProcess(pid);
        return processHandle.map(ProcessHandle::destroyForcibly).orElse(false);
    }
}
