package dev.isnow.mcrekus.module.impl.pumpkins.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.pumpkins.PumpkinsModule;
import dev.isnow.mcrekus.module.impl.pumpkins.config.PumpkinsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("setuppumpkins")
@Description("Command to setup pumpkins")
@CommandPermission("mcrekus.setuppumpkins")
@SuppressWarnings("unused")
public class SetupPumpkinsCommand extends BaseCommand {

    private final ModuleAccessor<PumpkinsModule> moduleAccessor = new ModuleAccessor<>(PumpkinsModule.class);

    @Default
    public void execute(Player player, String[] args) {

        final PumpkinsConfig config = moduleAccessor.getModule().getConfig();

        if(moduleAccessor.getModule().getSetupPlayers().contains(player)) {
            moduleAccessor.getModule().getSetupPlayers().remove(player);
            player.sendMessage(ComponentUtil.deserialize(config.getSetupPumpkinsOFF()));
        } else {
            moduleAccessor.getModule().getSetupPlayers().add(player);
            player.sendMessage(ComponentUtil.deserialize(config.getSetupPumpkinsON()));
        }
    }
}
