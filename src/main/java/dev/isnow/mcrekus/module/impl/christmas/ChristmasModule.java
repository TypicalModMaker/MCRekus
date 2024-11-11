package dev.isnow.mcrekus.module.impl.christmas;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.christmas.config.ChristmasConfig;
import dev.isnow.mcrekus.module.impl.model.ModelModule;
import dev.isnow.mcrekus.module.impl.pumpkins.config.PumpkinsConfig;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ChristmasModule extends Module<ChristmasConfig> {
    public ChristmasModule() {
        super("Christmas", ModelModule.class);
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
