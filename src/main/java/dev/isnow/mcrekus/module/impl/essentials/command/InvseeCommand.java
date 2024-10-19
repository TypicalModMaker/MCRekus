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
import dev.isnow.mcrekus.module.impl.essentials.menu.InvseeMenu;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@CommandAlias("invsee")
@Description("Command to preview players inventory")
@CommandPermission("mcrekus.invsee")
@SuppressWarnings("unused")
public class InvseeCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    @CommandCompletion("@players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();
        if (args.length < 1) {
            player.sendMessage(ComponentUtil.deserialize(config.getInvseeUsageMessage()));
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(ComponentUtil.deserialize(config.getInvseePlayerNotFoundMessage(), null, "%player%", args[0]));
            return;
        }

        if (moduleAccessor.getModule().getInvseeMenus().containsKey(target)) {
            player.sendMessage(ComponentUtil.deserialize(config.getInvseeAlreadyOpenMessage(), null, "%player%", target.getName()));
            return;
        }

        final InvseeMenu menu = new InvseeMenu(player, target);

        moduleAccessor.getModule().getInvseeMenus().put(target, menu);
        MCRekus.getInstance().getMenuAPI().openMenu(player, menu);
    }
}
