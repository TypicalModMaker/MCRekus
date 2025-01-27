package dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.item;

import dev.isnow.mcrekus.module.impl.model.parser.impl.object.DisplayObject;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.DisplayType;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.properties.Brightness;
import java.util.Locale;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public class ItemDisplay extends DisplayObject {
    private final Material item;
    private final DisplayType displayType;

    public ItemDisplay(final float[] matrix, final String name, final String additionalNBT, final Brightness brightness, final String displayType, final String item) {
        super(matrix, name, additionalNBT, brightness);

        this.displayType = DisplayType.valueOf(displayType.toUpperCase(Locale.ROOT));
        this.item = Material.getMaterial(item.toUpperCase(Locale.ROOT));
    }
}
