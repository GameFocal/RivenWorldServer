package com.gamefocal.rivenworld.commands.net.player.ui;

import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;

import java.util.Arrays;

@Command(name = "nuia", sources = "tcp")
public class NetUIInteraction extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        if (netConnection.getOpenUI() != null) {
            // Has a open UI
            GameUI opened = netConnection.getOpenUI();

            System.out.println(message.toString());

            String[] data = new String[0];
            if (message.args.length > 1) {
                data = new String[message.args.length - 1];
                data = Arrays.copyOfRange(message.args, 1, message.args.length);
            }

            opened.onAction(netConnection, InteractAction.USE, message.args[0], data);
        }
    }
}
