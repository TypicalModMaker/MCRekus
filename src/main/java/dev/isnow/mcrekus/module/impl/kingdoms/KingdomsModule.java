package dev.isnow.mcrekus.module.impl.kingdoms;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.kingdoms.champion.Champion;
import dev.isnow.mcrekus.module.impl.kingdoms.config.KingdomsConfig;
import dev.isnow.mcrekus.module.impl.kingdoms.placeholderapi.KingdomsNameExtension;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import lombok.Getter;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftMinecart;

@Getter
public class KingdomsModule extends Module<KingdomsConfig> {

    private ExecutorService entityThreadPool;

    private final HashMap<Integer, Champion> champions = new HashMap<>();

    public KingdomsModule() {
        super("Kingdoms");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        if (!plugin.getHookManager().isKingdomsHook()) {
            plugin.getLogger().warning("Kingdoms hook is not enabled, this module will not work.");
            return;
        }

        if (plugin.getHookManager().isPlaceholerAPIHook()) {
            new KingdomsNameExtension().register();
        }

        entityThreadPool = Executors.newFixedThreadPool(3, new ThreadFactoryBuilder().setNameFormat("mcrekus-entity-thread-%d").build());

        registerListeners("event");
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterListeners();

        entityThreadPool.shutdownNow();
    }
}
