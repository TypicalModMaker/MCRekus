package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

@Command({"heal", "ulecz"})
@Description("Command to heal yourself or someone else")
@Permission("mcrekus.heal")
@SuppressWarnings("unused")
public class HealCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        final Player player = source.asPlayer();
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        player.setFoodLevel(20);
        player.setSaturation(20);
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        source.reply(ComponentUtil.deserialize(config.getHealSelfMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();

        target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        target.setFoodLevel(20);
        target.setSaturation(20);
        target.getActivePotionEffects().forEach(effect -> target.removePotionEffect(effect.getType()));

        source.reply(ComponentUtil.deserialize(config.getHealSenderFormat(), null, "%player%", target.getName()));
    }
}
