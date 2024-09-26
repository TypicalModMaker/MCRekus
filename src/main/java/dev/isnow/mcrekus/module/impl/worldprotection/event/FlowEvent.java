package dev.isnow.mcrekus.module.impl.worldprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.worldprotection.WorldProtectionModule;
import dev.isnow.mcrekus.module.impl.worldprotection.config.WorldProtectionConfig;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

public class FlowEvent extends ModuleAccessor<WorldProtectionModule> implements Listener {

    @EventHandler
    public void onFlow(final BlockFromToEvent event) {
        final Material material = event.getBlock().getType();

        final WorldProtectionConfig config = getModule().getConfig();

        if (material == Material.LAVA && config.isDisableLavaFlow()) {
            event.setCancelled(true);
        }

        if (material == Material.WATER && config.isDisableWaterFlow()) {
            event.setCancelled(true);
        }
    }
}
