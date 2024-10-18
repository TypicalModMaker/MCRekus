package dev.isnow.mcrekus.module.impl.ranking;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.ranking.config.RankingConfig;
import dev.isnow.mcrekus.module.impl.ranking.hit.PlayerHit;
import dev.isnow.mcrekus.module.impl.ranking.kill.PlayerKill;
import dev.isnow.mcrekus.module.impl.ranking.placeholderapi.RankingExtension;
import dev.isnow.mcrekus.util.ComponentUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.scheduler.BukkitRunnable;

@Getter
public class RankingModule extends Module<RankingConfig> {
    private final HashMap<Player, Integer> rankingCache = new HashMap<>();
    private final HashMap<Player, PlayerHit> hitCache = new HashMap<>();

    private final HashMap<UUID, LinkedList<PlayerKill>> killCache = new HashMap<>();

    private final DecimalFormat decimalFormat = new DecimalFormat("#.###");

    public RankingModule() {
        super("Ranking");
    }

    @Override
    public void onEnable(final MCRekus plugin) {
        if(!MCRekus.getInstance().getHookManager().isPlaceholerAPIHook()) {
            RekusLogger.info("PlaceholderAPI not found, Ranking will not work.");
            return;
        }

        for(final Player player : Bukkit.getOnlinePlayers()) {
            MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
                rankingCache.put(player, data.getElo());
            });
        }

        registerListeners("event");
        registerCommands("command");

        new RankingExtension().register();
    }

    @Override
    public void onDisable(final MCRekus plugin) {
        unRegisterListeners();
        unRegisterCommands();

        for(final Player player : Bukkit.getOnlinePlayers()) {
            MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
                data.setElo(rankingCache.get(player));
                data.save(session);
            });
        }
    }

    public void handleKill(final Player player, final Player killer) {
        if (!killer.isOnline()) {
            return;
        }

        player.getWorld().strikeLightningEffect(player.getLocation());

        new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().respawn();
            }
        }.runTaskLater(MCRekus.getInstance(), 1L);

        final int playerElo = getRankingCache().get(player);
        final int killerElo = getRankingCache().get(killer);

        boolean giveRank = true;

        if (killCache.containsKey(player.getUniqueId())) {
            final PlayerKill foundKill = killCache.get(player.getUniqueId()).stream().filter(kill -> kill.getKiller().equals(killer.getUniqueId())).findFirst().orElse(null);

            if(foundKill != null) {
                if (foundKill.getTime() + (15000 * 60) > System.currentTimeMillis()) {
                    giveRank = false;
                    killer.sendMessage(ComponentUtil.deserialize("[P] <color:#AB1B1B>Zabiłeś gracza, którego pokonałeś w ciągu ostatnich 15 minut. Nie otrzymałeś punktów rankingowych."));
                } else {
                    killCache.get(player.getUniqueId()).set(killCache.get(player.getUniqueId()).indexOf(foundKill), foundKill);
                }

                foundKill.setTime(System.currentTimeMillis());
            } else {
                killCache.get(player.getUniqueId()).add(new PlayerKill(killer.getUniqueId(), System.currentTimeMillis()));
            }
        } else {
            killCache.put(player.getUniqueId(), new LinkedList<>(
                    List.of(new PlayerKill(killer.getUniqueId(), System.currentTimeMillis()))));
        }

        final int[] ranking = giveRank ? calculateRanking(playerElo, killerElo) : new int[]{0, 0};

        final int newPlayerElo = playerElo - ranking[1];
        final int newKillerElo = killerElo + ranking[0];

        getRankingCache().put(player, newPlayerElo);
        getRankingCache().put(killer, newKillerElo);

        final RankingConfig config = getConfig();

        final Title.Times times = Title.Times.times(Duration.ofMillis(config.getTitleFadeIn()), Duration.ofMillis(config.getTitleStay()), Duration.ofMillis(config.getTitleFadeOut()));

        final Title killerTitle = Title.title(ComponentUtil.deserialize(config.getTitleKiller()), ComponentUtil.deserialize(config.getSubtitleKiller().replaceAll("%old_elo%", String.valueOf(killerElo)).replaceAll("%new_elo%", String.valueOf(newKillerElo)).replaceAll("%player%", player.getName())), times);
        final Title playerTitle = Title.title(ComponentUtil.deserialize(config.getTitleKilled()), ComponentUtil.deserialize(config.getSubtitleKilled().replaceAll("%old_elo%", String.valueOf(playerElo)).replaceAll("%new_elo%", String.valueOf(newPlayerElo)).replaceAll("%player%", killer.getName())), times);

        player.showTitle(playerTitle);
        killer.showTitle(killerTitle);

        final Component message = ComponentUtil.deserialize("<color:#AB1B1B>☠ <hover:show_text:'&e" + playerElo + " &7➡ &e" + newPlayerElo + "'>" + player.getName() + "<reset> &7został zabity przez <color:#1CCB4C>\uD83D\uDDE1 <hover:show_text:'&e" + killerElo + " &7➡ &e" + newKillerElo + "'>" + killer.getName() + "<reset> &7(<color:#AB1B1B>" + decimalFormat.format(killer.getHealth()) + "❤&7)");
        Bukkit.broadcast(message);
    }

    private int[] calculateRanking(final int victimRanking, final int attackerRanking) {
        final int added = (int) ((0.04D * (victimRanking - attackerRanking)) + 25D);
        final int removed = (int) (added * 0.8D);

        return new int[]{
                Math.max(added, 3),
                Math.max(removed, 3)
        };
    }
}
