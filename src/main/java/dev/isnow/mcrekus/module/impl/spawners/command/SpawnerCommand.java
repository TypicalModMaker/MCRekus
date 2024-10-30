package dev.isnow.mcrekus.module.impl.spawners.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.config.SpawnersConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import java.util.List;
import org.bukkit.entity.Player;

@Command({"spawnersdisablemessage", "spawner"})
@Description("Command to manage spawners")
@Permission("mcrekus.spawner")
@SuppressWarnings("unused")
public class SpawnerCommand extends ModuleAccessor<SpawnersModule> {

    @Usage
    @Async
    public void execute(Player player, String[] args) {
        final List<Player> disabled = getModule().getSpawnersMessageDisabled();

        final boolean hasDisabled = disabled.contains(player);

        final SpawnersConfig config = getModule().getConfig();

        if (hasDisabled) {
            disabled.remove(player);
            player.sendMessage(ComponentUtil.deserialize(config.getSpawnerMessageTurnedOn()));
        } else {
            disabled.add(player);
            player.sendMessage(ComponentUtil.deserialize(config.getSpawnerMessageTurnedOff()));
        }
    }

}
