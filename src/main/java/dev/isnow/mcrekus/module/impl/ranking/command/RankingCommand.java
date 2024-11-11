package dev.isnow.mcrekus.module.impl.ranking.command;

import dev.isnow.mcrekus.module.ModuleAccessor;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.annotations.Async;
import dev.velix.imperat.annotations.Command;
import dev.velix.imperat.annotations.Description;
import dev.velix.imperat.annotations.Named;
import dev.velix.imperat.annotations.Optional;
import dev.velix.imperat.annotations.Permission;
import dev.velix.imperat.annotations.Suggest;
import dev.velix.imperat.annotations.Usage;
import java.util.HashMap;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

@Command("ranking")
@Description("Command to manage ranking")
@Permission("mcrekus.ranking")
@SuppressWarnings("unused")
public class RankingCommand extends ModuleAccessor<RankingModule> {

    @Usage
    @Async
    public void defaultUsage(final BukkitSource source) {
        source.reply(ComponentUtil.deserialize("[P] &cUsage: /ranking <set|add|remove|reset|disablehitcache> <player> [amount]"));
    }

    @Usage
    @Async
    public void execute(final BukkitSource source, final @Named("action") @Suggest({"set", "add", "remove", "reset", "disablehitcache"}) String action, final @Named("player") @Optional Player target, final @Named("amount") @Optional int amount) {
        if(action.equalsIgnoreCase("disablehitcache")) {
            final boolean disable = getModule().isDisableKillCache();
            getModule().setDisableKillCache(!disable);
            source.reply(ComponentUtil.deserialize("[P] &aKill cache is now " + (!disable ? "disabled" : "enabled")));
            return;
        }

        if(target == null) {
            source.reply(ComponentUtil.deserialize("[P] &cPlayer not found!"));
            return;
        }
        final Player player = source.asPlayer();

        final HashMap<Player, Integer> rankingCache = getModule().getRankingCache();
        final int defaultElo = getModule().getConfig().getDefaultElo();

        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        if(action.equalsIgnoreCase("reset")) {
            rankingCache.put(target, defaultElo);
            player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking reset"));
            return;
        }

        if(amount == 0) {
            player.sendMessage(ComponentUtil.deserialize("[P] &cUsage: /ranking " + action + " " + target.getName() + " <amount>"));
            return;
        }

        final int oldRanking = getModule().getRankingCache().getOrDefault(target, getModule().getConfig().getDefaultElo());

        switch (action) {
            case "set":
                rankingCache.put(target, amount);
                player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking set to " + amount));
                break;
            case "add":
                rankingCache.put(target, oldRanking + amount);
                player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking added by " + amount));
                break;
            case "remove":
                rankingCache.put(target, oldRanking - amount);
                player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking removed by " + amount));
                break;
            case "reset":
                rankingCache.put(target, defaultElo);
                player.sendMessage(ComponentUtil.deserialize("[P] &aPlayer ranking reset"));
                break;
            default:
                player.sendMessage(ComponentUtil.deserialize("[P] &cInvalid action!"));
                player.sendMessage(ComponentUtil.deserialize("[P] &cUsage: /ranking <set|add|remove|reset> <player> [amount]"));
        }
    }
}
