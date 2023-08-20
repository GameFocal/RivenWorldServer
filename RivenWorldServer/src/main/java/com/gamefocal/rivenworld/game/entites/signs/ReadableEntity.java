package com.gamefocal.rivenworld.game.entites.signs;

import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.GameEntity;
import com.gamefocal.rivenworld.game.InteractableEntity;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.ui.sign.RivenSignUI;

import java.util.ArrayList;

public abstract class ReadableEntity<T> extends GameEntity<T> implements InteractableEntity {

    private ArrayList<String> lines = new ArrayList<>();

    public void addLine(String s) {
        this.lines.add(s);
    }

    public void clearLines() {
        this.lines.clear();
    }

    public void addLineAtIndex(int i, String s) {
        this.lines.add(i, s);
    }

    public void clearLineAtIndex(int i) {
        this.lines.remove(i);
    }

    public ArrayList<String> getLines() {
        return lines;
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        RivenSignUI ui = new RivenSignUI();
        ui.open(connection, this);
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Read";
    }
}
