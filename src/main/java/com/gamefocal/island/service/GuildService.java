package com.gamefocal.island.service;

import com.gamefocal.island.entites.net.HiveNetConnection;
import com.gamefocal.island.entites.service.HiveService;
import com.gamefocal.island.models.GameGuildModel;
import com.google.auto.service.AutoService;

import javax.inject.Singleton;
import java.sql.SQLException;

@Singleton
@AutoService(HiveService.class)
public class GuildService implements HiveService<GuildService> {
    @Override
    public void init() {

    }

    public GameGuildModel makeGuild(HiveNetConnection owner, String name) {
        GameGuildModel g = new GameGuildModel();
        g.color = "#000000";
        g.name = name;
        g.owner = owner.getPlayer();
        try {
            DataService.guilds.create(g);

            owner.getPlayer().guild = g;

            DataService.players.update(owner.getPlayer());

            return g;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }

}
