package net.avicus.atlas;

import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandNumberFormatException;
import com.sk89q.minecraft.util.commands.CommandPermissionsException;
import com.sk89q.minecraft.util.commands.CommandUsageException;
import lombok.Getter;
import lombok.Setter;
import net.avicus.atlas.command.*;
import net.avicus.atlas.command.exception.CommandMatchException;
import net.avicus.atlas.component.AtlasComponentManager;
import net.avicus.atlas.component.dev.DebuggingComponent;
import net.avicus.atlas.component.network.AtlasQuickPlayComponent;
import net.avicus.atlas.component.network.StatusComponent;
import net.avicus.atlas.component.util.ArrowRemovalComponent;
import net.avicus.atlas.component.visual.*;
import net.avicus.atlas.external.ModuleSet;
import net.avicus.atlas.external.SetLoader;
import net.avicus.atlas.listener.AtlasListener;
import net.avicus.atlas.listener.BlockChangeListener;
import net.avicus.atlas.listener.EntityChangeListener;
import net.avicus.atlas.map.MapManager;
import net.avicus.atlas.map.rotation.RandomRotationProvider;
import net.avicus.atlas.map.rotation.Rotation;
import net.avicus.atlas.map.rotation.RotationProvider;
import net.avicus.atlas.map.rotation.XmlRotationProvider;
import net.avicus.atlas.match.Match;
import net.avicus.atlas.match.MatchFactory;
import net.avicus.atlas.match.MatchManager;
import net.avicus.atlas.module.kills.DeathMessage;
import net.avicus.atlas.module.observer.ObserverCommands;
import net.avicus.atlas.module.tutorial.TutorialModule;
import net.avicus.atlas.module.vote.VoteCommands;
import net.avicus.atlas.util.AtlasBridge;
import net.avicus.atlas.util.Events;
import net.avicus.atlas.util.Messages;
import net.avicus.atlas.util.Translations;
import net.avicus.compendium.AvicusCommandsManager;
import net.avicus.compendium.commands.AvicusCommandsRegistration;
import net.avicus.compendium.commands.exception.AbstractTranslatableCommandException;
import net.avicus.compendium.config.Config;
import net.avicus.compendium.locale.text.UnlocalizedText;
import net.avicus.compendium.settings.PlayerSettings;
import net.avicus.magma.MagmaConfig;
import net.avicus.magma.logging.ChatLogHandler;
import net.avicus.magma.restart.RestartMessageHandler;
import net.avicus.magma.util.TranslationProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Where all the action starts!
 */
public class Atlas extends JavaPlugin {

    private static Atlas instance;
    private AvicusCommandsManager commandManager;
    @Getter
    @Setter
    private AtlasBridge bridge;
    @Getter
    private MapManager mapManager;
    @Getter
    private MatchManager matchManager;

    @Getter
    private Logger mapErrorLogger;

    @Getter
    private SidebarComponent sideBar;

    @Getter
    private AtlasComponentManager componentManager;

    @Getter
    private SetLoader loader;

    @Getter
    private MatchFactory matchFactory;

    @Getter
    private AvicusCommandsRegistration registrar;

    /**
     * Returns a match if one is available right now.
     *
     * @return Current match, null if one doesn't exist
     * @throws IllegalArgumentException Thrown when a match is not available
     */
    @Nullable
    public static Match getMatch() {
        return get().getMatchManager().getRotation().getMatch();
    }

    /**
     * Accepts a consumer, gets a match and passes it to the consumer if one is
     * available.
     *
     * @param consumer Consumer that needs a {@link net.avicus.atlas.match.Match}
     */
    public static void performOnMatch(Consumer<Match> consumer) {
        Match match = getMatch();
        if (match != null) {
            consumer.accept(match);
        }
    }

    /**
     * Returns current instance of Atlas
     * @return Atlas instance
     */
    public static Atlas get() {
        return instance;
    }

