package de.thesystems.cloudnet.discord;
/*
 * Created by Mc_Ruben on 06.03.2019
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;

import java.util.Collection;

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

    public void sendMessage(MessageChannel channel, User receiver) {
        if (!this.enabled)
            return;
        if (this.embed) {
            EmbedBuilder builder = new EmbedBuilder();
            if (this.title != null && !this.title.isEmpty()) {
                builder.setTitle(this.title);
            }
            if (this.description != null && !this.description.isEmpty()) {
                builder.setDescription(this.description);
            }
            if (this.fields != null && !this.fields.isEmpty()) {
                for (Field field : this.fields) {
                    builder.addField(field.toMessageField());
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
            channel.sendMessage(builder.build()).queue();
        } else {
            if (this.title != null) {
                channel.sendMessage(this.title).queue();
            } else if (this.description != null) {
                channel.sendMessage(this.description).queue();
            }
        }
    }

    @AllArgsConstructor
    public static final class Field {
        private String name;
        private String value;
        private boolean inline;

        public MessageEmbed.Field toMessageField() {
            return new MessageEmbed.Field(this.name, this.value, this.inline);
        }
    }

}
