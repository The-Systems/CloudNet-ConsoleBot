package eu.thesystems.cloudnet.discord;
/*
 * Created by Mc_Ruben on 06.03.2019
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.requests.RestAction;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Getter
@AllArgsConstructor
public class EmbedMessageConfigEntry {

    private boolean enabled;
    private boolean embed;
    private String title;
    private String description;
    private Collection<Field> fields;
    private boolean footer;
    private String colorHex;
    private boolean deleteAfterTime;
    private long deletionMillis;

    public void sendMessage(MessageChannel channel, User receiver) {
        this.sendMessage(channel, receiver, null);
    }

    public void sendMessage(MessageChannel channel, User receiver, Map<String, String> replacements) {
        if (!this.enabled)
            return;
        RestAction<Message> restAction = null;
        if (this.embed) {
            EmbedBuilder builder = new EmbedBuilder();
            if (this.title != null && !this.title.isEmpty()) {
                builder.setTitle(replace(this.title, replacements));
            }
            if (this.description != null && !this.description.isEmpty()) {
                builder.setDescription(replace(this.description, replacements));
            }
            if (this.fields != null && !this.fields.isEmpty()) {
                for (Field field : this.fields) {
                    builder.addField(field.toMessageField(replacements));
                }
            }
            try {
                builder.setColor(Integer.parseInt(this.colorHex, 16));
            } catch (NumberFormatException e) {
                System.err.println("The color defined in the config.json was no hex-string");
            }
            if (receiver != null && this.footer) {
                builder.setFooter(receiver.getName() + "#" + receiver.getDiscriminator(), receiver.getAvatarUrl() != null ? receiver.getAvatarUrl() : receiver.getDefaultAvatarUrl());
            }
            restAction = channel.sendMessage(builder.build());
        } else {
            if (this.title != null) {
                restAction = channel.sendMessage(this.title);
            } else if (this.description != null) {
                restAction = channel.sendMessage(this.description);
            }
        }
        if (restAction != null) {
            if (this.deleteAfterTime && this.deletionMillis > 0) {
                restAction.queue(message -> message.delete().queueAfter(this.deletionMillis, TimeUnit.MILLISECONDS));
            } else {
                restAction.queue();
            }
        }
    }

    private static String replace(String s, Map<String, String> replacements) {
        if (replacements != null) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                s = s.replace(entry.getKey(), entry.getValue());
            }
        }
        return s;
    }

    @AllArgsConstructor
    public static final class Field {
        private String name;
        private String value;
        private boolean inline;

        public MessageEmbed.Field toMessageField(Map<String, String> replacements) {
            return new MessageEmbed.Field(replace(this.name, replacements), replace(this.value, replacements), this.inline);
        }
    }

}
