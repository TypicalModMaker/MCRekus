package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"anvil", "kowadlo"})
@Description("Command to open anvil")
@Permission("mcrekus.anvil")
@SuppressWarnings("unused")
public class AnvilCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void openAnvil(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        player.openAnvil(null, true);

        source.reply(ComponentUtil.deserialize(config.getOpenAnvilMessage()));

        player.playSound(player.getLocation(), config.getOpenAnvilSound(), 1.0F, 1.0F);
    }
}
