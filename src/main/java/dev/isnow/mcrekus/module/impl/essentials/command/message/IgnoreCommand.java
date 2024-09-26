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

@CommandAlias("ignore|zignoruj")
@Description("Command to ignore someone")
@CommandPermission("mcrekus.ignore")
@SuppressWarnings("unused")
public class IgnoreCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(
            EssentialsModule.class);

    @Default
    @CommandCompletion("@players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if (args.length < 1) {
            player.sendMessage(ComponentUtil.deserialize(config.getIgnoreUsageMessage()));
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(
                    ComponentUtil.deserialize(config.getMessagePlayerNotFoundMessage(), null,
                            "%player%", args[0]));
            return;
        }

        final MessageManager messageManager = moduleAccessor.getModule().getMessageManager();

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
