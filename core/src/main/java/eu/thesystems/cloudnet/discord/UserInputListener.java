package eu.thesystems.cloudnet.discord;
/*
 * Created by Mc_Ruben on 26.02.2019
 */

import eu.thesystems.cloudnet.discord.utility.MapBuilder;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.function.Consumer;

@AllArgsConstructor
public class UserInputListener extends ListenerAdapter {
    private CloudNetDiscordBot discordBot;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !this.discordBot.getProvider().getChannels().contains(event.getChannel().getIdLong()))
            return;

        if (!this.discordBot.getPermissionProvider().isUserAllowedToUseConsoleCommands(event.getAuthor())) {
            if (this.discordBot.getBlacklistedOrNotWhitelistedMessage() != null) {
                this.discordBot.getBlacklistedOrNotWhitelistedMessage().sendMessage(event.getChannel(), event.getAuthor());
            }
            /*event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setTitle("No Permissions")
                            .setDescription("You aren't allowed to use commands in the console.")
                            .setFooter(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl() != null ? event.getAuthor().getAvatarUrl() : event.getAuthor().getDefaultAvatarUrl())
                            .build()
            ).queue();*/
            return;
        }

        DiscordCommandInfo command = this.discordBot.getCommandFromLine(event.getMessage().getContentRaw());
        if (command == null) {
            if (this.discordBot.getUnknownCommandMessage() != null) {
                this.discordBot.getUnknownCommandMessage().sendMessage(event.getChannel(), event.getAuthor(), new MapBuilder<String, String>().put("%command%", event.getMessage().getContentRaw()).getMap());
            }
            return;
        }

        if (this.discordBot.getPermissionProvider().hasPermissionForCommand(event.getMember(), command.getPermission())) {
            if (command.getName().equals("clear")) {
                this.discordBot.clearChannel(event.getChannel(), (Consumer<TextChannel>) textChannel -> textChannel.sendMessage(
                        new EmbedBuilder()
                                .setTitle("Clear")
                                .setDescription("The channel was cleared.")
                                .setFooter(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl() != null ? event.getAuthor().getAvatarUrl() : event.getAuthor().getDefaultAvatarUrl())
                                .build()
                ).queue());
            } else {
                this.discordBot.dispatchCommand(new DiscordCommandSender(event.getMember(), this.discordBot), event.getMessage().getContentRaw());
            }
        } else {
            if (this.discordBot.getNoPermissionMessage() != null) {
                this.discordBot.getNoPermissionMessage().sendMessage(event.getChannel(), event.getAuthor(), new MapBuilder<String, String>().put("%command%", command.getName()).put("%permission%", command.getPermission()).getMap());
            }
            /*event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setTitle("No Permissions")
                            .setDescription("You aren't allowed to use that command, missing permission: " + command.getPermission())
                            .setFooter(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl() != null ? event.getAuthor().getAvatarUrl() : event.getAuthor().getDefaultAvatarUrl())
                            .build()
            ).queue();*/
        }
    }
}
