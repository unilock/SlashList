package cc.unilock.slashlist;

import cc.unilock.slashlist.config.Config;
import cc.unilock.slashlist.discord.Discord;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
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
        dependencies = {@Dependency(id = "miniplaceholders", optional = true)}
)
public final class SlashList {
    public static Config CONFIG;

    private static ProxyServer proxy;
    private static Logger logger;
    private static Discord discord;

    @Inject
    public SlashList(ProxyServer proxy, Logger logger, @DataDirectory Path path) {
        SlashList.proxy = proxy;
        SlashList.logger = logger;

        CONFIG = Config.createToml(path, "", "config", Config.class);

        if (CONFIG.discord.token.value().equals(CONFIG.discord.token.getDefaultValue()) || CONFIG.discord.guild.value().equals(CONFIG.discord.guild.getDefaultValue())) {
            SlashList.logger.error("Please configure the plugin via the config.toml file!");
        } else {
            discord = new Discord();
        }
    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent event) {
        if (discord != null) {
            discord.shutdown();
        }
    }

    public static ProxyServer getProxy() {
        return proxy;
    }

    public static Logger getLogger() {
        return logger;
    }

    public static Discord getDiscord() {
        return discord;
    }
}
