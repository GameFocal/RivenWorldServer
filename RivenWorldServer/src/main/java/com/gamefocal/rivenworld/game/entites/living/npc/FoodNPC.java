package com.gamefocal.rivenworld.game.entites.living.npc;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.econ.Shop;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.entites.living.NPC;
import com.gamefocal.rivenworld.game.interactable.InteractAction;
import com.gamefocal.rivenworld.game.inventory.InventoryStack;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.FancyClothShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.cloth.SimpleClothShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.iron.MediumIronShirt;
import com.gamefocal.rivenworld.game.items.clothes.chest.iron.SimpleIronShirt;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.FancyLeatherShoes;
import com.gamefocal.rivenworld.game.items.clothes.feet.leather.SimpleLeatherBoots;
import com.gamefocal.rivenworld.game.items.clothes.head.ClothCap;
import com.gamefocal.rivenworld.game.items.clothes.legs.cloth.FancyClothLegs;
import com.gamefocal.rivenworld.game.items.clothes.legs.cloth.SimpleClothLegs;
import com.gamefocal.rivenworld.game.items.clothes.legs.leather.MediumLeatherLegs;
import com.gamefocal.rivenworld.game.shops.GameShop;
import com.gamefocal.rivenworld.game.ui.inventory.RivenShopUI;
import com.gamefocal.rivenworld.service.ShopService;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class FoodNPC extends NPC<FoodNPC> {

    public FoodNPC() {
        this.equipmentSlots.head = new InventoryStack(new ClothCap(), 1);
        this.equipmentSlots.chest = new InventoryStack(new SimpleIronShirt(), 1);
        this.equipmentSlots.legs = new InventoryStack(new SimpleClothLegs(), 1);
        this.equipmentSlots.feet = new InventoryStack(new SimpleLeatherBoots(), 1);
    }

    @Override
    public void onInteract(HiveNetConnection connection, InteractAction action, InventoryStack inHand) {
        Shop shop = DedicatedServer.get(ShopService.class).getShop(GameShop.FOOD_STORE);
        RivenShopUI shopUI = new RivenShopUI();
        shopUI.open(connection,shop);
    }

    @Override
    public boolean canInteract(HiveNetConnection netConnection) {
        return true;
    }

    @Override
    public String onFocus(HiveNetConnection connection) {
        return "[e] Trade";
    }

    @Override
    public void onSync() {
        if (!this.getMeta().containsKey("appr")) {
            this.setMeta("isFemale", false);
            this.setMeta("appr", "eyJNb3JwaFByb3BlcnRpZXMiOlt7IlByb3BlcnR5TmFtZSI6InNocF9mYWNlXzAxIiwiRmxvYXRWYWx1ZSI6MSwiVGV4dHVyZVZhbHVlIjoiTm9uZSIsIkNvbG9yVmFsdWUiOnsiUiI6MCwiRyI6MCwiQiI6MCwiQSI6MH0sIk1hdGVyaWFsSW5kZXgiOi0xfSx7IlByb3BlcnR5TmFtZSI6InNocF9lYXJzXzAzIiwiRmxvYXRWYWx1ZSI6MSwiVGV4dHVyZVZhbHVlIjoiTm9uZSIsIkNvbG9yVmFsdWUiOnsiUiI6MCwiRyI6MCwiQiI6MCwiQSI6MH0sIk1hdGVyaWFsSW5kZXgiOi0xfSx7IlByb3BlcnR5TmFtZSI6InNocF9ub3NlXzA2IiwiRmxvYXRWYWx1ZSI6MSwiVGV4dHVyZVZhbHVlIjoiTm9uZSIsIkNvbG9yVmFsdWUiOnsiUiI6MCwiRyI6MCwiQiI6MCwiQSI6MH0sIk1hdGVyaWFsSW5kZXgiOi0xfSx7IlByb3BlcnR5TmFtZSI6Im1vZF9ub3NlX3RpcCIsIkZsb2F0VmFsdWUiOjAuMTI3NzQzNjAxNzk5MDExMjMwNDY4NzUsIlRleHR1cmVWYWx1ZSI6Ik5vbmUiLCJDb2xvclZhbHVlIjp7IlIiOjAsIkciOjAsIkIiOjAsIkEiOjB9LCJNYXRlcmlhbEluZGV4IjotMX0seyJQcm9wZXJ0eU5hbWUiOiJzaHBfZXllc18wNCIsIkZsb2F0VmFsdWUiOjEsIlRleHR1cmVWYWx1ZSI6Ik5vbmUiLCJDb2xvclZhbHVlIjp7IlIiOjAsIkciOjAsIkIiOjAsIkEiOjB9LCJNYXRlcmlhbEluZGV4IjotMX0seyJQcm9wZXJ0eU5hbWUiOiJzaHBfYnJvd18wMyIsIkZsb2F0VmFsdWUiOjEsIlRleHR1cmVWYWx1ZSI6Ik5vbmUiLCJDb2xvclZhbHVlIjp7IlIiOjAsIkciOjAsIkIiOjAsIkEiOjB9LCJNYXRlcmlhbEluZGV4IjotMX0seyJQcm9wZXJ0eU5hbWUiOiJzaHBfbW91dGhfMDUiLCJGbG9hdFZhbHVlIjoxLCJUZXh0dXJlVmFsdWUiOiJOb25lIiwiQ29sb3JWYWx1ZSI6eyJSIjowLCJHIjowLCJCIjowLCJBIjowfSwiTWF0ZXJpYWxJbmRleCI6LTF9XSwiSGFpckNvbG9yIjp7IlIiOjAuMjYzOTYwNzc4NzEzMjI2MzE4MzU5Mzc1LCJHIjowLjE2MjgyOTExNTk4NjgyNDAzNTY0NDUzMTI1LCJCIjowLjA2ODk2ODI1MTM0NzU0MTgwOTA4MjAzMTI1LCJBIjoxfSwiSGFpck5hbWVzIjpbIkZyaW5nZV8wMSIsIkJlYXJkXzA0IiwiRXllYnJvd3NfMDEiXSwiQXBwYXJlbE5hbWVzIjpbIkNoZXN0MV9UMSIsIlBhbnRzX1QxIiwiQm9vdHNfVDEiLCJIZWFkX051bGwiXSwiQXBwYXJlbE1hdGVyaWFsT3B0aW9ucyI6WzAsMCwwLDBdLCJXaWR0aEFuZEhlaWdodCI6eyJYIjoxLjEwMDAwMDAyMzg0MTg1NzkxMDE1NjI1LCJZIjoxLjAyODM0OTU2MTI5Nzc4NzMxNzkyNjEyMjM0MTMwNTAxNzQ3MTMxMzQ3NjU2MjV9LCJUZXh0dXJlUHJvcGVydGllcyI6W3siUHJvcGVydHlOYW1lIjoiU2tpbiBUZXh0dXJlIiwiRmxvYXRWYWx1ZSI6MCwiVGV4dHVyZVZhbHVlIjoiXC9HYW1lXC9DaGFyYWN0ZXJDdXN0b21pemVyXC9DaGFyYWN0ZXJfQ3VzdG9taXplclwvQ2hhcmFjdGVyc1wvQ0NfSHVtYW5cL01hdGVyaWFsc1wvU2tpblwvVF9Ta2luXzAzLlRfU2tpbl8wMyIsIkNvbG9yVmFsdWUiOnsiUiI6MCwiRyI6MCwiQiI6MCwiQSI6MH0sIk1hdGVyaWFsSW5kZXgiOi0xfV0sIlZlY3RvclByb3BlcnRpZXMiOlt7IlByb3BlcnR5TmFtZSI6IlNraW4gQ29sb3IiLCJGbG9hdFZhbHVlIjowLCJUZXh0dXJlVmFsdWUiOiJOb25lIiwiQ29sb3JWYWx1ZSI6eyJSIjowLjQ0MjE2NDk4NzMyNTY2ODMzNDk2MDkzNzUsIkciOjAuMTc1NzcxOTk2Mzc4ODk4NjIwNjA1NDY4NzUsIkIiOjAuMTIzODY1MDAwOTAzNjA2NDE0Nzk0OTIxODc1LCJBIjoxfSwiTWF0ZXJpYWxJbmRleCI6LTF9LHsiUHJvcGVydHlOYW1lIjoiRXllIENvbG9yIiwiRmxvYXRWYWx1ZSI6MCwiVGV4dHVyZVZhbHVlIjoiTm9uZSIsIkNvbG9yVmFsdWUiOnsiUiI6MC4zNjQwMzIxMTk1MTI1NTc5ODMzOTg0Mzc1LCJHIjowLjE3NjQ0NTQ2OTI2MDIxNTc1OTI3NzM0Mzc1LCJCIjowLjE3NjQ0NTM3OTg1MzI0ODU5NjE5MTQwNjI1LCJBIjoxfSwiTWF0ZXJpYWxJbmRleCI6LTF9LHsiUHJvcGVydHlOYW1lIjoiTGlwcyBDb2xvciIsIkZsb2F0VmFsdWUiOjAsIlRleHR1cmVWYWx1ZSI6Ik5vbmUiLCJDb2xvclZhbHVlIjp7IlIiOjAuNDQyMTY0OTg3MzI1NjY4MzM0OTYwOTM3NSwiRyI6MC4xNzU3NzE5OTYzNzg4OTg2MjA2MDU0Njg3NSwiQiI6MC4xMjM4NjUwMDA5MDM2MDY0MTQ3OTQ5MjE4NzUsIkEiOjF9LCJNYXRlcmlhbEluZGV4IjowfV0sIlNjYWxhclByb3BlcnRpZXMiOlt7IlByb3BlcnR5TmFtZSI6Ik9sZCIsIkZsb2F0VmFsdWUiOjAuMjAwMDAwMDAyOTgwMjMyMjM4NzY5NTMxMjUsIlRleHR1cmVWYWx1ZSI6Ik5vbmUiLCJDb2xvclZhbHVlIjp7IlIiOjAsIkciOjAsIkIiOjAsIkEiOjB9LCJNYXRlcmlhbEluZGV4IjotMX0seyJQcm9wZXJ0eU5hbWUiOiJGcmVja2xlcyIsIkZsb2F0VmFsdWUiOjAuNTU5NDIxNzE4MTIwNTc0OTUxMTcxODc1LCJUZXh0dXJlVmFsdWUiOiJOb25lIiwiQ29sb3JWYWx1ZSI6eyJSIjowLCJHIjowLCJCIjowLCJBIjowfSwiTWF0ZXJpYWxJbmRleCI6LTF9LHsiUHJvcGVydHlOYW1lIjoicm91Z2huZXNzIHNjYWxlIiwiRmxvYXRWYWx1ZSI6MSwiVGV4dHVyZVZhbHVlIjoiTm9uZSIsIkNvbG9yVmFsdWUiOnsiUiI6MCwiRyI6MCwiQiI6MCwiQSI6MH0sIk1hdGVyaWFsSW5kZXgiOi0xfSx7IlByb3BlcnR5TmFtZSI6Ik9sZCIsIkZsb2F0VmFsdWUiOjEsIlRleHR1cmVWYWx1ZSI6Ik5vbmUiLCJDb2xvclZhbHVlIjp7IlIiOjAsIkciOjAsIkIiOjAsIkEiOjB9LCJNYXRlcmlhbEluZGV4IjowfSx7IlByb3BlcnR5TmFtZSI6Ik11c2N1bGFyIiwiRmxvYXRWYWx1ZSI6MCwiVGV4dHVyZVZhbHVlIjoiTm9uZSIsIkNvbG9yVmFsdWUiOnsiUiI6MCwiRyI6MCwiQiI6MCwiQSI6MH0sIk1hdGVyaWFsSW5kZXgiOi0xfV0sIkNsYXNzIjoiQmx1ZXByaW50R2VuZXJhdGVkQ2xhc3MnXC9HYW1lXC9DaGFyYWN0ZXJDdXN0b21pemVyXC9DaGFyYWN0ZXJfQ3VzdG9taXplclwvQ2hhcmFjdGVyc1wvQ0NfSHVtYW5cL0NDX01hbGVfQlAuQ0NfTWFsZV9CUF9DJyJ9");
            this.setMeta("eq", Base64.getEncoder().encodeToString(this.equipmentSlots.toJson().toString().getBytes(StandardCharsets.UTF_8)));
        }
    }
}
