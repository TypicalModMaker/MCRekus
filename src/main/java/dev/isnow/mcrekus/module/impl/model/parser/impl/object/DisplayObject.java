package dev.isnow.mcrekus.module.impl.model.parser.impl.object;

import dev.isnow.mcrekus.module.impl.model.parser.impl.object.properties.Brightness;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public abstract class DisplayObject extends Transformable{
    private final String name;
    private final String additionalNBT;
    private final Brightness brightness;

    public DisplayObject(final float[] matrix, final String name, final String additionalNBT, final Brightness brightness) {
        super(matrix);
        this.name = name;
        this.additionalNBT = additionalNBT;
        this.brightness = brightness;
    }
}
