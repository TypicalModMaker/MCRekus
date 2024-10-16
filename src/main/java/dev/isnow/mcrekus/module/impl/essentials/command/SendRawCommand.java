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
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

@CommandAlias("sendraw")
@Description("Command to set raw message to player")
@CommandPermission("mcrekus.sendraw")
@SuppressWarnings("unused")
public class SendRawCommand extends BaseCommand {

    @Default
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(ComponentUtil.deserialize("&cUsage: /sendraw <player> <message>"));
            return;
        }

        final Player target = Bukkit.getPlayer(args[0]);
        if(target == null) {
            sender.sendMessage(ComponentUtil.deserialize("&cPlayer not found!"));
            return;
        }

        final String message = String.join(" ", args).replace(args[0] + " ", "");
        target.sendMessage(ComponentUtil.deserialize(message));
    }
}
