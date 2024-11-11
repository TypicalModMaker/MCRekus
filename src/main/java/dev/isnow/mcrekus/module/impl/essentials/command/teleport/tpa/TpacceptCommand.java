package dev.isnow.mcrekus.module.impl.essentials.command.teleport.tpa;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.TeleportUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Command("tpaccept")
@Description("Command to accept teleport request")
@Permission("mcrekus.tpaccept")
@SuppressWarnings("unused")
public class TpacceptCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final Player player = source.asPlayer();

        final EssentialsConfig config = getModule().getConfig();

        final TeleportManager teleportManager = getModule().getTeleportManager();

        if(!teleportManager.hasTpaRequest(player.getUniqueId())) {
            source.reply(ComponentUtil.deserialize(config.getTpacceptNoRequestMessage()));
            return;
        }

        final Player requester = Bukkit.getPlayer(teleportManager.getTpaRequest(player.getUniqueId()));

        teleportManager.removeTpaRequest(player.getUniqueId());

        if (requester == null) {
            source.reply(ComponentUtil.deserialize(config.getTpacceptRequesterOfflineMessage()));
            return;
        }

        requester.sendMessage(ComponentUtil.deserialize(config.getTpaRequestAcceptedRequesterMessage(), null, "%player%", player.getName(), "%time%", String.valueOf(config.getTpaDelayTime())));
        source.reply(ComponentUtil.deserialize(config.getTpaRequestAcceptedMessage(), null, "%player%", requester.getName()));

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