    @Override
    public void onEnable() {
        // assign instance
        instance = this;

        // load configuration
        this.saveDefaultConfig();
        this.reloadConfig();

        final Config config;
        try {
            config = new Config(new FileInputStream(new File(this.getDataFolder(), "config.yml")));
        } catch (FileNotFoundException e) {
            this.getLogger().log(Level.SEVERE, "Could not load configuration", e);
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // inject configuration -> AtlasConfig
        config.injector(AtlasConfig.class).inject();

        // Set server
        this.bridge = new AtlasBridge.SimpleAtlasBridge();

        // pre-load, and setup translations (runs static block in Translations)
        if (Translations.TYPE_BOOLEAN_FALSE == TranslationProvider.$NULL$) {
            this.getLogger().severe("Failed to pre-load.");
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        // sets up map-error loggers for map related logging
        this.mapErrorLogger = Logger.getLogger("map-error");
        this.mapErrorLogger.setUseParentHandlers(false);
        this.mapErrorLogger.addHandler(new ChatLogHandler("MAPS", "atlas.maperrors"));

        this.mapManager = new MapManager();
        this.mapManager.loadLibraries(AtlasConfig.getLibraries());
        this.matchFactory = new MatchFactory();

        // loads and registers commands for usage
        this.commandManager = new AvicusCommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        this.registrar = new AvicusCommandsRegistration(this, this.commandManager);

        // load module-sets in the `module-sets` folder
        this.loader = new SetLoader(new File(this.getDataFolder(), "module-sets"));
        Bukkit.getLogger().info("Beginning external module set loading...");
        this.loader.loadModules();
        this.loader.getLoadedModules().forEach(m -> {
            // Allow fail loads
            Bukkit.getLogger().info("Loading Module Set: " + m.getDescriptionFile().getName());
            try {
                ModuleSet set = m.getModuleInstance();

                // set-up
                set.setAtlas(this);
                set.setMatchFactory(this.matchFactory);
                Logger logger = Logger.getLogger(m.getDescriptionFile().getName());
                logger.setParent(this.getLogger());
                set.setLogger(logger);
                set.setRegistrar(this.registrar);

                // life!
                set.onEnable();
                Bukkit.getLogger().info("Loaded Module Set: " + m.getDescriptionFile().getName());
            } catch (Exception e) {
                Bukkit.getLogger().info("Failed to load Module Set: " + m.getDescriptionFile().getName());
                e.printStackTrace();
            }
        });
        Bukkit.getLogger().info(
                "Finished external module set loading! Loaded " + this.loader.getLoadedModules().size()
                        + " modules!");

        // Load rotation
        final RotationProvider rotationProvider = new XmlRotationProvider(
                new File(AtlasConfig.getRotationFile()), this.mapManager, this.matchFactory);
        this.getLogger().info("Using " + rotationProvider.getClass().getName() + " rotation provider");
        Rotation rotation;
        try {
            // Defined rotation
            rotation = rotationProvider.provideRotation();
        } catch (IllegalStateException e) {
            // Not defined, random rotation
            rotation = new RandomRotationProvider(this.mapManager, this.matchFactory).provideRotation();
        }
        this.matchManager = new MatchManager(this.matchFactory, rotation);

        // Register Player Settings.
        PlayerSettings.register(DeathMessage.SETTING);
        PlayerSettings.register(TutorialModule.SHOW_TUTORIAL_SETTING);

        // Components
        this.componentManager = new AtlasComponentManager(Bukkit.getPluginManager(), this, registrar);
        this.componentManager.register(StatusComponent.class);
        this.componentManager.register(TabListComponent.class);
        this.componentManager.register(DebuggingComponent.class);
        this.componentManager
                .register(AtlasQuickPlayComponent.class, MagmaConfig.Server.QuickPlay.isEnabled());
        this.componentManager.register(MapNotificationComponent.class);
        this.componentManager.register(SidebarComponent.class);
        this.componentManager.register(VisualEffectComponent.class);
        this.componentManager.register(SoundComponent.class);
        this.componentManager.register(ArrowRemovalComponent.class);
        this.sideBar = this.componentManager.get(SidebarComponent.class);
        this.loader.getLoadedModules()
                .forEach(m -> m.getModuleInstance().onComponentsEnable(this.componentManager));
        this.componentManager.enable();

        // Listeners
        Events.register(new BlockChangeListener());
        Events.register(new EntityChangeListener());
        Events.register(new AtlasListener());

        try {
            // Start the rotation
            this.matchManager.start();
        } catch (IOException e) {
            this.getLogger().log(Level.SEVERE, "Could not start rotation", e);
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        // Register commands
        this.registerCommands(registrar);

        // Fun
        // new Friday13(this);

        // Restart
        RestartMessageHandler.RESTART_HANDLER = () -> get().getMatchManager().getRotation()
                .queueRestart();
    }

    @Override
    public void onDisable() {
        if (this.matchManager != null) {
            this.matchManager.shutdown();
        }
        if (this.componentManager != null) {
            this.componentManager.disable();
        }
        if (this.loader != null) {
            this.loader.disableAll();
        }
    }

    /**
     * Registers a list of commands in the provided registrar.
     *
     * @param registrar Registrar to register the commands in
     */
    private void registerCommands(AvicusCommandsRegistration registrar) {
        registrar.register(ChannelCommands.class);
        registrar.register(DevCommands.class);
        registrar.register(GadgetCommands.class);
        registrar.register(JoinCommands.class);
        registrar.register(RotationCommands.class);
        registrar.register(StateCommands.class);
        registrar.register(GameCommands.class);
        registrar.register(ResourcePackCommand.class);
        registrar.register(GroupCommands.GroupParentCommand.class);
        registrar.register(GenericCommands.class);

        // Modular
        registrar.register(KitCommands.class);
        registrar.register(ObserverCommands.class);
        registrar.register(VoteCommands.class);

        try {
            Class.forName("com.sk89q.worldedit.WorldEdit");
            registrar.register(WorldEditCommands.class);
        } catch (ClassNotFoundException ignored) {
            getLogger().info("WorldEdit is not enabled!");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commandManager.execute(cmd.getName(), args, sender, sender);
        } catch (AbstractTranslatableCommandException e) {
            sender.sendMessage(AbstractTranslatableCommandException.format(e));
        } catch (CommandNumberFormatException e) {
            sender.sendMessage(AbstractTranslatableCommandException
                    .error(net.avicus.compendium.plugin.Messages.ERRORS_COMMAND_NUMBER_EXPECTED,
                            new UnlocalizedText(e.getActualText())));
        } catch (CommandPermissionsException e) {
            sender.sendMessage(AbstractTranslatableCommandException
                    .error(net.avicus.compendium.plugin.Messages.ERRORS_COMMAND_NO_PERMISSION));
        } catch (CommandUsageException e) {
            sender.sendMessage(AbstractTranslatableCommandException
                    .error(net.avicus.compendium.plugin.Messages.ERRORS_COMMAND_INVALID_USAGE,
                            new UnlocalizedText(e.getUsage())));
        } catch (CommandMatchException e) {
            sender.sendMessage(AbstractTranslatableCommandException.error(Messages.ERROR_MATCH_MISSING));
        } catch (CommandException e) {
            sender.sendMessage(AbstractTranslatableCommandException
                    .error(net.avicus.compendium.plugin.Messages.ERRORS_COMMAND_INTERNAL_ERROR));
            e.printStackTrace();
        }

        return true;
    }
}
