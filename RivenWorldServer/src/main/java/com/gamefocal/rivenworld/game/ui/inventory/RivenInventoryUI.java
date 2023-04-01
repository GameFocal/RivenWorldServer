package com.gamefocal.rivenworld.game.ui.inventory;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.ChatColor;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.Inventory;
import com.gamefocal.rivenworld.game.inventory.crafting.CraftingQueue;
import com.gamefocal.rivenworld.game.inventory.enums.EquipmentSlot;
import com.gamefocal.rivenworld.game.recipes.placables.CampFirePlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.placables.WorkBenchPlaceableRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.StoneHatchetRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.TorchRecipe;
import com.gamefocal.rivenworld.game.recipes.weapons.WoodenClubRecipe;
import com.gamefocal.rivenworld.game.ui.CraftingUI;
import com.gamefocal.rivenworld.game.ui.GameUI;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameGuildModel;
import com.gamefocal.rivenworld.models.PlayerModel;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.InventoryService;
import com.gamefocal.rivenworld.service.KingService;
import com.gamefocal.rivenworld.service.SkillService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.SQLException;

public class RivenInventoryUI extends GameUI<Inventory> implements CraftingUI {

    public RivenInventoryUI() {
        this.focus = false;
    }

    @Override
    public String name() {
        return "riven-inv";
    }

    @Override
    public JsonObject data(HiveNetConnection connection, Inventory obj) {
        connection.updatePlayerInventory();
        connection.syncEquipmentSlots();

        JsonObject o = new JsonObject();

        obj.getCraftingQueue().addAllowedRecipes(
                new WoodenClubRecipe(),
                new StoneHatchetRecipe(),
                new WorkBenchPlaceableRecipe(),
                new CampFirePlaceableRecipe(),
                new TorchRecipe()
        );

        /*
         * Load personal crafting recipes
         * */
        if (obj.getCraftingQueue() != null) {
            // Has a crafting queue
            o.add("crafting", obj.getCraftingQueue().toJson(obj));
        } else {
            System.err.println("No Crafting Queue");
        }

        /*
         * Skills
         * */
        JsonArray skills = DedicatedServer.get(SkillService.class).getPlayerSkills(connection);
        o.add("skills", skills);

        /*
         * Guild
         * */
        JsonObject guildData = new JsonObject();
        boolean inGuild = (connection.getPlayer().guild != null);
        boolean hasInvite = false;

        try {
            PlayerModel pl = DataService.players.queryForId(connection.getPlayer().id);
            inGuild = (pl.guild != null);
            hasInvite = pl.invitedToJoinGuild != null;

            if (hasInvite) {
                guildData.addProperty("invite", pl.invitedToJoinGuild.name);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        guildData.addProperty("inGuild", inGuild);
        guildData.addProperty("hasInvite", hasInvite);

        if (inGuild) {
            try {
                GameGuildModel model = DataService.guilds.queryForId(String.valueOf(connection.getPlayer().guild.id));
                if (model != null) {
                    guildData.addProperty("name", model.name);
                    guildData.addProperty("isOwner", model.owner.uuid.equals(connection.getPlayer().uuid));
                    guildData.addProperty("owner", model.owner.displayName);

                    JsonArray members = new JsonArray();
                    for (PlayerModel memberModel : model.members) {
                        if (!memberModel.uuid.equalsIgnoreCase(model.owner.uuid)) {
                            JsonObject m = new JsonObject();
                            m.addProperty("name", memberModel.displayName);
                            m.addProperty("id", memberModel.id);
                            members.add(m);
                        }
                    }

                    guildData.add("members", members);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        o.add("guild", guildData);

        /*
         * Kingdom
         * */
        JsonObject kingdom = new JsonObject();
        kingdom.addProperty("hasKing", (KingService.isTheKing != null));
        kingdom.addProperty("tax", String.valueOf(KingService.taxPer30Mins));

        if (KingService.isTheKing != null) {
            kingdom.addProperty("name", KingService.kingdomName);
            kingdom.addProperty("king", KingService.isTheKing.displayName);
            kingdom.addProperty("guild", (KingService.isTheKing.guild != null) ? KingService.isTheKing.guild.name : "Lone King");
        }

        o.add("king", kingdom);

        System.out.println(o);

        return o;
    }

    @Override
    public void onOpen(HiveNetConnection connection, Inventory object) {
        DedicatedServer.get(InventoryService.class).trackInventory(connection.getPlayer().inventory);
        object.attachToUI(this);
    }

    @Override
    public void onClose(HiveNetConnection connection, Inventory object) {
        object.detachFromUI(this);
    }

    @Override
    public void onAction(HiveNetConnection connection, InteractAction action, String tag, String[] data) {
        if (tag.equalsIgnoreCase("close")) {
            this.close(connection);
        } else if (tag.equalsIgnoreCase("mv")) {
            String fromSlot = data[0];
            String toSlot = data[1];
            this.getAttached().transferToSlot(Integer.parseInt(fromSlot), Integer.parseInt(toSlot));
            this.update(connection);
            connection.updatePlayerInventory();
        } else if (tag.equalsIgnoreCase("drop")) {
            String fromSlot = data[0];
            this.getAttached().dropCompleteSlot(connection, Integer.parseInt(fromSlot));
            this.update(connection);
            connection.updatePlayerInventory();
        } else if (tag.equalsIgnoreCase("split")) {
            String slot = data[0];
            String toSlot = data[1];
            this.getAttached().split(Integer.parseInt(data[0]), Integer.parseInt(toSlot));
            this.update(connection);
            connection.updatePlayerInventory();
        } else if (tag.equalsIgnoreCase("eq")) {

            if (this.getAttached().equipFromSlot(connection, Integer.parseInt(data[0]), EquipmentSlot.valueOf(data[1]))) {
                this.update(connection);
            }

        } else if (tag.equalsIgnoreCase("ueq")) {
            System.out.println("UNEQ");
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
        } else if (tag.equalsIgnoreCase("leave")) {
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
                } else {
                    // Leave the guild
                    connection.getPlayer().guild = null;
                    DataService.players.createOrUpdate(connection.getPlayer());
                    connection.sendChatMessage(ChatColor.GREEN + "You've left the " + model.name + " Guild.");
                }

                this.update(connection);

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

                    } else {
                        System.out.println("Player not found");
                    }

                    this.update(connection);
                } else {
                    System.out.println("FAIL-1");
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

                    this.update(connection);

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

                    this.update(connection);

                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public CraftingQueue queue() {
        return this.getAttached().getCraftingQueue();
    }

    @Override
    public Inventory getSource() {
        return this.getAttached();
    }

    @Override
    public Inventory getDest() {
        return this.getAttached();
    }

    @Override
    public Location getLocation() {
        return this.getOwner().getPlayer().location;
    }
}
