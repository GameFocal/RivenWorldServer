package com.gamefocal.island.entites.util.cli;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class CliUtil {

    /**
     * Run a linux command
     *
     * @param cmd The command to run
     * @return The response given
     * @throws IOException thrown when file/folder doesn't exist
     */
    public static String exec(String cmd, String inDir, boolean collectOutput) throws IOException {
        StringBuilder response = new StringBuilder();

        ProcessBuilder pb = new ProcessBuilder().command("/bin/sh", "-c", cmd);
        pb.directory(new File(inDir));
        Process p = pb.start();

        if (collectOutput) {
            try (InputStreamReader in = new InputStreamReader(p.getInputStream())) {
                char[] buffer = new char[8192];
                int count;
                while ((count = in.read(buffer)) != -1) {
                    response.append(buffer, 0, count);
                }
            }


            return response.toString();
        }

        return "";
    }

    public static String exec(String cmd) throws IOException {
        return exec(cmd, "/usr/local/gamefocal", true);
    }

    public static String exec(String cmd, String inDir) throws IOException {
        return exec(cmd, inDir, true);
    }

}
