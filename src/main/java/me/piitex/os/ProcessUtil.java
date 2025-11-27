package me.piitex.os;

import me.piitex.os.exceptions.UnSupportedOSException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ProcessUtil {
    private static final Logger logger = LoggerFactory.getLogger(ProcessUtil.class);

    private static final boolean VALIDATE_OS = OSUtil.getOS().toLowerCase().contains("window") || OSUtil.getOS().toLowerCase().contains("linux");

    static {
        if (!VALIDATE_OS) {
            throw new UnSupportedOSException("This system is unsupported for ProcessUtil.");
        }
    }

    public static boolean isProcessRunning(long pid) {
        if (!VALIDATE_OS) {
            return false;
        }

        try {
            // Define the command based on the operating system
            List<String> command = new ArrayList<>();
            if (OSUtil.getOS().contains("Windows")) {
                // Command: tasklist /FI "PID eq [pid]"
                command = Arrays.asList(
                        "tasklist",
                        "/FI",
                        "PID eq " + pid
                );
            } else if (OSUtil.getOS().contains("Linux")) {
                command = Arrays.asList(
                        "ps",
                        "-p",
                        String.valueOf(pid)
                );
            }

            ProcessBuilder processBuilder = new ProcessBuilder(command);
            Process process = processBuilder.start();

            if (OSUtil.getOS().contains("Windows")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(" " + pid + " ") || line.startsWith(String.valueOf(pid))) {
                        return true;
                    }
                }
                return false;
            } else if (OSUtil.getOS().contains("Linux")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                int lineCount = 0;
                while (reader.readLine() != null) {
                    lineCount++;
                }
                return lineCount > 1;
            } else {
                return false;
            }
        } catch (IOException e) {
            logger.error("Unable to check process condition for PID {}", pid, e);
            Thread.currentThread().interrupt(); // Restore interrupted status
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

    public static boolean isValidOS() {
        return VALIDATE_OS;
    }
}
