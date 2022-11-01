package com.gamefocal.island.entites.net;

import java.util.Map;

public class HiveCommandMessage extends HiveMessage {
    public Map<String, Object> params;
    public NetCommandSource commandSource = NetCommandSource.UNKNOWN;
}
