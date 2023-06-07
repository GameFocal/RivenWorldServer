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

@Command(name = "say", sources = "chat,cli")
public class SayCommand extends HiveCommand {

    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        boolean can = true;
        if (source == CommandSource.CHAT) {
            can = netConnection.isAdmin();
        }

        if (can) {
            System.out.println("Message sent to server.");
            DedicatedServer.sendChatMessageToAll(ChatColor.RED + "[SERVER]: " + message.argAsSingle());
        }
    }
}
