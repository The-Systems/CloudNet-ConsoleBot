package eu.thesystems.cloudnet.discord;
/*
 * Created by Mc_Ruben on 26.02.2019
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;

@Getter
@AllArgsConstructor
public class DiscordCommandSender {

    private Member member;
    private CloudNetDiscordBot discordBot;

    public String getName() {
        return member.getNickname() != null ? member.getNickname() : member.getUser().getName();
    }

    public void sendMessage(String s) {
        System.out.println(s);
    }

    public void sendMessage(String... strings) {
        for (String string : strings) {
            this.sendMessage(string);
        }
    }

    public boolean hasPermission(String permission) {
        return this.discordBot.getPermissionProvider().hasPermissionForCommand(this.member, permission);
    }
}
