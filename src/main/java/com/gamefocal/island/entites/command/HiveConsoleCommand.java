package com.gamefocal.island.entites.command;

public abstract class HiveConsoleCommand {

    public String name;

    public abstract boolean onCommand(String name, String[] args);

}
