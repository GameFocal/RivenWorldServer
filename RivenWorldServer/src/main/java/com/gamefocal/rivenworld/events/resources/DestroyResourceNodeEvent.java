package com.gamefocal.rivenworld.events.resources;

import com.gamefocal.rivenworld.entites.events.Event;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.resources.ResourceNodeEntity;
import com.gamefocal.rivenworld.models.GameResourceNode;

public class DestroyResourceNodeEvent extends Event<DestroyResourceNodeEvent> {

    private HiveNetConnection by;

    private ResourceNodeEntity nodeEntity;

    private GameResourceNode resourceNodeModel;

    public DestroyResourceNodeEvent(HiveNetConnection by, ResourceNodeEntity nodeEntity, GameResourceNode resourceNodeModel) {
        this.by = by;
        this.nodeEntity = nodeEntity;
        this.resourceNodeModel = resourceNodeModel;
    }

    public HiveNetConnection getBy() {
        return by;
    }

    public ResourceNodeEntity getNodeEntity() {
        return nodeEntity;
    }

    public GameResourceNode getResourceNodeModel() {
        return resourceNodeModel;
    }
}
