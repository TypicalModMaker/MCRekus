package dev.isnow.mcrekus.module.impl.essentials.command.time;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("day|dzien")
@Description("Command to set time to day")
@CommandPermission("mcrekus.day")
@SuppressWarnings("unused")
public class DayCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        player.getWorld().setTime(1000L);
        player.sendMessage(ComponentUtil.deserialize(config.getDayMessage()));
    }
}
