package com.gamefocal.rivenworld.commands.chat;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.CharacterCustomizationService;

@Command(name = "scc",sources = "chat")
public class CharDebugCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        DedicatedServer.get(CharacterCustomizationService.class).enterCharacterCreation(netConnection);
        System.out.println("Starting Char Customization for player");
    }
}
