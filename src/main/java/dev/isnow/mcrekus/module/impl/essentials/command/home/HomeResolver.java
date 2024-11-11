package dev.isnow.mcrekus.module.impl.essentials.command.home;

import dev.isnow.mcrekus.MCRekus;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.bukkit.entity.Player;

public class HomeResolver implements SuggestionResolver<BukkitSource> {

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context,
            CommandParameter<BukkitSource> parameter) {
        return List.of();
    }

    @Override
    public CompletableFuture<Collection<String>> asyncAutoComplete(SuggestionContext<BukkitSource> context, CommandParameter<BukkitSource> parameter) {
        final Player player = context.source().asPlayer();

        CompletableFuture<Collection<String>> future = new CompletableFuture<>();

        MCRekus.getInstance().getDatabaseManager().getUserAsync(player, (session, data) -> {
            if(data == null) {
                player.sendMessage("§cWystąpił błąd podczas ładowania danych gracza. Spróbuj ponownie później.");
                future.complete(List.of());
                return;
            }

            future.complete(data.getHomeLocations().keySet());
        });

        return future;
    }
}
