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
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"msgtoggle", "wylaczwiadomosci"})
@Description("Command to toggle messages globally")
@Permission("mcrekus.msgtoggle")
@SuppressWarnings("unused")
public class MsgToggleCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final Player player = source.asPlayer();

        final EssentialsConfig config = getModule().getConfig();

        final MessageManager messageManager = getModule().getMessageManager();

        if (messageManager.isMessagesEnabled(player.getUniqueId())) {
            source.reply(ComponentUtil.deserialize(config.getMsgToggleDisabledMessage()));
            messageManager.disableMessages(player.getUniqueId());
        } else {
            source.reply(ComponentUtil.deserialize(config.getMsgToggleEnabledMessage()));
            messageManager.enableMessages(player.getUniqueId());
        }
    }

}
