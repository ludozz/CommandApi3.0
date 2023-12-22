package me.ludozz.commandapi;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import me.ludozz.commandapi.brigadier.ArgumentWrapper;
import me.ludozz.commandapi.brigadier.CommandSyntaxException;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.common.util.report.qual.ReportOverride;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@SuppressWarnings("unused")
public interface Executable {

    Executor asyncExecutor = Executors.newSingleThreadExecutor(
            new ThreadFactoryBuilder().setNameFormat("command-executor-thread-%d").build());

    void executeCommand(@NotNull CommandSender sender, @NotNull String alias, @NotNull ArgumentWrapper[] args)
            throws CommandSyntaxException;

    /**Always use this method to execute a command!!! VVV**/
    @ReportOverride
    default void execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull ArgumentWrapper[] args) throws CommandSyntaxException {
        final CompletableFuture<Void> throwableCatcher = new CompletableFuture<>();
        if (isAsync()) {
            asyncExecutor.execute(() -> {
                try {
                    if (!hasPermission(sender)) return;
                    executeCommand(sender, alias, args);
                } catch (Throwable ex) {
                    final List<String> arguments = new ArrayList<>();
                    arguments.add(alias);
                    for (ArgumentWrapper arg : args) {
                        arguments.add(arg.getArgument());
                    }
                    throw new CommandException("Unhandled exception executing '" + (sender instanceof Player ? "/" : "")
                            + String.join(" ", arguments) + "' in " + this, ex);
                }
            });
            try {
                throwableCatcher.get();
            } catch (ExecutionException ex) {
                if (ex.getCause() instanceof CommandSyntaxException) {
                    throw (CommandSyntaxException) ex.getCause();
                }
                if (ex.getCause() instanceof CommandException) {
                    throw (CommandException) ex.getCause();
                }
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
            return;
        }
        executeCommand(sender, alias, args);
    }



    boolean hasPermission(@NotNull CommandSender sender);

    boolean hasPermissionSilent(@NotNull CommandSender sender);

    @ApiStatus.Experimental
    default boolean isAsync() {
        return false;
    }

}
