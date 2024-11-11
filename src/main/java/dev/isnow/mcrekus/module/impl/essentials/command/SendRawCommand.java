package dev.isnow.mcrekus.module.impl.essentials.command;

import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Greedy;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Usage;
import org.bukkit.entity.Player;

@Command("sendraw")
@Description("Command to set raw message to player")
@Permission("mcrekus.sendraw")
@SuppressWarnings("unused")
public class SendRawCommand {

    @Usage
    @Async
    public void executeDefault(final BukkitSource source) {
        source.reply(ComponentUtil.deserialize("&cUsage: /sendraw <player> <message>"));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, @Named("player") final Player player, @Named("message") @Greedy final String message) {
        player.sendMessage(ComponentUtil.deserialize(message));
    }
}
