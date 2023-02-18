package com.gamefocal.island.commands.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.service.EnvironmentService;

@Command(name = "daycycle", sources = "cli,chat")
public class SetDayCycleCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        String cycle = message.args[0];

        if (cycle.equalsIgnoreCase("reset")) {
            EnvironmentService.resetSecondsInDay();
        } else {
            EnvironmentService.setSecondsInDay(Float.parseFloat(cycle));
        }

        DedicatedServer.get(EnvironmentService.class).broadcastEnvChange(true);
    }
}
