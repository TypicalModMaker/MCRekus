package dev.isnow.mcrekus.module.impl.xray;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.xray.config.XrayConfig;
import lombok.Getter;

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
