package com.gamefocal.island.commands.net.inv.hotbar;

import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.inventory.InventoryItem;
import com.gamefocal.island.game.inventory.InventoryStack;
import com.gamefocal.island.game.inventory.equipment.EquipmentSlot;
import com.gamefocal.island.game.tasks.seqence.ExecSequenceAction;
import com.gamefocal.island.game.tasks.seqence.WaitSequenceAction;
import com.gamefocal.island.service.TaskService;

@Command(name = "hotbexec",sources = "tcp")
public class NetHotbarExec extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        Integer slot = Integer.parseInt(message.args[0]);

        if (netConnection.getPlayer().hotbar.isLinked(slot)) {

            // Has a hotbar item linked

            InventoryStack stack = netConnection.getPlayer().findStackFromUUID(netConnection.getPlayer().hotbar.items[slot]);

            InventoryItem item = stack.getItem();

            if (item.isEquipable()) {
                // Is Equipment
                if (!stack.isEquiped(netConnection.getPlayer())) {
                    // Equip

                    int slotIndex = netConnection.getPlayer().inventory.getSlotIndexByUUID(item.getItemUUID());

                    if (slotIndex >= 0) {

                        netConnection.equipFromInventory(slotIndex);

//                        if (stack.equip(netConnection.getPlayer())) {
//                            netConnection.getPlayer().inventory.clear(slotIndex);
//
//                            TaskService.scheduleTaskSequence(false, new ExecSequenceAction() {
//                                @Override
//                                public void run() {
//                                    netConnection.updateInventory(netConnection.getPlayer().inventory);
//                                }
//                            }, new WaitSequenceAction(5L), new ExecSequenceAction() {
//                                @Override
//                                public void run() {
//                                    netConnection.syncEquipmentSlots();
//                                }
//                            });
//                        }
                    }
                } else {
                    // Unequip
                    EquipmentSlot eqSlot = stack.getEquipmentSlot(netConnection.getPlayer());
                    if (eqSlot != null) {
                        netConnection.unequipTool(eqSlot);
//                        InventoryStack stack1 = netConnection.getPlayer().equipmentSlots.getItemBySlot(eqSlot);
//                        if (netConnection.getPlayer().inventory.canAdd(stack1)) {
//                            netConnection.getPlayer().equipmentSlots.setBySlot(eqSlot, null);
//                            netConnection.getPlayer().inventory.add(stack1);
//
//                            TaskService.scheduleTaskSequence(false, new ExecSequenceAction() {
//                                @Override
//                                public void run() {
//                                    netConnection.updateInventory(netConnection.getPlayer().inventory);
//                                }
//                            }, new WaitSequenceAction(5L), new ExecSequenceAction() {
//                                @Override
//                                public void run() {
//                                    netConnection.syncEquipmentSlots();
//                                }
//                            });
//                        }
                    }
                }
            } else if (item.isConsumable()) {
                // TODO: Consume Action Here
            } else {
                // TODO: Other actions
            }

        }

    }
}
