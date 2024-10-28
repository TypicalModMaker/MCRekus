package dev.isnow.mcrekus.module.impl.customevents.event.impl.invasions.event;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.impl.customevents.event.BukkitEvent;
import dev.isnow.mcrekus.module.impl.customevents.event.CustomEvent;
import dev.isnow.mcrekus.module.impl.kingdoms.KingdomsModule;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.kingdoms.events.invasion.KingdomInvadeEndEvent;

public class InvadeBukkitEvent extends BukkitEvent implements Listener {

    public InvadeBukkitEvent(final CustomEvent customEvent) {
        super(customEvent);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInvade(final KingdomInvadeEndEvent event) {
        getCustomEvent().addScore(event.getInvasion().getInvader().getPlayer());

        final KingdomsModule module = MCRekus.getInstance().getModuleManager().getModuleByClass(KingdomsModule.class);

        if (module != null) {
            for (final Player player : module.getChampions().get(event.getInvasion().getChampion().getEntityId()).getAttackingPlayers()) {
                getCustomEvent().addScore(player);
            }
        }
    }
}
