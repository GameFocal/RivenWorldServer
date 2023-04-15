package com.gamefocal.rivenworld.game.entites.stations;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.generics.PassiveResourceStation;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.resources.water.CleanWaterBucket;

import java.util.concurrent.TimeUnit;

public class PassiveWaterWellStation extends PassiveResourceStation<PassiveWaterWellStation> {
    public PassiveWaterWellStation() {
        this.amt = 0;
        this.producesName = "Clean Water";
        this.producesDesc = "This well will generate clean water for drinking passively";

        this.amtPerTimeSpan = 1;
        this.timeSpanInSeconds = TimeUnit.MINUTES.toSeconds(5);
    }

    @Override
    public void onGather(HiveNetConnection connection, long amt) {
        if (amt <= this.amt) {
            amt = this.amt;
        }

        InventoryStack stack = new InventoryStack(new CleanWaterBucket(), (int) amt);

        if (connection.getPlayer().inventory.canAdd(stack)) {
            connection.getPlayer().inventory.add(stack);
            this.amt -= amt;
        }
    }
}
