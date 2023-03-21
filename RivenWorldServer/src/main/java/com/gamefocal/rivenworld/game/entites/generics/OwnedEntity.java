package com.gamefocal.rivenworld.game.entites.generics;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;
import com.google.gson.JsonObject;

public interface OwnedEntity {

    void onReleaseOwnership();

    void onTakeOwnership(HiveNetConnection connection);

    boolean onPeerCmd(HiveNetConnection connection, String cmd, JsonObject data);

    boolean onPeerUpdate(HiveNetConnection connection, Location location, JsonObject data);

}
