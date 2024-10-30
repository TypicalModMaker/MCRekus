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
import dev.velix.imperat.annotations.Greedy;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"r", "reply", "odpisz"})
@Description("Command to reply to someone")
@Permission("mcrekus.message")
@SuppressWarnings("unused")
public class ReplyCommand extends ModuleAccessor<EssentialsModule> {

    @Async
    @Usage
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getReplyUsageMessage()));
    }

    @Async
    @Usage
    public void execute(final BukkitSource source, @Named("message") @Suggest("wiadomość") @Greedy final String message) {
        final EssentialsConfig config = getModule().getConfig();

        final MessageManager messageManager = getModule().getMessageManager();

        final Player player = source.asPlayer();

        if(!messageManager.hasLastMessage(player.getUniqueId())) {
            player.sendMessage(ComponentUtil.deserialize(config.getMessageNoLastMessage()));
            return;
        }

        final Player target = player.getServer().getPlayer(messageManager.getLastMessage(player.getUniqueId()));

        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getMessagePlayerNotFoundMessage(), null, "%player%", messageManager.getLastMessage(player.getUniqueId())));
            return;
        }

        if (messageManager.isIgnored(target.getUniqueId(), player.getUniqueId()) || !messageManager.isMessagesEnabled(target.getUniqueId())) {
            player.sendMessage(ComponentUtil.deserialize(config.getMessagePlayerIgnoredMessage(), null, "%player%", target.getName()));
            return;
        }

        target.sendMessage(ComponentUtil.deserialize(config.getMessageSenderFormat(), null, "%player%", player.getName(), "%message%", message, "%author%", target.getName()));
        player.sendMessage(ComponentUtil.deserialize(config.getMessageReceiverFormat(), null, "%player%", target.getName(), "%message%", message, "%author%", player.getName()));

        target.playSound(target.getLocation(), config.getMessageReceivedSound(), 1.0F, 1.0F);

        messageManager.setLastMessage(player.getUniqueId(), target.getUniqueId());
        messageManager.setLastMessage(target.getUniqueId(), player.getUniqueId());
    }
}
