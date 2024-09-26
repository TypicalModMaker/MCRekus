package dev.isnow.mcrekus.module.impl.worldprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.worldprotection.WorldProtectionModule;
import dev.isnow.mcrekus.module.impl.worldprotection.config.WorldProtectionConfig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.ThunderChangeEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherEvent extends ModuleAccessor<WorldProtectionModule> implements Listener {

    @EventHandler
    public void onWeather(final WeatherChangeEvent event) {
        final WorldProtectionConfig config = getModule().getConfig();

        if(!config.isDisableStorm() || !event.toWeatherState()) return;

        event.setCancelled(true);
    }


    @EventHandler
    public void onThunder(final ThunderChangeEvent event) {
        final WorldProtectionConfig config = getModule().getConfig();

        if(!config.isDisableThunder() || !event.toThunderState()) return;

        event.setCancelled(true);
    }
}