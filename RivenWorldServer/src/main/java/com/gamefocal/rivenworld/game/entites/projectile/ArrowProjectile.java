package com.gamefocal.rivenworld.game.entites.projectile;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.items.ammo.IronArrow;
import com.gamefocal.rivenworld.game.items.ammo.SteelArrow;
import com.gamefocal.rivenworld.game.items.ammo.StoneArrow;
import com.gamefocal.rivenworld.game.items.ammo.WoodenArrow;
import com.gamefocal.rivenworld.service.CombatService;

public class ArrowProjectile extends FlyingProjectile<ArrowProjectile> {

    public ArrowProjectile(HiveNetConnection firedBy, float speed) {
        super(firedBy, speed);
        this.type = "NetArrow";

        this.damage = 0;
        if (firedBy.selectedAmmo != null) {
            if (DedicatedServer.get(CombatService.class).getAmountCountOfType(firedBy, firedBy.selectedAmmo) > 0) {
                if (WoodenArrow.class.isAssignableFrom(firedBy.selectedAmmo)) {
                    this.damage = 10;
                } else if (StoneArrow.class.isAssignableFrom(firedBy.selectedAmmo)) {
                    this.damage = 15;
                } else if (IronArrow.class.isAssignableFrom(firedBy.selectedAmmo)) {
                    this.damage = 20;
                } else if (SteelArrow.class.isAssignableFrom(firedBy.selectedAmmo)) {
                    this.damage = 30;
                }
            }
        }

//        this.damage = 10;
        this.despawnOnHit = true;
    }
}
