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
import org.bukkit.entity.Player;

@Command({"feed", "nakarm"})
@Description("Command to feed yourself or someone else")
@Permission("mcrekus.feed")
@SuppressWarnings("unused")
public class FeedCommand extends ModuleAccessor<EssentialsModule> {

    @Async
    @Usage
    public void defaultUsage(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();
        final Player player = source.asPlayer();

        player.setFoodLevel(20);
        source.reply(ComponentUtil.deserialize(config.getFeedSelfMessage()));
    }

    @Async
    @Usage
    public void feedOther(final BukkitSource source, @Named("player") final Player target) {
        final EssentialsConfig config = getModule().getConfig();

        target.setFoodLevel(20);
        source.reply(ComponentUtil.deserialize(config.getFeedSenderFormat(), null, "%player%", target.getName()));
    }
}
