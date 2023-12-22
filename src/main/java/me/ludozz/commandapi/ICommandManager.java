package me.ludozz.commandapi;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import org.bukkit.command.Command;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class ICommandManager {

    static {
        downloadLatest();
    }

    public abstract void unregisterCommands(@NotNull Plugin plugin);

    public abstract void registerCommands(@NotNull Plugin plugin, @NotNull Command... commands);

    public abstract boolean registerCommand(@NotNull Plugin plugin, @NotNull Command commands);

    static ICommandManager getInstance(@NotNull Plugin plugin, @NotNull String requestedApiVersion) {
        return CommandManager.getInstance(plugin, requestedApiVersion);
    }

    private static void downloadLatest() {
        try {
            String downloadUrl = null;
            final String apiUrl = "https://api.github.com/repos/ludozz/CommandApi/releases/latest";
            final URL url = new URL(apiUrl);
            final URLConnection connection = url.openConnection();
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                final StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                final String jsonResponse = response.toString();
                int startIndex = jsonResponse.indexOf("browser_download_url");
                if (startIndex != -1) {
                    int endIndex = jsonResponse.indexOf("\"", startIndex + 21);
                    downloadUrl = jsonResponse.substring(startIndex + 21, endIndex);
                }
            }
            if (downloadUrl == null) {
                CommandManager.getLogger().severe("Could not download latest CommandApi version!");
                return;
            }
            final InputStream in = new URL(downloadUrl).openStream();
            Files.copy(in, Paths.get("libraries/CommandApi.jar"), StandardCopyOption.REPLACE_EXISTING);
            final URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("Paths.get(\"libraries/CommandApi.jar\")")});

        } catch (IOException ex) {
            ex.printStackTrace();
            CommandManager.getLogger().severe("Could not download latest CommandApi version!");
        }
    }


}
