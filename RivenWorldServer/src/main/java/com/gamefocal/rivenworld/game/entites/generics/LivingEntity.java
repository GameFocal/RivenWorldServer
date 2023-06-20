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
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.NetworkUpdateFrequency;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.LocationUtil;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.gamefocal.rivenworld.game.util.WorldDirection;
import com.gamefocal.rivenworld.service.PlayerService;

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
    private boolean isBlocked = false;
    private Vector3 locationGoal = new Vector3().setZero();
    protected boolean useFineNavigation = true;

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

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
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
                if (currentCell != null) {
                    WorldCell goingToCell = currentCell.getNeighborFromFwdVector(this.velocity);

//            if (this.isAggro) {
//                for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
//                    connection.drawDebugBox(Color.GREEN, goingToCell.getCenterInGameSpace(true), new Location(50, 50, 50), 1);
//                }
//            }

                    if (goingToCell != null && (!goingToCell.isCanTraverse() || !currentCell.isCanTraverse())) {
                        // Can not go here, adjust the velocity based on romba logic
                        // TODO: Check neighbors (Left, Right, and Back) to see what is closer to the goal.

                        WorldCell newGoal = null;
                        float dist = Float.MAX_VALUE;
                        boolean canTraverse = false;

                        for (WorldCell n : currentCell.getNeighbors(false)) {
                            if (n.canTravelFromCell(currentCell, null)) {
                                float dist2 = Location.fromVector(this.locationGoal).dist(n.getCenterInGameSpace(true));
                                if (dist2 < dist) {
                                    newGoal = n;
                                    dist = dist2;
                                    canTraverse = true;
                                }
                            }
                        }

                        if (canTraverse) {
                            if (newGoal.equals(currentCell)) {
                                this.velocity.setZero();
                            } else {
                                // Has another option
                                Vector3 newVec = newGoal.getCenterInGameSpace(true).toVector();
                                newVec.sub(this.location.toVector()).nor();

                                this.velocity = newVec;
                            }
                        } else {
                            this.velocity.setZero();
                        }
                    }
                }
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

    public abstract boolean onHarvest(HiveNetConnection connection);

    public abstract boolean onHit(HiveNetConnection connection);
}
