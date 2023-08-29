package com.gamefocal.rivenworld.patch;

import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.models.GameMetaModel;

import java.util.LinkedList;

public class PatchUtility {

    private LinkedList<ServerVersionPatch> patches = new LinkedList<>();

    public PatchUtility() {
        this.patches.add(new Version106Patch());
    }

    public boolean run() {
        boolean hasPatched = false;
        patches.sort((o1, o2) -> {
            if (o1.version() > o2.version()) {
                return +1;
            } else if (o1.version() < o2.version()) {
                return -1;
            }

            return 0;
        });
        for (ServerVersionPatch patch : patches) {
            if (patch.shouldPatch()) {
                DedicatedServer.isLocked = true;
                DedicatedServer.lockMessage = "Server Upgrading World";
                System.out.println("Patching to version " + patch.version());
                patch.onPatch();
                GameMetaModel.setMetaValue("version", String.valueOf(patch.version()));
                System.out.println("Patch complete, current world version " + DedicatedServer.getWorldFileVersion());
                hasPatched = true;
            }
        }

        return hasPatched;
    }

}
