package dev.isnow.mcrekus;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.isnow.mcrekus.command.CommandManager;
import dev.isnow.mcrekus.config.ConfigManager;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.event.LoginEvent;
import dev.isnow.mcrekus.event.QuitEvent;
import dev.isnow.mcrekus.hook.HookManager;
import dev.isnow.mcrekus.module.ModuleManager;
import dev.isnow.mcrekus.packet.RekusPacketListener;
import dev.isnow.mcrekus.util.DataUtil;
import dev.isnow.mcrekus.util.DateUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.migration.MigrationUtil;
import io.github.mqzen.menus.Lotus;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import lombok.Getter;
import lombok.Setter;
import me.tofaa.entitylib.APIConfig;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.spigot.SpigotEntityLibPlatform;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.glassfish.jaxb.runtime.v2.runtime.reflect.Lister.Pack;

@Getter@Setter
public final class MCRekus extends JavaPlugin {
    @Getter
    private static MCRekus instance;

    private HookManager hookManager;
    private ConfigManager configManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private DatabaseManager databaseManager;
    private PacketListenerCommon packetListener;

    private boolean shuttingDown;

    private ExecutorService threadPool;
    private ScheduledExecutorService scheduler;

    private Lotus menuAPI;
    private Economy economy;

    @Override
    public void onLoad() {
//        if(PacketEvents.getAPI().isLoaded() || PacketEvents.getAPI().isInitialized()) {
//            return;
//        }
        packetListener = new RekusPacketListener().asAbstract(PacketListenerPriority.NORMAL);

        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));

        PacketEvents.getAPI().load();
        PacketEvents.getAPI().getEventManager().registerListener(packetListener);
    }

    @Override
    public void onEnable() {
        final long startTime = System.currentTimeMillis();

        instance = this;
        RekusLogger.watermark();

        RekusLogger.info("Initializing config");
        configManager = new ConfigManager();

        threadPool = Executors.newFixedThreadPool(configManager.getGeneralConfig().getThreadAmount(), new ThreadFactoryBuilder().setNameFormat("mcrekus-worker-thread-%d").build());
        scheduler = Executors.newScheduledThreadPool(configManager.getGeneralConfig().getThreadAmount(), new ThreadFactoryBuilder().setNameFormat("mcrekus-scheduler-thread-%d").build());

        RekusLogger.info("Initializing command manager");
        commandManager = new CommandManager(this);

        RekusLogger.info("Loading hooks");
        hookManager = new HookManager();
        menuAPI = Lotus.load(this);
        if(hookManager.isVaultHook()) {
            economy = Bukkit.getServicesManager().getRegistration(Economy.class).getProvider();
        }

        RekusLogger.info("Initializing database connection");

        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        databaseManager = new DatabaseManager();
        Thread.currentThread().setContextClassLoader(originalClassLoader);

        if (!databaseManager.getDatabase().isConnected()) {
            RekusLogger.info("Failed to connect to the database! This plugin won't work without an database.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        } else {
            RekusLogger.info("Connected successfully.");

            if (configManager.getDatabaseConfig().isMigrate()) {
                RekusLogger.info("Starting migration");
                try {
                    MigrationUtil.migrate();
                } catch (Exception e) {
                    RekusLogger.error("Failed to migrate data: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            for(final Player player : Bukkit.getOnlinePlayers()) {
                databaseManager.preloadPlayer(player);
            }
        }

        try {
            RekusLogger.info("Initializing PacketEvents");
            PacketEvents.getAPI().init();

            SpigotEntityLibPlatform platform = new SpigotEntityLibPlatform(this);
            APIConfig settings = new APIConfig(PacketEvents.getAPI())
                    .usePlatformLogger();

            EntityLib.init(platform, settings);
        } catch (Exception e) {
            RekusLogger.error("Failed to initialize PacketEvents: " + e.getMessage());
        }

        RekusLogger.info("Loading modules");
        moduleManager = new ModuleManager(this);
        moduleManager.loadModules();

        Bukkit.getPluginManager().registerEvents(new LoginEvent(), this);
        Bukkit.getPluginManager().registerEvents(new QuitEvent(), this);

        final String date = DateUtil.formatElapsedTime((System.currentTimeMillis() - startTime));
        RekusLogger.info("Finished loading in " + date + " seconds.");
    }

    @Override
    public void onDisable() {
        final long startTime = System.currentTimeMillis();
        RekusLogger.watermark();
        shuttingDown = true;

        if (moduleManager == null) {
            return;
        }

        RekusLogger.info("Saving player data");
        for(final Player onlinePlayer : MCRekus.getInstance().getServer().getOnlinePlayers()) {
            DataUtil.saveData(onlinePlayer);
        }

        RekusLogger.info("Shutting down thread pool");
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(15000, java.util.concurrent.TimeUnit.MILLISECONDS);
        } catch (InterruptedException ignored) {}
        RekusLogger.info("Disabling modules");
        moduleManager.unloadModules();
        RekusLogger.info("Disabled modules successfully!");

        databaseManager.getDatabase().shutdown();

        RekusLogger.info("Shutting down PacketEvents");
        PacketEvents.getAPI().getEventManager().unregisterListener(packetListener);
        PacketEvents.getAPI().terminate();

        final String date = DateUtil.formatElapsedTime((System.currentTimeMillis() - startTime));
        RekusLogger.info("Finished disabling in " + date + " seconds.");
    }
}
