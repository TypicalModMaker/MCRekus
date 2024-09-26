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
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("message|msg|wiadomosc")
@Description("Command to message someone")
@CommandPermission("mcrekus.message")
@SuppressWarnings("unused")
public class MessageCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("@players [wiadomość]")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length < 2) {
            player.sendMessage(ComponentUtil.deserialize(config.getMessageUsageMessage()));
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getMessagePlayerNotFoundMessage(), null, "%player%", args[0]));
            return;
        }

        final MessageManager messageManager = moduleAccessor.getModule().getMessageManager();

        if (messageManager.isIgnored(target.getUniqueId(), player.getUniqueId()) || !messageManager.isMessagesEnabled(target.getUniqueId())) {
            player.sendMessage(ComponentUtil.deserialize(config.getMessagePlayerIgnoredMessage(), null, "%player%", target.getName()));
            return;
        }

        if(target == player) {
            player.sendMessage(ComponentUtil.deserialize(config.getMessageYourselfMessage()));
            return;
        }


        final String message = String.join(" ", args).substring(args[0].length() + 1);
        target.sendMessage(ComponentUtil.deserialize(config.getMessageSenderFormat(), null, "%player%", player.getName(), "%message%", message, "%author%", target.getName()));
        player.sendMessage(ComponentUtil.deserialize(config.getMessageReceiverFormat(), null, "%player%", target.getName(), "%message%", message, "%author%", player.getName()));
        target.playSound(target.getLocation(), config.getMessageReceivedSound(), 1.0F, 1.0F);

        messageManager.setLastMessage(player.getUniqueId(), target.getUniqueId());
        messageManager.setLastMessage(target.getUniqueId(), player.getUniqueId());
    }
}
