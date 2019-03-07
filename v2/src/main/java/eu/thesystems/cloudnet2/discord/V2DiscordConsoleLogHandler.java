package eu.thesystems.cloudnet2.discord;
/*
 * Created by Mc_Ruben on 26.02.2019
 */

import eu.thesystems.cloudnet.discord.CloudNetDiscordBot;
import lombok.AllArgsConstructor;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

@AllArgsConstructor
public class V2DiscordConsoleLogHandler extends Handler {
    private CloudNetDiscordBot discordBot;

    @Override
    public void publish(LogRecord record) {
        if (!this.discordBot.isShutdown()) {
            String message = this.getFormatter().format(record);
            if (message.charAt(message.length() - 1) == '\n') {
                message = message.substring(0, message.length() - 1);
            }
            this.handleMessage(message);
        }
    }

    public void handleMessage(String message) {
        this.discordBot.getProvider().handleConsoleInput(message);
    }

    @Override
    public void flush() {
    }

    @Override
    public void close() {
    }

}
