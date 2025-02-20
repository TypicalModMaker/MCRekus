package dev.isnow.mcrekus.util;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

@UtilityClass
public class ComponentUtil {
    private final Pattern tinify_pattern = Pattern.compile("<tinify>(.*?)</tinify>");
    private final Pattern all =  Pattern.compile(".*");
    private final Pattern right = Pattern.compile(">>");
    private final Pattern left = Pattern.compile("<<");

    private final LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER =
            LegacyComponentSerializer.legacyAmpersand().toBuilder()
                    .hexColors()
                    .useUnusualXRepeatedCharacterHexFormat()
                    .build();

    private final MiniMessage MINI_MESSAGE = MiniMessage.builder().postProcessor(it ->
            it.replaceText(replacementBuilder -> {
                replacementBuilder.match(all).replacement((matchResult, builder) -> LEGACY_COMPONENT_SERIALIZER.deserialize(matchResult.group()));
            }).replaceText(replacementBuilder -> {
                replacementBuilder.match(right).replacement("»");
            }).replaceText(replacementBuilder -> {
                replacementBuilder.match(left).replacement("«");
            })).preProcessor(str -> {
                final Matcher matcher = tinify_pattern.matcher(str);

                final StringBuilder result = new StringBuilder();

                while (matcher.find()) {
                    final String innerText = matcher.group(1);
                    final String tinifiedText = tinifyText(MiniMessage.miniMessage().stripTags(innerText));

                    matcher.appendReplacement(result, Matcher.quoteReplacement(tinifiedText));
                }

                matcher.appendTail(result);
                return result.toString();
            }).build();

    public Component deserialize(final String input) {
        return deserialize(input, null);
    }

    public Component deserialize(String input, final Player player, final Object... placeholders) {
        if (player != null && MCRekus.getInstance().getHookManager().isPlaceholerAPIHook()) {
            input = PlaceholderAPI.setPlaceholders(player, input);
        }

        if(MCRekus.getInstance().getConfigManager() != null) {
            input = input.replaceAll("\\[P]", MCRekus.getInstance().getConfigManager().getGeneralConfig().getPrefix());
        }

        return MINI_MESSAGE.deserialize(placeholders != null ? formatPlaceholders(input, placeholders) : input).decoration(TextDecoration.ITALIC, false);
    }

    public Component deserialize(String input, final Player player) {
        return deserialize(input, player, null);
    }

    public String serialize(final Component input) {
        return MINI_MESSAGE.serialize(input.decoration(TextDecoration.ITALIC, false));
    }

    public String formatPlaceholders(final String input, final Object... placeholders) {
        if (placeholders.length % 2 != 0) {
            return input;
        }

        final Map<String, Object> placeholderMap = new HashMap<>();
        for (int i = 0; i < placeholders.length; i += 2) {
            if (placeholders[i] instanceof String) {
                placeholderMap.put((String) placeholders[i], placeholders[i + 1]);
            }
        }

        return replacePlaceholders(input, placeholderMap);
    }

    private String replacePlaceholders(final String input, final Map<String, Object> placeholderMap) {
        final StringBuilder result = new StringBuilder(input);

        for (final Map.Entry<String, Object> entry : placeholderMap.entrySet()) {
            final String placeholder = entry.getKey();
            final Object value = entry.getValue();

            int index = result.indexOf(placeholder);
            while (index != -1) {
                result.replace(index, index + placeholder.length(), value.toString());
                index = result.indexOf(placeholder, index + value.toString().length());
            }
        }

        return result.toString();
    }

    public String formatLocation(final RekusLocation input, final boolean longVersion) {
        return "X: " + (int) input.getX() + (longVersion ? " Y: " + (int) input.getY() : "") + " Z: " + (int) input.getZ();
    }

    public String formatLocation(final Location input, final boolean longVersion) {
        return "X: " + input.getBlockX() + (longVersion ? " Y: " + input.getBlockY() : "") + " Z: " + input.getBlockZ();
    }

    private String tinifyText(String text) {
        final StringBuilder tinified = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isLetter(c)) {
                tinified.append(toSmallCaps(c));
            } else {
                tinified.append(c);
            }
        }
        return tinified.toString();
    }

    private char toSmallCaps(char c) {
        final String smallCaps = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘꞯʀꜱᴛᴜᴠᴡxʏᴢ";

        c = Character.toLowerCase(c);

        if (c >= 'a' && c <= 'z') {
            return smallCaps.charAt(c - 'a');
        }

        return switch (c) {
            case 'ą' -> 'ᴀ';
            case 'ć' -> 'ᴄ';
            case 'ę' -> 'ᴇ';
            case 'ł' -> 'ᴌ';
            case 'ń' -> 'ɴ';
            case 'ó' -> 'ᴏ';
            case 'ś' -> 's';
            case 'ź', 'ż' -> 'ᴢ';
            default -> c;
        };
    }
}