package dev.isnow.mcrekus.module.impl.worldprotection.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.worldprotection.WorldProtectionModule;
import dev.isnow.mcrekus.module.impl.worldprotection.config.WorldProtectionConfig;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.land.Land;

public class FlowEvent extends ModuleAccessor<WorldProtectionModule> implements Listener {

    @EventHandler
    public void onFlow(final BlockFromToEvent event) {
        final Material material = event.getBlock().getType();

        final WorldProtectionConfig config = getModule().getConfig();

        if (material == Material.LAVA && config.isDisableLavaFlow()) {
            if(MCRekus.getInstance().getHookManager().isKingdomsHook()) {
                final Land to = Land.getLand(event.getToBlock());
                final Land from = Land.getLand(event.getBlock());

                if (to != null && to.isClaimed() && to.getKingdom() != null && from != null && from.isClaimed()) {
                    final Kingdom kingdom = to.getKingdom();
                    final Kingdom fromKingdom = from.getKingdom();
                    if (kingdom.equals(fromKingdom)) {
                        return;
                    }
                }
            }
            event.setCancelled(true);
        }

        if (material == Material.WATER && config.isDisableWaterFlow()) {
            event.setCancelled(true);
        }
    }
}
