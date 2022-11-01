package com.gamefocal.island;

public class Main {

    public static void main(String[] args) {

        System.out.println("\n" +
                "    ____  _                _       __           __    __    \n" +
                "   / __ \\(_)   _____  ____| |     / /___  _____/ /___/ /____\n" +
                "  / /_/ / / | / / _ \\/ __ \\ | /| / / __ \\/ ___/ / __  / ___/\n" +
                " / _, _/ /| |/ /  __/ / / / |/ |/ / /_/ / /  / / /_/ (__  ) \n" +
                "/_/ |_/_/ |___/\\___/_/ /_/|__/|__/\\____/_/  /_/\\__,_/____/  \n" +
                "                                                            \n");
        System.out.println("RivenWorlds Dedicated Server - Version v0.01");
        System.out.println("Developed by GameFocal, LLC (c) Copyright All Rights Reserved");
        System.out.println("Using this Dedicated Server you agree to the EULA found at https://rivenworlds.com/eula");

        DedicatedServer server = new DedicatedServer("config.json");

        DedicatedServer.awaitConsoleInput();

    }

}
