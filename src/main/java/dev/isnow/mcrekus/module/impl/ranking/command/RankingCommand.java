package dev.isnow.mcrekus.module.impl.ranking.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.essentials.EssentialsModule;
import dev.isnow.mcrekus.module.impl.essentials.config.EssentialsConfig;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@CommandAlias("ranking")
@Description("Command to manage ranking")
@CommandPermission("mcrekus.ranking")
@SuppressWarnings("unused")
public class RankingCommand extends BaseCommand {

    private final ModuleAccessor<RankingModule> moduleAccessor = new ModuleAccessor<>(RankingModule.class);

    @Default
    @CommandCompletion("set|add|remove|reset @players [amount]")
    public void execute(Player player, String[] args) {
        if(args.length < 2) {
            player.sendMessage(ComponentUtil.deserialize("[P] &cUsage: /ranking <set|add|remove|reset> <player> [amount]"));
            return;
        }

        final String action = args[0];
        final Player target = player.getServer().getPlayer(args[1]);
        if(target == null) {
            player.sendMessage(ComponentUtil.deserialize("[P] &cPlayer not found!"));
            return;
        }

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        if(action.equalsIgnoreCase("reset")) {
            moduleAccessor.getModule().getRankingCache().put(target, moduleAccessor.getModule().getConfig().getDefaultElo());
            player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking reset"));
            return;
        }

        if(args.length < 3) {
            player.sendMessage(ComponentUtil.deserialize("[P] &cUsage: /ranking " + args[0] + " " + target.getName() + " <amount>"));
            return;
        }

        final int oldRanking = moduleAccessor.getModule().getRankingCache().getOrDefault(target, moduleAccessor.getModule().getConfig().getDefaultElo());

        switch (action) {
            case "set":
                final int amount = Integer.parseInt(args[2]);
                moduleAccessor.getModule().getRankingCache().put(target, amount);
                player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking set to " + amount));
                break;
            case "add":
                final int addAmount = Integer.parseInt(args[2]);
                moduleAccessor.getModule().getRankingCache().put(target, oldRanking + addAmount);
                player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking added by " + addAmount));
                break;
            case "remove":
                final int removeAmount = Integer.parseInt(args[2]);
                moduleAccessor.getModule().getRankingCache().put(target, oldRanking - removeAmount);
                player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking removed by " + removeAmount));
                break;
            case "reset":
                moduleAccessor.getModule().getRankingCache().put(target, moduleAccessor.getModule().getConfig().getDefaultElo());
                player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking reset"));
                break;
            default:
                player.sendMessage(ComponentUtil.deserialize("[P] &cInvalid action!"));
                player.sendMessage(ComponentUtil.deserialize("[P] &cUsage: /ranking <set|add|remove|reset> <player> [amount]"));
        }
    }
}
