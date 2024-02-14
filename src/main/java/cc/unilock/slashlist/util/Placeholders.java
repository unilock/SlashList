package cc.unilock.slashlist.util;

import cc.unilock.slashlist.SlashList;
import io.github.miniplaceholders.api.MiniPlaceholders;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.NotNull;

public final class Placeholders {
    private static final boolean MINIPLACEHOLDERS_LOADED = SlashList.getProxy().getPluginManager().isLoaded("miniplaceholders");

    public static String formatPlayer(@NotNull String username) {
        final TagResolver.Builder builder = TagResolver.builder().resolvers(
                Placeholder.unparsed("username", username)
        );

        return format(builder, SlashList.CONFIG.commands.list.player_format.value());
    }

    public static String formatServerInfo(@NotNull String name, @NotNull Integer playerCount, @NotNull Integer maxPlayerCount) {
        final TagResolver.Builder builder = TagResolver.builder().resolvers(
                Placeholder.unparsed("server_name", name),
                Placeholder.unparsed("online_players", playerCount.toString()),
                Placeholder.unparsed("max_players", maxPlayerCount.toString())
        );

        return format(builder, SlashList.CONFIG.commands.list.server_format.value());
    }

    public static String formatServerEmpty() {
        return format(TagResolver.builder(), SlashList.CONFIG.commands.list.server_empty_format.value());
    }

    public static String formatServerOffline() {
        return format(TagResolver.builder(), SlashList.CONFIG.commands.list.server_offline_format.value());
    }

    public static String format(TagResolver.Builder builder, String template) {
        if (MINIPLACEHOLDERS_LOADED) {
            builder.resolver(MiniPlaceholders.getGlobalPlaceholders());
        }

        return PlainTextComponentSerializer.plainText().serialize(MiniMessage.miniMessage().deserialize(template, builder.build()));
    }
}
