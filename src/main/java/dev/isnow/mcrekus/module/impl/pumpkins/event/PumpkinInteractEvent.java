package dev.isnow.mcrekus.module.impl.pumpkins.event;

import static dev.isnow.mcrekus.module.impl.deathchest.DeathChest.ANGLE_INCREMENT;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import com.github.retrooper.packetevents.protocol.particle.Particle;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleData;
import com.github.retrooper.packetevents.protocol.particle.data.ParticleDustData;
import com.github.retrooper.packetevents.protocol.particle.type.ParticleType;
import com.github.retrooper.packetevents.util.Vector3d;
import com.github.retrooper.packetevents.util.Vector3f;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerParticle;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.pumpkins.PumpkinsModule;
import dev.isnow.mcrekus.module.impl.pumpkins.config.PumpkinsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import io.github.retrooper.packetevents.util.SpigotConversionUtil;
import java.time.Duration;
import java.util.HashMap;
import me.tofaa.entitylib.meta.other.FireworkRocketMeta;
import me.tofaa.entitylib.wrapper.WrapperEntity;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class PumpkinInteractEvent extends ModuleAccessor<PumpkinsModule> implements Listener {

    private final HashMap<Player, Long> lastInteractionTime = new HashMap<>();

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        lastInteractionTime.remove(event.getPlayer());
    }

    @EventHandler
    public void onInteract(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Block block = event.getClickedBlock();

        if (block == null) return;

        if (block.getType() != Material.PLAYER_HEAD) return;

        if(event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (lastInteractionTime.containsKey(player)) {
            final long lastInteraction = lastInteractionTime.get(player);

            if (System.currentTimeMillis() - lastInteraction < 5000) {
                return;
            }
        }
        lastInteractionTime.put(player, System.currentTimeMillis());

        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        databaseManager.getPumpkinAsync(RekusLocation.fromBukkitLocation(block.getLocation()), (session, pumpkinData) -> {
            if (pumpkinData == null) return;

            databaseManager.getUserAsync(player, (userSession, userData) -> {
                if (userData == null) return;

                if (userData.getPumpkins().stream().anyMatch(pumpkin -> pumpkin.getId() == pumpkinData.getId())) {
                    RekusLogger.info("Player has already collected this pumpkin");
                    userSession.closeSession();
                    session.closeSession();
                    return;
                }

                userData.addPumpkin(pumpkinData);

                databaseManager.saveUser(userData, userSession);

                final PumpkinsConfig config = getModule().getConfig();

                new BukkitRunnable() {
                    int i = 0;
                    int t = 0;
                    double radius = 3.0;
                    double angle = 0;
                    final Location loc = block.getLocation().clone();

                    @Override
                    public void run() {

                        if(radius <= 0) {
                            createFirework(loc, player);
                            final Title.Times times = Title.Times.times(Duration.ofSeconds(config.getPumpkinInteractTitleFadeIn()), Duration.ofSeconds(config.getPumpkinInteractTitleStay()), Duration.ofSeconds(config.getPumpkinInteractTitleFadeOut()));
                            final Title title = Title.title(ComponentUtil.deserialize(config.getPumpkinInteractTitle()), ComponentUtil.deserialize(config.getPumpkinInteractSubTitle().replaceAll("%amount%", String.valueOf(userData.getPumpkins().size()))), times);

                            player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 2);

                            player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.5f);
                            Bukkit.getScheduler().runTaskLater(MCRekus.getInstance(), () ->
                                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 1.7f), 10L);
                            Bukkit.getScheduler().runTaskLater(MCRekus.getInstance(), () ->
                                    player.playSound(player, Sound.BLOCK_NOTE_BLOCK_PLING, 1, 2.0f), 20L);
                            Bukkit.getScheduler().runTaskLater(MCRekus.getInstance(), () ->
                                    player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 1, 0.9f), 20L);



                            player.showTitle(title);
                            this.cancel();
                            return;
                        }


                        angle += ANGLE_INCREMENT;
                        final double x = radius * Math.cos(angle);
                        final double z = radius * Math.sin(angle);

                        radius -= 0.05;

                        Location particleLoc = loc.clone().add(x, 0, z);

//                        currentLocation.getWorld().spawnParticle(org.bukkit.Particle.FLAME, currentLocation, 0, 0, 0, 0, 0);
                        particleLoc.getWorld().spawnParticle(org.bukkit.Particle.SPELL_MOB, particleLoc, 0, 0, 0, 0, 0);
                        particleLoc.getWorld().spawnParticle(org.bukkit.Particle.REDSTONE, particleLoc, 0, 0, 0, 0, 0, new org.bukkit.Particle.DustOptions(Color.PURPLE, 1));

                        player.playSound(player, Sound.BLOCK_NOTE_BLOCK_HAT, 1, 0.5f + (t * 0.025f));

                        if (i == 10) {
                            particleLoc.getWorld()
                                    .spawnParticle(org.bukkit.Particle.END_ROD, particleLoc, 0, 0,
                                            0, 0, 0);
                            i = 0;
                        }
                        t++;
                        i++;
                    }
                }.runTaskTimer(MCRekus.getInstance(), 0, 1);


                userSession.closeSession();
                session.closeSession();
            });
        });
    }

    public void createFirework(final Location loc, final Player player) {
        final WrapperEntity entity = new WrapperEntity(EntityTypes.FIREWORK_ROCKET);

        entity.addViewerSilently(player.getUniqueId());

        final FireworkRocketMeta fireworkRocketMeta = (FireworkRocketMeta) entity.getEntityMeta();

        final ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET);
        final FireworkMeta fireworkMeta = (FireworkMeta) firework.getItemMeta();
        final FireworkEffect effect = FireworkEffect.builder().flicker(false).withColor(Color.ORANGE, Color.PURPLE).with(FireworkEffect.Type.BURST).build();
        fireworkMeta.addEffect(effect);
        fireworkMeta.setPower(2);
        firework.setItemMeta(fireworkMeta);

        fireworkRocketMeta.setFireworkItem(SpigotConversionUtil.fromBukkitItemStack(firework));
        entity.spawn(SpigotConversionUtil.fromBukkitLocation(loc.clone().add(0.5, 0, 0.5)));

        new BukkitRunnable() {
            @Override
            public void run() {
                entity.despawn();
                entity.remove();
            }
        }.runTaskLaterAsynchronously(MCRekus.getInstance(),20 * 3);
    }

    private void sendParticleDust(final Player player, final ParticleType<ParticleDustData> particle, final Location loc) {

        Particle<ParticleDustData> particleObject = new Particle<>(particle);

        WrapperPlayServerParticle packet = new WrapperPlayServerParticle(
                particleObject,
                true,
                new Vector3d(loc.getX(), loc.getY(), loc.getZ()),
                new Vector3f(0.5f, 0.5f, 0.5f),
                0,
                1
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }

    private void sendParticle(final Player player, final ParticleType<ParticleData> particle, final Location loc) {

        Particle<?> particleObject = new Particle<>(particle);

        WrapperPlayServerParticle packet = new WrapperPlayServerParticle(
                particleObject,
                true,
                new Vector3d(loc.getX(), loc.getY(), loc.getZ()),
                new Vector3f(0.5f, 0.5f, 0.5f),
                0,
                1
        );

        PacketEvents.getAPI().getPlayerManager().sendPacket(player, packet);
    }
}
