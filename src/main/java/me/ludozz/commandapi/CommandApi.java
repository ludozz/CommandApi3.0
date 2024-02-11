package me.ludozz.commandapi;

import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface CommandApi {

    void unregisterCommands(@NotNull Plugin plugin);

    void registerCommands(@NotNull Plugin plugin, @NotNull Command... commands);

    boolean registerCommand(@NotNull Plugin plugin, @NotNull Command commands);

    @ApiStatus.Experimental
    void registerListener(@NotNull final Plugin plugin, @NotNull final Listener listener);

    String getApiVersion();

    static CommandApi getInstance(@NotNull Plugin plugin, @NotNull String requestedApiVersion) {
        return CommandManager.getInstance(plugin, requestedApiVersion);
    }

}
