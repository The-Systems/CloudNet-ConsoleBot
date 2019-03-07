package eu.thesystems.cloudnet3.discord.command;
/*
 * Created by Mc_Ruben on 27.02.2019
 */

import de.dytanic.cloudnet.command.Command;
import de.dytanic.cloudnet.command.ICommandSender;
import de.dytanic.cloudnet.common.Properties;
import eu.thesystems.cloudnet.discord.CloudNetDiscordBot;

public class V3CommandDiscordConsole extends Command {

    private CloudNetDiscordBot discordBot;

    public V3CommandDiscordConsole(CloudNetDiscordBot discordBot) {
        super(new String[]{"discord-console", "dc"}, "discord-console.command.dc", "Reload the config of your bot", "dc reload", "discord-console");
        this.discordBot = discordBot;
    }

    @Override
    public void execute(ICommandSender sender, String s, String[] args, String s1, Properties properties) {
        if (args.length == 0 || (!args[0].equalsIgnoreCase("reload") && !args[0].equalsIgnoreCase("rl"))) {
            sender.sendMessage(getUsage());
            return;
        }
        sender.sendMessage("Trying to reload the bot config...");
        if (this.discordBot.reloadDiscordConfig()) {
            sender.sendMessage("Successfully reloaded the bot config");
        } else {
            sender.sendMessage("There was an error while trying to reload the bot config");
        }
    }
}
