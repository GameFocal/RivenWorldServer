package com.gamefocal.rivenworld.game.ui.king;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.service.KingService;
import com.google.gson.JsonObject;

public class KingUI extends GameUI<Void> {
    @Override
    public String name() {
        return "king";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Void obj) {

        JsonObject o = new JsonObject();

        o.addProperty("name", KingService.kingdomName);
        o.addProperty("taxRate", KingService.taxPer30Mins);

        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, Void obj) {

    }

    @Override
    public void onClose(HiveNetConnection connection, Void obj) {

    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if (tag.equalsIgnoreCase("update")) {
            if (KingService.isTheKing != null && KingService.isTheKing.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                String name = data[0];
                float tax = Float.parseFloat(data[1]);
                KingService.updateKingdom(name, tax);
            }
        } else if (tag.equalsIgnoreCase("leave")) {
            if (KingService.isTheKing != null && KingService.isTheKing.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                KingService.releaseClaim();
            }
        }
    }
}
