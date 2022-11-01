package com.gamefocal.island.entites.command;


import com.gamefocal.island.entites.net.NetCommandSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    String name();

    String aliases() default "";

    NetCommandSource netSource() default NetCommandSource.ANY;

}
