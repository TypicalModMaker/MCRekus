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
import org.bukkit.entity.Player;

@Command({"day", "dzien"})
@Description("Command to set time to day")
@Permission("mcrekus.day")
@SuppressWarnings("unused")
public class DayCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.asPlayer().getWorld().setTime(1000L);
        source.reply(ComponentUtil.deserialize(config.getDayMessage()));
    }
}
