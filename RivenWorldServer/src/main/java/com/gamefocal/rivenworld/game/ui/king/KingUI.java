package com.gamefocal.rivenworld.game.ui.king;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.service.ClaimService;
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
        o.addProperty("taxAmt", DedicatedServer.get(ClaimService.class).upkeepCost() + " Upkeep");

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
//            if (KingService.isTheKing != null && KingService.isTheKing.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
//                String name = data[0];
//                float tax = Float.parseFloat(data[1]);
//                KingService.updateKingdom(name, tax);
//            }
        } else if (tag.equalsIgnoreCase("leave")) {
            if (KingService.isTheKing != null && KingService.isTheKing.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                KingService.releaseClaim();
                this.close(connection);
            }
        } else if (tag.equalsIgnoreCase("updateTax")) {
            /*
             * Update tax percent
             * */
            if (KingService.isTheKing != null && KingService.isTheKing.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {

                System.out.println(data[0]);

                float tax = Math.min(1f, Float.parseFloat(data[0]));

                KingService.updateKingdom(KingService.kingdomName, tax);
                this.update(connection);
            }
        } else if (tag.equalsIgnoreCase("updateName")) {
            if (KingService.isTheKing != null && KingService.isTheKing.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {
                KingService.updateKingdom(data[0], KingService.taxPer30Mins);
                this.update(connection);
            }
        }
    }
}
