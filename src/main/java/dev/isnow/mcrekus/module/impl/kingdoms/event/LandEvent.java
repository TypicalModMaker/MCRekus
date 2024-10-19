package dev.isnow.mcrekus.module.impl.kingdoms.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.util.ComponentUtil;
import java.time.Duration;
import net.kyori.adventure.title.Title;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.kingdoms.constants.group.Kingdom;
import org.kingdoms.constants.land.Land;
import org.kingdoms.events.lands.LandChangeEvent;

public class LandEvent implements Listener {
    @EventHandler
    public void onLand(LandChangeEvent event) {
        if (event.getToLand() == null || event.getToLand().getKingdom() == null) return;

        final Land fromLand = event.getFromLand();
        final Land toLand = event.getToLand();

        final Kingdom toLandKingdom = toLand.getKingdom();

        if (fromLand != null && fromLand.getKingdom() != null && fromLand.getKingdom().getName().equals(toLandKingdom.getName())) return;

        final Title.Times times = Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500));
        final Title title = Title.title(ComponentUtil.deserialize("&7&l-=( &#00FF00" + toLandKingdom.getName() + "<reset> &7&l)=-"), ComponentUtil.deserialize("&7" + (toLandKingdom.getLore() == null ? "<tinify>Brak Opisu</tinify>" : "<tinify>" + toLandKingdom.getLore() + "</tinify>") + " &f| " + (toLandKingdom.isPacifist() ? "<gradient:#084CFB:#00DFFF><bold><tinify>Pacyfistyczne</tinify></gradient>" : "<gradient:#FF0000:#FF3737><bold><tinify>Agresywne</tinify></gradient>")), times);

        new BukkitRunnable() {
            @Override
            public void run() {
                event.getPlayer().showTitle(title);
            }
        }.runTaskLater(MCRekus.getInstance(), 3L);
    }
}
