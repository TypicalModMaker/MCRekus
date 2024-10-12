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
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

@CommandAlias("gamma")
@Description("Command to get gamma")
@CommandPermission("mcrekus.gamma")
@SuppressWarnings("unused")
public class GammaCommand extends BaseCommand {

    private final List<Player> gammaPlayers = new ArrayList<>();

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    @Default
    public void execute(Player player, String[] args) {

        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(gammaPlayers.contains(player)) {
            gammaPlayers.remove(player);
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
            player.sendMessage(ComponentUtil.deserialize(config.getGammaOffMessage()));
        } else {
            gammaPlayers.add(player);
            player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
            player.sendMessage(ComponentUtil.deserialize(config.getGammaOnMessage()));
        }
    }
}
