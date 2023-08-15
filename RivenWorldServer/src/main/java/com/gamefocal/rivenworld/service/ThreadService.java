package com.gamefocal.rivenworld.service;

import com.gamefocal.rivenworld.entites.exception.RWThreadFactory;
import com.gamefocal.rivenworld.entites.service.HiveService;
import com.gamefocal.rivenworld.entites.thread.AsyncThread;
import com.gamefocal.rivenworld.entites.thread.HiveAsyncThread;
import com.gamefocal.rivenworld.entites.thread.ThreadExceptionHandler;
import com.google.auto.service.AutoService;
import com.google.inject.Singleton;
import org.reflections.Reflections;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoService(HiveService.class)
@Singleton
public class ThreadService implements HiveService {

    private ConcurrentHashMap<String, Thread> threads = new ConcurrentHashMap<>();

    private ExecutorService pool = Executors.newFixedThreadPool(10,new RWThreadFactory());

    public Future queueToPool(Runnable runnable) {
        return this.pool.submit(runnable);
    }

    public Thread runDedicatedThread(String name, Runnable runnable) {
        Thread t = new Thread(runnable);
        t.setName(name);
        t.setUncaughtExceptionHandler(new ThreadExceptionHandler(name, runnable)); // So we can catch any exception and then kill the thread to prevent run aways
        t.start();

        this.threads.put(t.getName(), t);
//        System.out.println("+Starting Thread with name " + name + " (" + this.threads.size() + " total)");

        return t;
    }

    public boolean isRunning(String name) {
        if (this.threads.containsKey(name)) {
            return this.threads.get(name).isAlive() && !this.threads.get(name).isInterrupted();
        }

        return false;
    }

    public boolean killThread(String name) {
        if (this.threads.containsKey(name)) {
            this.threads.get(name).interrupt();
            this.threads.remove(name);

            return true;
        }
        return false;
    }

    public int killThreads(String regex) {
        int c = 0;
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE);
        for (Map.Entry<String, Thread> e : this.threads.entrySet()) {
            if (e.getKey() != null && !e.getKey().isEmpty()) {
                Matcher matcher = pattern.matcher(e.getKey());
                if (matcher.find()) {
                    this.threads.get(e.getKey()).interrupt();
                    this.threads.remove(e.getKey());
                    c++;
                }
            }
        }
        return c;
    }

    @Override
    public void init() {
        Set<Class<? extends HiveAsyncThread>> threadRunnables = new Reflections("com.gamefocal").getSubTypesOf(HiveAsyncThread.class);
        for (Class<? extends HiveAsyncThread> c : threadRunnables) {
            if (HiveAsyncThread.class.isAssignableFrom(c)) {
//                System.out.println("\t\tAsync Thread Interface " + c.getSimpleName());
                if (c.isAnnotationPresent(AsyncThread.class)) {

                    AsyncThread at = (AsyncThread) c.getAnnotation(AsyncThread.class);
                    if (at != null) {

                        try {
                            HiveAsyncThread t = (HiveAsyncThread) c.newInstance();

//                            System.out.println("\t\t\tStarting Async Thread: " + at.name());

                            this.runDedicatedThread(at.name(), t);
                        } catch (InstantiationException | IllegalAccessException e) {
                            e.printStackTrace();
                        }

                    }

                }
            } else {
                System.err.println(c.getSimpleName() + " does not except HiveAsyncThread");
            }
        }
    }
}
