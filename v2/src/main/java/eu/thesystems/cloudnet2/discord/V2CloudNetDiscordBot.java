package eu.thesystems.cloudnet2.discord;
/*
 * Created by Mc_Ruben on 02.03.2019
 */

import de.dytanic.cloudnet.command.Command;
import de.dytanic.cloudnetcore.CloudNet;
import eu.thesystems.cloudnet.discord.CloudNetDiscordBot;
import eu.thesystems.cloudnet.discord.DiscordCommandInfo;
import eu.thesystems.cloudnet.discord.DiscordCommandSender;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class V2CloudNetDiscordBot extends CloudNetDiscordBot<String> {

    private V2DiscordConsoleLogHandler consoleLogHandler;

    @Override
    protected void addLogHandler() {
        consoleLogHandler = new V2DiscordConsoleLogHandler(this);
        consoleLogHandler.setFormatter(CloudNet.getLogger().getFormatter());
        CloudNet.getLogger().addHandler(consoleLogHandler);
    }

    @Override
    protected void handleLogInput(String s) {
        if (consoleLogHandler != null) {
            consoleLogHandler.handleMessage(s);
        }
    }

    @Override
    protected void removeLogHandler() {
        if (consoleLogHandler != null) {
            CloudNet.getLogger().removeHandler(consoleLogHandler);
        }
    }

    @Override
    protected Collection<String> getCachedLogEntries() {
        return getCloud().getPreConsoleOutput().stream().map(s -> s + "\n").collect(Collectors.toList());
    }

    @Override
    public DiscordCommandInfo getCommandFromLine(String line) {
        String[] args = line.split(" ");
        String name = args[0].toLowerCase();

        Command result = null;

        Set<String> checked = new HashSet<>();
        for (String commandName : getCloud().getCommandManager().getCommands()) {
            Command command = getCloud().getCommandManager().getCommand(commandName);
            if (checked.contains(command.getName())) {
                continue;
            }
            checked.add(command.getName());

            if (command.getName().equalsIgnoreCase(name)) {
                result = command;
            } else {
                for (String alias : command.getAliases()) {
                    if (alias.equalsIgnoreCase(name)) {
                        result = command;
                        break;
                    }
                }
            }
            if (result != null) {
                break;
            }
        }
        checked.clear();

        if (result == null)
            return null;
        return new DiscordCommandInfo(result.getName(), result.getPermission());
    }

    @Override
    public void dispatchCommand(DiscordCommandSender sender, String line) {
        getCloud().getCommandManager().dispatchCommand(new V2DiscordCommandSender(sender), line);
    }

    protected abstract CloudNet getCloud();
}
