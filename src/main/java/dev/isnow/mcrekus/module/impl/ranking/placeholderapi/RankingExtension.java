package dev.isnow.mcrekus.module.impl.ranking.placeholderapi;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankingExtension extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "ranking";
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
        if(identifier.equals("elo")) {
            final RankingModule rankingModule = (RankingModule) MCRekus.getInstance().getModuleManager().getModuleByName("Ranking");

            return String.valueOf(rankingModule.getRankingCache().get(player));
        }

        return null;
    }

}
