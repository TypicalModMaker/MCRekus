package dev.isnow.mcrekus.module.impl.model.util;

import com.github.retrooper.packetevents.protocol.entity.type.EntityType;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetPassengers;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.util.RekusLogger;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import me.tofaa.entitylib.EntityLib;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class MountableWrappedEntity extends WrapperEntity {

    private final Player mountedPlayer;

    public MountableWrappedEntity(final EntityType entityType) {
        super(entityType);

        mountedPlayer = null;
    }

    public MountableWrappedEntity(final EntityType entityType, final Player mountedPlayer) {
        super(entityType);

        this.mountedPlayer = mountedPlayer;
    }

    public boolean spawn(final Location location, final Player player) {
        final boolean spawned = this.spawn(SpigotConversionUtil.fromBukkitLocation(location), EntityLib.getApi().getDefaultContainer());

        if(spawned && player != null) {
            addViewer(player.getUniqueId());
        }

        return spawned;
    }

    @Override
    public void addViewer(UUID uuid) {
        if (viewers.add(uuid)) {
            if (location == null) {
                if (EntityLib.getApi().getSettings().isDebugMode()) {
                    EntityLib.getPlatform().getLogger().warning("Location is null for entity " + this.getEntityId() + ". Cannot spawn.");
                }
            } else {
                if (isSpawned()) {
                    sendPacket(uuid, createSpawnPacket());
                    sendPacket(uuid, getEntityMeta().createPacket());
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (mountedPlayer != null && mountedPlayer.isOnline()) {
                                RekusLogger.debug("Mounting entity " + getEntityId() + " to player " + mountedPlayer.getEntityId());
                                final List<Integer> passengers = mountedPlayer.getPassengers().stream().map(Entity::getEntityId).collect(Collectors.toList());
                                passengers.add(getEntityId());

                                final int[] passengerArray = passengers.stream().mapToInt(Integer::intValue).toArray();

                                final WrapperPlayServerSetPassengers wrapper = new WrapperPlayServerSetPassengers(mountedPlayer.getEntityId(), passengerArray);
                                sendPacket(uuid, wrapper);
                            }
                        }
                    }.runTaskLaterAsynchronously(MCRekus.getInstance(), 5L);
                }

                if (EntityLib.getApi().getSettings().isDebugMode()) {
                    EntityLib.getPlatform().getLogger().info("Added viewer " + uuid + " to entity " + this.getEntityId());
                }

            }
        }
    }

    private static void sendPacket(UUID user, PacketWrapper<?> wrapper) {
        if (wrapper != null) {
            Object channel = EntityLib.getApi().getPacketEvents().getProtocolManager().getChannel(user);
            if (channel == null) {
                if (EntityLib.getApi().getSettings().isDebugMode()) {
                    EntityLib.getPlatform().getLogger().warning("Failed to send packet to " + user + " because the channel was null. They may be disconnected/not online.");
                }

            } else {
                EntityLib.getApi().getPacketEvents().getProtocolManager().sendPacket(channel, wrapper);
            }
        }
    }
}
