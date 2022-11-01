package com.gamefocal.island.entites.events;

public interface EventHook<C> {
    void run(Event<?> event);
    EventPriority getPriority();
}
