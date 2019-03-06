package de.thesystems.cloudnet.discord;
/*
 * Created by Mc_Ruben on 27.02.2019
 */

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class DiscordPermissionUser {

    private transient String id;
    private Collection<String> permissions;

    public DiscordPermissionUser(String id, Collection<String> permissions) {
        this.id = id;
        this.permissions = permissions.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public DiscordPermissionUser(long id, Collection<String> permissions) {
        this(String.valueOf(id), permissions);
    }

    public DiscordPermissionUser setId(long id) {
        this.id = String.valueOf(id);
        return this;
    }

    public DiscordPermissionUser setId(String id) {
        this.id = id;
        return this;
    }

    public void addPermission(String permission) {
        this.permissions.add(permission);
    }

    public boolean hasPermission(String permission) {
        permission = permission.toLowerCase();
        if (this.permissions.contains("*")) {
            return !this.permissions.contains("-" + permission);
        }
        if (this.permissions.contains("-*")) {
            return this.permissions.contains(permission);
        }
        return this.permissions.contains(permission) && !this.permissions.contains("-" + permission);
    }

    public boolean hasPermissionNegative(String permission) {
        permission = "-" + (permission.toLowerCase());
        return this.permissions.contains("-*") || this.permissions.contains(permission);
    }

}
