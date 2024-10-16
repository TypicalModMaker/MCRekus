package dev.isnow.mcrekus.data;

import dev.isnow.mcrekus.MCRekus;
import dev.isnow.mcrekus.data.base.BaseData;
import dev.isnow.mcrekus.module.Module;
import dev.isnow.mcrekus.module.impl.ranking.RankingModule;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import dev.isnow.mcrekus.util.serializer.database.RekusLocationSerializer;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jetbrains.annotations.NotNull;

@Entity
@Table(name = "mcrekus_players")
@Getter
@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlayerData extends BaseData {

    @Column(unique = true, name = "playeruuid", nullable = false)
    private UUID uuid;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true , mappedBy = "playerData")
    @MapKey(name = "name")
    private Map<String, HomeData> homeLocations;

    @Column(name = "last_location")
    @Convert(converter = RekusLocationSerializer.class)
    private RekusLocation lastLocation;

    @Column(name = "player_name", nullable = false)
    private String name;

    @Column(name = "player_time", nullable = false)
    private long time;

    @Column(name = "elo", nullable = false)
    private int elo;

    @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REFRESH})
    @JoinTable(
            name = "mcrekus_collected_pumpkins",
            joinColumns = { @JoinColumn(name = "player_id") },
            inverseJoinColumns = { @JoinColumn(name = "pumpkin_id") }
    )
    Set<PumpkinData> pumpkins = new HashSet<>();

    public PlayerData(final Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();

        this.lastLocation = RekusLocation.fromBukkitLocation(player.getLocation());

        Module<?> rankingModule = MCRekus.getInstance().getModuleManager().getModuleByName("Ranking");
        if(rankingModule != null) {
            this.elo = ((RankingModule) rankingModule).getConfig().getDefaultElo();
        }
    }

    public PlayerData(final UUID uuid, final String name) {
        this.uuid = uuid;
        this.name = name;

        Module<?> rankingModule = MCRekus.getInstance().getModuleManager().getModuleByName("Ranking");
        if(rankingModule != null) {
            this.elo = ((RankingModule) rankingModule).getConfig().getDefaultElo();
        }
    }

    public PlayerData() {}

    public void addPumpkin(@NotNull PumpkinData pumpkinData) {
        pumpkins.add(pumpkinData);
    }
}
