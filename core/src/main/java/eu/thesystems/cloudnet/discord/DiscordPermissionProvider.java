package eu.thesystems.cloudnet.discord;
/*
 * Created by Mc_Ruben on 26.02.2019
 */

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.User;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class DiscordPermissionProvider {

    private boolean whitelist, blacklist;
    private Collection<Long> whitelistedUsers, blacklistedUsers;
    private Collection<DiscordPermissionUser> permissionUsers;

    public boolean isUserAllowedToUseConsoleCommands(User user) {
        if (this.blacklist && (blacklistedUsers == null || blacklistedUsers.contains(user.getIdLong()))) {
            return false;
        }
        if (this.whitelist) {
            if (this.whitelistedUsers == null)
                return false;
            return this.whitelistedUsers.contains(user.getIdLong());
        }
        return true;
    }

    public boolean hasPermissionForCommand(Member member, String permission) {
        if (this.permissionUsers == null || permission == null)
            return true;
        User user = member.getUser();
        DiscordPermissionUser permissionUser = this.permissionUsers.stream().filter(discordPermissionUser -> discordPermissionUser.getId().equals(user.getId())).findFirst().orElse(null);
        if (permissionUser != null) {
            if (permissionUser.hasPermission(permission)) {
                return true;
            }
            if (permissionUser.hasPermissionNegative(permission)) {
                return false;
            }
        }
        for (DiscordPermissionUser discordPermissionUser : member.getRoles().stream().map(role -> this.findPermissionUser(role.getName())).filter(Objects::nonNull).collect(Collectors.toList())) {
            if (discordPermissionUser.hasPermissionNegative(permission)) {
                return false;
            }
            if (discordPermissionUser.hasPermission(permission)) {
                return true;
            }
        }
        for (DiscordPermissionUser discordPermissionUser : member.getRoles().stream().map(role -> this.findPermissionUser(role.getId())).filter(Objects::nonNull).collect(Collectors.toList())) {
            if (discordPermissionUser.hasPermissionNegative(permission)) {
                return false;
            }
            if (discordPermissionUser.hasPermission(permission)) {
                return true;
            }
        }
        permissionUser = this.findPermissionUser("*");
        if (permissionUser != null) {
            if (permissionUser.hasPermission(permission)) {
                return true;
            }
        }
        return false;
    }

    private DiscordPermissionUser findPermissionUser(String val) {
        return this.permissionUsers.stream().filter(discordPermissionUser -> discordPermissionUser.getId().equals(val)).findFirst().orElse(null);
    }
}
