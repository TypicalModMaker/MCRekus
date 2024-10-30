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
import org.bukkit.entity.Player;

@Command("workbench|wb|crafting")
@Description("Command to open workbench")
@Permission("mcrekus.workbench")
@SuppressWarnings("unused")
public class WorkBenchCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final Player player = source.asPlayer();

        final EssentialsConfig config = getModule().getConfig();

        player.openWorkbench(null, true);

        source.reply(ComponentUtil.deserialize(config.getOpenWorkbenchMessage()));

        player.playSound(player.getLocation(), config.getOpenWorkbenchSound(), 1.0F, 1.0F);
    }
}
