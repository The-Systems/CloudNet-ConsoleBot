package eu.thesystems.cloudnet3.discord;
/*
 * Created by Mc_Ruben on 02.03.2019
 */

import de.dytanic.cloudnet.command.ICommandSender;
import de.dytanic.cloudnet.common.logging.ILogger;
import eu.thesystems.cloudnet.discord.DiscordCommandSender;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class V3DiscordCommandSender implements ICommandSender {

    private ILogger logger;
    private DiscordCommandSender sender;

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public void sendMessage(String s) {
        this.sender.sendMessage(s);
    }

    @Override
    public void sendMessage(String... strings) {
        this.sender.sendMessage(strings);
    }

    @Override
    public boolean hasPermission(String s) {
        return this.sender.hasPermission(s);
    }
}
