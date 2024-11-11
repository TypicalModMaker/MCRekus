package dev.isnow.mcrekus.module.impl.model.command;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.module.impl.model.ModelModule;
import dev.velix.imperat.BukkitSource;
import dev.velix.imperat.command.parameters.CommandParameter;
import dev.velix.imperat.context.SuggestionContext;
import dev.velix.imperat.resolvers.SuggestionResolver;
import java.util.Collection;

public class ModelResolver implements SuggestionResolver<BukkitSource> {

    @Override
    public Collection<String> autoComplete(SuggestionContext<BukkitSource> context,
            CommandParameter<BukkitSource> parameter) {
        final ModelModule module = MCRekus.getInstance().getModuleManager().getModuleByClass(ModelModule.class);

        return module.getParsedModels().keySet();
    }
}
