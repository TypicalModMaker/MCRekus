package dev.isnow.mcrekus.module.impl.essentials.command.home;


import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.home.menu.HomeMenu;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("home|dom")
@Description("Command to open home menu")
@CommandPermission("mcrekus.home")
@SuppressWarnings("unused")
public class HomeCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    private final HomeMenu homeMenu = new HomeMenu();

    @Default
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        player.sendMessage(ComponentUtil.deserialize(config.getOpenHomeMessage()));

        MCRekus.getInstance().getMenuAPI().openMenu(player, homeMenu);
    }
}
