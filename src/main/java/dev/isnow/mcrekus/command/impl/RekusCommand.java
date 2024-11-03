package dev.isnow.mcrekus.command.impl;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.DateUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Optional;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.Usage;
import dev.velix.imperat.util.ImperatDebugger;
import org.bukkit.entity.Player;
import org.hibernate.stat.Statistics;

@Command({"mcrekus", "rekus"})
@Description("Master MCRekus command")
@Permission("mcrekus.*")
@SuppressWarnings("unused")
public class RekusCommand {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final Player player = source.asPlayer();

        source.reply(ComponentUtil.deserialize("&aCommands:"));
        source.reply(ComponentUtil.deserialize("&a/mcrekus reload - Reloads the general config and modules"));
        source.reply(ComponentUtil.deserialize("&a/mcrekus manualsave - Saves all player data to the database manually"));
        source.reply(ComponentUtil.deserialize("&a/mcrekus dbstatistics - Shows database statistics [DEBUG MODE ONLY]"));
        source.reply(ComponentUtil.deserialize("&a/mcrekus resetconfig [moduleName] - Resets config to default values"));
    }
    
    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("action") @Suggest({"reload", "manualsave", "dbstatistics", "resetconfig"}) final String action, @Named("module") @Optional final String moduleName) {
        
        if (action.equalsIgnoreCase("reload")) {
            final long startTime = System.currentTimeMillis();

            source.reply(ComponentUtil.deserialize("&aReloading general config..."));
            MCRekus.getInstance().getConfigManager().reloadConfigs();
            source.reply(ComponentUtil.deserialize("&aReloaded general config successfully!"));

            source.reply(ComponentUtil.deserialize("&aReloading modules..."));
            MCRekus.getInstance().getModuleManager().unloadModules();
            MCRekus.getInstance().getModuleManager().loadModules();
            source.reply(ComponentUtil.deserialize("&aReloaded modules successfully!"));

            final String date = DateUtil.formatElapsedTime((System.currentTimeMillis() - startTime));

            source.reply(ComponentUtil.deserialize("&cFinished reloading in " + date + " seconds."));
            source.reply(ComponentUtil.deserialize("&aSome modules may require a server restart to take effect."));
            return;
        }

        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        if (action.equalsIgnoreCase("manualsave")) {
            source.reply(ComponentUtil.deserialize("&aSaving player data..."));
            for(final Player onlinePlayer : MCRekus.getInstance().getServer().getOnlinePlayers()) {
                databaseManager.getUserAsync(onlinePlayer, (session, user) -> user.save(session));
            }
            source.reply(ComponentUtil.deserialize("&aSaved player data successfully!"));
            return;
        }

        if (action.equalsIgnoreCase("dbstatistics")) {
            final Statistics statistics = databaseManager.getDatabase().getStatistics();

            source.reply(ComponentUtil.deserialize("&aDatabase statistics:"));
            source.reply(ComponentUtil.deserialize("&aHit count: " + statistics.getSecondLevelCacheHitCount()));
            source.reply(ComponentUtil.deserialize("&aMiss count: " + statistics.getSecondLevelCacheMissCount()));
            source.reply(ComponentUtil.deserialize("&aPut count: " + statistics.getSecondLevelCachePutCount()));
        }

        if(action.equalsIgnoreCase("resetconfig")) {
            if(moduleName == null || moduleName.isEmpty()) {
                source.reply(ComponentUtil.deserialize("&cUsage: /mcrekus resetconfigs [moduleName]"));
                return;
            }

            final Module module = MCRekus.getInstance().getModuleManager().getModuleByName(moduleName);

            if(module == null) {
                source.reply(ComponentUtil.deserialize("&cModule not found!"));
                return;
            }

            module.getConfig().delete();
            module.setConfig(module.createConfig());

            source.reply(ComponentUtil.deserialize("&aConfig reset for module " + module.getName()));
        }
    }
}
