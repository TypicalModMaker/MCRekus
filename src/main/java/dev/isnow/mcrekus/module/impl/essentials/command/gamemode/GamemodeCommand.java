package dev.isnow.mcrekus.module.impl.essentials.command.gamemode;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.DefaultProvider;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.SuggestionProvider;
import dev.velix.imperat.annotations.Usage;
import java.util.Map;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@Command({"gm", "gamemode"})
@Description("Gamemode command")
@Permission("mcrekus.gamemode")
@SuppressWarnings("unused")
public class GamemodeCommand extends ModuleAccessor<EssentialsModule> {

    public static final Map<String, GameMode> GAME_MODES = Map.ofEntries(
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

    @Usage
    @Async
    public void usage(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getGamemodeNoArgsMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("gamemode") @SuggestionProvider("gamemode") final String gamemode) {
        final EssentialsConfig config = getModule().getConfig();

        final GameMode foundGameMode = GAME_MODES.get(gamemode.toLowerCase());

        final Player player = source.asPlayer();

        if (foundGameMode != null && player.hasPermission("mcrekus.gamemode." + foundGameMode.name().toLowerCase())) {
            player.setGameMode(foundGameMode);
            player.setAllowFlight(foundGameMode == GameMode.CREATIVE || foundGameMode == GameMode.SPECTATOR);
            player.sendMessage(ComponentUtil.deserialize(config.getGamemodeChangedMessage(), null, "%gamemode%", getTranslation(foundGameMode)));
            if(config.getGamemodeChangedSound() != null) {
                player.playSound(player.getLocation(), config.getGamemodeChangedSound(), 1.0F, 1.0F);
            }
        } else {
            player.sendMessage(ComponentUtil.deserialize(config.getGamemodeInvalidMessage()));
        }

    }


    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("gamemode") @SuggestionProvider("gamemode") final String gamemode, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();

        final GameMode foundGameMode = GAME_MODES.get(gamemode.toLowerCase());

        if (foundGameMode != null) {
            target.setGameMode(foundGameMode);
            target.setAllowFlight(foundGameMode == GameMode.CREATIVE || foundGameMode == GameMode.SPECTATOR);
            source.reply(ComponentUtil.deserialize(config.getGamemodeChangedOtherMessage(), null, "%gamemode%", getTranslation(foundGameMode), "%player%", target.getName()));
            if(config.getGamemodeChangedSound() != null && !source.isConsole()) {
                final Player player = source.asPlayer();

                player.playSound(player.getLocation(), config.getGamemodeChangedSound(), 1.0F, 1.0F);
            }
        } else {
            source.reply(ComponentUtil.deserialize(config.getGamemodeInvalidMessage()));
        }
    }



    private String getTranslation(final GameMode input) {
        return switch (input) {
            case SURVIVAL -> getModule().getConfig().getGamemodeSurvival();
            case CREATIVE -> getModule().getConfig().getGamemodeCreative();
            case ADVENTURE -> getModule().getConfig().getGamemodeAdventure();
            case SPECTATOR -> getModule().getConfig().getGamemodeSpectator();
            default -> "Unknown";
        };
    }
}

