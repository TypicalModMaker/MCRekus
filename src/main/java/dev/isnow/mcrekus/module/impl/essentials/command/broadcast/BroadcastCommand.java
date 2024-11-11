package dev.isnow.mcrekus.module.impl.essentials.command.broadcast;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Greedy;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.SuggestionProvider;
import dev.velix.imperat.annotations.Usage;
import java.time.Duration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;

@Command({"bc", "broadcast"})
@Description("Command to broadcast message")
@Permission("mcrekus.broadcast")
@SuppressWarnings("unused")
public class BroadcastCommand extends ModuleAccessor<EssentialsModule> {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        final EssentialsConfig config = getModule().getConfig();

        source.reply(ComponentUtil.deserialize(config.getBroadcastNoArgsMessage()));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("type") @SuggestionProvider("broadcastType") final String type, @Named("message") @Greedy final String message) {
        final EssentialsConfig config = getModule().getConfig();

        final BroadcastType foundType;
        try {
            foundType = BroadcastType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            source.reply(ComponentUtil.deserialize(config.getBroadcastInvalidTypeMessage()));
            return;
        }

        switch (foundType) {
            case CHAT:
                Bukkit.broadcast(ComponentUtil.deserialize(config.getBroadcastChatMessage(), null, "%message%", message));
                break;
            case TITLE:
                final Title.Times times = Title.Times.times(Duration.ofSeconds(config.getBroadcastTitleFadeIn()), Duration.ofSeconds(config.getBroadcastTitleDuration()), Duration.ofSeconds(config.getBroadcastTitleFadeOut()));
                final Title title = Title.title(ComponentUtil.deserialize(config.getBroadcastTitleMessage()), ComponentUtil.deserialize(message), times);
                Bukkit.getOnlinePlayers().forEach(p -> p.showTitle(title));
                break;
            case ACTIONBAR:
                Bukkit.getOnlinePlayers().forEach(p -> p.sendActionBar(ComponentUtil.deserialize(config.getBroadcastActionbarMessage(), null, "%message%", message)));
                break;
        }
    }
}
