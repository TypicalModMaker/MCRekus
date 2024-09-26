package dev.isnow.mcrekus.module.impl.essentials.command.teleport.tpa;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.TeleportUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@CommandAlias("tpaccept")
@Description("Command to accept teleport request")
@CommandPermission("mcrekus.tpaccept")
@SuppressWarnings("unused")
public class TpacceptCommand extends BaseCommand {
    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        final TeleportManager teleportManager = moduleAccessor.getModule().getTeleportManager();

        if(!teleportManager.hasTpaRequest(player.getUniqueId())) {
            player.sendMessage(ComponentUtil.deserialize(config.getTpacceptNoRequestMessage()));
            return;
        }

        final Player requester = Bukkit.getPlayer(teleportManager.getTpaRequest(player.getUniqueId()));

        teleportManager.removeTpaRequest(player.getUniqueId());

        if (requester == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getTpacceptRequesterOfflineMessage()));
            return;
        }

        requester.sendMessage(ComponentUtil.deserialize(config.getTpaRequestAcceptedRequesterMessage(), null, "%player%", player.getName(), "%time%", String.valueOf(config.getTpaDelayTime())));
        player.sendMessage(ComponentUtil.deserialize(config.getTpaRequestAcceptedMessage(), null, "%player%", requester.getName()));

        BukkitTask bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                teleportManager.removePlayerTeleporting(requester.getUniqueId());
                TeleportUtil.teleportPlayers(null, requester, player, config);
            }
        }.runTaskLater(MCRekus.getInstance(), config.getTpaDelayTime() * 20L);

        teleportManager.addPlayerTeleporting(requester.getUniqueId(), bukkitRunnable);

        Bukkit.getScheduler().cancelTask(teleportManager.getTpaRequestTaskId(player.getUniqueId()));
        teleportManager.removeTpaRequestTask(player.getUniqueId());
    }
}
