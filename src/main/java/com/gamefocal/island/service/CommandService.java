package com.gamefocal.island.service;

import com.gamefocal.island.entites.command.Command;
import com.gamefocal.island.entites.command.HiveCommand;
import com.gamefocal.island.entites.command.HiveCommandPromise;
import com.gamefocal.island.entites.command.HiveConsoleCommand;
import com.gamefocal.island.entites.net.HiveReplyMessage;
import com.gamefocal.island.entites.service.HiveService;
import com.google.auto.service.AutoService;
import org.reflections.Reflections;

import javax.inject.Singleton;
import java.util.Hashtable;
import java.util.Set;

@Singleton
@AutoService(HiveService.class)
public class CommandService implements HiveService<CommandService> {

    private Hashtable<String, HiveConsoleCommand> consoleCommands = new Hashtable<>();

    private Hashtable<String, HiveCommand> netCommands = new Hashtable<>();

    private Hashtable<String, HiveCommandPromise> awaitingReply = new Hashtable<>();

    @Override
    public void init() {
        System.out.println("\tLoading Commands...");

        // Load the commands
        Set<Class<? extends HiveConsoleCommand>> commandClasses = new Reflections("com.gamefocal").getSubTypesOf(HiveConsoleCommand.class);
        Set<Class<? extends HiveCommand>> newCommandClasses = new Reflections("com.gamefocal").getSubTypesOf(HiveCommand.class);

        System.out.println("\t\tFound " + commandClasses.size() + " console commands to load.");
        System.out.println("\t\tFound " + newCommandClasses.size() + " net commands to load.");

        this.loadCommands(HiveConsoleCommand.class, commandClasses);
        this.loadCommands(HiveCommand.class, newCommandClasses);
    }

    private <T> void loadCommands(T t, Set<T> commandClasses) {
        for (T cc2 : commandClasses) {
            Class<T> cc = (Class<T>) cc2;
            Command hc = (Command) cc.getAnnotation(Command.class);
            if (hc != null) {
                try {

                    if (HiveConsoleCommand.class.isAssignableFrom(cc)) {
                        // A hive console command
                        HiveConsoleCommand c = (HiveConsoleCommand) cc.newInstance();
                        c.name = hc.name();
                        this.consoleCommands.put(c.name, c);

                        /*
                         * Load Aliases for command
                         * */
                        for (String sub : hc.aliases().split(",")) {
                            this.consoleCommands.put(sub, c);
                        }

                        System.out.println("\t\t\tLoaded CLI CMD " + c.name + ".");
                    } else if (HiveCommand.class.isAssignableFrom(cc)) {
                        // A hive net command
                        HiveCommand c = (HiveCommand) cc.newInstance();
                        c.name = hc.name();
                        c.allowedSource = hc.netSource();
                        this.netCommands.put(c.name, c);

                        /*
                         * Load Aliases for command
                         * */
                        for (String sub : hc.aliases().split(",")) {
                            this.netCommands.put(sub, c);
                        }

                        System.out.println("\t\t\tLoaded as NET CMD " + c.name + ".");
                    }
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            } else {
                System.err.println("HC is null");
            }
        }
    }

    public void triggerReplyHooks(HiveReplyMessage replyMessage) {
        if (this.awaitingReply.containsKey(replyMessage.id)) {
            HiveCommandPromise promise = this.awaitingReply.get(replyMessage.id);
            promise.setReplyMessage(replyMessage);
            promise.run();

            this.awaitingReply.remove(replyMessage.id);
        }
    }

    public HiveConsoleCommand findConsoleCommand(String name) {
        if (this.consoleCommands.containsKey(name)) {
            return this.consoleCommands.get(name);
        }

        return null;
    }

    public HiveCommand findNetCommand(String name) {
        if (this.netCommands.containsKey(name)) {
            return this.netCommands.get(name);
        }

        return null;
    }
}
