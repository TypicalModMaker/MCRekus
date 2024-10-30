package dev.isnow.mcrekus.module.impl.xray.command;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.xray.XrayModule;
import dev.isnow.mcrekus.module.impl.xray.config.XrayConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Greedy;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@Command("banxray")
@Description("Command to clear and ban a player")
@Permission("mcrekus.banxray")
@SuppressWarnings("unused")
public class BanXrayCommand extends ModuleAccessor<XrayModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final XrayConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getBanXrayUsageMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("player") final Player player, @Named("args") @Greedy final String args) {
        final XrayConfig config = getModule().getConfig();

        player.getInventory().clear();
        Bukkit.dispatchCommand(source.origin(), "tempban " + player.getName() + " Udowodniono u Ciebie używanie niedozwolonej modyfikacji tekstur - X-ray. " + String.join(" ", args));

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isOnline()) {
                    source.reply(ComponentUtil.deserialize(config.getBanXrayNotSuccessfulMessage(), null, "%player%", player.getName()));
                } else {
                    source.reply(ComponentUtil.deserialize(config.getBanXraySuccessMessage(), null, "%player%", player.getName()));
                }
            }
        }.runTaskLaterAsynchronously(MCRekus.getInstance(), 20L);
    }
}
