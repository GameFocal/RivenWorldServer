package com.gamefocal.island.entites.util;


import com.gamefocal.island.entites.util.cli.CliUtil;

public class PidUtil {

    public static boolean isProcessRunning(int pid) throws java.io.IOException {
        String r = CliUtil.exec("ps -p " + pid);
        return r.contains(String.valueOf(pid));
    }

}
