package cc.unilock.slashlist.discord;

import cc.unilock.slashlist.SlashList;
import cc.unilock.slashlist.discord.command.ICommand;
import cc.unilock.slashlist.discord.command.ListCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class Discord extends ListenerAdapter {
    private final JDA jda;
    private final Map<String, ICommand> commands = new HashMap<>();

    public Discord() {
        commands.put("list", new ListCommand());

        var builder = JDABuilder.createDefault(SlashList.CONFIG.discord.token.value()).addEventListeners(this);

        try {
            this.jda = builder.build();
        } catch (Exception e) {
            SlashList.getLogger().error("Failed to log into Discord! " + e);
            throw new RuntimeException("Failed to log into Discord!", e);
        }
    }

    @Override
    public void onReady(@Nonnull ReadyEvent event) {
        SlashList.getLogger().info("Bot ready!");

        var guild = jda.getGuildById(SlashList.CONFIG.discord.guild.value());

        if (guild == null) {
            SlashList.getLogger().error("Invalid guild ID!");
            throw new RuntimeException("Invalid guild ID!");
        }

        for (ICommand command : commands.values()) {
            guild.upsertCommand(command.getName(), command.getDescription()).queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {
        var command = event.getName();

        if (!commands.containsKey(command)) {
            return;
        }

        commands.get(command).handle(event);
    }

    public void shutdown() {
        jda.shutdown();
    }
}
