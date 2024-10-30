package dev.isnow.mcrekus.module.impl.essentials.command.teleport;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.TeleportUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"tphere", "teleporthere"})
@Description("Command to teleport person to yourself")
@Permission("mcrekus.tphere")
@SuppressWarnings("unused")
public class TeleportHereCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getTeleportHereNoArgsMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();

        TeleportUtil.teleportPlayers(source.asPlayer(), target, source.asPlayer(), config);
    }
}
