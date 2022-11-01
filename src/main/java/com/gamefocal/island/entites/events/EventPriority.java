package com.gamefocal.island.entites.events;

public enum EventPriority {
    FIRST(9999),
    HIGH(10),
    NORMAL(1),
    LOW(-10),
    LAST(-9999);

    private int priority = 1;

    EventPriority(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }
}
