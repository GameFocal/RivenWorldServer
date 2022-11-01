package com.gamefocal.island.entites.command;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveCommandMessage;
import com.gamefocal.island.entites.net.HiveReplyMessage;
import com.gamefocal.island.entites.net.MessageHandler;
import com.gamefocal.island.entites.net.NetCommandSource;
import com.gamefocal.island.service.CommandService;

public class CommandHandler {

    private MessageHandler messageHandler;

    public CommandHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public void handle(Object obj, NetCommandSource source) {
        if (HiveCommandMessage.class.isAssignableFrom(obj.getClass())) {
            // Is a command packet

            HiveCommandMessage message = (HiveCommandMessage) obj;

            HiveCommand hc = DedicatedServer.get(CommandService.class).findNetCommand(message.cmd);
            if (hc != null && (hc.allowedSource == source || hc.allowedSource == NetCommandSource.ANY)) {
                message.commandSource = source;
                HiveReplyMessage hr = hc.onCommand((HiveCommandMessage) message);

                if (hr != null) {
                    this.messageHandler.routeOut(hr, message.channel);
                }
            } else {
                System.out.println("No command by name: " + message.cmd + " from source " + source.toString());
            }

        } else if (HiveReplyMessage.class.isAssignableFrom(obj.getClass())) {
            // Is a reply packet

            HiveReplyMessage message = (HiveReplyMessage) obj;

            CommandService commandService = DedicatedServer.get(CommandService.class);
            commandService.triggerReplyHooks(message);

        }
    }

}
