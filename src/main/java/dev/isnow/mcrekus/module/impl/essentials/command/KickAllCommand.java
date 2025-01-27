package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@Command("kickall")
@Description("Command to kick all players")
@Permission("mcrekus.kickall")
@SuppressWarnings("unused")
public final class KickAllCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        for(final Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("mcrekus.kickall.bypass")) {
                continue;
            }

            p.kick(ComponentUtil.deserialize(config.getKickAllReason()));
            p.sendMessage(ComponentUtil.deserialize(config.getKickAllReason()));
        }

        source.reply(ComponentUtil.deserialize(config.getKickAllMessage()));
    }

}
