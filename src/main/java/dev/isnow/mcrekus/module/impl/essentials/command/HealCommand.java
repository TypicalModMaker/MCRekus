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
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

@CommandAlias("heal|ulecz")
@Description("Command to heal yourself or someone else")
@CommandPermission("mcrekus.heal")
@SuppressWarnings("unused")
public class HealCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("@players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
            player.setFoodLevel(20);
            player.setSaturation(20);
            player.sendMessage(ComponentUtil.deserialize(config.getHealSelfMessage()));
            return;
        }

        final Player target = player.getServer().getPlayer(args[0]);
        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getHealPlayerNotFoundMessage(), null, "%player%", args[0]));
            return;
        }

        target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        target.setFoodLevel(20);
        target.setSaturation(20);
        player.sendMessage(ComponentUtil.deserialize(config.getHealSenderFormat(), null, "%player%", target.getName()));
    }
}
