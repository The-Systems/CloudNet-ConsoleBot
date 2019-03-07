package eu.thesystems.cloudnet2.discord;
/*
 * Created by Mc_Ruben on 02.03.2019
 */

import de.dytanic.cloudnet.command.CommandSender;
import de.dytanic.cloudnet.lib.player.permission.PermissionEntity;
import eu.thesystems.cloudnet.discord.DiscordCommandSender;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class V2DiscordCommandSender implements CommandSender {

    private DiscordCommandSender sender;

    @Override
    public void sendMessage(String... strings) {
        this.sender.sendMessage(strings);
    }

    @Override
    public boolean hasPermission(String s) {
        return this.sender.hasPermission(s);
    }

    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public PermissionEntity getPermissionEntity() {
        return null;
    }
}
