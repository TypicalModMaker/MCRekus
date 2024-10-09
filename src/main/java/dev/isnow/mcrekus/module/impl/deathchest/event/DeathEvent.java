package dev.isnow.mcrekus.module.impl.deathchest.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChest;
import dev.isnow.mcrekus.module.impl.deathchest.DeathChestModule;
import dev.isnow.mcrekus.module.impl.deathchest.config.DeathChestConfig;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class DeathEvent extends ModuleAccessor<DeathChestModule> implements Listener {

    @EventHandler
    public void onDeath(final PlayerDeathEvent event) {
        final List<ItemStack> drops = event.getDrops();

        if (drops.isEmpty()) {
            return;
        }

        final Block playerBlock = event.getPlayer().getLocation().getBlock();

        playerBlock.setType(Material.CHEST);
        final Chest chest = (Chest) playerBlock.getState();

        for (final ItemStack drop : drops) {
            if (drop == null) {
                continue;
            }

            chest.getInventory().addItem(drop);
        }

        event.getDrops().clear();

        final DeathChest deathChest = new DeathChest(event.getPlayer().getName(), playerBlock.getLocation());
        deathChest.setup();

        getModule().getDeathChests().put(RekusLocation.fromBukkitLocation(playerBlock.getLocation()), deathChest);
    }

}
