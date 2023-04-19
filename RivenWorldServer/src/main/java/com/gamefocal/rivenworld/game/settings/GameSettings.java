package com.gamefocal.rivenworld.game.settings;

// TODO: Save this to a JSON file, and then load it in on server start

import java.util.ArrayList;
import java.util.List;

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
    public float coinMultiple = 1f;
    public int shopStartingStock = 5;

    // Spawn
    public List<String> spawnPoints = new ArrayList<>(); // Spawn Points
    public boolean randomSpawn = true; // Randomly select the spawn points
    public boolean dropInventoryOnDeath = true; // Drop the inventory on death
    public boolean dropArmorOnDeath = true; // Drop equipment on death

    // Resources
    public float miningMultiple = 1f;
    public float woodCuttingMultiple = 1f;
    public float gatheringMultiple = 1f;

    // Guilds
    public int guildMemberLimit = 0; // 0 to disable

    // Decay
    public boolean enableDecay = true; // Enable the decay of the world
    public boolean requireClaimFuel = true; // Require claim fuel to block decay on claims, if false it will never decay a claim even without fuel.
    public float chunkDecayRate = 7200; // The minutes it takes to completely decay the chunk

    // King/ Taxes
    public float minTaxRate = 0;
    public float maxTaxRate = 60;
    public float taxPerXMinutes = 60;
    public boolean lockKingCastleChunks = true;

    // Resources
    public float respawnMultiple = 1;
    public float groundLayerRespawnTimeInMinutes = 60;

    // Day/Night
    public long minutesInDay = 2;
    public long minutesInNight = 1;

}
