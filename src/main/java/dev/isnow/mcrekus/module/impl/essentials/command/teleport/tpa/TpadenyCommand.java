package dev.isnow.mcrekus.module.impl.essentials.command.teleport.tpa;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command("tpadeny")
@Description("Command to deny teleport request")
@Permission("mcrekus.tpadeny")
@SuppressWarnings("unused")
public class TpadenyCommand extends ModuleAccessor<EssentialsModule> {

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
        if (requester == null) {
            source.reply(ComponentUtil.deserialize(config.getTpacceptRequesterOfflineMessage()));
            return;
        }

        teleportManager.removeTpaRequest(player.getUniqueId());
        source.reply(ComponentUtil.deserialize(config.getTpaRequestDeniedMessage(), null, "%player%", requester.getName()));
        requester.sendMessage(ComponentUtil.deserialize(config.getTpaDeniedSenderMessage(), null, "%player%", player.getName()));

    }
}
