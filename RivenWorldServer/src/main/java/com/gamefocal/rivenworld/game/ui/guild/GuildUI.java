package com.gamefocal.rivenworld.game.ui.guild;

import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.models.GameGuildMemberModel;
import com.gamefocal.rivenworld.models.GameGuildModel;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.gamefocal.rivenworld.service.DataService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.SQLException;

public class GuildUI extends GameUI<GameGuildModel> {
    @Override
    public String name() {
        return "guild";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, GameGuildModel obj) {

        JsonObject o = new JsonObject();

        boolean inGuild = (connection.getPlayer().guild != null);
        boolean hasInvite = false;

        try {
            PlayerModel pl = DataService.players.queryForId(connection.getPlayer().id);
            inGuild = (pl.guild != null);
            hasInvite = pl.invitedToJoinGuild != null;

            if (hasInvite) {
                o.addProperty("invite", pl.invitedToJoinGuild.name);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        o.addProperty("inGuild", inGuild);
        o.addProperty("hasInvite", hasInvite);

        if (inGuild) {
            try {
                GameGuildModel model = DataService.guilds.queryForId(String.valueOf(connection.getPlayer().guild.id));
                if (model != null) {
                    JsonObject g = new JsonObject();
                    g.addProperty("name", model.name);
                    g.addProperty("isOwner", model.owner.uuid.equals(connection.getPlayer().uuid));
                    g.addProperty("owner", model.owner.displayName);

                    JsonArray members = new JsonArray();
                    for (PlayerModel memberModel : model.members) {
                        if (!memberModel.uuid.equalsIgnoreCase(model.owner.uuid)) {
                            JsonObject m = new JsonObject();
                            m.addProperty("name", memberModel.displayName);
                            m.addProperty("id", memberModel.uuid);
                            members.add(m);
                        }
                    }

                    g.add("members", members);

                    o.add("guild", g);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, GameGuildModel object) {

    }

    @Override
    public void onClose(HiveNetConnection connection, GameGuildModel object) {

    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if (tag.equalsIgnoreCase("make-guild")) {

            String name = data[0];

            GameGuildModel model = new GameGuildModel();
            model.name = name;
            model.owner = connection.getPlayer();
            model.color = "none";

            try {
                DataService.guilds.createOrUpdate(model);

                connection.getPlayer().guild = model;
                DataService.players.createOrUpdate(connection.getPlayer());

//                this.update(connection);
                this.update(connection);

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

//            System.out.println(data[0]);
        } else if (tag.equalsIgnoreCase("disband")) {
            // Disband if you are the owner

            try {
                GameGuildModel model = DataService.guilds.queryForId(String.valueOf(connection.getPlayer().guild.id));

                if (model.owner.uuid.equalsIgnoreCase(connection.getPlayer().uuid)) {

                    for (PlayerModel playerModel : model.members) {
                        if (playerModel.isOnline()) {
                            playerModel.getActiveConnection().sendChatMessage(ChatColor.ORANGE + "The leader of " + model.name + " has disbanded the guild.");
                        }
                        playerModel.guild = null;
                        DataService.players.createOrUpdate(playerModel);
                    }

                    DataService.guilds.delete(model);

                    this.update(connection);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        } else if (tag.equalsIgnoreCase("kick")) {

            try {
                GameGuildModel model = DataService.guilds.queryForId(String.valueOf(connection.getPlayer().guild.id));

                if (model.owner.uuid.equalsIgnoreCase(connection.getPlayer().uuid) && !model.owner.uuid.equalsIgnoreCase(data[0])) {

                    PlayerModel otherGuildMember = DataService.players.queryForId(data[0]);
                    if (otherGuildMember != null) {
                        otherGuildMember.guild = null;
                        DataService.players.update(otherGuildMember);

                        if (otherGuildMember.isOnline()) {
                            otherGuildMember.getActiveConnection().sendChatMessage(ChatColor.ORANGE + "You have been kicked out of the " + model.name + " guild.");
                        }

                    }

                    this.update(connection);
                }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else if (tag.equalsIgnoreCase("accept")) {

            try {
                if (connection.getPlayer().invitedToJoinGuild != null) {
//                    GameGuildModel model = DataService.guilds.queryForId(String.valueOf(connection.getPlayer().guild.id));

                    connection.getPlayer().guild = connection.getPlayer().invitedToJoinGuild;
                    connection.getPlayer().invitedToJoinGuild = null;

                    DataService.players.createOrUpdate(connection.getPlayer());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } else if (tag.equalsIgnoreCase("decline")) {
            try {
                if (connection.getPlayer().invitedToJoinGuild != null) {
//                    GameGuildModel model = DataService.guilds.queryForId(String.valueOf(connection.getPlayer().guild.id));

                    connection.getPlayer().invitedToJoinGuild = null;

                    DataService.players.createOrUpdate(connection.getPlayer());
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
}
