package com.gamefocal.rivenworld.entites.thread;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.service.ThreadService;
import io.airbrake.javabrake.Airbrake;

public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {

    private String name;
    private Runnable asyncThread;

    public ThreadExceptionHandler(String name, Runnable asyncThread) {
        this.name = name;
        this.asyncThread = asyncThread;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        Airbrake.report(e);
        System.err.println("Uncaught Exception in HiveAsyncThread (" + t.getName() + ")");
        e.printStackTrace();

        t.interrupt();

        DedicatedServer.get(ThreadService.class).runDedicatedThread(this.name, this.asyncThread);

//        System.err.println("Uncaught Exception in Thread: " + t.getName());
//        e.printStackTrace();
//        System.err.println("Interrupting the Thread...");
//        t.interrupt();
    }
}
