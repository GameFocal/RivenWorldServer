package com.gamefocal.rivenworld.game.world;

import com.gamefocal.rivenworld.game.ai.path.WorldCell;

import java.util.Collection;

public interface WorldScanner<T> {

    boolean pass(WorldCell t);

    void scan(WorldCell start, Collection<T> collection);

}
