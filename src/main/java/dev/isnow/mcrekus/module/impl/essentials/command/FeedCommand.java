package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("feed|nakarm")
@Description("Command to feed yourself or someone else")
@CommandPermission("mcrekus.feed")
@SuppressWarnings("unused")
public class FeedCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("@players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.setFoodLevel(20);
            player.sendMessage(ComponentUtil.deserialize(config.getFeedSelfMessage()));
            return;
        }

        final Player target = player.getServer().getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getFeedPlayerNotFoundMessage(), null, "%player%", args[0]));
            return;
        }

        target.setFoodLevel(20);
        player.sendMessage(ComponentUtil.deserialize(config.getFeedSenderFormat(), null, "%player%", target.getName()));
    }
}
