package net.avicus.hook.backend;

import net.avicus.compendium.config.Config;
import net.avicus.compendium.config.ConfigFile;
import net.avicus.hook.DbConfig;
import net.avicus.hook.HookConfig;
import net.avicus.hook.HookPlugin;
import net.avicus.hook.backend.buycraft.BuycraftDispatcher;
import net.avicus.hook.backend.buycraft.BuycraftTask;
import net.avicus.hook.backend.leaderboard.LeaderboardTask;
import net.avicus.hook.backend.leaderboard.XPLeaderboardTask;
import net.avicus.hook.backend.votes.Votes;
import net.avicus.libraries.quest.database.DatabaseConfig;
import net.avicus.magma.Magma;
import net.avicus.magma.database.Database;
import net.buycraft.plugin.platform.standalone.runner.StandaloneBuycraftRunnerBuilder;
import net.buycraft.plugin.platform.standalone.runner.StandaloneUtilities;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class Backend {

    private final Logger log;

    public Backend(HookPlugin plugin) {
        this.log = plugin.getLogger();
    }

    public static void init(HookPlugin plugin) {
        if (!HookConfig.isBackend()) {
            return;
        }

        File remote = new File(plugin.getDataFolder(), "backend.yml");
        InputStream local = plugin.getResource("backend.yml");

        Config config = new Config(local);

        if (!remote.exists()) {
            config.save(remote);
        }
        config = new ConfigFile(remote);

        config.injector(BackendConfig.class).inject();

        Backend backend = new Backend(plugin);
        backend.start();
    }

    private Database connectDatabase(DatabaseConfig config) {
        this.log.info("Establishing new database connection...");
        Database database = new Database(config);
        database.connect();
        this.log.info("Connected.");
        return database;
    }

    public void start() {
        // Run backend in the background.
        this.log.info("This server is a backend server.");

        Runnable backendTask = new Runnable() {
            @Override
            public void run() {
                try {
                    boolean allDone = true;

                    for (Thread thread : runBackEnd()) {
                        if (thread.isAlive()) {
                            allDone = false;
                            break;
                        }
                    }
                } catch (Exception e) {
                    Backend.this.log.info("A backend task failed. Trying again.");
                    e.printStackTrace();
                }
            }
        };

        Bukkit.getScheduler().runTaskTimerAsynchronously(HookPlugin.getInstance(), backendTask, 0, 120 * 60); // immediately, then after every 2 minutes
    }

    public List<Thread> runBackEnd() {
        this.log.info("Backend starting up!");

        List<Thread> runningTasks = new ArrayList<>();

        // Leaderboards
        if (BackendConfig.Leaderboards.isEnabled()) {
            this.log.info("Enabling Leaderboards");
            Database database = connectDatabase(DbConfig.MySQLConfig.create());

            LeaderboardTask task = new LeaderboardTask(database);
            task.start();
            runningTasks.add(task);
        }

        // XP Leaderboard
        if (BackendConfig.Leaderboards.isXpEnabled() && Magma.get().getCurrentSeason() != null) {
            this.log.info("Enabling XP Leaderboards");
            Database database = connectDatabase(DbConfig.MySQLConfig.create());

            XPLeaderboardTask task = new XPLeaderboardTask(database);
            task.start();
            runningTasks.add(task);
        }

        // Buycraft
        if (BackendConfig.Buycraft.isEnabled()) {
            this.log.info("Enabling Buycraft");
            Database database = connectDatabase(DbConfig.MySQLConfig.create());

            // Handles incoming commands
            BuycraftDispatcher dispatcher = new BuycraftDispatcher(database);

            // Performs command fetching
            StandaloneBuycraftRunnerBuilder builder = StandaloneBuycraftRunnerBuilder.builder()
                    .apiKey(BackendConfig.Buycraft.getApiKey())
                    .determiner(StandaloneUtilities.ALWAYS_OFFLINE_PLAYER_DETERMINER)
                    .dispatcher(dispatcher)
                    .executorService(Executors.newScheduledThreadPool(BackendConfig.Buycraft.getPoolSize()))
                    .logger(this.log)
                    .build();

            BuycraftTask task = new BuycraftTask(builder);

            long period = BackendConfig.Buycraft.getPeriod();

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(task, 0, period);
        }

        // VOTES!
        Votes.init();

        this.log.info("Backend started up.");

        return runningTasks;
    }
}
