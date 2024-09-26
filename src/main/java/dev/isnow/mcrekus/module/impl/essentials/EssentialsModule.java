package dev.isnow.mcrekus.module.impl.essentials;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.data.EssentialsPlayerData;
import dev.isnow.mcrekus.module.impl.essentials.message.MessageManager;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import lombok.Getter;

@Getter
public class EssentialsModule extends Module<EssentialsConfig> {

    private final TeleportManager teleportManager = new TeleportManager();
    private final MessageManager messageManager = new MessageManager();

    public EssentialsModule() {
        super("Essentials", EssentialsPlayerData.class);
    }

    @Override
    public void onEnable(MCRekus plugin) {

        registerListeners("event");
        registerCommands("command");
    }

    @Override
    public void onDisable(MCRekus plugin) {
        unRegisterCommands();
    }
}
