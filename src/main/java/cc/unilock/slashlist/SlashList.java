package cc.unilock.slashlist;

import cc.unilock.discordlib.DiscordLib;
import cc.unilock.slashlist.config.Config;
import cc.unilock.slashlist.discord.command.ListCommand;
import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "slashlist",
        name = "SlashList",
        description = "/list",
        version = Tags.VERSION,
        authors = {"unilock"},
        dependencies = {
                @Dependency(id = "discordlib"),
                @Dependency(id = "miniplaceholders", optional = true)
        }
)
public final class SlashList {
    public static Config CONFIG;

    private static ProxyServer proxy;
    private static Logger logger;

    @Inject
    public SlashList(ProxyServer proxy, Logger logger, @DataDirectory Path path) {
        SlashList.proxy = proxy;
        SlashList.logger = logger;

        CONFIG = Config.createToml(path, "", "config", Config.class);

        if (DiscordLib.getDiscord() != null) {
            DiscordLib.getDiscord().registerCommand(new ListCommand());
        }
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    public static Logger getLogger() {
        return logger;
    }
}
