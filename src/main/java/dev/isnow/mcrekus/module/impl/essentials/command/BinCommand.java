package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.menu.BinMenu;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"bin", "smietnik", "kosz"})
@Description("Command to open bin")
@Permission("mcrekus.bin")
@SuppressWarnings("unused")
public class BinCommand extends ModuleAccessor<EssentialsModule> {

    private final BinMenu binMenu = new BinMenu();

    @Usage
    @Async
    public void openBin(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        MCRekus.getInstance().getMenuAPI().openMenu(player, binMenu);

        source.reply(ComponentUtil.deserialize(config.getBinOpenedMessage()));
    }
}
