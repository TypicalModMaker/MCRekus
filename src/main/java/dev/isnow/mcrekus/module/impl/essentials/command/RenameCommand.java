package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Greedy;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@Command("rename")
@Description("Command to rename an item")
@Permission("mcrekus.rename")
@SuppressWarnings("unused")
public class RenameCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getRenameNoArgsMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("name") @Greedy final String name) {
        final EssentialsConfig config = getModule().getConfig();
        final Player player = source.asPlayer();

        final ItemStack item = player.getInventory().getItemInMainHand();
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(ComponentUtil.deserialize(name));
        item.setItemMeta(itemMeta);

        source.reply(ComponentUtil.deserialize(config.getRenameMessage(), null, "%name%", name));
        player.playSound(player.getLocation(), config.getRenameSound(), 1.0F, 1.0F);
    }
}
