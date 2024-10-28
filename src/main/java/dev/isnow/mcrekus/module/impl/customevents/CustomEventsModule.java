package dev.isnow.mcrekus.module.impl.customevents;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.customevents.config.CustomEventsConfig;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEventManager;
import dev.isnow.mcrekus.module.impl.customevents.placeholder.CustomEventsExtension;
import java.util.List;
import lombok.Getter;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

@Getter
public class CustomEventsModule extends Module<CustomEventsConfig> {
    private CustomEventManager customEventManager;

    public CustomEventsModule() {
        super("CustomEvents");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        customEventManager = new CustomEventManager();
        customEventManager.setup();

        registerCommands("command");
        registerListeners("bukkitevent");

        if(MCRekus.getInstance().getHookManager().isPlaceholerAPIHook()) {
            new CustomEventsExtension().register();
        }
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterCommands();
        unRegisterListeners();

        if (customEventManager.getCurrentEvent() != null) {
            customEventManager.getCurrentEvent().stop();
        }

        customEventManager.getEventScheduler().cancel();
        customEventManager.getScore().clear();

    }
}
