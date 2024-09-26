package dev.isnow.mcrekus.command.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.DateUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

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
            return;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            final long startTime = System.currentTimeMillis();

            player.sendMessage(ComponentUtil.deserialize("&aReloading general config..."));
            MCRekus.getInstance().getConfigManager().reloadConfigs();
            player.sendMessage(ComponentUtil.deserialize("&aReloaded general config successfully!"));

            player.sendMessage(ComponentUtil.deserialize("&aReloading modules..."));
            MCRekus.getInstance().getModuleManager().unloadModules();
            player.sendMessage(ComponentUtil.deserialize("&aReloading module cache..."));
            MCRekus.getInstance().getPlayerDataManager().saveAll();
            MCRekus.getInstance().getPlayerDataManager().unloadAllCaches();
            MCRekus.getInstance().getModuleManager().loadModules();
            for(final Player onlinePlayer : MCRekus.getInstance().getServer().getOnlinePlayers()) {
                MCRekus.getInstance().getThreadPool().execute(() -> {
                    MCRekus.getInstance().getPlayerDataManager().preloadPlayerData(new AsyncPlayerPreLoginEvent(onlinePlayer.getName(), onlinePlayer.getAddress().getAddress(), onlinePlayer.getUniqueId()));
                });
            }
            player.sendMessage(ComponentUtil.deserialize("&aReloaded modules successfully!"));

            final String date = DateUtil.formatElapsedTime((System.currentTimeMillis() - startTime));

            player.sendMessage(ComponentUtil.deserialize("&cFinished reloading in " + date + " seconds."));
            player.sendMessage(ComponentUtil.deserialize("&aSome modules may require a server restart to take effect."));

        }
    }
}
