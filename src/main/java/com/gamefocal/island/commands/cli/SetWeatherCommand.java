package com.gamefocal.island.commands.cli;

import com.gamefocal.island.DedicatedServer;
import com.gamefocal.island.entites.net.*;
import com.gamefocal.island.game.weather.GameWeather;
import com.gamefocal.island.service.EnvironmentService;

@Command(name = "weather", sources = "cli,chat")
public class SetWeatherCommand extends HiveCommand {
    @Override
    public void onCommand(HiveNetMessage message, CommandSource source, HiveNetConnection netConnection) throws Exception {

        String state = message.args[0];

        if (state.equalsIgnoreCase("clear")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.CLEAR);
        } else if (state.equalsIgnoreCase("cloudy")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.PARTLY_CLOUD);
        } else if (state.equalsIgnoreCase("verycloudy")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.CLOUDY);
        } else if (state.equalsIgnoreCase("shower")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.RAIN_LIGHT);
        } else if (state.equalsIgnoreCase("rain")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.RAIN);
        } else if (state.equalsIgnoreCase("thunderstorm")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.RAIN_THUNDERSTORM);
        } else if (state.equalsIgnoreCase("litesnow")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.SNOW_LIGHT);
        } else if (state.equalsIgnoreCase("blizzard")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.BLIZARD);
        } else if (state.equalsIgnoreCase("snow")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.SNOW);
        } else if (state.equalsIgnoreCase("dust")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.DUST);
        } else if (state.equalsIgnoreCase("duststorm")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.DUST_STORM);
        } else if (state.equalsIgnoreCase("overcast")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.OVERCAST);
        } else if (state.equalsIgnoreCase("foggy")) {
            DedicatedServer.get(EnvironmentService.class).setWeather(GameWeather.FOGGY);
        } else if (state.equalsIgnoreCase("auto-off")) {
            // Freeze Weather Change
            EnvironmentService.setAutoWeather(false);
        } else if (state.equalsIgnoreCase("auto-on")) {
            // Freeze Weather Change
            EnvironmentService.setAutoWeather(true);
        }

//        if (time.equalsIgnoreCase("dawn")) {
//            DedicatedServer.get(EnvironmentService.class).setDayPercent(.25f);
//        } else if (time.equalsIgnoreCase("day")) {
//            DedicatedServer.get(EnvironmentService.class).setDayPercent(.5f);
//        } else if (time.equalsIgnoreCase("dusk")) {
//            DedicatedServer.get(EnvironmentService.class).setDayPercent(.75f);
//        } else if (time.equalsIgnoreCase("night")) {
//            DedicatedServer.get(EnvironmentService.class).setDayPercent(0f);
//        }

        DedicatedServer.get(EnvironmentService.class).broadcastEnvChange(true);
    }
}
