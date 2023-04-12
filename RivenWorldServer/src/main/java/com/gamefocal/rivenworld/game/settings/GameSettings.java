package com.gamefocal.rivenworld.game.settings;

// TODO: Save this to a JSON file, and then load it in on server start

public class GameSettings {

    // PvP
    public String pvpMode = "normal"; // normal = all day, night = weak during the day, night-only = only at night, off = disabled
    public float damageMultiple = 1f;
    public boolean protectInClaim = false;

    // Raiding
    public String raidMode = "normal"; // normal = raiding only when online, night = only when online and at night, opengame = anytime, off = disabled
    public float raidLogOffCoolDown = 60; // Seconds for the cooldown that people are still allowed to raid after logging off

    // Econ
    public boolean enableEconomy = true; // Enable econ NPCs
    public boolean lockNPCTowns = true; // Prevent NPC towns from being claimed
    public float coinMultipule = 1f;

    // Spawn
    public String[] spawnPoints = new String[0]; // Spawn Points
    public boolean randomSpawn = true; // Randomly select the spawn points
    public boolean dropInventoryOnDeath = true; // Drop the inventory on death
    public boolean dropArmorOnDeath = true; // Drop equipment on death

    // Resources
    public float miningMultiple = 1f;
    public float woodCuttingMultiple = 1f;
    public float gatheringMultiple = 1f;

    // Guilds
    public int guildMemberLimit = 0; // 0 to disable

    // King/ Taxes
    public float minTaxRate = 0;
    public float maxTaxRate = 60;
    public float taxPerXMinutes = 30;

}
