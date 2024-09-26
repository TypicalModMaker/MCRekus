package dev.isnow.mcrekus.module.impl.essentials.command;

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
import org.bukkit.entity.Player;

@CommandAlias("god|godmode|niesmiertelnosc")
@Description("Command to have godmode")
@CommandPermission("mcrekus.god")
@SuppressWarnings("unused")
public class GodCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("@players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            final boolean godMode = player.isInvulnerable();
            player.setInvulnerable(!godMode);
            player.sendMessage(ComponentUtil.deserialize(godMode ? config.getGodModeDisabledMessage() : config.getGodModeEnabledMessage()));
            return;
        }

        final Player target = player.getServer().getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getGodModePlayerNotFoundMessage(), null, "%player%", args[0]));
            return;
        }

        final boolean godMode = target.isInvulnerable();
        target.setInvulnerable(!godMode);
        player.sendMessage(ComponentUtil.deserialize(godMode ? config.getGodModeDisabledOtherMessage() : config.getGodModeEnabledOtherMessage(), null, "%player%", target));

    }
}
