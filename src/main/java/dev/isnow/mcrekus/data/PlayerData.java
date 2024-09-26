package dev.isnow.mcrekus.data;

import dev.isnow.mcrekus.data.base.BaseData;
import dev.isnow.mcrekus.module.impl.essentials.home.Home;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import dev.isnow.mcrekus.util.serializer.database.HomeSerializer;
import dev.isnow.mcrekus.util.serializer.database.RekusLocationSerializer;
import io.ebean.annotation.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import java.util.HashMap;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

@Entity
@Table(name = "mcrekus_players")
@Getter
@Setter
public class PlayerData extends BaseData {
    @NotNull
    @Column(unique = true, name = "playeruuid")
    private UUID uuid;

    @Convert(converter = HomeSerializer.class)
    @Column(name = "homeLocations")
    private HashMap<String, Home> homeLocations;

    @Convert(converter = RekusLocationSerializer.class)
    @Column(name = "lastLocation")
    private RekusLocation lastLocation;

    @Transient
    private String name;

    @Version
    private long version;

    public PlayerData(final Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();

        this.homeLocations = new HashMap<>();
        this.lastLocation = RekusLocation.fromBukkitLocation(player.getLocation());
    }

    public PlayerData() {}
}
