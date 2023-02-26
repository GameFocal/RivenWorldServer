package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.EnvironmentService;

@Command(name = "daycycle", sources = "cli,chat")
public class SetDayCycleCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CONSOLE || (source == CommandSource.CHAT && netConnection.isAdmin())) {

            String cycle = message.args[0];

            if (cycle.equalsIgnoreCase("reset")) {
                EnvironmentService.resetSecondsInDay();
            } else {
                EnvironmentService.setSecondsInDay(Float.parseFloat(cycle));
            }

            DedicatedServer.get(EnvironmentService.class).broadcastEnvChange(true);
        }
    }
}
