package dev.isnow.mcrekus.module.impl.essentials.command.teleport;

import dev.isnow.mcrekus.MCRekus;
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
import org.bukkit.scheduler.BukkitRunnable;

@Command({"otp", "offlineteleport"})
@Description("Command to offline teleport to someone")
@Permission("mcrekus.offlineteleport")
@SuppressWarnings("unused")
public class OfflineTeleportCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getOfflineTeleportNoArgsMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("target") final String target) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        MCRekus.getInstance().getDatabaseManager().getUserAsync(target, (session, data) -> {
            if (data == null) {
                source.reply(ComponentUtil.deserialize(config.getOfflineTeleportPlayerNotFoundMessage(), null, "%player%", target));
                return;
            }

            if (data.getLastLocation() == null) {
                source.reply(ComponentUtil.deserialize(config.getOfflineTeleportNoLocationMessage(), null, "%player%", target));
                return;
            }

            new BukkitRunnable() {
                @Override
                public void run() {
                    player.teleport(data.getLastLocation().toBukkitLocation());
                }
            }.runTask(MCRekus.getInstance());

            source.reply(ComponentUtil.deserialize(config.getOfflineTeleportSuccessMessage(), null, "%player%", target));
            session.closeSession();
        });

    }
}
