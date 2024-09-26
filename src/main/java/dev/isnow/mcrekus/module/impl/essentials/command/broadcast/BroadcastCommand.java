package dev.isnow.mcrekus.module.impl.essentials.command.broadcast;

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
import java.time.Duration;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Player;

@CommandAlias("bc|broadcast")
@Description("Command to broadcast message")
@CommandPermission("mcrekus.broadcast")
@SuppressWarnings("unused")
public class BroadcastCommand extends BaseCommand {

    private final ModuleAccessor<EssentialsModule> moduleAccessor = new ModuleAccessor<>(EssentialsModule.class);

    public BroadcastCommand() {
        MCRekus.getInstance().getCommandManager().registerCompletion("broadcastType", context -> Arrays.stream(BroadcastType.values())
                .map(BroadcastType::name)
                .collect(Collectors.toList()));
    }

    @Default
    @CommandCompletion("@broadcastType [message]")
    public void execute(Player player, String[] args) {
        final EssentialsConfig config = moduleAccessor.getModule().getConfig();

        if(args.length == 0) {
            player.sendMessage(ComponentUtil.deserialize(config.getBroadcastNoArgsMessage()));
            return;
        }

        final BroadcastType type;
        try {
            type = BroadcastType.valueOf(args[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            player.sendMessage(ComponentUtil.deserialize(config.getBroadcastInvalidTypeMessage()));
            return;
        }

        final String message = String.join(" ", args).replaceFirst(args[0] + " ", "");

        switch (type) {
            case CHAT:
                player.getServer().broadcast(ComponentUtil.deserialize(config.getBroadcastChatMessage(), null, "%message%", message));
                break;
            case TITLE:
                final Title.Times times = Title.Times.times(Duration.ofSeconds(config.getBroadcastTitleFadeIn()), Duration.ofSeconds(config.getBroadcastTitleDuration()), Duration.ofSeconds(config.getBroadcastTitleFadeOut()));
                final Title title = Title.title(ComponentUtil.deserialize(config.getBroadcastTitleMessage()), ComponentUtil.deserialize(message), times);
                player.getServer().getOnlinePlayers().forEach(p -> p.showTitle(title));
                break;
            case ACTIONBAR:
                player.getServer().getOnlinePlayers().forEach(p -> p.sendActionBar(ComponentUtil.deserialize(config.getBroadcastActionbarMessage(), null, "%message%", message)));
                break;
        }
    }
}