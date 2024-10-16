package dev.isnow.mcrekus.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.DateUtil;
import org.bukkit.entity.Player;
import org.hibernate.stat.Statistics;

@CommandAlias("mcrekus|rekus")
@Description("Master MCRekus command")
@CommandPermission("mcrekus.*")
@SuppressWarnings("unused")
public class RekusCommand extends BaseCommand {

    @Default
    @CommandCompletion("reload|manualsave|dbstatistics|resetconfig")
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize("&aCommands:"));
            player.sendMessage(ComponentUtil.deserialize("&a/mcrekus reload - Reloads the general config and modules"));
            player.sendMessage(ComponentUtil.deserialize("&a/mcrekus manualsave - Saves all player data to the database manually"));
            player.sendMessage(ComponentUtil.deserialize("&a/mcrekus dbstatistics - Shows database statistics [DEBUG MODE ONLY]"));
            player.sendMessage(ComponentUtil.deserialize("&a/mcrekus resetconfig [moduleName] - Resets config to default values"));
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            final long startTime = System.currentTimeMillis();

            player.sendMessage(ComponentUtil.deserialize("&aReloading general config..."));
            MCRekus.getInstance().getConfigManager().reloadConfigs();
            player.sendMessage(ComponentUtil.deserialize("&aReloaded general config successfully!"));

            player.sendMessage(ComponentUtil.deserialize("&aReloading modules..."));
            MCRekus.getInstance().getModuleManager().unloadModules();
            MCRekus.getInstance().getModuleManager().loadModules();
            player.sendMessage(ComponentUtil.deserialize("&aReloaded modules successfully!"));

            final String date = DateUtil.formatElapsedTime((System.currentTimeMillis() - startTime));

            player.sendMessage(ComponentUtil.deserialize("&cFinished reloading in " + date + " seconds."));
            player.sendMessage(ComponentUtil.deserialize("&aSome modules may require a server restart to take effect."));
            return;
        }

        final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();

        if (args[0].equalsIgnoreCase("manualsave")) {
            player.sendMessage(ComponentUtil.deserialize("&aSaving player data..."));
            for(final Player onlinePlayer : MCRekus.getInstance().getServer().getOnlinePlayers()) {
                databaseManager.getUserAsync(onlinePlayer, (session, user) -> user.save(session));
            }
            player.sendMessage(ComponentUtil.deserialize("&aSaved player data successfully!"));
            return;
        }

        if (args[0].equalsIgnoreCase("dbstatistics")) {
            final Statistics statistics = databaseManager.getDatabase().getStatistics();

            player.sendMessage(ComponentUtil.deserialize("&aDatabase statistics:"));
            player.sendMessage(ComponentUtil.deserialize("&aHit count: " + statistics.getSecondLevelCacheHitCount()));
            player.sendMessage(ComponentUtil.deserialize("&aMiss count: " + statistics.getSecondLevelCacheMissCount()));
            player.sendMessage(ComponentUtil.deserialize("&aPut count: " + statistics.getSecondLevelCachePutCount()));
        }

        if(args[0].equalsIgnoreCase("resetconfig")) {
            if(args.length < 2) {
                player.sendMessage(ComponentUtil.deserialize("&cUsage: /mcrekus resetconfigs [moduleName]"));
                return;
            }


            final Module module = MCRekus.getInstance().getModuleManager().getModuleByName(args[1]);

            if(module == null) {
                player.sendMessage(ComponentUtil.deserialize("&cModule not found!"));
                return;
            }

            module.getConfig().delete();
            module.setConfig(module.createConfig());

            player.sendMessage(ComponentUtil.deserialize("&aConfig reset for module " + module.getName()));
        }
    }
}
