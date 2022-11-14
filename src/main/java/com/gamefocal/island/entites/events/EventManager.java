package com.gamefocal.island.entites.events;

import com.gamefocal.island.DedicatedServer;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class EventManager {

    private ConcurrentHashMap<Class, LinkedList<EventHook>> hooks = new ConcurrentHashMap<>();

    public EventManager() {
        this.loadEvents();
    }

    public void register(Event event) {
        System.out.println("\tRegistered Event: " + event.getClass().getSimpleName());
        this.hooks.put(event.getClass(), new LinkedList<>());
    }

    public void loadEvents() {
//        System.out.println("\t Scanning for Event Hooks...");
        try {
            Set<Class<? extends EventInterface>> eventHandlers = new Reflections("com.gamefocal").getSubTypesOf(EventInterface.class);
            for (Class<? extends EventInterface> c : eventHandlers) {
//                System.out.println("\t\tInterface " + c.getSimpleName());
                for (Method m : c.getMethods()) {
                    m.setAccessible(true);
                    if (m.getAnnotation(EventHandler.class) != null) {

                        EventHandler annon = m.getAnnotation(EventHandler.class);

                        // Has a event handler
                        for (Parameter p : m.getParameters()) {
                            Class hookTo = p.getType();

//                            System.out.println("\t\t\tFound Hook " + m.getName() + " to " + hookTo.getSimpleName() + " event.");

                            // Setup the hooks if not already
                            if (!this.hooks.containsKey(hookTo)) {
                                this.hooks.put(hookTo, new LinkedList<>());
                            }

                            if (this.hooks.containsKey(hookTo)) {
                                EventHook hook = new EventHook() {
                                    @Override
                                    public EventPriority getPriority() {
                                        return annon.priority();
                                    }

                                    @Override
                                    public void run(Event event) {
                                        EventInterface eventInterface = DedicatedServer.get(c);

                                        m.setAccessible(true);
                                        try {
                                            m.invoke(eventInterface, event);
                                        } catch (IllegalAccessException | InvocationTargetException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                };

                                // Hook to the event
                                this.hooks.get(hookTo).add(hook);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
//            System.err.println("Failed to hook to events, possibly no hooks setup... skipping.");
        }
    }

    public ConcurrentHashMap<Class, LinkedList<EventHook>> getHooks() {
        return hooks;
    }
}
