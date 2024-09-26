package dev.isnow.mcrekus.module.impl.essentials.command.message;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.message.MessageManager;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("r|reply|odpisz")
@Description("Command to reply to someone")
@CommandPermission("mcrekus.message")
@SuppressWarnings("unused")
public class ReplyCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("[wiadomość]")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length < 1) {
            player.sendMessage(ComponentUtil.deserialize(config.getReplyUsageMessage()));
            return;
        }

        final MessageManager messageManager = moduleAccessor.getModule().getMessageManager();

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

        final String message = String.join(" ", args);

        target.sendMessage(ComponentUtil.deserialize(config.getMessageSenderFormat(), null, "%player%", player.getName(), "%message%", message, "%author%", target.getName()));
        player.sendMessage(ComponentUtil.deserialize(config.getMessageReceiverFormat(), null, "%player%", target.getName(), "%message%", message, "%author%", player.getName()));

        target.playSound(target.getLocation(), config.getMessageReceivedSound(), 1.0F, 1.0F);

        messageManager.setLastMessage(player.getUniqueId(), target.getUniqueId());
        messageManager.setLastMessage(target.getUniqueId(), player.getUniqueId());
    }
}
