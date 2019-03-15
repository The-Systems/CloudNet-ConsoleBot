package eu.thesystems.cloudnet2.discord;
/*
 * Created by Mc_Ruben on 02.03.2019
 */

import de.dytanic.cloudnetcore.CloudNet;
import de.dytanic.cloudnetcore.api.CoreModule;
import eu.thesystems.cloudnet.discord.CloudNetDiscordBot;
import eu.thesystems.cloudnet2.discord.command.V2CommandDiscordConsole;

public class CloudNet2Bot extends CoreModule {

    private CloudNetDiscordBot discordBot;

    @Override
    public void onBootstrap() {
        this.discordBot = new V2CloudNetDiscordBot() {
            @Override
            public String getVersion() {
                return CloudNet2Bot.this.getModuleConfig().getVersion() + " by " + CloudNet2Bot.this.getModuleConfig().getAuthor();
            }

            @Override
            protected CloudNet getCloud() {
                return CloudNet2Bot.this.getCloud();
            }
        };
        this.discordBot.enableBot();

        this.registerCommand(new V2CommandDiscordConsole(this.discordBot));
    }

    @Override
    public void onShutdown() {
        this.discordBot.disableBot();
    }

}
