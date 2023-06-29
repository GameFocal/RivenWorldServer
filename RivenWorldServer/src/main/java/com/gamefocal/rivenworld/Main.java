package com.gamefocal.rivenworld;

import io.airbrake.javabrake.Airbrake;
import io.airbrake.javabrake.Config;
import io.airbrake.javabrake.Notice;
import io.airbrake.javabrake.Notifier;

public class Main {

    public static void main(String[] args) {

        System.out.println("\n" +
                "    ____  _                _       __           __    __    \n" +
                "   / __ \\(_)   _____  ____| |     / /___  _____/ /___/ /____\n" +
                "  / /_/ / / | / / _ \\/ __ \\ | /| / / __ \\/ ___/ / __  / ___/\n" +
                " / _, _/ /| |/ /  __/ / / / |/ |/ / /_/ / /  / / /_/ (__  ) \n" +
                "/_/ |_/_/ |___/\\___/_/ /_/|__/|__/\\____/_/  /_/\\__,_/____/  \n" +
                "                                                            \n");
        System.out.println("RivenWorlds Dedicated Server - Version v" + DedicatedServer.serverVersion + DedicatedServer.serverMinorVersion);
        System.out.println("Developed by GameFocal, LLC (c) Copyright All Rights Reserved");

        Config config = new Config();
        config.projectId = 484168;
        config.projectKey = "61183875372e7a37b4fbb0820e90b503";
        Notifier notifier = new Notifier(config);

        notifier.addFilter(
                (Notice notice) -> {
                    notice.setContext("environment", "beta-test");
                    return notice;
                });

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Airbrake.report(e);
            }
        });

        DedicatedServer server = new DedicatedServer("config.json");

        DedicatedServer.awaitConsoleInput();

    }

}
