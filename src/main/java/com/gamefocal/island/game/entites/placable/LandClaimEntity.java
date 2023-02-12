package com.gamefocal.island.game.entites.placable;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.game.interactable.InteractAction;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.ui.claim.ClaimUI;
import com.gamefocal.island.models.GameChunkModel;
import com.gamefocal.island.service.DataService;

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

            try {
                GameChunkModel chunk = DataService.chunks.queryBuilder().where().eq("entityModel", this.getModel()).queryForFirst();

                ClaimUI claimUI = new ClaimUI();
                claimUI.open(connection, chunk.claim);

                System.out.println("OPEN");

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
