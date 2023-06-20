package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.EnvironmentService;

@Command(name = "daycycle", sources = "cli,chat")
public class SetDayCycleCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (source == CommandSource.CONSOLE || (source == CommandSource.CHAT && netConnection.isAdmin())) {

//            if (message.args.length == 0) {
//                // Reset
//                DedicatedServer.get(EnvironmentService.class).getGameTimeObject().setDayDuration((int) DedicatedServer.settings.minutesInDay);
//                DedicatedServer.get(EnvironmentService.class).getGameTimeObject().setNightDuration((int) DedicatedServer.settings.minutesInDay);
//            } else if (message.args.length == 2) {
//                DedicatedServer.get(EnvironmentService.class).getGameTimeObject().setDayDuration(Integer.parseInt(message.args[0]));
//                DedicatedServer.get(EnvironmentService.class).getGameTimeObject().setNightDuration(Integer.parseInt(message.args[1]));
//            }

//            String cycle = message.args[0];
//
//            if (cycle.equalsIgnoreCase("reset")) {
//                EnvironmentService.resetSecondsInDay();
//            } else {
//                EnvironmentService.setSecondsInDay(Float.parseFloat(cycle));
//            }

            DedicatedServer.get(EnvironmentService.class).broadcastEnvChange(true);
        }
    }
}
