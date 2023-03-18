package com.gamefocal.rivenworld.game.entites.placable;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.ui.claim.ClaimUI;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.service.DataService;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class LandClaimEntity extends PlaceableEntity<LandClaimEntity> implements TickEntity {

    private Long lastUiUpdate = 0L;

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
        try {
            if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastUiUpdate) >= 15) {
                this.lastUiUpdate = System.currentTimeMillis();
                GameChunkModel chunk = DataService.chunks.queryBuilder().where().eq("entityModel_uuid", this.getModel().uuid).queryForFirst();
                if (chunk != null) {
                    if (chunk.claim != null) {
                        chunk.claim.fuelInventory.updateUIs();
                    }
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
