package com.gamefocal.island.entites.events;


import com.gamefocal.island.DedicatedServer;

import java.util.LinkedList;

public abstract class Event<C> {

    protected boolean isCanceled = false;

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public void call() {
        EventManager eventService = DedicatedServer.get(EventManager.class);

        if (eventService != null) {

            if (!eventService.getHooks().containsKey(this.getClass())) {
                // If no hooks are set put them here and init the linkedlist
                eventService.getHooks().put(this.getClass(), new LinkedList<>());
            }

            if (eventService.getHooks().containsKey(this.getClass())) {

                LinkedList<EventHook> hooks = eventService.getHooks().get(this.getClass());
                hooks.sort((o1, o2) -> {
                    if (o1.getPriority().getPriority() > o2.getPriority().getPriority()) {
                        return -1;
                    } else if (o1.getPriority().getPriority() < o2.getPriority().getPriority()) {
                        return +1;
                    } else {
                        return 0;
                    }
                });

                for (EventHook hook : hooks) {
                    hook.run(this);
                    if (isCanceled) {
//                        isCanceled = false;
                        break;
                    }
                }
            } else {
                System.err.println("No Event Hooks for event " + this.getClass());
            }
        } else {
            System.err.println("Failed to call Event, EventService is not started... skipping.");
        }
    }

}
