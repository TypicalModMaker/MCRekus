package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"god", "godmode", "niesmiertelnosc"})
@Description("Command to have godmode")
@Permission("mcrekus.god")
@SuppressWarnings("unused")
public class GodCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        final boolean godMode = player.isInvulnerable();
        player.setInvulnerable(!godMode);
        source.reply(ComponentUtil.deserialize(godMode ? config.getGodModeDisabledMessage() : config.getGodModeEnabledMessage()));
    }

    @Usage
    @Async
    public void executeOther(final BukkitSource source, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        final boolean godMode = target.isInvulnerable();
        target.setInvulnerable(!godMode);
        source.reply(ComponentUtil.deserialize(godMode ? config.getGodModeDisabledOtherMessage() : config.getGodModeEnabledOtherMessage(), null, "%player%", target));
    }
}
