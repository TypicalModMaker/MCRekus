package dev.isnow.mcrekus.module.impl.model.parser.impl.object.type;

import dev.isnow.mcrekus.module.impl.model.parser.impl.object.DisplayObject;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.properties.Align;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.properties.Brightness;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Color;

@Getter
@Setter
public class TextDisplay extends DisplayObject {
    private final String text;

    private final Color color, backgroundColor;

    private final double alpha, backgroundAlpha;

    private final boolean bold, italic, underline, strikeThrough, obfuscated;

    private final int lineLength;

    private final Align align;

    // TODO: Align
    public TextDisplay(final float[] matrix, final String name, final String additionalNBT, final Brightness brightness,
            final Color color, final Color backgroundColor, final double alpha, final double backgroundAlpha,
            final boolean bold, final boolean italic, final boolean underline, final boolean strikeThrough,
            final boolean obfuscated, final int lineLength, final Align align) {
        super(matrix, name, additionalNBT, brightness);

        this.text = name; // Lmao

        // FIX COLOR PARSE
        this.color = color;
        this.backgroundColor = backgroundColor;
        this.alpha = alpha;
        this.backgroundAlpha = backgroundAlpha;
        this.bold = bold;
        this.italic = italic;
        this.underline = underline;
        this.strikeThrough = strikeThrough;
        this.obfuscated = obfuscated;
        this.lineLength = lineLength;
        this.align = align;
    }
}
