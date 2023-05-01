package com.gamefocal.rivenworld.game.util;

import com.badlogic.gdx.math.Vector3;

public class AIUtils {
    public static Vector3 getRunAwayLocation(Vector3 animalPosition, Vector3 playerPosition, float runAwayDistance) {
        // Calculate the direction vector from the player to the animal
        Vector3 direction = new Vector3(animalPosition).sub(playerPosition).nor();

        // Multiply the direction by the runAwayDistance to get the desired run away distance
        Vector3 runAwayVector = new Vector3(direction).scl(runAwayDistance);

        // Add the runAwayVector to the animal's current position to get the target location
        Vector3 targetLocation = new Vector3(animalPosition).add(runAwayVector);

        return targetLocation;
    }
}
