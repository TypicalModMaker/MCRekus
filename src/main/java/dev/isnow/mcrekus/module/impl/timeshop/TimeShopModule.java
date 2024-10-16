package dev.isnow.mcrekus.module.impl.timeshop;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.timeshop.config.TimeShopConfig;
import dev.isnow.mcrekus.util.RekusLogger;
import java.util.HashMap;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Getter
public class TimeShopModule extends Module<TimeShopConfig> {
    private final HashMap<Player, Long> joinTime = new HashMap<>();

    public TimeShopModule() {
        super("TimeShop");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        for(final Player player : Bukkit.getOnlinePlayers()) {
            joinTime.put(player, System.currentTimeMillis());
        }

        registerListeners("event");
        registerCommands("commands");
    }

    @Override
    public void onDisable(final MCRekus plugin) {

        unRegisterListeners();
        unRegisterCommands();
    }
}
