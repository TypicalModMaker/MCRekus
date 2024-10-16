package dev.isnow.mcrekus.module.impl.timeshop.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.timeshop.TimeShopModule;
import dev.isnow.mcrekus.module.impl.timeshop.menu.TimeShopMenu;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("timeshop|sklepzaczas|czas")
@Description("Command to open time shop gui")
@CommandPermission("mcrekus.timeshop")
@SuppressWarnings("unused")
public class TimeShopCommand extends BaseCommand {
    private final ModuleAccessor<TimeShopModule> moduleAccessor = new ModuleAccessor<>(TimeShopModule.class);

    @Default
    public void execute(Player player, String[] args) {
        player.sendMessage(ComponentUtil.deserialize(moduleAccessor.getModule().getConfig().getTimeShopOpenMessage()));

        if(args.length > 2 && player.hasPermission("mcrekus.timeshop.admin")) {
            if(args[0].equalsIgnoreCase("addTime")) {
                final Player target = MCRekus.getInstance().getServer().getPlayer(args[1]);
                if(target == null) {
                    player.sendMessage(ComponentUtil.deserialize("&cGracz nie jest online!"));
                    return;
                }

                moduleAccessor.getModule().getJoinTime().put(target, moduleAccessor.getModule().getJoinTime().get(target) - Long.parseLong(args[2]));
                player.sendMessage(ComponentUtil.deserialize("&aPomyslnie dodano czas!"));
                return;
            }
        }

        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
            MCRekus.getInstance().getMenuAPI().openMenu(player, new TimeShopMenu(data));
        });
    }
}
