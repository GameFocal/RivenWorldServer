package com.gamefocal.island.commands.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.service.EnvironmentService;

@Command(name = "time", sources = "cli,chat")
public class SetTimeCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        String time = message.args[0];

        if (time.equalsIgnoreCase("dawn")) {
            DedicatedServer.get(EnvironmentService.class).setDayPercent(.25f);
        } else if (time.equalsIgnoreCase("day")) {
            DedicatedServer.get(EnvironmentService.class).setDayPercent(.5f);
        } else if (time.equalsIgnoreCase("dusk")) {
            DedicatedServer.get(EnvironmentService.class).setDayPercent(.75f);
        } else if (time.equalsIgnoreCase("night")) {
            DedicatedServer.get(EnvironmentService.class).setDayPercent(0f);
        }

        DedicatedServer.get(EnvironmentService.class).broadcastEnvChange(true);
    }
}
