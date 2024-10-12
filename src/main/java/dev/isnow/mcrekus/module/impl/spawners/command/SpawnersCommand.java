package dev.isnow.mcrekus.module.impl.spawners.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.spawners.SpawnersModule;
import dev.isnow.mcrekus.module.impl.spawners.progress.ProgressTracker;
import dev.isnow.mcrekus.module.impl.spawners.spawners.RekusSpawner;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

@CommandAlias("spawners")
@Description("Command to manage spawners")
@CommandPermission("mcrekus.spawners")
@SuppressWarnings("unused")
public class SpawnersCommand extends BaseCommand {

    private final static String BASE_LINE = "━━━━━━━━";

    private final ModuleAccessor<SpawnersModule> moduleAccessor = new ModuleAccessor<>(SpawnersModule.class);

    @Default
    @CommandCompletion("testBar start|stop")
    public void execute(Player player, String[] args) {
        if(args.length < 1) {
            player.sendMessage(ComponentUtil.deserialize("&cUsage: /spawners <testBar>"));
            return;
        }

        final boolean hasProgressTracker = moduleAccessor.getModule().hasProgressTracker(player);

        if(args[0].equalsIgnoreCase("testBar")) {
            if(args.length < 2) {
                player.sendMessage(ComponentUtil.deserialize("&cUsage: /spawners testBar <start|stop>"));
                return;
            }

            if(args[1].equalsIgnoreCase("start")) {
                if(hasProgressTracker) {
                    player.sendMessage(ComponentUtil.deserialize("&cYou already have a title animation running!"));
                    return;
                }

                final ProgressTracker progressTracker = new ProgressTracker(player);
                progressTracker.setup();
                moduleAccessor.getModule().addProgressTracker(player, progressTracker);
            } else if(args[1].equalsIgnoreCase("stop")) {
                if(!hasProgressTracker) {
                    player.sendMessage(ComponentUtil.deserialize("&cYou don't have a title animation running!"));
                    return;
                }

                final ProgressTracker progressTracker = moduleAccessor.getModule().getProgressTracker(player);
                progressTracker.getTask().cancel();
                moduleAccessor.getModule().removeProgressTracker(player);
            }
        }
    }

}
