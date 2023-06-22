package com.gamefocal.rivenworld.game.entites.generics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.goals.agro.AvoidPlayerGoal;
import com.gamefocal.rivenworld.game.ai.goals.agro.TargetPlayerGoal;
import com.gamefocal.rivenworld.game.ai.goals.enums.AiBehavior;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.ai.path.AStarPathfinding;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.NetworkUpdateFrequency;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.gamefocal.rivenworld.game.util.WorldDirection;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public abstract class LivingEntity<T> extends GameEntity<T> implements AiTick {

    public AiBehavior aiBehavior;
    public boolean isAggro = false;
    public boolean isMoving = false;
    public float maxHealth = 100f;
    public float health = 100f;
    public float energy = 100f;
    public float food = 100f;
    public float water = 100f;
    public float speed = 85;
    public float awareness = 400;
    public boolean isResting = false;
    public boolean isFeeding = false;
    public String specialState = "none";
    public transient AiStateMachine stateMachine;
    public transient HiveNetConnection owner;
    public transient boolean isReadyForAI = false;
    public transient float realVelocity = 0;
    public boolean canBeDamaged = true;
    public transient Long lastAttacked = 0L;
    public transient HiveNetConnection lastAttackedBy = null;
    public transient boolean attackResponse = false;
    public boolean canMove = true;
    protected long lastPassiveSound = 0L;
    protected boolean isAlive = true;
    private float maxSpeed = 1;
    private Vector3 velocity = new Vector3(0, 0, 0);
    private transient Vector3 locationGoal = new Vector3().setZero();
    private transient PriorityQueue<WorldCell> collisionAvoidancePath = new PriorityQueue<>();
    private transient boolean useCollisionAvoidance = false;

    protected boolean[] romba = new boolean[]{true, true, true, true};

    public LivingEntity(float maxHealth, AiStateMachine stateMachine) {
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.stateMachine = stateMachine;
        this.useWorldSyncThread = false;
        this.updateFrequency = NetworkUpdateFrequency.REALTIME;
    }

    public LivingEntity() {
        this.maxHealth = 100f;
        this.health = maxHealth;
        this.stateMachine = new PassiveAiStateMachine();
        this.useWorldSyncThread = false;
        this.updateFrequency = NetworkUpdateFrequency.REALTIME;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public Vector3 getLocationGoal() {
        return locationGoal;
    }

    public void setLocationGoal(Vector3 locationGoal) {
        this.locationGoal = locationGoal;
        this.collisionAvoidancePath = new PriorityQueue<>(new Comparator<WorldCell>() {
            @Override
            public int compare(WorldCell o1, WorldCell o2) {

                double g1 = o1.getCenterInGameSpace(true).dist(location);
                double h1 = o1.getCenterInGameSpace(true).dist(Location.fromVector(locationGoal));
                double f1 = g1 + h1;

                double g2 = o2.getCenterInGameSpace(true).dist(location);
                double h2 = o2.getCenterInGameSpace(true).dist(Location.fromVector(locationGoal));
                double f2 = g2 + h2;

                if (g1 > f2) {
                    return +1;
                } else if (f1 < f2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
    }

    public void resetVelocity() {
        this.velocity = new Vector3(0, 0, 0);
    }

    public void kill() {
        this.isResting = false;
        this.isFeeding = false;
        this.isMoving = false;
        this.isAlive = false;
        this.speed = 0;
        this.maxSpeed = 0;
        this.specialState = "dead";
        this.resetVelocity();
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(float energy) {
        this.energy = energy;
    }

    public float getFood() {
        return food;
    }

    public void setFood(float food) {
        this.food = food;
    }

    public float getWater() {
        return water;
    }

    public void setWater(float water) {
        this.water = water;
    }

    public AiStateMachine getStateMachine() {
        return stateMachine;
    }

    public void resetSpeed() {
        this.speed = this.maxSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void attackPlayer(HiveNetConnection connection) {

    }

    public boolean isAlive() {
        return isAlive;
    }

    public void heal() {
        this.health = this.maxHealth;
    }

    public void heal(float newHealth) {
        this.maxHealth = newHealth;
        this.health = this.maxHealth;
    }

    @Override
    public void onSpawn() {
    }

    @Override
    public void onDespawn() {

    }

    @Override
    public void onSync() {
        super.onSync();

        this.setMeta("state", this.specialState);

        // Sync speed and other data
        this.setMeta("resting", this.isResting);

        if (this.isMoving) {
            this.setMeta("speed", this.speed);
        } else {
            this.setMeta("speed", 0);
        }

        this.setMeta("feeding", isFeeding);
        this.setMeta("vel", realVelocity);
    }

    @Override
    public void onTick() {
        Vector3 oldPosition = this.location.toVector();

        if (this.stateMachine != null && this.isAlive && this.canMove) {
            this.stateMachine.tick(this);

            if (this.stateMachine.getCurrentGoal() != null && (AvoidPlayerGoal.class.isAssignableFrom(this.stateMachine.getCurrentGoal().getClass()) || TargetPlayerGoal.class.isAssignableFrom(this.stateMachine.getCurrentGoal().getClass()))) {
                // Check for a cell that can be traversed
                WorldCell currentCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(this.location);
                WorldCell goalCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromVector(this.locationGoal));
                WorldCell goingToCell = currentCell.getNeighborFromFwdVector(this.velocity);

                if (goingToCell != null && (!goingToCell.isCanTraverse() || !currentCell.isCanTraverse())) {

                    this.findLocalPath(this.locationGoal);

//                    this.collisionAvoidancePath.clear();
//                    this.collisionAvoidancePath.addAll(currentCell.getRadiusCells(40));
                }

//                if (this.collisionAvoidancePath.size() > 0) {
//                    boolean canTraverse = false;
//                    WorldCell newGoal = null;
//
////                    while (this.collisionAvoidancePath.size() > 0) {
//                    while (this.collisionAvoidancePath.size() > 0) {
//                        WorldCell c = this.collisionAvoidancePath.poll();
//                        if (c.canTravelFromCell(currentCell, null) && !c.equals(goalCell)) {
//                            newGoal = c;
//                            canTraverse = true;
//                            break;
//                        }
//                    }
////                    }
//
////                        if (path == null || path.size() == 0) {
////                            // No path found... cancel the path
////                            canTraverse = false;
////                        } else {
////                            newGoal = path.get(0);
////                            canTraverse = true;
////                        }
//
//                    if (canTraverse) {
//                        if (newGoal.equals(currentCell)) {
//                            this.velocity.setZero();
//                        } else {
//                            // Has another option
//                            Vector3 newVec = newGoal.getCenterInGameSpace(true).toVector();
//                            newVec.sub(this.location.toVector()).nor();
//
//                            this.velocity = newVec;
//                        }
//                    } else {
//                        this.velocity.setZero();
//                    }
//                }
            }
        }

        // Movement based on fwd velocity
        Vector3 newPosition = this.location.toVector();
        newPosition.mulAdd(this.velocity, (this.speed * 2));
        newPosition.z = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(Location.fromVector(newPosition));
        double deg = VectorUtil.getDegrees(this.location.toVector(), newPosition);

        this.location = Location.fromVector(newPosition);
        this.location.setRotation(0, 0, (float) deg);

        // Update the chunks
        DedicatedServer.instance.getWorld().entityChunkUpdate(this.getModel());

        DedicatedServer.instance.getWorld().entityChunkUpdate(this.getModel());
        DedicatedServer.instance.getWorld().getCollisionManager().updateEntity(this, oldPosition);

    }

    public void findLocalPath(Vector3 targetLocation) {
        // Create an instance of AStarPathfinding

        // Fetch the world from the server
        WorldCell currentCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(this.location);
        WorldCell targetCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromVector(targetLocation));

        // Use AStarPathfinding to find a path
        List<WorldCell> path = AStarPathfinding.findPathToClosestCell(currentCell, targetCell, null);

        if (path == null) {
            this.velocity.setZero();
        } else {
            if (!path.isEmpty()) {
                // We have a valid path
                WorldCell nextCell = path.get(0); // Next cell in the path

                // Calculate the direction vector from current location to the next cell
                Vector3 direction = nextCell.getCenterInGameSpace(true).toVector();
                direction.sub(this.location.toVector()).nor();

                // Adjust velocity based on the direction
                this.velocity = direction.scl(this.speed);
            } else {
                // If there is no valid path, stop the entity by setting velocity to zero
                this.velocity.setZero();
            }
        }
    }

    public abstract boolean onHarvest(HiveNetConnection connection);

    public abstract boolean onHit(HiveNetConnection connection);
}
