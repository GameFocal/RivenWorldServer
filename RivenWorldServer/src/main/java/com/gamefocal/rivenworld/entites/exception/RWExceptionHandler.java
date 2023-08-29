package com.gamefocal.rivenworld.entites.exception;

import io.airbrake.javabrake.Airbrake;

public class RWExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.err.println("Uncaught Exception in Thread: " + t.getName());
        Airbrake.report(e);
        e.printStackTrace();
//        System.err.println("Interrupting the Thread...");
//        t.interrupt();
    }
}
