package dev.isnow.mcrekus.module.impl.essentials.command.teleport.tpa;

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
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@CommandAlias("tpa")
@Description("Command to request teleport to someone")
@CommandPermission("mcrekus.tpa")
@SuppressWarnings("unused")
public class TpaCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    private final Cooldown<UUID> cooldown = new Cooldown<>(moduleAccessor.getModule().getConfig().getTpaCooldownTime() * 1000L);

    @Default
    @CommandCompletion("@players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();
        final TeleportManager teleportManager = moduleAccessor.getModule().getTeleportManager();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getTpaNoArgsMessage()));
            return;
        }

        final String cooldownTime = cooldown.isOnCooldown(player.getUniqueId());

        if(!cooldownTime.equals("-1")) {
            player.sendMessage(ComponentUtil.deserialize(config.getTpaCooldownMessage(), null, "%time%", cooldownTime));
            return;
        }

        final Player target = player.getServer().getPlayer(args[0]);

        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getTpaPlayerNotFoundMessage(), null, "%player%", args[0]));
            return;
        }

        if(player.getUniqueId().equals(target.getUniqueId())) {
            player.sendMessage(ComponentUtil.deserialize(config.getTpaCantTeleportToYourselfMessage()));
            return;
        }

        if(teleportManager.hasTpaRequest(target.getUniqueId())) {
            player.sendMessage(ComponentUtil.deserialize(config.getTpaPlayerAlreadyRequestedMessage(), null, "%player%", target.getName()));
            return;
        }

        teleportManager.addTpaRequest(player.getUniqueId(), target.getUniqueId());

        target.sendMessage(ComponentUtil.deserialize(config.getTpaRequestReceivedMessage(), null, "%player%", player.getName(), "%time%", String.valueOf(config.getTpaExpiryTime())));
        player.sendMessage(ComponentUtil.deserialize(config.getTpaRequestSentMessage(), null, "%player%", target.getName()));

        cooldown.addCooldown(player.getUniqueId());

        BukkitTask expireTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(teleportManager.hasTpaRequest(target.getUniqueId()) && teleportManager.getTpaRequestTaskId(target.getUniqueId()) == getTaskId()) {
                    teleportManager.removeTpaRequest(player.getUniqueId());
                    teleportManager.removeTpaRequestTask(target.getUniqueId());
                    player.sendMessage(ComponentUtil.deserialize(config.getTpaRequestExpiredSenderMessage(), null, "%player%", target.getName()));
                    target.sendMessage(ComponentUtil.deserialize(config.getTpaRequestExpiredReceivedMessage(), null, "%player%", player.getName()));
                } else {
                    RekusLogger.debug("Tpa request expired but was already accepted or denied");
                }
            }
        }.runTaskLaterAsynchronously(MCRekus.getInstance(), config.getTpaExpiryTime() * 20L);

        teleportManager.addTpaRequestTask(target.getUniqueId(), expireTask);
    }
}
