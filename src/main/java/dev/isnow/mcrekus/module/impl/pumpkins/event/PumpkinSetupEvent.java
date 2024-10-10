package dev.isnow.mcrekus.module.impl.pumpkins.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.PumpkinData;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.pumpkins.PumpkinsModule;
import dev.isnow.mcrekus.module.impl.pumpkins.config.PumpkinsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class PumpkinSetupEvent extends ModuleAccessor<PumpkinsModule> implements Listener {


    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        if (block.getType() != Material.PLAYER_HEAD) return;

        if (!getModule().getSetupPlayers().contains(player)) return;

        final PumpkinsConfig config = getModule().getConfig();

        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1);
        player.sendMessage(ComponentUtil.deserialize(config.getSetupPumpkinPlace()));

        PumpkinData data = new PumpkinData(RekusLocation.fromBukkitLocation(block.getLocation()));

        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        databaseManager.savePumpkin(data, databaseManager.openSession());
    }

    @EventHandler
    public void onBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getBlock();

        if (block.getType() != Material.PLAYER_HEAD) return;

        if (!getModule().getSetupPlayers().contains(player)) return;

        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        databaseManager.getPumpkinAsync(RekusLocation.fromBukkitLocation(block.getLocation()), (session, pumpkinData) -> {
            if (pumpkinData == null) return;

            databaseManager.deletePumpkin(pumpkinData, session);

            final PumpkinsConfig config = getModule().getConfig();

            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 0.1F);
            player.sendMessage(ComponentUtil.deserialize(config.getSetupPumpkinRemove()));

            session.closeSession();
        });
    }


}
