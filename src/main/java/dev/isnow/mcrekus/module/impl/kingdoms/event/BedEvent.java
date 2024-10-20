package dev.isnow.mcrekus.module.impl.kingdoms.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.kingdoms.KingdomsModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.damagesource.DamageSources;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.player.KingdomPlayer;

public class BedEvent extends ModuleAccessor<KingdomsModule> implements Listener {

    private List<RekusLocation> bedLocations = new ArrayList<>();

    @EventHandler
    public void onBedExplode(final EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player)) return;

        if (event.getDamageSource() == null || event.getDamageSource().getSourceLocation() == null) return;

        final RekusLocation loc = RekusLocation.fromBukkitLocationTrimmed(event.getDamageSource().getSourceLocation());

        if (bedLocations.contains(loc)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBedClick(final PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        final Player player = event.getPlayer();

        if (player.hasPermission("mcrekus.netherbed.bypass")) return;

        if (!player.getLocation().getWorld().getName().equalsIgnoreCase("earthsmp_nether")) return;

        if (!Tag.BEDS.isTagged(event.getClickedBlock().getType())) return;

        final KingdomPlayer kingdomPlayer = KingdomPlayer.getKingdomPlayer(player.getUniqueId());

        if(kingdomPlayer.getKingdom() == null || !kingdomPlayer.getKingdom().isPacifist()) return;


        final Location bukkitLoc = event.getClickedBlock().getLocation();

        final ArrayList<RekusLocation> addLocations = new ArrayList<>();

        for(int x = -1; x <= 1; x++) {
            for(int z = -1; z <= 1; z++) {
                if(!Tag.BEDS.isTagged(player.getWorld().getBlockAt((int) (bukkitLoc.getX() + x), (int) bukkitLoc.getY(), (int) (bukkitLoc.getZ() + z)).getType())) {
                    continue;
                }

                final RekusLocation loc2 = new RekusLocation(null, bukkitLoc.getX() + x, bukkitLoc.getY(), bukkitLoc.getZ() + z);
                addLocations.add(loc2);
            }
        }

        bedLocations.addAll(addLocations);

        new BukkitRunnable() {
            @Override
            public void run() {
                bedLocations.removeAll(addLocations);
            }
        }.runTaskLaterAsynchronously(MCRekus.getInstance(), 20L);
    }
}
