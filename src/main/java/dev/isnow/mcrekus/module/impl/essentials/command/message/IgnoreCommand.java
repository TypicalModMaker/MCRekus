package dev.isnow.mcrekus.module.impl.essentials.command.message;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.message.MessageManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"ignore", "zignoruj"})
@Description("Command to ignore someone")
@Permission("mcrekus.ignore")
@SuppressWarnings("unused")
public class IgnoreCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(getModule().getConfig().getIgnoreUsageMessage()));
    }
    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();
        final Player player = source.asPlayer();
//
//        final Player target = Bukkit.getPlayer(args[0]);
//        if (target == null) {
//            player.sendMessage(
//                    ComponentUtil.deserialize(config.getMessagePlayerNotFoundMessage(), null,
//                            "%player%", args[0]));
//            return;
//        }

        final MessageManager messageManager = getModule().getMessageManager();

        if (messageManager.isIgnored(player.getUniqueId(), target.getUniqueId())) {
            player.sendMessage(ComponentUtil.deserialize(config.getMessageUnignoringMessage(), null,
                    "%player%", target.getName()));

            messageManager.removeIgnoredPlayer(player.getUniqueId(), target.getUniqueId());
        } else {
            player.sendMessage(
                    ComponentUtil.deserialize(config.getMessageIgnoringMessage(), null, "%player%",
                            target.getName()));

            messageManager.addIgnoredPlayer(player.getUniqueId(), target.getUniqueId());
        }

    }
}
