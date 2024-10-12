package dev.isnow.mcrekus.data;

import dev.isnow.mcrekus.data.base.BaseData;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import dev.isnow.mcrekus.util.serializer.database.RekusLocationSerializer;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "mcrekus_homes")
@Getter
@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HomeData extends BaseData {
    @Column(name = "name")
    private String name;

    @Column(name = "location")
    @Convert(converter = RekusLocationSerializer.class)
    private RekusLocation location;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private PlayerData playerData;

    public HomeData(final String name, final RekusLocation location, final PlayerData playerData) {
        this.name = name;
        this.location = location;

        this.playerData = playerData;
    }

    public HomeData() {}
}
