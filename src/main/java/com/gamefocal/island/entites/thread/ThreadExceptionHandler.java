package com.gamefocal.island.entites.thread;

public class ThreadExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        System.err.println("Uncaught Exception in HiveAsyncThread (" + t.getName() + ")");
        e.printStackTrace();
        System.err.println("Killing Thread and restarting...");

//        System.err.println("Uncaught Exception in Thread: " + t.getName());
//        e.printStackTrace();
//        System.err.println("Interrupting the Thread...");
//        t.interrupt();
    }
}
