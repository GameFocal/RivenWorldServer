package com.gamefocal.rivenworld.entites.injection;

public interface InjectionRoot {

    // Attempt to auto handle injection for plugins.
    public default void onLoad() {
        AppInjector.registerInjectionRoot(this);
    }

}
