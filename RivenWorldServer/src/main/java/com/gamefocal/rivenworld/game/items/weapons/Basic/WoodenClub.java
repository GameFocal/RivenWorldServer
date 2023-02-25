package com.gamefocal.rivenworld.game.items.weapons.Basic;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.interactable.Intractable;
import com.gamefocal.rivenworld.game.items.weapons.MeleeWeapon;

public class WoodenClub extends MeleeWeapon {
    @Override
    public String slug() {
        return "WoodenClub";
    }

    @Override
    public void onInteract(Intractable intractable, HiveNetConnection connection, InteractAction action) {

    }

    @Override
    public float hit() {
        return 0;
    }

    @Override
    public float block() {
        return 0;
    }
}
