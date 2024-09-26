package dev.isnow.mcrekus;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.isnow.mcrekus.command.CommandManager;
import dev.isnow.mcrekus.config.ConfigManager;
import dev.isnow.mcrekus.data.PlayerDataManager;
import dev.isnow.mcrekus.event.LoginEvent;
import dev.isnow.mcrekus.hook.HookManager;
import dev.isnow.mcrekus.module.ModuleManager;
import dev.isnow.mcrekus.util.DateUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import io.github.mqzen.menus.Lotus;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter@Setter
public final class MCRekus extends JavaPlugin {
    @Getter
    private static MCRekus instance;

    private HookManager hookManager;
    private ConfigManager configManager;
    private ModuleManager moduleManager;
    private CommandManager commandManager;
    private PlayerDataManager playerDataManager;

    private boolean shuttingDown;

    private ExecutorService threadPool;
    private Lotus menuAPI;

    @Override
    public void onEnable() {
        final long startTime = System.currentTimeMillis();

        instance = this;
        RekusLogger.watermark();

        Bukkit.getPluginManager().registerEvents(new LoginEvent(), this);
        RekusLogger.info("Initializing config");
        configManager = new ConfigManager();

        playerDataManager = new PlayerDataManager();

        threadPool = Executors.newFixedThreadPool(configManager.getGeneralConfig().getThreadAmount(), new ThreadFactoryBuilder().setNameFormat("mcrekus-worker-thread-%d").build());

        RekusLogger.info("Initializing command manager");
        commandManager = new CommandManager(this);

        RekusLogger.info("Loading hooks");
        hookManager = new HookManager();
        menuAPI = Lotus.load(this);

        RekusLogger.info("Loading modules");
        moduleManager = new ModuleManager(this);
        moduleManager.loadModules();

        final String date = DateUtil.formatElapsedTime((System.currentTimeMillis() - startTime));
        RekusLogger.info("Finished loading in " + date + " seconds.");
    }

    @Override
    public void onDisable() {
        final long startTime = System.currentTimeMillis();
        RekusLogger.watermark();
        shuttingDown = true;

        RekusLogger.info("Disabling modules");
        moduleManager.unloadModules();
        RekusLogger.info("Disabled modules successfully!");

        RekusLogger.info("Saving player data");
        playerDataManager.saveAll();

        threadPool.shutdownNow();

        final String date = DateUtil.formatElapsedTime((System.currentTimeMillis() - startTime));
        RekusLogger.info("Finished disabling in " + date + " seconds.");
    }
}
