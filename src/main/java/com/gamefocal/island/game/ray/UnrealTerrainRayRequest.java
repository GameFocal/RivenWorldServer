package com.gamefocal.island.game.ray;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.net.HiveNetMessage;
import com.gamefocal.island.game.tasks.HiveTask;
import com.gamefocal.island.game.util.Location;
import com.gamefocal.island.service.PlayerService;
import com.gamefocal.island.service.RayService;
import com.gamefocal.island.service.TaskService;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.UUID;

public class UnrealTerrainRayRequest {

    private String id;

    private Hashtable<UUID, Float> peers = new Hashtable<>();

    private boolean isValid = false;

    private boolean isComplete = false;

    private Location checkLocation;

    private float returnedHeight;

    private Long started = 0L;

    private RayRequestCallback callback;

    private HiveTask task;

    private int maxPeers = 1;

    public UnrealTerrainRayRequest(Location checkLocation, RayRequestCallback callback, int maxPeers) {
        this.id = DigestUtils.md5Hex(UUID.randomUUID().toString());
        this.checkLocation = checkLocation;
        this.callback = callback;
        this.maxPeers = maxPeers;
    }

    public HiveTask getTask() {
        return task;
    }

    public int getMaxPeers() {
        return maxPeers;
    }

    public void addPeer(HiveNetConnection connection) {
        this.peers.put(connection.getUuid(), -999f);
    }

    public boolean isPeer(HiveNetConnection connection) {
        return this.peers.containsKey(connection.getUuid());
    }

    public boolean peerIsOpen(HiveNetConnection connection) {
        return (this.peers.get(connection.getUuid()) == -999f);
    }

    public void recordPeer(HiveNetConnection peer, float height) {
        if (this.isPeer(peer)) {
            this.peers.put(peer.getUuid(), height);
        }
    }

    public float validateRequests() {

        HashMap<Float, Integer> votes = new HashMap<>();
        for (Float f : this.peers.values()) {
            if (votes.containsKey(f)) {
                votes.put(f, votes.get(f) + 1);
            } else {
                votes.put(f, 1);
            }
        }

        Integer voteCount = 0;
        Float highestVoted = -999f;
        Integer noReply = 0;
        for (Map.Entry<Float, Integer> v : votes.entrySet()) {
            if (v.getValue() == -999f) {
                noReply++;
                continue;
            }
            if (v.getValue() > voteCount) {
                voteCount = v.getValue();
                highestVoted = v.getKey();
            }
        }

        if (noReply >= (this.peers.size() * .5)) {
            return -999f;
        }

        this.returnedHeight = highestVoted;
        this.isValid = true;
        this.isComplete = true;

        return this.returnedHeight;
    }

    public String getId() {
        return id;
    }

    public Location getCheckLocation() {
        return checkLocation;
    }

    public float getReturnedHeight() {
        return returnedHeight;
    }

    public UnrealTerrainRayRequest processReturn(Location location) {
        this.returnedHeight = location.getZ();
        return this;
    }

    public Location getReturnedLocation() {
        return this.checkLocation.setZ(this.returnedHeight);
    }

    public Hashtable<UUID, Float> getPeers() {
        return peers;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public Long getStarted() {
        return started;
    }

    public RayRequestCallback getCallback() {
        return callback;
    }

    public void cancelTask() {
        this.task.cancel();
    }

    public void init() {
        HiveNetMessage msg = new HiveNetMessage();
        msg.cmd = "urr";
        msg.args = new String[]{
                this.id,
                this.checkLocation.toString()
        };

        for (UUID c : this.peers.keySet()) {
            DedicatedServer.get(PlayerService.class).players.get(c).sendTcp(msg.toString());
        }

        this.started = System.currentTimeMillis();
        this.task = TaskService.scheduleRepeatingTask(() -> {
            // Check to see if the requests is complete or if time has expired

            this.validateRequests();

            if (this.isValid && this.isComplete) {

                System.out.println("Ray Request Validated...");

                this.callback.run(this);
                this.cancelTask();

                // Remove from requests
                DedicatedServer.get(RayService.class).clearRequest(this.id);
            }

        }, 20L, 20L, false);
    }
}
