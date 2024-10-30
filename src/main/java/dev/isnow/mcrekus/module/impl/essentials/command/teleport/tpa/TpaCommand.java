package dev.isnow.mcrekus.module.impl.essentials.command.teleport.tpa;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.teleport.TeleportManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import java.util.UUID;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

@Command("tpa")
@Description("Command to request teleport to someone")
@Permission("mcrekus.tpa")
@SuppressWarnings("unused")
public class TpaCommand extends ModuleAccessor<EssentialsModule> {

    private final Cooldown<UUID> cooldown = new Cooldown<>(getModule().getConfig().getTpaCooldownTime() * 1000L);

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getTpaNoArgsMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("target") final Player target) {
        final EssentialsConfig config = getModule().getConfig();
        final TeleportManager teleportManager = getModule().getTeleportManager();

        final Player player = source.asPlayer();

        final String cooldownTime = cooldown.isOnCooldown(player.getUniqueId());

        if(!cooldownTime.equals("-1")) {
            source.reply(ComponentUtil.deserialize(config.getTpaCooldownMessage(), null, "%time%", cooldownTime));
            return;
        }

//        final Player target = player.getServer().getPlayer(args[0]);
//
//        if(target == null) {
//            player.sendMessage(ComponentUtil.deserialize(config.getTpaPlayerNotFoundMessage(), null, "%player%", args[0]));
//            return;
//        }

        if(player.getUniqueId().equals(target.getUniqueId())) {
            source.reply(ComponentUtil.deserialize(config.getTpaCantTeleportToYourselfMessage()));
            return;
        }

        if(teleportManager.hasTpaRequest(target.getUniqueId())) {
            source.reply(ComponentUtil.deserialize(config.getTpaPlayerAlreadyRequestedMessage(), null, "%player%", target.getName()));
            return;
        }

        teleportManager.addTpaRequest(player.getUniqueId(), target.getUniqueId());

        target.sendMessage(ComponentUtil.deserialize(config.getTpaRequestReceivedMessage(), null, "%player%", player.getName(), "%time%", String.valueOf(config.getTpaExpiryTime())));
        source.reply(ComponentUtil.deserialize(config.getTpaRequestSentMessage(), null, "%player%", target.getName()));

        cooldown.addCooldown(player.getUniqueId());

        BukkitTask expireTask = new BukkitRunnable() {
            @Override
            public void run() {
                if(teleportManager.hasTpaRequest(target.getUniqueId()) && teleportManager.getTpaRequestTaskId(target.getUniqueId()) == getTaskId()) {
                    teleportManager.removeTpaRequest(player.getUniqueId());
                    teleportManager.removeTpaRequestTask(target.getUniqueId());
                    source.reply(ComponentUtil.deserialize(config.getTpaRequestExpiredSenderMessage(), null, "%player%", target.getName()));
                    target.sendMessage(ComponentUtil.deserialize(config.getTpaRequestExpiredReceivedMessage(), null, "%player%", player.getName()));
                } else {
                    RekusLogger.debug("Tpa request expired but was already accepted or denied");
                }
            }
        }.runTaskLaterAsynchronously(MCRekus.getInstance(), config.getTpaExpiryTime() * 20L);

        teleportManager.addTpaRequestTask(target.getUniqueId(), expireTask);
    }
}
