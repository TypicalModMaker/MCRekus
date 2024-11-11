package dev.isnow.mcrekus.module.impl.timeshop.commands;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.timeshop.TimeShopModule;
import dev.isnow.mcrekus.module.impl.timeshop.menu.TimeShopMenu;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"timeshop", "sklepzaczas", "czas"})
@Description("Command to open time shop gui")
@Permission("mcrekus.timeshop")
@SuppressWarnings("unused")
public class TimeShopCommand extends ModuleAccessor<TimeShopModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        source.reply(ComponentUtil.deserialize(getModule().getConfig().getTimeShopOpenMessage()));

        final Player player = source.asPlayer();

        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
            MCRekus.getInstance().getMenuAPI().openMenu(player, new TimeShopMenu(data));
        });
    }

    @Usage
    @Async
    @Permission("mcrekus.timeshop.admin")
    public void addTimeOther(final BukkitSource source, @Named("player") final Player target, @Named("time") final long time) {
        getModule().getJoinTime().put(target, getModule().getJoinTime().get(target) - time);

        source.reply(ComponentUtil.deserialize("&aPomyslnie dodano czas!"));
    }
}
