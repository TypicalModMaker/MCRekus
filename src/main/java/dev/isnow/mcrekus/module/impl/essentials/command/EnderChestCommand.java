package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command({"ec", "enderchest", "ender"})
@Description("Command to open enderchest")
@Permission("mcrekus.enderchest")
@SuppressWarnings("unused")
public class EnderChestCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void openEnderChest(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        player.openInventory(player.getEnderChest());

        source.reply(ComponentUtil.deserialize(config.getOpenEnderChestMessage()));

        player.playSound(player.getLocation(), config.getOpenEnderChestSound(), 1.0F, 1.0F);
    }

    @Usage
    @Async
    @Permission("mcrekus.enderchest.other")
    public void openEnderChestOther(final BukkitSource source, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        source.reply(ComponentUtil.deserialize(config.getOpenEnderChestMessage(), null, "%player%", target.getName()));

        player.openInventory(target.getEnderChest());
    }
}
