package dev.isnow.mcrekus.config;

import de.exlll.configlib.YamlConfigurationProperties;
import de.exlll.configlib.YamlConfigurations;
import dev.isnow.mcrekus.util.Range;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import dev.isnow.mcrekus.util.serializer.config.RangeSerializer;
import dev.isnow.mcrekus.util.serializer.config.RekusLocationSerializer;
import java.nio.file.Path;
import lombok.Getter;

@Getter
public abstract class RekusConfig {
    private static final YamlConfigurationProperties PROPERTIES = YamlConfigurationProperties.newBuilder()
            .addSerializer(RekusLocation.class, new RekusLocationSerializer())
            .addSerializer(Range.class, new RangeSerializer())
            .build();

    private final String name;
    private final Path path;

    public RekusConfig(final String name, final Path path) {
        this.name = name;
        this.path = path;
    }

    public RekusConfig load() {
        if(!path.toFile().exists()) {
            save();
        }

        return YamlConfigurations.load(path, getClass(), PROPERTIES);
    }

    public <T> void save() {
        YamlConfigurations.save(path, (Class<T>) this.getClass(), (T) this, PROPERTIES);
    }

}