package dev.isnow.mcrekus.module.impl.spawners.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.module.impl.spawners.progress.ProgressTracker;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.entity.Player;

@CommandAlias("spawnersdisablemessage|spawner")
@Description("Command to manage spawners")
@CommandPermission("mcrekus.spawners")
@SuppressWarnings("unused")
public class SpawnersDisableMessageCommand extends BaseCommand {

    private final ModuleAccessor<SpawnersModule> moduleAccessor = new ModuleAccessor<>(SpawnersModule.class);

    @Default
    public void execute(Player player, String[] args) {
        final boolean hasDisabled = moduleAccessor.getModule().getSpawnersMessageDisabled().contains(player);

        final SpawnersConfig config = moduleAccessor.getModule().getConfig();

        if (hasDisabled) {
            moduleAccessor.getModule().getSpawnersMessageDisabled().remove(player);
            player.sendMessage(ComponentUtil.deserialize(config.getSpawnerMessageTurnedOn()));
        } else {
            moduleAccessor.getModule().getSpawnersMessageDisabled().add(player);
            player.sendMessage(ComponentUtil.deserialize(config.getSpawnerMessageTurnedOff()));
        }
    }

}
