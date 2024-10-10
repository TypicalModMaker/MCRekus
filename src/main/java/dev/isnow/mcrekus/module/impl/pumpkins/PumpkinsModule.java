package dev.isnow.mcrekus.module.impl.pumpkins;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.kingdoms.champion.Champion;
import dev.isnow.mcrekus.module.impl.kingdoms.config.KingdomsConfig;
import dev.isnow.mcrekus.module.impl.pumpkins.config.PumpkinsConfig;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public class PumpkinsModule extends Module<PumpkinsConfig> {

    private final List<Player> setupPlayers = new ArrayList<>();

    public PumpkinsModule() {
        super("Pumpkins");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        registerListeners("event");
        registerCommands("commands");
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterListeners();
        unRegisterCommands();
    }
}
