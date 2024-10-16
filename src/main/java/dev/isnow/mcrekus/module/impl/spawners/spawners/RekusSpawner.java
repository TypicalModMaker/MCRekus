package dev.isnow.mcrekus.module.impl.spawners.spawners;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.Random;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@EqualsAndHashCode(callSuper = false)
@Data
public class RekusSpawner extends ModuleAccessor<SpawnersModule> {
    private final RekusLocation location;

    private BukkitTask task;
    private boolean isBroken;
    private Player repairingPlayer;
    private Cooldown<UUID> cooldowns = new Cooldown<>(getModule().getConfig().getCooldownTime() * 1000L);

    public RekusSpawner(final RekusLocation location) {
        this.location = location;
        scheduleBreakingSpawner();
    }

    public void scheduleBreakingSpawner() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if(isBroken) {
                    RekusLogger.warn("Tried to break a spawner at " + location + " whilst its broken!");
                    cancel();
                    return;
                }

                isBroken = true;
            }
        }.runTaskLaterAsynchronously(MCRekus.getInstance(), new Random().nextInt(getModule().getConfig().getMinTimeTillBreak(), getModule().getConfig().getMaxTimeTillBreak()) * 20L);
    }

    public void repairSpawner() {
        isBroken = false;
        repairingPlayer = null;

        scheduleBreakingSpawner();
    }

    public void addCooldown(final UUID uuid) {
        cooldowns.addCooldown(uuid);
    }
}
