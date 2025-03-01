package dev.isnow.mcrekus.module.impl.deathchest.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChest;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChestModule;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathEvent extends ModuleAccessor<DeathChestModule> implements Listener {

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final List<ItemStack> drops = new ArrayList<>(event.getDrops());

        if (drops.isEmpty()) {
            return;
        }

        final Block playerBlock = event.getPlayer().getLocation().getBlock();

        playerBlock.setType(Material.CHEST);

        event.getDrops().clear();

        final DeathChest deathChest = new DeathChest(event.getPlayer().getName(), playerBlock.getLocation());
        deathChest.setup(drops);

        getModule().getDeathChests().put(RekusLocation.fromBukkitLocation(playerBlock.getLocation()), deathChest);
    }

}
