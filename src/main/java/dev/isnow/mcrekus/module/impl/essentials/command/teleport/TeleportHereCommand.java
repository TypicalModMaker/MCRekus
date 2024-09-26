package dev.isnow.mcrekus.module.impl.essentials.command.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.TeleportUtil;
import org.bukkit.entity.Player;

@CommandAlias("tphere|teleporthere")
@Description("Command to teleport person to yourself")
@CommandPermission("mcrekus.tphere")
@SuppressWarnings("unused")
public class TeleportHereCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("@players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getTeleportHereNoArgsMessage()));
            return;
        }

        if(args.length == 1) {
            final Player target = player.getServer().getPlayer(args[0]);

            TeleportUtil.teleportPlayers(player, target, player, config);
        }
    }
}
