package dev.isnow.mcrekus.module.impl.model.parser.impl.object.type;

import dev.isnow.mcrekus.module.impl.model.parser.impl.object.DisplayObject;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.properties.Brightness;
import dev.isnow.mcrekus.util.RekusLogger;
import java.util.Locale;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.block.BlockState;

@Getter
public class BlockDisplay extends DisplayObject {
    private final Material material;
    private final String materialData;

    public BlockDisplay(final float[] matrix, final String name, final String additionalNBT, final Brightness brightness, final String parsedData) {
        super(matrix, name, additionalNBT, brightness);

        material = Material.getMaterial(name.toUpperCase(Locale.ROOT));
        materialData = parsedData;
    }
}
