package dev.isnow.mcrekus.module.impl.kingdoms.placeholderapi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.kingdoms.constants.player.KingdomPlayer;

public class KingdomsNameExtension extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "kingdomsx";
    }

    @Override
    public @NotNull String getAuthor() {
        return "5170";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(final Player player, final String identifier) {
        if(identifier.equals("name")) {
            final KingdomPlayer kp = KingdomPlayer.getKingdomPlayer(player);

            if(kp.getKingdom() != null) {
                return kp.getKingdom().getName();
            } else {
                return "ʙʀᴀᴋ";
            }
        }

        return null;
    }

}
