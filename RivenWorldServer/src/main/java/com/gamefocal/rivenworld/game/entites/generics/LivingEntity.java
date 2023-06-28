package com.gamefocal.rivenworld.game.entites.generics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.ai.AiStateMachine;
import com.gamefocal.rivenworld.game.ai.goals.enums.AiBehavior;
import com.gamefocal.rivenworld.game.ai.machines.PassiveAiStateMachine;
import com.gamefocal.rivenworld.game.ai.path.AStarPathfinding;
import com.gamefocal.rivenworld.game.ai.path.WorldCell;
import com.gamefocal.rivenworld.game.entites.NetworkUpdateFrequency;
import com.gamefocal.rivenworld.game.sounds.GameSounds;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.game.util.VectorUtil;
import com.gamefocal.rivenworld.service.PlayerService;

import java.util.LinkedList;
import java.util.List;

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
    public Vector3 lookAt = null;
    public boolean useAutoRotate = true;
    public Location guardLocation = null;
    public int guardRadius = 1500;
    public GameSounds passiveSound = GameSounds.BEAR_PASSIVE;
    public GameSounds aggroSound = GameSounds.BEAR_AGGRO;
    protected long lastPassiveSound = 0L;
    protected boolean isAlive = true;
    protected boolean useFineNavigation = true;
    private float maxSpeed = 1;
    private Vector3 velocity = new Vector3(0, 0, 0);
    private boolean isBlocked = false;
    private Vector3 locationGoal = new Vector3().setZero();
    private LinkedList<WorldCell> visitedCells = new LinkedList<WorldCell>();
    private long deathAt = 0L;

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
//        if (this.locationGoal != null && !this.locationGoal.epsilonEquals(100, 100, 100)) {
//            // TODO: See if we can clear the stuff here.
//            this.visitedCells.clear();
//        }
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
        this.deathAt = System.currentTimeMillis();
        this.resetVelocity();
    }

    public long getDeathAt() {
        return deathAt;
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

    public boolean canAggroToPlayer(HiveNetConnection target) {
        return true;
    }

    @Override
    public void onSync() {
        super.onSync();

        if (!this.isAlive) {
            this.specialState = "dead";
        }

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
        }

        // Movement based on fwd velocity
        Vector3 newPosition = this.location.toVector();
        newPosition.mulAdd(this.velocity, (this.speed * 2));
        newPosition.z = DedicatedServer.instance.getWorld().getRawHeightmap().getHeightFromLocation(Location.fromVector(newPosition));

        double deg = 0;
        if (this.lookAt != null) {
            deg = VectorUtil.getDegrees(this.location.toVector(), newPosition);
        } else {
            deg = VectorUtil.getDegrees(this.location.toVector(), newPosition);
        }

        this.location = Location.fromVector(newPosition);

        if (this.useAutoRotate) {
            this.location.setRotation(0, 0, (float) deg);
        }

        // Update the chunks
        DedicatedServer.instance.getWorld().entityChunkUpdate(this.getModel());

        DedicatedServer.instance.getWorld().entityChunkUpdate(this.getModel());
        DedicatedServer.instance.getWorld().getCollisionManager().updateEntity(this, oldPosition);

    }

    public abstract boolean onHarvest(HiveNetConnection connection);

    public abstract boolean onHit(HiveNetConnection connection);

    public void SmartTraversal() {
        WorldCell currentCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(location);

        if (currentCell != null) {
            WorldCell goingToCell = currentCell.getNeighborFromFwdVector(this.velocity);
            WorldCell goalCell = DedicatedServer.instance.getWorld().getGrid().getCellFromGameLocation(Location.fromVector(locationGoal));

            // Can't Traverse
            if (goingToCell != null && (!goingToCell.isCanTraverse() || !currentCell.isCanTraverse()) && currentCell.getCenterInGameSpace(false).dist(goalCell.getCenterInGameSpace(false)) <= 5000) {
                WorldCell newGoal = null;
                WorldCell finalGoal = goalCell;

                /*
                 * Check if the player is within a enclosed area... if so render the walls for debug
                 * */
                if (AStarPathfinding.isAreaEnclosed(goalCell, 20)) {

                    System.out.println("ENCLOSED");

                    WorldCell close = AStarPathfinding.findClosestTraversableCell(goalCell, 20);
                    if (close != null) {
                        finalGoal = close;
                    }
                }

//                PriorityQueue<WorldCell> closeCellCanReach = AStarPathfinding.getPriorityQueue(location, Location.fromVector(locationGoal));
//                closeCellCanReach.addAll(currentCell.getNeighbors(false));
//
//                while (closeCellCanReach.size() > 0) {
//                    WorldCell n = closeCellCanReach.poll();
//                    if (n.canTravelFromCell(null, null) && !this.visitedCells.contains(n)) {
//                        // Can traverse
//                        this.visitedCells.add(n);
//                        newGoal = n;
//                        break;
//                    } else {
//                        for (HiveNetConnection con : DedicatedServer.get(PlayerService.class).players.values()) {
//                            con.drawDebugBox(Color.RED, n.getCenterInGameSpace(true).cpy(), new Location(50, 50, 50), 2);
//                        }
//                    }
//                }

                /*
                 * Do A* if no path is found
                 * */
                if (newGoal == null) {

                    /*
                     * Find closest cell to player
                     * */
                    // Do A* to get close

                    for (HiveNetConnection con : DedicatedServer.get(PlayerService.class).players.values()) {
                        con.drawDebugBox(Color.GREEN, finalGoal.getCenterInGameSpace(true), new Location(50, 50, 50), 2);
                    }

                    List<WorldCell> p = AStarPathfinding.findPath(currentCell, finalGoal, null, currentCell.getRadiusCells(20), 0);

                    for (HiveNetConnection con : DedicatedServer.get(PlayerService.class).players.values()) {
                        for (WorldCell pp : p) {
                            con.drawDebugBox(Color.BLUE, pp.getCenterInGameSpace(true).cpy(), new Location(50, 50, 50), 2);
                        }
                    }

                    if (p != null) {
                        newGoal = p.get(0);
                    }
                }

                if (newGoal != null) {
                    for (HiveNetConnection con : DedicatedServer.get(PlayerService.class).players.values()) {
                        con.drawDebugLine(Color.GREEN, location.cpy().addZ(200), newGoal.getCenterInGameSpace(true).cpy().setZ(location.cpy().addZ(200).getZ()), 1);
                        con.drawDebugBox(Color.YELLOW, newGoal.getCenterInGameSpace(true).cpy(), new Location(50, 50, 50), 2);
                    }

                    Vector3 newVec = newGoal.getCenterInGameSpace(true).toVector();
                    newVec.sub(location.toVector()).nor();
                    this.velocity = newVec;
                } else {
                    this.velocity.setZero();
                }
            }
        }

//                PriorityQueue<WorldCell> cells = AStarPathfinding.getClosestToGoal(location, Location.fromVector(locationGoal), currentCell.getNeighbors(false));
//                while (cells.size() > 0) {
//                    WorldCell c = cells.poll();
//                    if (c.canTravelFromCell(currentCell, null) && !goingToCell.equals(c) && !this.visitedCells.contains(c)) {
//                        this.visitedCells.add(c);
//                        canTraverse = true;
//                        newGoal = c;
//                        break;
//                    }
//                }
//
//                if (canTraverse) {
//                    if (newGoal.equals(currentCell)) {
//                        this.velocity.setZero();
//                    } else {
//                        // Has another option
//                        Vector3 newVec = newGoal.getCenterInGameSpace(true).toVector();
//                        newVec.sub(location.toVector()).nor();
//
//                        this.velocity = newVec;
//                    }
//                } else {
//                    this.visitedCells.clear();
//                    this.velocity.setZero();
//                }
    }
}
