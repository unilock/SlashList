package cc.unilock.slashlist.discord.command;

import cc.unilock.discordlib.discord.command.ICommand;
import cc.unilock.discordlib.lib.net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import cc.unilock.slashlist.SlashList;
import cc.unilock.slashlist.util.Placeholders;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerPing;

import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class ListCommand implements ICommand {
    private final HashMap<RegisteredServer, Integer> serverMaxPlayers = new HashMap<>();

    public ListCommand() {

    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "list players";
    }

    @Override
    public void handle(SlashCommandInteraction interaction) {
        interaction.deferReply().setEphemeral(SlashList.CONFIG.commands.list.ephemeral.value()).queue();

        final var servers = SlashList.getProxy().getAllServers();
        updateMaxPlayers(servers);

        final var sb = new StringBuilder();

        sb.append("```").append(SlashList.CONFIG.commands.list.codeblock_lang.value()).append('\n');
        for (var server : servers) {
            var name = server.getServerInfo().getName();

            if (SlashList.CONFIG.excluded_servers.value().contains(name)) {
                continue;
            }

            var players = server.getPlayersConnected();

            var playerCount = players.size();
            var maxPlayerCount = serverMaxPlayers.get(server);

            var serverInfo = Placeholders.formatServerInfo(name, playerCount, maxPlayerCount);
            sb.append(serverInfo).append('\n');

            if (maxPlayerCount == 0) {
                sb.append(Placeholders.formatServerOffline()).append('\n');
            } else if (playerCount == 0) {
                sb.append(Placeholders.formatServerEmpty()).append('\n');
            } else {
                for (var player : players) {
                    var playerName = Placeholders.formatPlayer(player.getUsername());
                    sb.append(playerName).append('\n');
                }
            }

            sb.append('\n');
        }
        sb.append("```");

        interaction.getHook().editOriginal(sb.toString()).queue();
    }

    private void updateMaxPlayers(Collection<RegisteredServer> servers) {
        CompletableFuture.allOf(servers.parallelStream().map((server) -> server.ping().handle((ping, e) -> handlePing(server, ping, e))).toArray(CompletableFuture[]::new)).join();
    }

    private CompletableFuture<Void> handlePing(RegisteredServer server, ServerPing ping, Throwable e) {
        if (e != null) {
            SlashList.getLogger().warn("Could not ping server: " + e);
            serverMaxPlayers.put(server, 0);
        } else {
            serverMaxPlayers.put(server, ping.getPlayers().map(ServerPing.Players::getMax).orElse(0));
        }

        return null;
    }
}
