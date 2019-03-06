package de.thesystems.cloudnet.discord;
/*
 * Created by Mc_Ruben on 26.02.2019
 */

import lombok.AllArgsConstructor;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.util.function.Consumer;

@AllArgsConstructor
public class UserInputListener extends ListenerAdapter {
    private CloudNetDiscordBot discordBot;

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !discordBot.getProvider().getChannels().contains(event.getChannel().getIdLong()))
            return;

        if (!discordBot.getPermissionProvider().isUserAllowedToUseConsoleCommands(event.getAuthor())) {
            event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setTitle("No Permissions")
                            .setDescription("You aren't allowed to use commands in the console.")
                            .setFooter(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl() != null ? event.getAuthor().getAvatarUrl() : event.getAuthor().getDefaultAvatarUrl())
                            .build()
            ).queue();
            return;
        }

        DiscordCommandInfo command = this.discordBot.getCommandFromLine(event.getMessage().getContentRaw());
        if (command == null) {
            if (this.discordBot.getUnknownCommandMessage() != null) {
                this.discordBot.getUnknownCommandMessage().sendMessage(event.getChannel(), event.getAuthor());
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
            event.getChannel().sendMessage(
                    new EmbedBuilder()
                            .setTitle("No Permissions")
                            .setDescription("You aren't allowed to use that command, missing permission: " + command.getPermission())
                            .setFooter(event.getAuthor().getName() + "#" + event.getAuthor().getDiscriminator(), event.getAuthor().getAvatarUrl() != null ? event.getAuthor().getAvatarUrl() : event.getAuthor().getDefaultAvatarUrl())
                            .build()
            ).queue();
        }
    }
}
