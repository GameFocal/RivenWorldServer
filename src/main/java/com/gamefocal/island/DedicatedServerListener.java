package com.gamefocal.island;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gamefocal.island.entites.net.HiveNetMessage;

public class DedicatedServerListener extends Listener {

    @Override
    public void connected(Connection connection) {
//        super.connected(connection);
        System.out.println("New Connection... awaiting for register");
        connection.sendTCP("Hello World");
    }

    @Override
    public void disconnected(Connection connection) {
//        super.disconnected(connection);
        System.out.println("Lost Connection");
    }

    @Override
    public void received(Connection connection, Object o) {

        if (HiveNetMessage.class.isAssignableFrom(o.getClass())) {
            // Is a HiveNetMessage
            System.out.println("INBOUND: " + ((HiveNetMessage) o).cmd + ", " + String.join(",", ((HiveNetMessage) o).args));
        } else {
            System.err.println("Invalid Traffic Inbount....");
        }

//        super.received(connection, o);
    }
}
