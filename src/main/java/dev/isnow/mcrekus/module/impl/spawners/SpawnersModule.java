package dev.isnow.mcrekus.module.impl.spawners;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.SpawnerData;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.module.impl.spawners.progress.ProgressTracker;
import dev.isnow.mcrekus.module.impl.spawners.spawners.RekusSpawner;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;


@Getter
public class SpawnersModule extends Module<SpawnersConfig> {
    public SpawnersModule() {
        super("Spawners");
    }

    private final HashMap<Player, ProgressTracker> playerTasks = new HashMap<>();
    private final HashMap<RekusLocation, RekusSpawner> spawners = new HashMap<>();

    private BukkitTask particleTask;

    @Override
    public void onEnable(final MCRekus plugin) {
        if (!Bukkit.getPluginManager().isPluginEnabled("RoseStacker")) {
            RekusLogger.error("RoseStacker is not enabled! Disabling Spawners module...");
            return;
        }

        registerCommands("command");
        registerListeners("event");

        particleTask = new BukkitRunnable() {
            @Override
            public void run() {

                List<Location> locations = spawners.values().stream()
                        .filter(RekusSpawner::isBroken)
                        .map(rekusSpawner -> rekusSpawner.getLocation().toBukkitLocation())
                        .collect(Collectors.toList());

                for(final Location location : locations) {
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

        loadSpawners();
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterListeners();
        unRegisterCommands();

        particleTask.cancel();
        for(final BukkitTask task : spawners.values().stream().map(RekusSpawner::getTask).toList()) {
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

    public void loadSpawners() {
        MCRekus.getInstance().getDatabaseManager().getSpawnersAsync((session, spawnerData) -> {
            for(final SpawnerData data : spawnerData) {
                final RekusLocation location = data.getLocation();
                final RekusSpawner spawner = new RekusSpawner(location);
                spawners.put(location, spawner);
            }
        });
    }
}
