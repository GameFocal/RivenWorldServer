package com.gamefocal.rivenworld.entites.injection;

import com.gamefocal.rivenworld.DedicatedServer;
import com.google.inject.AbstractModule;

public class InjectionModule extends AbstractModule {

    private final DedicatedServer server;

    public InjectionModule(DedicatedServer server) {
        this.server = server;
    }

    @Override
    protected void configure() {
        bind(DedicatedServer.class).toInstance(this.server);
    }
}
