package dev.isnow.mcrekus.module.impl.essentials.command.teleport;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.TeleportUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandAlias("otp|offlineteleport")
@Description("Command to offline teleport to someone")
@CommandPermission("mcrekus.offlineteleport")
@SuppressWarnings("unused")
public class OfflineTeleportCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("[Name]")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        switch (args.length) {
            case 0:
                player.sendMessage(ComponentUtil.deserialize(config.getOfflineTeleportNoArgsMessage()));
                return;
            case 1:
                MCRekus.getInstance().getDatabaseManager().getUserAsync(args[0], (session, data) -> {
                    if (data == null) {
                        player.sendMessage(ComponentUtil.deserialize(config.getOfflineTeleportPlayerNotFoundMessage(), null, "%player%", args[0]));
                        return;
                    }

                    if (data.getLastLocation() == null) {
                        player.sendMessage(ComponentUtil.deserialize(config.getOfflineTeleportNoLocationMessage(), null, "%player%", args[0]));
                        return;
                    }

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            player.teleport(data.getLastLocation().toBukkitLocation());
                        }
                    }.runTask(MCRekus.getInstance());

                    player.sendMessage(ComponentUtil.deserialize(config.getOfflineTeleportSuccessMessage(), null, "%player%", args[0]));
                    session.closeSession();
                });
        }

    }
}
