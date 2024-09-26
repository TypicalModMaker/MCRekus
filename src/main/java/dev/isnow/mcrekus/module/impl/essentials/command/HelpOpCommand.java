package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cooldown.Cooldown;
import java.util.UUID;
import org.bukkit.entity.Player;

@CommandAlias("helpop|help|hop")
@Description("Command to message admins for help")
@CommandPermission("mcrekus.helpop")
@SuppressWarnings("unused")
public class HelpOpCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);
    private final Cooldown<UUID> cooldown = new Cooldown<>(moduleAccessor.getModule().getConfig().getHelpopCooldownTime() * 1000L);

    @Default
    @CommandCompletion("wiadomość")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getHelpopNoArgsMessage()));
            return;
        }

        final String cooldownTime = cooldown.isOnCooldown(player.getUniqueId());
        if(!player.hasPermission("mcrekus.helpop.bypass") && !cooldownTime.equals("-1")) {
            player.sendMessage(ComponentUtil.deserialize(config.getHelpopCooldownMessage(), null, "%time%", cooldownTime));
            return;
        }

        final String message = String.join(" ", args);
        MCRekus.getInstance().getServer().getOnlinePlayers().stream()
                .filter(p -> p.hasPermission("mcrekus.helpop.receive"))
                .forEach(p -> p.sendMessage(ComponentUtil.deserialize(config.getHelpopMessage(), player, "%message%", message, "%player%", player.getName())));

        player.sendMessage(ComponentUtil.deserialize(config.getHelpopMessageSentMessage()));
    }
}
