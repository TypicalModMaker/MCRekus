package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onBlockPlace(final BlockPlaceEvent event) {
        if (!getModule().getSpawnCuboid().isIn(event.getBlockPlaced())) return;

        final Player player = event.getPlayer();

        if(player.hasPermission("mcrekus.spawnprotection")) return;

        event.setCancelled(true);

        player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnBlockPlaceMessage()));
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        if (!getModule().getSpawnCuboid().isIn(event.getBlock())) return;

        final Player player = event.getPlayer();

        if(player.hasPermission("mcrekus.spawnprotection")) return;

        event.setCancelled(true);

        player.sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnBlockBreakMessage()));
    }

    @EventHandler
    public void onBlockInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();

        if(player.hasPermission("mcrekus.spawnprotection")) return;

        if (switch (event.getAction()) {
            case LEFT_CLICK_BLOCK, RIGHT_CLICK_BLOCK -> false;
            default -> true;
        }) return;

        final Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        if(clickedBlock.getType() == Material.BARREL || clickedBlock.getType() == Material.CHEST || clickedBlock.getType() == Material.ENDER_CHEST || clickedBlock.getType() == Material.SHULKER_BOX) return;

        if (!getModule().getSpawnCuboid().isIn(clickedBlock)) return;

        event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onBucketEmpty(final PlayerBucketEmptyEvent event) {
        final Player player = event.getPlayer();

        if(player.hasPermission("mcrekus.spawnprotection")) return;

        final Block clickedBlock = event.getBlockClicked();

        if (!getModule().getSpawnCuboid().isIn(clickedBlock)) return;

        event.setCancelled(true);
    }

}
