package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.EnvironmentService;

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
        } else if (time.equalsIgnoreCase("stop")) {
            EnvironmentService.setFreezeTime(true);
        } else if (time.equalsIgnoreCase("start")) {
            EnvironmentService.setFreezeTime(false);
        }

        DedicatedServer.get(EnvironmentService.class).broadcastEnvChange(true);
    }
}
