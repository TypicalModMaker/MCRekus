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

@CommandAlias("ec|enderchest|ender")
@Description("Command to open enderchest")
@CommandPermission("mcrekus.enderchest")
@SuppressWarnings("unused")
public class EnderChestCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        player.openInventory(player.getEnderChest());

        player.sendMessage(ComponentUtil.deserialize(config.getOpenEnderChestMessage()));

        player.playSound(player.getLocation(), config.getOpenEnderChestSound(), 1.0F, 1.0F);
    }
}
