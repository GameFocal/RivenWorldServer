package com.gamefocal.rivenworld.entites.events;

public interface EventHook<C> {
    void run(Event<?> event);
    EventPriority getPriority();
}
