package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Greedy;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.Usage;
import java.util.UUID;
import org.bukkit.entity.Player;

@Command({"helpop", "help"})
@Description("Command to message admins for help")
@Permission("mcrekus.helpop")
@SuppressWarnings("unused")
public class HelpOpCommand extends ModuleAccessor<EssentialsModule> {

    private final Cooldown<UUID> cooldown = new Cooldown<>(getModule().getConfig().getHelpopCooldownTime() * 1000L);

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getHelpopNoArgsMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("message") @Suggest("wiadomość") @Greedy final String message) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        final String cooldownTime = cooldown.isOnCooldown(player.getUniqueId());
        if(!player.hasPermission("mcrekus.helpop.bypass") && !cooldownTime.equals("-1")) {
            player.sendMessage(ComponentUtil.deserialize(config.getHelpopCooldownMessage(), null, "%time%", cooldownTime));
            return;
        }

        MCRekus.getInstance().getServer().getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("mcrekus.helpop.receive"))
                .forEach(p -> p.sendMessage(ComponentUtil.deserialize(config.getHelpopMessage(), player, "%message%", message, "%player%", player.getName())));

        player.sendMessage(ComponentUtil.deserialize(config.getHelpopMessageSentMessage()));
        cooldown.addCooldown(player.getUniqueId());
    }
}
