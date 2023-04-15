package com.gamefocal.rivenworld.game.entites.placable;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.TickEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.ui.claim.ClaimUI;
import com.gamefocal.rivenworld.game.util.ShapeUtil;
import com.gamefocal.rivenworld.models.GameChunkModel;
import com.gamefocal.rivenworld.models.GameLandClaimModel;
import com.gamefocal.rivenworld.serializer.InventoryDataType;
import com.gamefocal.rivenworld.service.ClaimService;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.KingService;
import com.j256.ormlite.field.DatabaseField;

import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class LandClaimEntity extends PlaceableEntity<LandClaimEntity> implements TickEntity {

    public Inventory fuelInventory = new Inventory(1);

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
    public BoundingBox getBoundingBox() {
        return ShapeUtil.makeBoundBox(this.location.toVector(), 25, 50);
    }

    public GameChunkModel getAttachedChunk() {
        try {
            return DataService.chunks.queryBuilder().where().eq("entityModel_uuid", this.uuid).queryForFirst();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

    public GameLandClaimModel getLandClaim() {
        GameChunkModel chunkModel = this.getAttachedChunk();
        if (chunkModel != null) {
            return chunkModel.claim;
        }

        return null;
    }

    @Override
    public void onTick() {
        if (this.fuelInventory != null) {
            // Check for fuel
            int i = 0;
            for (InventoryStack s : this.fuelInventory.getItems()) {
                if (s != null && s.getAmount() > 0) {
                    float f = this.consumeFuel(s);
                    if (f > 0) {
                        KingService.warChest.getInventory().add(s);
                        DedicatedServer.instance.getWorld().playSoundAtLocation(GameSounds.PLACE_CORE, this.location, 250, .45f, 1f);
                        this.fuelInventory.clear(i);

                        GameChunkModel c = this.getAttachedChunk();
                        if (c != null) {
                            c.claim.fuel += f;
                            if (c.claim.fuel > c.claim.maxFuel()) {
                                c.claim.fuel = c.claim.maxFuel();
                            }

                            try {
                                DataService.landClaims.update(c.claim);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                            this.fuelInventory.updateUIs();
                        }
                    }
                }
                i++;
            }
        }


        // Update UIs passively if open
//        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastUiUpdate) >= 15) {
//            this.lastUiUpdate = System.currentTimeMillis();
//            this.fuelInventory.updateUIs();
//        }
    }

    public float consumeFuel(InventoryStack stack) {
        float value = 1f;
        if (ClaimService.itemValue.containsKey(stack.getItem().getClass())) {
            value = ClaimService.itemValue.get(stack.getItem().getClass());
        }

        value = value * stack.getAmount();
        return value;
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
                    claimUI.open(connection, this);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
//            });

        }

    }
}
