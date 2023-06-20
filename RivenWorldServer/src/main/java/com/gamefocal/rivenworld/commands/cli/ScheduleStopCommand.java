package com.gamefocal.rivenworld.commands.cli;

import com.badlogic.gdx.graphics.Color;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.tasks.HiveTask;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.PlayerService;
import com.gamefocal.rivenworld.service.SaveService;
import com.gamefocal.rivenworld.service.TaskService;
import org.apache.commons.lang3.ArrayUtils;

import java.util.concurrent.TimeUnit;

@Command(name = "softstop", sources = "chat,cli")
public class ScheduleStopCommand extends HiveCommand {

    public static HiveTask stopTask = null;

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        boolean can = true;
        if (source == CommandSource.CHAT) {
            can = netConnection.isAdmin();
        }

        if (can && stopTask == null) {

            final long milliWait = TimeUnit.SECONDS.toMillis(Long.parseLong(message.args[0]));

            final long timeInSeconds = (System.currentTimeMillis() + milliWait);

            String msg = "Kicked from server";
            if (message.args.length > 1) {
                msg = String.join(" ", ArrayUtils.subarray(message.args, 1, message.args.length));
            }

            DedicatedServer.sendChatMessageToAll(ChatColor.RED + "Server Stopping by Admin Command...");

            String finalMsg1 = msg;
            stopTask = TaskService.scheduleRepeatingTask(() -> {
                if (System.currentTimeMillis() > timeInSeconds) {
                    SaveService.saveGame();

                    String finalMsg = finalMsg1;
                    DataService.exec(() -> {
                        DedicatedServer.kickAllPlayers(finalMsg);
                    });

                    DataService.exec(() -> {
                        System.exit(0);
                    });

                    stopTask.cancel();
                    return;
                }

                float diff = timeInSeconds - System.currentTimeMillis();
                float toWait = milliWait;
                float per = diff / toWait;

                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                    connection.setProgressBar("Server Shutting Down in " + Math.round(diff / 1000) + "s", per, Color.RED);
                }

            }, 20L, 20L, false);
        }
    }
}
