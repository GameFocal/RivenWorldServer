package com.gamefocal.rivenworld.commands.net.vote;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.*;
import com.gamefocal.rivenworld.service.PeerVoteService;
import org.apache.commons.lang3.ArrayUtils;

import java.util.UUID;

@Command(sources = "tcp", name = "pvor")
public class NetVoteReply extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {
        DedicatedServer.get(PeerVoteService.class).processReply(
                netConnection,
                UUID.fromString(message.args[0]),
                String.join("|", ArrayUtils.subarray(message.args, 1, message.args.length))
        );
    }
}
