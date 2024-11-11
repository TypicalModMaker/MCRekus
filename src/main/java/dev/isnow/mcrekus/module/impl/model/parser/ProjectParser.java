package dev.isnow.mcrekus.module.impl.model.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;
import dev.isnow.mcrekus.module.impl.model.parser.impl.group.Group;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.Transformable;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.properties.Align;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.properties.Brightness;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.BlockDisplay;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.TextDisplay;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.item.HeadDisplay;
import dev.isnow.mcrekus.module.impl.model.parser.impl.object.type.item.ItemDisplay;
import dev.isnow.mcrekus.util.FileUtil;
import dev.isnow.mcrekus.util.RekusLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.bukkit.Color;

@UtilityClass
public class ProjectParser {
    private final Pattern NAME_DATA_PATTERN = Pattern.compile("^([a-zA-Z_][a-zA-Z0-9_]*)\\[(.*?)]");

    public Model parseFile(final File file) {
        try {
            final String decompressed = FileUtil.decompressB64GZIP(file);
            final JsonElement parsedJson = JsonParser.parseString(decompressed);

            return parseProjectObject(parsedJson.getAsJsonArray().get(0).getAsJsonObject());
        } catch (Exception e) {
            RekusLogger.error("Failed to parse " + file.getName());
            e.printStackTrace();
        }

        return null;
    }

    private Model parseProjectObject(final JsonObject projectObject) {
        final String name = projectObject.get("name").getAsString();
        final String additionalNBT = projectObject.get("nbt").getAsString();
        final float[] matrix = parseMatrix(projectObject.getAsJsonArray("transforms"));
        final Transformable transformable = new Transformable(matrix);
        final List<Transformable> children = parseChildren(projectObject.getAsJsonArray("children"));

        return new Model(name, additionalNBT, transformable, children);
    }

    private List<Transformable> parseChildren(final JsonArray childrenArray) {
        final List<Transformable> children = new ArrayList<>();

        for(final JsonElement childElement : childrenArray) {
            final JsonObject childObject = childElement.getAsJsonObject();

            if(childObject.has("isCollection")) {
                final float[] matrix = parseMatrix(childObject.getAsJsonArray("transforms"));
                final String name = childObject.get("name").getAsString();

                final Group group = new Group(matrix, name, parseChildren(childObject.getAsJsonArray("children")));

                children.add(group);
            } else if (childObject.has("isBlockDisplay")) {
                final float[] matrix = parseMatrix(childObject.getAsJsonArray("transforms"));
                final String additionalNBT = childObject.get("nbt").getAsString();
                final Brightness brightness = parseBrightness(childObject.getAsJsonObject("brightness"));

                String name = childObject.get("name").getAsString();

                final Matcher matcher = NAME_DATA_PATTERN.matcher(name);
                String additionalMaterialData;
                if(matcher.find()) {name = matcher.group(1);
                    additionalMaterialData = "[" + matcher.group(2) + "]";
                } else {
                    additionalMaterialData = "";
                }

                final BlockDisplay blockDisplay = new BlockDisplay(matrix, name, additionalNBT, brightness, additionalMaterialData);

                children.add(blockDisplay);
            } else if (childObject.has("isItemDisplay")) {
                final float[] matrix = parseMatrix(childObject.getAsJsonArray("transforms"));
                final String additionalNBT = childObject.get("nbt").getAsString();
                final Brightness brightness = parseBrightness(childObject.getAsJsonObject("brightness"));

                String name = childObject.get("name").getAsString();
                final Matcher matcher = NAME_DATA_PATTERN.matcher(name);
                String displayType;
                if(matcher.find()) {
                    name = matcher.group(1);
                    displayType = matcher.group(2).split("=")[1];
                } else {
                    displayType = "none";
                }

                final ItemDisplay itemDisplay;

                if(name.startsWith("player_head")) {
                    final String customTexture = childObject.get("customTexture").getAsString();
                    final String textureValue = childObject.get("tagHead").getAsJsonObject().get("Value").getAsString();

                    itemDisplay = new HeadDisplay(matrix, name, additionalNBT, brightness, displayType, customTexture, textureValue);
                } else {
                    itemDisplay = new ItemDisplay(matrix, name, additionalNBT, brightness, displayType, name.split("\\[")[0]);
                }

                children.add(itemDisplay);
            } else if (childObject.has("isTextDisplay")) {
                final float[] matrix = parseMatrix(childObject.getAsJsonArray("transforms"));
                final String name = childObject.get("name").getAsString();
                final String additionalNBT = childObject.get("nbt").getAsString();
                final Brightness brightness = parseBrightness(childObject.getAsJsonObject("brightness"));

                final JsonObject options = childObject.getAsJsonObject("options");

                final java.awt.Color awtColor = java.awt.Color.decode(options.get("color").getAsString());
                final Color color = Color.fromRGB(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());

                final double alpha = options.get("alpha").getAsDouble();

                final java.awt.Color awtBackgroundColor = java.awt.Color.decode(options.get("backgroundColor").getAsString());
                final Color backgroundColor = Color.fromRGB(awtBackgroundColor.getRed(), awtBackgroundColor.getGreen(), awtBackgroundColor.getBlue());

                final double backgroundAlpha = options.get("backgroundAlpha").getAsDouble();

                final boolean bold = options.get("bold").getAsBoolean();
                final boolean italic = options.get("italic").getAsBoolean();
                final boolean underline = options.get("underline").getAsBoolean();
                final boolean strikeThrough = options.get("strikeThrough").getAsBoolean();
                final boolean obfuscated = options.get("obfuscated").getAsBoolean();

                final int lineLength = options.get("lineLength").getAsInt();

                final Align align = Align.valueOf(options.get("align").getAsString().toUpperCase(Locale.ROOT));

                final TextDisplay textDisplay = new TextDisplay(matrix, name, additionalNBT, brightness, color, backgroundColor, alpha, backgroundAlpha, bold, italic, underline, strikeThrough, obfuscated, lineLength, align);

                children.add(textDisplay);
            }
        }

        return children;
    }

    private Brightness parseBrightness(final JsonObject brightnessObject) {
        final int sky = brightnessObject.get("sky").getAsInt();
        final int block = brightnessObject.get("block").getAsInt();

        return new Brightness(sky, block);
    }

    private float[] parseMatrix(final JsonArray matrixArray) {
        final float[] matrix = new float[16];

        for(int i = 0; i < matrixArray.size(); i++) {
            matrix[i] = matrixArray.get(i).getAsFloat();
        }

        return matrix;
    }

}
