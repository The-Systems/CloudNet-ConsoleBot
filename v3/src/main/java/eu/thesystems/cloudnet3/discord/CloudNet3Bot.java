package eu.thesystems.cloudnet3.discord;
/*
 * Created by Mc_Ruben on 02.03.2019
 */

import de.dytanic.cloudnet.CloudNet;
import de.dytanic.cloudnet.driver.module.ModuleLifeCycle;
import de.dytanic.cloudnet.driver.module.ModuleTask;
import de.dytanic.cloudnet.module.NodeCloudNetModule;
import eu.thesystems.cloudnet.discord.CloudNetDiscordBot;
import eu.thesystems.cloudnet3.discord.command.V3CommandDiscordConsole;

public class CloudNet3Bot extends NodeCloudNetModule {

    private CloudNetDiscordBot discordBot;

    @ModuleTask(event = ModuleLifeCycle.LOADED)
    public void enableBot() {
        this.discordBot = new V3CloudNetDiscordBot() {
            @Override
            public String getVersion() {
                return CloudNet3Bot.this.getModuleConfig().getVersion() + " by " + CloudNet3Bot.this.getModuleConfig().getAuthor();
            }

            @Override
            protected CloudNet getCloudNet() {
                return CloudNet3Bot.this.getCloudNet();
            }
        };
        this.discordBot.enableBot();

        this.registerCommand(new V3CommandDiscordConsole(this.discordBot));
    }

    @ModuleTask(event = ModuleLifeCycle.STARTED)
    public void initConfig() {
        if (this.discordBot != null) {
            this.discordBot.reloadDiscordConfig();
        }
    }

    @ModuleTask(event = ModuleLifeCycle.STOPPED)
    public void disableBot() {
        this.discordBot.disableBot();
    }

}
