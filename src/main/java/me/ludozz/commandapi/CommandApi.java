package me.ludozz.commandapi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.command.Command;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

@SuppressWarnings("unused")
public abstract class CommandApi {

    static {
        downloadLatest();
    }

    public abstract void unregisterCommands(@NotNull Plugin plugin);

    public abstract void registerCommands(@NotNull Plugin plugin, @NotNull Command... commands);

    public abstract boolean registerCommand(@NotNull Plugin plugin, @NotNull Command commands);

    @ApiStatus.Experimental
    public abstract void registerListener(@NotNull final Plugin plugin,
                                          @NotNull final Listener listener);

    public abstract String getApiVersion();

    public static CommandApi getInstance(@NotNull Plugin plugin, @NotNull String requestedApiVersion) {
        return CommandManager.getInstance(plugin, requestedApiVersion);
    }

    private static void downloadLatest() {
        try {
            String downloadUrl = getUrl();
            if (downloadUrl == null) {
                CommandManager.getLogger().severe("Could not download latest CommandApi version!");
                return;
            }
            final InputStream in = new URL(downloadUrl).openStream();
            Files.copy(in, Paths.get("libraries/CommandApi.jar"), StandardCopyOption.REPLACE_EXISTING);
            loadClassesFromJar("libraries/CommandApi.jar");
        } catch (IOException ex) {
            ex.printStackTrace();
            CommandManager.getLogger().severe("Could not download latest CommandApi version!");
        }
    }

    private static String getUrl() throws IOException {
        final String apiUrl = "https://api.github.com/repos/ludozz/CommandApi/releases/latest";
        final URL url = new URL(apiUrl);
        final URLConnection connection = url.openConnection();
        connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
        final JsonObject jsonResponds = new Gson().fromJson(new InputStreamReader(connection.getInputStream()),
                JsonObject.class);
        final JsonArray assets = jsonResponds.get("assets").getAsJsonArray();
        return assets.get(0).getAsJsonObject().get("browser_download_url").getAsString();
    }

    private static void loadClassesFromJar(String jarFilePath) throws IOException {
        final File file = new File(jarFilePath);
        try (JarFile jarFile = new JarFile(file)) {
            final URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});
            final Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                final JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.')
                            .replaceAll(".class$", "");
                    try {
                        classLoader.loadClass(className);
                        Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
