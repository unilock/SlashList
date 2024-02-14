package cc.unilock.slashlist.config;

import folk.sisby.kaleido.api.ReflectiveConfig;
import folk.sisby.kaleido.lib.quiltconfig.api.annotations.Comment;
import folk.sisby.kaleido.lib.quiltconfig.api.values.TrackedValue;
import folk.sisby.kaleido.lib.quiltconfig.api.values.ValueList;

public class Config extends ReflectiveConfig {
    @Comment("Excluded servers")
    public final TrackedValue<ValueList<String>> excluded_servers = value(ValueList.create("", ""));

    @Comment("Settings related to Discord")
    public final Discord discord = new Discord();

    @Comment("Settings related to commands")
    public final Commands commands = new Commands();

    public static class Discord extends Section {
        @Comment("Bot token")
        public final TrackedValue<String> token = value("NULL");

        @Comment("Guild ID")
        public final TrackedValue<String> guild = value("NULL");
    }

    public static class Commands extends Section {
        @Comment("Settings related to /list")
        public final ListCommand list = new ListCommand();

        public static class ListCommand extends Section {
            @Comment("Whether the command should be ephemeral")
            public final TrackedValue<Boolean> ephemeral = value(false);

            @Comment("Code block language")
            public final TrackedValue<String> codeblock_lang = value("asciidoc");

            @Comment("Server format")
            public final TrackedValue<String> server_format = value("[<server_name>] <online_players>/<max_players>");

            @Comment("Server offline format")
            public final TrackedValue<String> server_offline_format = value("Server offline");

            @Comment("Server empty format")
            public final TrackedValue<String> server_empty_format = value("No players online");

            @Comment("Player format")
            public final TrackedValue<String> player_format = value("- <username>");
        }
    }
}
