package dev.isnow.mcrekus.module.impl.xray;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.menu.InvseeMenu;
import dev.isnow.mcrekus.module.impl.essentials.message.MessageManager;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.module.impl.xray.config.XrayConfig;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class XrayModule extends Module<XrayConfig> {

    public XrayModule() {
        super("Xray");
    }

    @Override
    public void onEnable(MCRekus plugin) {
        registerCommands("command");
        registerListeners("event");
    }

    @Override
    public void onDisable(MCRekus plugin) {
        unRegisterCommands();
    }
}
