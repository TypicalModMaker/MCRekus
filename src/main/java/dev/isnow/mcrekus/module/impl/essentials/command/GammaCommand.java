package dev.isnow.mcrekus.module.impl.essentials.command;


import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@Command("gamma")
@Description("Command to get gamma")
@Permission("mcrekus.gamma")
@SuppressWarnings("unused")
public class GammaCommand extends ModuleAccessor<EssentialsModule> {

    private final List<Player> gammaPlayers = new ArrayList<>();

    @Usage
    @Async
    public void execute(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();

        if(gammaPlayers.contains(player)) {
            gammaPlayers.remove(player);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            source.reply(ComponentUtil.deserialize(config.getGammaOffMessage()));
        } else {
            gammaPlayers.add(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
            source.reply(ComponentUtil.deserialize(config.getGammaOnMessage()));
        }
    }
}
