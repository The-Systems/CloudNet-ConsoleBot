package eu.thesystems.cloudnet3.discord;
/*
 * Created by Mc_Ruben on 26.02.2019
 */

import de.dytanic.cloudnet.common.logging.AbstractLogHandler;
import de.dytanic.cloudnet.common.logging.LogEntry;
import eu.thesystems.cloudnet.discord.CloudNetDiscordBot;
import lombok.*;

@AllArgsConstructor
public class V3DiscordConsoleLogHandler extends AbstractLogHandler {
    private CloudNetDiscordBot discordBot;

    @Override
    public void handle(LogEntry logEntry) {
        if (!this.discordBot.isShutdown()) {
            String message = this.getFormatter().format(logEntry);
            this.discordBot.getProvider().handleConsoleInput(message);
        }
    }

    @Override
    public void close() throws Exception {
    }

}
