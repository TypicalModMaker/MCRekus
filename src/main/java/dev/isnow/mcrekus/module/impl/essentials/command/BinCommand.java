package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.menu.BinMenu;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("bin|smietnik|kosz")
@Description("Command to open bin")
@CommandPermission("mcrekus.bin")
@SuppressWarnings("unused")
public class BinCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    private final BinMenu binMenu = new BinMenu();

    @Default
    public void execute(Player player, String[] args) {
        MCRekus.getInstance().getMenuAPI().openMenu(player, binMenu);

        player.sendMessage(ComponentUtil.deserialize(moduleAccessor.getModule().getConfig().getBinOpenedMessage()));
    }
}
