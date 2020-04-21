package net.avicus.magma;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.avicus.libraries.quest.database.DatabaseConfig;
import net.avicus.magma.api.APIClient;
import net.avicus.magma.command.AbortCmd;
import net.avicus.magma.database.Database;
import net.avicus.magma.network.NetworkConstants;
import net.avicus.magma.redis.Redis;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class Magma extends Plugin implements Listener {

    private static Magma magma;
    @Getter
    private Configuration configuration;
    @Getter
    private Database database;
    @Getter
    private Redis redis;
    @Getter
    private APIClient apiClient;

    public Magma() {
        magma = this;
    }

    public static Magma get() {
        return magma;
    }

    @Override
    public void onEnable() {
        try {
            this.configuration = ConfigurationProvider
                    .getProvider(YamlConfiguration.class).load(new File(getDataFolder(), "config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
            ProxyServer.getInstance().stop();
        }

        getLogger().info("Connecting to Redis...");
        final Redis.Builder builder = Redis.builder(this.configuration.getString("redis.hostname"))
                .database(this.configuration.getInt("redis.database"));
        if (this.configuration.getBoolean("redis.auth.enabled")) {
            builder.password(this.configuration.getString("redis.auth.password"));
        }
        this.redis = builder.build();
        try {
            this.redis.enable();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            ProxyServer.getInstance().stop();
            return;
        }
        getLogger().info("Connected to Redis!");

        getLogger().info("Connecting to API...");
        try {
            this.apiClient = new APIClient(this.configuration.getString("api.url"),
                    this.configuration.getString("api.key"));
        } catch (Exception e) {
            getLogger().severe("Failed to connect to API!");
            e.printStackTrace();
            ProxyServer.getInstance().stop();
            return;
        }
        getLogger().info("Connected to API!");

        getLogger().info("Connecting to database...");
        this.database = new Database(DatabaseConfig.builder(
                this.configuration.getString("database.hostname"),
                this.configuration.getString("database.database"),
                this.configuration.getString("database.auth.username"),
                this.configuration.getString("database.auth.password")
        ).embedded(this.configuration.getBoolean("database.embedded", false)).reconnect(true).build());
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.database.enable();
        } catch (Exception e) {
            e.printStackTrace();

        }
        getLogger().info("Connected to database!");

        this.getProxy().registerChannel(NetworkConstants.CONNECT_CHANNEL);
        this.getProxy().getPluginManager().registerListener(this, this);

        new ServerTask(this, this.database).start();

        PluginManager manager = getProxy().getPluginManager();

        // no /end!
        manager.unregisterCommands(null);

        manager.registerCommand(this, new AbortCmd());

        manager.registerListener(this, new RegistrationListener(this.database));
        manager.registerListener(this, new KickListener(this));
        manager.registerListener(this, new MotdListener(this, this.database).start());
        manager.registerListener(this, new StatusCheckTask(this, this.database).start());
    }

    @EventHandler
    public void message(final PluginMessageEvent event) {
        if (!event.getTag().equals(NetworkConstants.CONNECT_CHANNEL)) {
            return;
        }
        final ByteArrayDataInput input = ByteStreams.newDataInput(event.getData());
        final ProxiedPlayer player = this.getProxy().getPlayer(input.readUTF());
        if (player == null) {
            return;
        }
        final String server = input.readUTF();
        if (server.equals(NetworkConstants.LOBBY_SERVER)) {
            this.getLobby(player).ifPresent(player::connect);
        } else {
            Optional.ofNullable(this.getProxy().getServerInfo(server)).ifPresent(player::connect);
        }
    }

    public Optional<ServerInfo> getLobby(final ProxiedPlayer player) {
        return this.getLobby(player, Collections.emptySet(), Collections.emptySet());
    }

    public Optional<ServerInfo> getLobby(final ProxiedPlayer player, final Collection<String> skipIn,
                                         final Collection<String> skipOut) {
        final List<String> servers = new ArrayList<>(
                player.getPendingConnection().getListener().getServerPriority());
        servers.removeAll(skipIn);
        return servers.stream()
                .map(name -> {
                    final ServerInfo server = this.getProxy().getServerInfo(name);
                    if (server == null) {
                        skipOut.add(name);
                    }
                    return server;
                })
                .filter(Objects::nonNull)
                .findFirst();
    }
}
