package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@CommandAlias("rename")
@Description("Command to rename an item")
@CommandPermission("mcrekus.rename")
@SuppressWarnings("unused")
public class RenameCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getRenameNoArgsMessage()));
            return;
        }

        final String name = String.join(" ", args);

        final ItemStack item = player.getInventory().getItemInMainHand();
        final ItemMeta itemMeta = item.getItemMeta();
        itemMeta.displayName(ComponentUtil.deserialize(name));
        item.setItemMeta(itemMeta);

        player.sendMessage(ComponentUtil.deserialize(config.getRenameMessage(), null, "%name%", name));
        player.playSound(player.getLocation(), config.getRenameSound(), 1.0F, 1.0F);
    }
}
