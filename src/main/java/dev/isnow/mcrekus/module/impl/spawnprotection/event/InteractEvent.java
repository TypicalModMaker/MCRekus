package dev.isnow.mcrekus.module.impl.spawnprotection.event;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawnprotection.SpawnProtectionModule;
import dev.isnow.mcrekus.module.impl.spawnprotection.command.SetupSpawnCommand;
import dev.isnow.mcrekus.module.impl.spawnprotection.config.SpawnProtectionConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.Cuboid;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InteractEvent extends ModuleAccessor<SpawnProtectionModule> implements Listener {

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final ItemStack usedItem = event.getItem();

        if (usedItem == null) return;

        final Block clickedBlock = event.getClickedBlock();

        if (clickedBlock == null) return;

        final ItemMeta itemMeta = usedItem.getItemMeta();

        if (itemMeta == null || itemMeta.displayName() == null) return;

        final Player player = event.getPlayer();

        if (!player.hasPermission("mcrekus.setupspawn")) return;

        String setupSpawnCommand = ComponentUtil.serialize(SetupSpawnCommand.SELECTION_WAND);
        String displayName = ComponentUtil.serialize(itemMeta.displayName());

        if (!setupSpawnCommand.equals(displayName)) return;

        final Action action = event.getAction();

        final Cuboid spawnCuboid = getModule().getSpawnCuboid();

        if (action == Action.LEFT_CLICK_BLOCK) {
            spawnCuboid.setFirst(event.getClickedBlock().getLocation());
            player.sendMessage(ComponentUtil.deserialize("&aFirst location set"));
        } else if (action == Action.RIGHT_CLICK_BLOCK) {
            spawnCuboid.setSecond(event.getClickedBlock().getLocation());
            player.sendMessage(ComponentUtil.deserialize("&aSecond location set"));
        }

        final SpawnProtectionConfig spawnProtectionConfig = getModule().getConfig();
        spawnProtectionConfig.setPos1(new RekusLocation(spawnCuboid.getWorld(), spawnCuboid.getXMin(), spawnCuboid.getYMin(), spawnCuboid.getZMin()));
        spawnProtectionConfig.setPos2(new RekusLocation(spawnCuboid.getWorld(), spawnCuboid.getXMax(), spawnCuboid.getYMax(), spawnCuboid.getZMax()));

        spawnProtectionConfig.save();

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) return;

        if (event.getPlayer().hasPermission("mcrekus.spawnprotection")) return;

        if (!getModule().getSpawnCuboid().isIn(event.getPlayer())) return;

        event.setCancelled(true);

        if (event.getClickedBlock() != null && event.getClickedBlock().getType().toString().contains("PLATE")) return;

        event.getPlayer().sendMessage(ComponentUtil.deserialize(getModule().getConfig().getSpawnBlockInteractMessage()));
    }
}
