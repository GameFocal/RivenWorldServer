package com.gamefocal.rivenworld.events.resources;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.models.GameResourceNode;

public class RespawnResourceNodeEvent extends Event<RespawnResourceNodeEvent> {

    private ResourceNodeEntity nodeEntity;

    private GameResourceNode resourceNodeModel;

    public RespawnResourceNodeEvent(ResourceNodeEntity nodeEntity, GameResourceNode resourceNodeModel) {
        this.nodeEntity = nodeEntity;
        this.resourceNodeModel = resourceNodeModel;
    }


    public ResourceNodeEntity getNodeEntity() {
        return nodeEntity;
    }

    public GameResourceNode getResourceNodeModel() {
        return resourceNodeModel;
    }
}
