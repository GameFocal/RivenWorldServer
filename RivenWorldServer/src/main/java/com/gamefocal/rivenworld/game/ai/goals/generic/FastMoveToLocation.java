package com.gamefocal.rivenworld.game.ai.goals.generic;

import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.generics.LivingEntity;
import com.gamefocal.rivenworld.game.util.Location;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public abstract class FastMoveToLocation extends MoveToLocationGoal {
    protected Location targetTrackLocation = null;
    protected Location actualLocation = null;
    protected int maxDistance = 20;
    protected boolean usePathfinder = false;
    protected long lastFastCalc = 0L;

    public FastMoveToLocation(Location location) {
        this.goal = location;
        this.targetTrackLocation = location;
    }

    public abstract void updateActualLocation(LivingEntity livingEntity);

    public int getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    @Override
    public void onStart(LivingEntity livingEntity) {
//        super.onStart(livingEntity);
    }

    @Override
    public boolean onGoalReached(LivingEntity livingEntity) {
        return false;
//        return super.onGoalReached(livingEntity);
    }

    @Override
    public void onTick(LivingEntity livingEntity) {

        this.updateActualLocation(livingEntity);

        Vector3 dir = actualLocation.toVector().cpy().sub(livingEntity.location.toVector()).nor();

        WorldCell currentCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(livingEntity.location);
        WorldCell goalCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(actualLocation);
//        WorldCell forwardCell = currentCell.getNeighborFromFwdVector(dir);

        boolean hasLineofSight = true;


        ArrayList<WorldCell> lineOfSight = currentCell.getCellsInLine(goalCell);
        for (WorldCell c : lineOfSight) {
            if (!c.canTravelFromCell(null, null)) {
                hasLineofSight = false;
                livingEntity.resetVelocity();
                livingEntity.lookAt = this.actualLocation.toVector();
                if (targetTrackLocation == null || !targetTrackLocation.toVector().epsilonEquals(actualLocation.toVector(), 100)) {
                    // Has moved...
                    this.reroutePath(livingEntity, goalCell.getCenterInGameSpace(true));
                    targetTrackLocation = actualLocation.cpy();
                }

                livingEntity.setLocationGoal(this.actualLocation.toVector());
            }
        }

        // Check to see if the player is at the end of the line of sight

        /*
         * Reroute using local if needed
         * */
        if (!hasLineofSight) {
            if (this.waypoints.size() == 0) {
                // Check to see if it has been more than 10 seconds.
                if (this.lastFastCalc == 0) {
                    this.lastFastCalc = System.currentTimeMillis();
                } else if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - this.lastFastCalc) >= 5) {
                    this.complete(livingEntity);
                    return;
                }
            }
            super.onTick(livingEntity);
        } else {
            this.waypoints.clear();
            /*
             * Quick vector
             * */
            targetTrackLocation = actualLocation.cpy();

            livingEntity.setVelocity(dir);
            livingEntity.setLocationGoal(this.targetTrackLocation.toVector());

            livingEntity.setVelocity(dir);
        }
    }
}
