package com.gamefocal.island.entites.net;

public @interface Command {

    public String name();

    public String sources() default "";

}

3
