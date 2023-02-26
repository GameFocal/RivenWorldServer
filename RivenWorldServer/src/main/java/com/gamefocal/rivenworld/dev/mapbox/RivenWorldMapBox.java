package com.gamefocal.rivenworld.dev.mapbox;

import com.badlogic.gdx.math.MathUtils;
import com.gamefocal.rivenworld.DedicatedServer;
import com.gamefocal.rivenworld.entites.net.HiveNetConnection;
import com.gamefocal.rivenworld.game.util.Location;
import com.gamefocal.rivenworld.models.GameEntityModel;
import com.gamefocal.rivenworld.models.GameResourceNode;
import com.gamefocal.rivenworld.service.DataService;
import com.gamefocal.rivenworld.service.PlayerService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class RivenWorldMapBox {
    public JPanel mainPanel;
    private JLabel picture;

    public RivenWorldMapBox() {
        new Thread(() -> {
            while (true) {

//                System.out.println("Update Mapbox");

                try {
                    File f = new File(getClass().getClassLoader().getResource("rivenworld_full.png").getFile());
                    BufferedImage img = ImageIO.read(f);
                    Graphics g = img.getGraphics();
                    ;

//                    g.setColor(Color.YELLOW);
//                    String len = "F: " + DevCommands.factor;
//                    g.drawString(len, 0, 50);
//                    g.drawString("O: " + DevCommands.offset.toString(), 0, 100);

                    // Draw Players
                    g.setColor(Color.RED);
                    for (HiveNetConnection connection : DedicatedServer.get(PlayerService.class).players.values()) {
                        Location location = connection.getPlayer().location;
                        Location mapped = mappedLocation(location);

//                        int localX = (int) ((location.getX() * DevCommands.offset.getX()) / DevCommands.factor);
//                        int localY = (int) ((location.getY() * DevCommands.offset.getY()) / DevCommands.factor);
                        g.drawOval((int) mapped.getX(), (int) mapped.getY(), 5, 5);
                    }

                    g.setColor(Color.GREEN);
                    for (GameEntityModel e : DataService.gameEntities.queryForAll()) {
                        Location location = e.location;
                        Location mapped = mappedLocation(location);
//                        int localX = (int) ((location.getX() * DevCommands.offset.getX()) / DevCommands.factor);
//                        int localY = (int) ((location.getY() * DevCommands.offset.getY()) / DevCommands.factor);
                        g.drawRect((int) mapped.getX(), (int) mapped.getY(), 1, 1);
                    }

                    g.setColor(Color.YELLOW);
                    for (GameResourceNode r : DataService.resourceNodes.queryForAll()) {
                        Location location = r.location;
                        Location mapped = mappedLocation(location);
                        g.drawRect((int) mapped.getX(), (int) mapped.getY(), 1, 1);
                    }

                    g.dispose();

                    picture.setIcon(new ImageIcon(img));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Location mappedLocation(Location gameLoc) {
        return new Location(
                MathUtils.map(-25181.08f, 176573.27f, 0, 1008, gameLoc.getX()),
                MathUtils.map(-25181.08f, 176573.27f, 0, 1008, gameLoc.getY()),
                0f
        );
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
