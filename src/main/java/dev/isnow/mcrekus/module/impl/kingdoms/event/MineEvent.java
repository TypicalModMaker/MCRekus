package dev.isnow.mcrekus.module.impl.kingdoms.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.kingdoms.KingdomsModule;
import dev.isnow.mcrekus.module.impl.kingdoms.config.KingdomsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.land.Land;
import org.kingdoms.constants.land.abstraction.KingdomItem;
import org.kingdoms.constants.land.location.SimpleLocation;
import org.kingdoms.constants.land.turrets.Turret;
import org.kingdoms.constants.player.KingdomPlayer;
import org.kingdoms.events.items.KingdomItemPlaceEvent;
import org.kingdoms.events.items.KingdomItemRemoveContext;

public class MineEvent extends ModuleAccessor<KingdomsModule> implements Listener {

    @EventHandler
    public void onBlockPlace(final KingdomItemPlaceEvent<? extends KingdomItem<?>> event) {
        if(event.getBukkitEvent() == null) return;

        final BlockPlaceEvent blockPlaceEvent = (BlockPlaceEvent) event.getBukkitEvent();
        RekusLogger.debug("BlockPlaceEvent");


        final Player player = blockPlaceEvent.getPlayer();
        if (player.hasPermission("mcrekus.mine.bypass")) return;

        final KingdomPlayer kingdomPlayer = event.getPlayer();
        if (kingdomPlayer == null || kingdomPlayer.getKingdom() == null || !kingdomPlayer.getKingdom().isPacifist()) return;

        final ItemStack item = blockPlaceEvent.getItemInHand();
        final Component displayName = item.getItemMeta().displayName();
        if (item.getItemMeta() == null || displayName == null) return;

        final String serialized = ComponentUtil.serialize(displayName);
        if (!serialized.contains("Mina")) return;

        final KingdomsConfig config = getModule().getConfig();
        player.sendMessage(ComponentUtil.deserialize(config.getCantPlaceMinesMessage()));

        event.setCancelled(true);

                                // FUCK KINGDOMS
        kingdomPlayer.getKingdom().getKingdom()             .getKingdom()
        .getKingdom().getKingdom().getKingdom()             .getKingdom()
                                  .getKingdom()             .getKingdom()
                                  .getKingdom()             .getKingdom()
                             	  .getKingdom()             .getKingdom()
        .getKingdom().getKingdom().getKingdom().getKingdom().getKingdom()
        .getKingdom().getKingdom().getKingdom().getKingdom().getKingdom()
        .getKingdom()             .getKingdom()
        .getKingdom()             .getKingdom()
        .getKingdom()             .getKingdom()
        .getKingdom()             .getKingdom().getKingdom().getKingdom()
        .getKingdom()             .getKingdom().getKingdom().getKingdom();


        new BukkitRunnable() {
            @Override
            public void run() {
                final Turret turret = getTurretAtLocation(kingdomPlayer.getKingdom(), event.getKingdomItem().getLocation());

                if (turret != null) {
                    final KingdomItemRemoveContext context = new KingdomItemRemoveContext();
                    context.setPlaySound(false);
                    context.setDropsItem(false);

                    turret.remove(context);
                }
            }
        }.runTaskLater(MCRekus.getInstance(), 2L);
    }

    private Turret getTurretAtLocation(final Kingdom kingdom, final SimpleLocation location) {
        Turret foundTurret = null;

        for(final Land land : kingdom.getLands()) {
            if (land.getTurrets().containsKey(location)) {
                foundTurret = land.getTurrets().get(location);
            }
        }

        return foundTurret;

    }
}
