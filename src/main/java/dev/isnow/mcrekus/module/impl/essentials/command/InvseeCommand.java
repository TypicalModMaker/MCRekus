package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.essentials.menu.InvseeMenu;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command("invsee")
@Description("Command to preview players inventory")
@Permission("mcrekus.invsee")
@SuppressWarnings("unused")
public class InvseeCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getInvseeUsageMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();

        if (getModule().getInvseeMenus().containsKey(target)) {
            source.reply(ComponentUtil.deserialize(config.getInvseeAlreadyOpenMessage(), null, "%player%", target.getName()));
            return;
        }

        final InvseeMenu menu = new InvseeMenu(source.asPlayer(), target);
        menu.doRunnable();
        getModule().getInvseeMenus().put(target, menu);
        MCRekus.getInstance().getMenuAPI().openMenu(source.asPlayer(), menu);
    }
}
