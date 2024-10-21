package dev.isnow.mcrekus.module.impl.xray.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.xray.XrayModule;
import dev.isnow.mcrekus.module.impl.xray.config.XrayConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandAlias("banxray")
@Description("Command to clear and ban a player")
@CommandPermission("mcrekus.banxray")
@SuppressWarnings("unused")
public class BanXrayCommand extends BaseCommand {

    private final ModuleAccessor<XrayModule> moduleAccessor = new ModuleAccessor<>(XrayModule.class);

    @Default
    @CommandCompletion("@players [Czas]")
    public void execute(Player player, String[] args) {
        final XrayConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getBanXrayUsageMessage()));
            return;
        }

        final String playerName = args[0];
        final Player target = player.getServer().getPlayer(playerName);

        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getBanXrayPlayerNotFoundMessage(), null, "%player%", playerName));
            return;
        }

        target.getInventory().clear();
        Bukkit.dispatchCommand(player, "tempban " + playerName + " Udowodniono u Ciebie używanie niedozwolonej modyfikacji tekstur - X-ray. " + String.join(" ", args).replace(playerName, ""));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (target.isOnline()) {
                    player.sendMessage(ComponentUtil.deserialize(config.getBanXrayNotSuccessfulMessage(), null, "%player%", playerName));
                } else {
                    player.sendMessage(ComponentUtil.deserialize(config.getBanXraySuccessMessage(), null, "%player%", playerName));
                }
            }
        }.runTaskLaterAsynchronously(MCRekus.getInstance(), 20L);
    }
}
