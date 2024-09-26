package dev.isnow.mcrekus.module.impl.essentials.command.teleport.tpa;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("tpadeny")
@Description("Command to deny teleport request")
@CommandPermission("mcrekus.tpadeny")
@SuppressWarnings("unused")
public class TpadenyCommand extends BaseCommand {
    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);


    @Default
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        final TeleportManager teleportManager = moduleAccessor.getModule().getTeleportManager();

        if(!teleportManager.hasTpaRequest(player.getUniqueId())) {
            player.sendMessage(ComponentUtil.deserialize(config.getTpacceptNoRequestMessage()));
            return;
        }

        final Player requester = Bukkit.getPlayer(moduleAccessor.getModule().getTeleportManager().getTpaRequest(player.getUniqueId()));
        if (requester == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getTpacceptRequesterOfflineMessage()));
            return;
        }

        teleportManager.removeTpaRequest(player.getUniqueId());
        player.sendMessage(ComponentUtil.deserialize(config.getTpaRequestDeniedMessage(), null, "%player%", requester.getName()));
        requester.sendMessage(ComponentUtil.deserialize(config.getTpaDeniedSenderMessage(), null, "%player%", player.getName()));

    }
}
