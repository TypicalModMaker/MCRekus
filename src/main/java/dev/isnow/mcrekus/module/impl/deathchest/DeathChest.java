package dev.isnow.mcrekus.module.impl.deathchest;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.deathchest.config.DeathChestConfig;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@EqualsAndHashCode(callSuper = false)
@Data
@RequiredArgsConstructor
public class DeathChest extends ModuleAccessor<DeathChestModule> {
    public static final double ANGLE_INCREMENT = Math.PI / 16;
    private static final double RADIUS = 0.5;

    public final String playerName;
    public final Location blockLocation;

    public long time;
    public long deathTime;

    public BukkitTask particleTask;
    public BukkitTask countdownTask;

    public Hologram hologram;

    public void setup() {
        final DeathChestConfig config = getModule().getConfig();

        time = config.getTime();
        deathTime = System.currentTimeMillis();

        final Location particleInitLocalization = blockLocation.clone().add(0.5, 0, 0.5);

        final Particle particle = Particle.valueOf(config.getParticle());

        blockLocation.getChunk().setForceLoaded(true);

        particleTask = new BukkitRunnable() {
            double angle = 0;
            double heightOffset = 0;
            boolean goingUp = true;

            @Override
            public void run() {
                angle += ANGLE_INCREMENT;

                if (goingUp) {
                    heightOffset += 0.025;
                    if (heightOffset >= 1.0) {
                        goingUp = false;
                    }
                } else {
                    heightOffset -= 0.025;
                    if (heightOffset <= 0.0) {
                        goingUp = true;
                    }
                }

                final double xOffset = RADIUS * Math.cos(angle);
                final double zOffset = RADIUS * Math.sin(angle);
                final Location particleLocation = particleInitLocalization.clone().add(xOffset, 1 + heightOffset, zOffset);

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        particleLocation.getWorld().spawnParticle(particle, particleLocation, 0, 0, 0, 0, 0);
                    }
                }.runTask(MCRekus.getInstance());

            }
        }.runTaskTimerAsynchronously(MCRekus.getInstance(), 0L, 1L);

        final String hologramRandom = "DeathChest-" + new Random().nextInt(999999);

        final boolean hologramHook = MCRekus.getInstance().getHookManager().isHologramHook();

        if (hologramHook)  {
            final List<String> hologramList = Arrays.asList(config.getHologram());

            final SimpleDateFormat sdf = new SimpleDateFormat(config.getTimeFormat());
            sdf.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));

            for (int i = 0; i < hologramList.size(); i++) {
                String s = hologramList.get(i);
                s = s.replaceAll("%player%", playerName);
                s = s.replaceAll("%time%", sdf.format(new Date(deathTime)));
                hologramList.set(i, s);
            }

            hologram = DHAPI.createHologram(hologramRandom, blockLocation.clone().add(0.5, config.getHologramOffset(), 0.5), hologramList);
        }

        countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (time == 0 || blockLocation.getBlock().getType() == Material.AIR) {
                    remove();
                }

                time--;

                if(hologramHook) {
                    final Hologram hologram = DHAPI.getHologram(hologramRandom);

                    if (hologram == null) {
                        time = 0;
                        return;
                    }

                    final List<String> hologramLines = Arrays.asList(config.getHologram());

                    final SimpleDateFormat sdf = new SimpleDateFormat(config.getTimeFormat());
                    sdf.setTimeZone(TimeZone.getTimeZone("Europe/Warsaw"));

                    for (int i = 0; i < hologramLines.size(); i++) {
                        String s = hologramLines.get(i);
                        s = s.replaceAll("%player%", playerName);
                        s = s.replaceAll("%time%", sdf.format(new Date(deathTime)));
                        s = s.replaceAll("%time-remaining%", String.valueOf(time));

                        hologram.getPage(0).setLine(i, s);
                    }
                }
            }
        }.runTaskTimerAsynchronously(MCRekus.getInstance(), 20, 20);
    }

    public void remove() {
        final boolean hologramHook = MCRekus.getInstance().getHookManager().isHologramHook();
        particleTask.cancel();
        countdownTask.cancel();

        final Block block = blockLocation.getBlock();

        if(Bukkit.isPrimaryThread()) {
            block.setType(Material.AIR);
            block.getChunk().setForceLoaded(false);
        } else {
            new BukkitRunnable() {
                @Override
                public void run() {
                    block.setType(Material.AIR);
                    block.getChunk().setForceLoaded(false);
                }
            }.runTask(MCRekus.getInstance());
        }

        if (hologramHook) {
            hologram.destroy();
        }

        getModule().getDeathChests().remove(RekusLocation.fromBukkitLocation(blockLocation));
    }
}
