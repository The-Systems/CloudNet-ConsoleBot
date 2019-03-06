package de.thesystems.cloudnet.discord;
/*
 * Created by Mc_Ruben on 26.02.2019
 */

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.TextChannel;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class DiscordConsoleProvider implements Runnable {

    @Setter
    private long delay;
    @Setter
    @Getter
    private List<Long> channels;
    @Getter
    private Thread thread;

    private CloudNetDiscordBot discordBot;

    private BlockingDeque<String> messages = new LinkedBlockingDeque<>();

    public DiscordConsoleProvider(CloudNetDiscordBot discordBot) {
        this.discordBot = discordBot;
    }

    public void handleConsoleInput(String line) {
        this.messages.offer(line.replace("*", "\\*").replace("_", "\\_").replace("~", "\\~"));
    }

    @Override
    public void run() {
        StringBuilder builder = new StringBuilder();
        this.thread = Thread.currentThread();
        while (!Thread.interrupted()) {
            try {
                while (this.channels == null || this.channels.isEmpty() || this.discordBot.getJda() == null) {
                    Thread.sleep(750);
                }

                String message;
                do {
                    message = messages.take();

                    if (builder.length() + message.length() >= 1950) {
                        if (builder.length() >= 1950) {
                            messages.offerFirst(message);
                            break;
                        }
                        int length = 1950 - builder.length();
                        if (length <= 0) {
                            messages.offerFirst(message);
                            break;
                        }
                        message = message.substring(0, length);
                        messages.offerFirst(message.substring(length));
                        break;
                    }

                    builder.append(message);
                } while (!messages.isEmpty());

                if (builder.length() > 0) {
                    if (this.channels != null && !this.channels.isEmpty() && !this.discordBot.isShutdown()) {
                        JDA jda = this.discordBot.getJda();
                        if (jda != null) {
                            for (Long channelId : this.channels) {
                                TextChannel textChannel = jda.getTextChannelById(channelId);
                                if (textChannel != null) {
                                    textChannel.sendMessage(builder.toString()).queue();
                                }
                            }
                        }
                    }
                    builder.setLength(0);
                }

                Thread.sleep(Math.max(100, delay));
            } catch (InterruptedException e) {
                break;
                //e.printStackTrace();
            }
        }
    }
}
