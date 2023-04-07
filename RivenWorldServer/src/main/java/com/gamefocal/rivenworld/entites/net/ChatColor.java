package com.gamefocal.rivenworld.entites.net;

public enum ChatColor {
    RESET("&t"),
    BOLD("&b"),
    RED("&r"),
    GREEN("&g"),
    BLUE("&u"),
    ORANGE("&o"),
    H1("&h1"),
    ITALIC("&i"),
    SMALL("&p"),
    CURSIVE("&c")
    ;

    private String c;

    ChatColor(String c) {
        this.c = c;
    }

    public String getC() {
        return c;
    }

    @Override
    public String toString() {
        return this.c;
    }
}
