package com.gamefocal.rivenworld.game.entites.placable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.ui.claim.ClaimUI;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.service.DataService;

import java.sql.SQLException;

public class LandClaimEntity extends PlaceableEntity<LandClaimEntity> {

    public LandClaimEntity() {
        this.type = "LandClaimPlaceable";
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onTick() {

    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Manage Claim";
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        super.onInteract(connection, action, inHand);

        if (action == InteractAction.USE) {
            // use the claim entity

            System.out.println("USE LAND CLAIM");

//            DataService.exec(() -> {
            try {
                GameChunkModel chunk = DataService.chunks.queryBuilder().where().eq("entityModel_uuid", this.getModel().uuid).queryForFirst();

                if (chunk != null) {
                    ClaimUI claimUI = new ClaimUI();
                    claimUI.open(connection, chunk.claim);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
//            });

        }

    }
}
