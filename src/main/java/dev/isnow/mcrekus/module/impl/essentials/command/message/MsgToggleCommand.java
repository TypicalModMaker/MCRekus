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

@CommandAlias("msgtoggle|wylaczwiadomosci")
@Description("Command to toggle messages globally")
@CommandPermission("mcrekus.msgtoggle")
@SuppressWarnings("unused")
public class MsgToggleCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        final MessageManager messageManager = moduleAccessor.getModule().getMessageManager();

        if (messageManager.isMessagesEnabled(player.getUniqueId())) {
            player.sendMessage(ComponentUtil.deserialize(config.getMsgToggleDisabledMessage()));
            messageManager.disableMessages(player.getUniqueId());
        } else {
            player.sendMessage(ComponentUtil.deserialize(config.getMsgToggleEnabledMessage()));
            messageManager.enableMessages(player.getUniqueId());
        }
    }

}
