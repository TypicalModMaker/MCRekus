package dev.isnow.mcrekus.module.impl.essentials.command.time;

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

@Command({"night", "noc"})
@Description("Command to set time to night")
@Permission("mcrekus.night")
@SuppressWarnings("unused")
public class NightCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.asPlayer().getWorld().setTime(13000L);
        source.reply(ComponentUtil.deserialize(config.getNightMessage()));
    }
}
