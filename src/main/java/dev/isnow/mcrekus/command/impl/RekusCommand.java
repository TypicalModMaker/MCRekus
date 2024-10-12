package dev.isnow.mcrekus.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.database.DatabaseManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.DateUtil;
import org.bukkit.entity.Player;

@CommandAlias("mcrekus|rekus")
@Description("Master MCRekus command")
@CommandPermission("mcrekus.*")
@SuppressWarnings("unused")
public class RekusCommand extends BaseCommand {

    @Default
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize("&aCommands:"));
            player.sendMessage(ComponentUtil.deserialize("&a/mcrekus reload - Reloads the general config and modules"));
            player.sendMessage(ComponentUtil.deserialize("&a/mcrekus manualsave - Saves all player data to the database manually"));
            player.sendMessage(ComponentUtil.deserialize("&a/mcrekus dbstatistics - Shows database statistics [DEBUG MODE ONLY]"));
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
        }

        if (args[0].equalsIgnoreCase("manualsave")) {
            player.sendMessage(ComponentUtil.deserialize("&aSaving player data..."));
            MCRekus.getInstance().getThreadPool().execute(() -> MCRekus.getInstance().getDatabaseManager().saveAllUsers());
            player.sendMessage(ComponentUtil.deserialize("&aSaved player data successfully!"));
        }

        if (args[0].equalsIgnoreCase("dbstatistics")) {
            final DatabaseManager databaseManager = MCRekus.getInstance().getDatabaseManager();
            player.sendMessage(ComponentUtil.deserialize("&aDatabase statistics:"));
            player.sendMessage(ComponentUtil.deserialize("&aHit count: " + MCRekus.getInstance().getDatabaseManager().hitCount()));
            player.sendMessage(ComponentUtil.deserialize("&aMiss count: " + MCRekus.getInstance().getDatabaseManager().missCount()));
            player.sendMessage(ComponentUtil.deserialize("&aPut count: " + MCRekus.getInstance().getDatabaseManager().putCount()));
        }
    }
}
