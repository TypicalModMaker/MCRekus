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

@CommandAlias("anvil|kowadlo|kowad")
@Description("Command to open anvil")
@CommandPermission("mcrekus.anvil")
@SuppressWarnings("unused")
public class AnvilCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        player.openAnvil(null, true);

        player.sendMessage(ComponentUtil.deserialize(config.getOpenAnvilMessage()));

        player.playSound(player.getLocation(), config.getOpenAnvilSound(), 1.0F, 1.0F);
    }
}
