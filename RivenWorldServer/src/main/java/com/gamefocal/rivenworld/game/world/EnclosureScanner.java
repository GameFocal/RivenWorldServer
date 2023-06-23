package com.gamefocal.rivenworld.game.world;

import com.gamefocal.rivenworld.game.ai.path.WorldCell;

import java.util.Collection;
import java.util.LinkedList;

public class EnclosureScanner implements WorldScanner<WorldCell> {

    private LinkedList<WorldCell> closed = new LinkedList<>();

    private LinkedList<WorldCell> passed = new LinkedList<>();

    private LinkedList<WorldCell> edges = new LinkedList<>();

    @Override
    public boolean pass(WorldCell t) {

        if (t == null) {
            return false;
        }

        return t.isCanTraverse();
    }

    @Override
    public void scan(WorldCell start, Collection<WorldCell> collection) {
        this.scanTile(start);
        collection.add(start);
    }

    public LinkedList<WorldCell> getClosed() {
        return closed;
    }

    public LinkedList<WorldCell> getPassed() {
        return passed;
    }

    public LinkedList<WorldCell> getEdges() {
        return edges;
    }

    private void scanTile(WorldCell t) {
        if (this.closed.contains(t)) {
            return;
        }

        this.closed.add(t);
        if (this.pass(t)) {
            this.passed.add(t);
            // Check if this is an edge...
            if (!this.pass(t.getNorth()) || !this.pass(t.getEast()) || !this.pass(t.getSouth()) || !this.pass(t.getWest())) {
                // Something did not pass so this must be a edge.
                this.edges.add(t);
            }
        } else {
            return;
        }

        for (WorldCell tt : t.getNeighbors(false)) {
            this.scanTile(tt);
        }
    }

    public void clear() {
        this.closed.clear();
        this.passed.clear();
        this.edges.clear();
    }

}
