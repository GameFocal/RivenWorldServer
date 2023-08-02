package com.gamefocal.rivenworld.game.farming;

import java.util.concurrent.TimeUnit;

public enum CropType {

    // Tier 1 Food
    CARROT(TimeUnit.HOURS.toMinutes(2)),
    CUCUMBER(TimeUnit.HOURS.toMinutes(2)),
    ONION(TimeUnit.HOURS.toMinutes(2)),
    WHEAT(TimeUnit.HOURS.toMinutes(2)),


    // Tier 2 FoodB
    CORN(TimeUnit.HOURS.toMinutes(4)),
    CABBAGE(TimeUnit.HOURS.toMinutes(4)),
    POTATO(TimeUnit.HOURS.toMinutes(4)),
    TOMATO(TimeUnit.HOURS.toMinutes(4)),


    // Tier 3 Food
    PUMPKIN(TimeUnit.HOURS.toMinutes(8)),
    WATERMELON(TimeUnit.HOURS.toMinutes(8)),
    ;

    private long timeInMinutes;

    CropType(long timeInMinutes) {
        this.timeInMinutes = timeInMinutes;
    }
}
