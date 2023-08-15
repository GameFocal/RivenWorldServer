package com.gamefocal.rivenworld.entites.exception;

import java.util.concurrent.ThreadFactory;

public class RWThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        final Thread thread = new Thread(r);
        thread.setUncaughtExceptionHandler(new RWExceptionHandler());
        return thread;
    }
}
