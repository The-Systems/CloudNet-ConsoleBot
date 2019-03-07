package eu.thesystems.cloudnet3.discord;
/*
 * Created by Mc_Ruben on 02.03.2019
 */

import de.dytanic.cloudnet.CloudNet;
import de.dytanic.cloudnet.command.Command;
import de.dytanic.cloudnet.common.logging.LogEntry;
import eu.thesystems.cloudnet.discord.CloudNetDiscordBot;
import eu.thesystems.cloudnet.discord.DiscordCommandInfo;
import eu.thesystems.cloudnet.discord.DiscordCommandSender;

import java.util.Collection;

public abstract class V3CloudNetDiscordBot extends CloudNetDiscordBot<LogEntry> {

    private V3DiscordConsoleLogHandler consoleLogHandler;

    @Override
    protected void addLogHandler() {
        getCloudNet().getLogger().addLogHandler(consoleLogHandler = new V3DiscordConsoleLogHandler(this));
    }

    @Override
    protected void handleLogInput(LogEntry o) {
        if (consoleLogHandler != null) {
            consoleLogHandler.handle(o);
        }
    }

    @Override
    protected void removeLogHandler() {
        if (consoleLogHandler != null) {
            getCloudNet().getLogger().removeLogHandler(consoleLogHandler);
        }
    }

    @Override
    protected Collection<LogEntry> getCachedLogEntries() {
        return getCloudNet().getQueuedConsoleLogHandler().getCachedQueuedLogEntries();
    }

    @Override
    public DiscordCommandInfo getCommandFromLine(String line) {
        Command command = getCloudNet().getCommandMap().getCommandFromLine(line);
        if (command == null)
            return null;
        return new DiscordCommandInfo(command.getNames()[0], command.getPermission());
    }

    @Override
    public void dispatchCommand(DiscordCommandSender sender, String line) {
        getCloudNet().getCommandMap().dispatchCommand(new V3DiscordCommandSender(sender), line);
    }

    protected abstract CloudNet getCloudNet();

}
