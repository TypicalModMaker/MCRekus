package dev.isnow.mcrekus.module.impl.essentials.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import java.util.Map;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@CommandAlias("gm|gamemode")
@Description("Gamemode command")
@CommandPermission("mcrekus.gamemode")
@SuppressWarnings("unused")
public class GamemodeCommand extends BaseCommand {

    private static final Map<String, GameMode> GAME_MODES = Map.ofEntries(
            Map.entry("0", GameMode.SURVIVAL),
            Map.entry("survival", GameMode.SURVIVAL),
            Map.entry("przetrwanie", GameMode.SURVIVAL),
            Map.entry("1", GameMode.CREATIVE),
            Map.entry("creative", GameMode.CREATIVE),
            Map.entry("kreatywny", GameMode.CREATIVE),
            Map.entry("2", GameMode.ADVENTURE),
            Map.entry("adventure", GameMode.ADVENTURE),
            Map.entry("przygodowy", GameMode.ADVENTURE),
            Map.entry("3", GameMode.SPECTATOR),
            Map.entry("obserwator", GameMode.SPECTATOR),
            Map.entry("spectator", GameMode.SPECTATOR)
    );

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    public GamemodeCommand() {
        MCRekus.getInstance().getCommandManager().registerCompletion("gamemode", context -> GAME_MODES.keySet());
    }

    @Default
    @CommandCompletion("@gamemode @players")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getGamemodeNoArgsMessage()));
            return;
        }

        if(args.length == 1) {
            final GameMode gameMode = GAME_MODES.get(args[0].toLowerCase());

            if (gameMode != null && player.hasPermission("mcrekus.gamemode." + gameMode.name().toLowerCase())) {
                player.setGameMode(gameMode);
                player.sendMessage(ComponentUtil.deserialize(config.getGamemodeChangedMessage(), null, "%gamemode%", getTranslation(gameMode)));
                if(config.getGamemodeChangedSound() != null) {
                    player.playSound(player.getLocation(), config.getGamemodeChangedSound(), 1.0F, 1.0F);
                }
            } else {
                player.sendMessage(ComponentUtil.deserialize(config.getGamemodeInvalidMessage()));
            }

            return;
        }

        if(args.length == 2) {
            final Player target = player.getServer().getPlayer(args[1]);

            if(target == null) {
                player.sendMessage(ComponentUtil.deserialize(config.getGamemodePlayerNotFoundMessage()));
                return;
            }

            final GameMode gameMode = GAME_MODES.get(args[0].toLowerCase());

            if (gameMode != null) {
                target.setGameMode(gameMode);
                player.sendMessage(ComponentUtil.deserialize(config.getGamemodeChangedOtherMessage(), null, "%gamemode%", getTranslation(gameMode), "%player%", target.getName()));
                if(config.getGamemodeChangedSound() != null) {
                    player.playSound(player.getLocation(), config.getGamemodeChangedSound(), 1.0F, 1.0F);
                }
            } else {
                player.sendMessage(ComponentUtil.deserialize(config.getGamemodeInvalidMessage()));
            }

            return;
        }

        player.sendMessage(ComponentUtil.deserialize(config.getGamemodeUsageMessage()));
    }

    private String getTranslation(final GameMode input) {
        return switch (input) {
            case SURVIVAL -> moduleAccessor.getModule().getConfig().getGamemodeSurvival();
            case CREATIVE -> moduleAccessor.getModule().getConfig().getGamemodeCreative();
            case ADVENTURE -> moduleAccessor.getModule().getConfig().getGamemodeAdventure();
            case SPECTATOR -> moduleAccessor.getModule().getConfig().getGamemodeSpectator();
            default -> "Unknown";
        };
    }
}
