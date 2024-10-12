package dev.isnow.mcrekus.module.impl.spawners;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.module.impl.spawners.progress.ProgressTracker;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


@Getter
public class SpawnersModule extends Module<SpawnersConfig> {
    public SpawnersModule() {
        super("Spawners");
    }

    private final HashMap<Player, ProgressTracker> playerTasks = new HashMap<>();
    private final ArrayList<Location> brokenSpawners = new ArrayList<>();
    private final Cooldown<UUID> cooldown = new Cooldown<>(getConfig().getCooldownTime() * 1000L);

    private final HashMap<RekusLocation, BukkitTask> spawners = new HashMap<>();

    private BukkitTask particleTask;

    @Override
    public void onEnable(final MCRekus plugin) {
        registerCommands("command");
        registerListeners("event");

        particleTask = new BukkitRunnable() {
            @Override
            public void run() {
                for(final Location location : brokenSpawners) {
                    for(double z = 0.2; z < 1; z += 0.2) {
                        for(double x = 0.2; x < 1; x += 0.2) {
                            location.getWorld().spawnParticle(org.bukkit.Particle.FLAME, location.clone().add(x, 1, z), 1, 0, 0, 0, 0);
                            location.getWorld().spawnParticle(org.bukkit.Particle.FLAME, location.clone().add(x, 0.5, z), 1, 0, 0, 0, 0);
                            location.getWorld().spawnParticle(org.bukkit.Particle.FLAME, location.clone().add(x, 0, z), 1, 0, 0, 0, 0);
                        }
                    }

                    Bukkit.getScheduler().runTask(plugin, () -> location.getWorld().playSound(location, Sound.ENTITY_MINECART_RIDING, 0.1F, new Random().nextFloat(0.2F, 0.4F)));
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20L);

        for (final Chunk chunk : Bukkit.getWorlds().getFirst().getLoadedChunks()) {
            loadChunk(chunk);
        }

    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterListeners();
        unRegisterCommands();

        particleTask.cancel();
        for(final BukkitTask task : spawners.values()) {
            task.cancel();
        }

        for(final ProgressTracker progressTracker : playerTasks.values()) {
            progressTracker.getTask().cancel();
        }
    }

    public ProgressTracker getProgressTracker(final Player player) {
        return playerTasks.get(player);
    }

    public void addProgressTracker(final Player player, final ProgressTracker progressTracker) {
        playerTasks.put(player, progressTracker);
    }

    public void removeProgressTracker(final Player player) {
        playerTasks.remove(player);
    }

    public boolean hasProgressTracker(final Player player) {
        return playerTasks.containsKey(player);
    }

    public boolean isSpawnerBroken(final Location location) {
        return brokenSpawners.contains(location);
    }

    public void addBrokenSpawner(final Location location) {
        brokenSpawners.add(location);
    }

    public boolean isSpawnerBeingRepaired(final Location location) {
        return playerTasks.values().stream().anyMatch(progressTracker -> progressTracker.getSpawnerLocation().equals(RekusLocation.fromBukkitLocation(location)));
    }

    public Player getRepairingPlayer(final Location location) {
        return playerTasks.entrySet().stream().filter(entry -> entry.getValue().getSpawnerLocation().equals(RekusLocation.fromBukkitLocation(location))).map(
                Entry::getKey).findFirst().orElse(null);
    }

    public void repairBrokenSpawner(final Location location) {
        brokenSpawners.remove(location);
    }

    public void addSpawnerToRepair(final RekusLocation location, final BukkitTask task) {
        spawners.put(location, task);
    }

    public void removeSpawnerToRepair(final RekusLocation location) {
        spawners.remove(location);
    }

    public void scheduleBreakingSpawner(final RekusLocation spawner) {
        final BukkitTask breakingTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(isSpawnerBroken(spawner.toBukkitLocation())) {
                    RekusLogger.warn("Tried to break a spawner at " + spawner + " whilst its broken!");
                    cancel();
                    removeSpawnerTask(spawner);
                    return;
                }

                addBrokenSpawner(spawner.toBukkitLocation());
            }
        }.runTaskLaterAsynchronously(MCRekus.getInstance(), new Random().nextInt(getConfig().getMinTimeTillBreak(), getConfig().getMaxTimeTillBreak()) * 20L);
        addSpawnerToRepair(spawner, breakingTask);
    }

    public BukkitTask getSpawnerTask(final RekusLocation location) {
        return spawners.get(location);
    }

    public void removeSpawnerTask(final RekusLocation location) {
        spawners.remove(location);
    }

    public void loadChunk(final Chunk chunk) {
        final World world = chunk.getWorld();

        final ChunkSnapshot chunkSnapshot = chunk.getChunkSnapshot(false, false, false, false);

        final int maxHeight = world.getMaxHeight() - 1;
        final int minHeight = world.getMinHeight() + 1;

        for (int x = 0; x <= 15; x++) {
            for (int y = minHeight; y <= maxHeight; y++) {
                for (int z = 0; z <= 15; z++) {
                    final Material type = chunkSnapshot.getBlockType(x, y, z);

                    if (type == Material.SPAWNER) {
                        final RekusLocation location = new RekusLocation(world, chunk.getX() * 16 + x, y, chunk.getZ() * 16 + z);
                        final BukkitTask task = getSpawnerTask(location);
                        if(isSpawnerBroken(location.toBukkitLocation()) || (task != null && !task.isCancelled())) {
                            continue;
                        }

                        RekusLogger.debug("Found spawner at " + location);
                        scheduleBreakingSpawner(location);
                    }
                }
            }
        }
    }
}
