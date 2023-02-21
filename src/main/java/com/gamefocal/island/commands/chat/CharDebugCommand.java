package com.gamefocal.island.commands.chat;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.service.CharacterCustomizationService;

@Command(name = "scc",sources = "chat")
public class CharDebugCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        DedicatedServer.get(CharacterCustomizationService.class).enterCharacterCreation(netConnection);
        System.out.println("Starting Char Customization for player");
    }
}
