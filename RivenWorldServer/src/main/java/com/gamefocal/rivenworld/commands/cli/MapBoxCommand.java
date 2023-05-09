package com.gamefocal.rivenworld.commands.cli;

import com.gamefocal.rivenworld.dev.mapbox.RivenWorldMapBox;
import com.gamefocal.rivenworld.entites.net.*;

import javax.swing.*;

@Command(name = "mapbox", sources = "cli")
public class MapBoxCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        System.out.println("Prepping Mapbox");

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new RivenWorldMapBox().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("RivenWorld Mapbox Debugger");
        frame.pack();
        frame.setVisible(true);

        System.out.println("Showing Mapbox");

    }
}
