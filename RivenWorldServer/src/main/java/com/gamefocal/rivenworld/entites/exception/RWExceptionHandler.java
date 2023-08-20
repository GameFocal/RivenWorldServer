package com.gamefocal.rivenworld.entites.exception;

public class RWExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println("Uncaught Exception in Thread: " + t.getName());
        e.printStackTrace();
//        System.err.println("Interrupting the Thread...");
//        t.interrupt();
    }
}
