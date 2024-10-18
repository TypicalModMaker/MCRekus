package dev.isnow.mcrekus.hook;

import dev.isnow.mcrekus.util.RekusLogger;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class HookManager {

    private final boolean placeholerAPIHook;
    private final boolean kingdomsHook;
    private final boolean vehiclesHook;
    private final boolean hologramHook;
    private final boolean vaultHook;

    public HookManager() {
        placeholerAPIHook = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (placeholerAPIHook) {
            RekusLogger.info("Hooking into PlaceholderAPI");
        }

        kingdomsHook = Bukkit.getServer().getPluginManager().getPlugin("Kingdoms") != null;
        if (kingdomsHook) {
            RekusLogger.info("Hooking into KingdomsX");
        }

        vehiclesHook = Bukkit.getServer().getPluginManager().getPlugin("Vehicles") != null;
        if (vehiclesHook) {
            RekusLogger.info("Hooking into Vehicles");
        }

        hologramHook = Bukkit.getServer().getPluginManager().getPlugin("DecentHolograms") != null;
        if (hologramHook) {
            RekusLogger.info("Hooking into DecentHolograms");
        }

        vaultHook = Bukkit.getServer().getPluginManager().getPlugin("Vault") != null;
        if (vaultHook) {
            RekusLogger.info("Hooking into Vault");
        }
    }
}