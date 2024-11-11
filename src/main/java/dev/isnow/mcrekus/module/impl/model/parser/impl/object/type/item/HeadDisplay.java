package dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.item;

import dev.isnow.mcrekus.module.impl.model.parser.impl.object.properties.Brightness;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter @Setter
public class HeadDisplay extends ItemDisplay {
    private String customTexture;
    private String textureValue;

    public HeadDisplay(final float[] matrix, final String name, final String additionalNBT, final Brightness brightness, final String displayType, final String customTexture, final String textureValue) {
        super(matrix, name, additionalNBT, brightness, displayType, "PLAYER_HEAD");

        this.customTexture = customTexture;
        this.textureValue = textureValue;
    }
}
