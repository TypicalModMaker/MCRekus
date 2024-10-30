package dev.isnow.mcrekus.module.impl.essentials.command.home;


import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.menu.HomeMenu;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"home", "dom"})
@Description("Command to open home menu")
@Permission("mcrekus.home")
@SuppressWarnings("unused")
public class HomeCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getOpenHomeMessage()));

        MCRekus.getInstance().getDatabaseManager().getUserAsync(source.asPlayer(), (session, data) -> MCRekus.getInstance().getMenuAPI().openMenu(source.asPlayer(), new HomeMenu(data, session)));
    }
}
