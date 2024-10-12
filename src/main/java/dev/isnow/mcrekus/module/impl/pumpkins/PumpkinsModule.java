package dev.isnow.mcrekus.module.impl.pumpkins;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.pumpkins.config.PumpkinsConfig;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.entity.Player;

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
