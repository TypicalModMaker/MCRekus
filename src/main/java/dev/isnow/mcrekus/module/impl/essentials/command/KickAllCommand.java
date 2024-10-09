package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("kickall")
@Description("Command to kick all players")
@CommandPermission("mcrekus.kickall")
@SuppressWarnings("unused")
public final class KickAllCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    public void execute(final Player player, final String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        for(final Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("mcrekus.kickall.bypass")) {
                continue;
            }

            p.sendMessage(ComponentUtil.deserialize(config.getKickAllReason()));
        }

        player.sendMessage(ComponentUtil.deserialize(config.getKickAllMessage()));
    }

}
