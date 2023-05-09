package com.gamefocal.rivenworld.game.ai.path;

import java.util.List;

public interface AiPathResult {
    void onPath(List<WorldCell> path);
}
