package de.thesystems.cloudnet2.discord.command;
/*
 * Created by Mc_Ruben on 27.02.2019
 */

import de.dytanic.cloudnet.command.Command;
import de.dytanic.cloudnet.command.CommandSender;
import de.thesystems.cloudnet.discord.CloudNetDiscordBot;

public class V2CommandDiscordConsole extends Command {

    private CloudNetDiscordBot discordBot;

    public V2CommandDiscordConsole(CloudNetDiscordBot discordBot) {
        super("discord-console", "discord-console.command.dc", "dc");
        this.discordBot = discordBot;
    }

    @Override
    public void onExecuteCommand(CommandSender sender, String[] args) {
        if (args.length == 0 || (!args[0].equalsIgnoreCase("reload") && !args[0].equalsIgnoreCase("rl"))) {
            sender.sendMessage("dc reload");
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
