package dev.isnow.mcrekus.data;

import dev.isnow.mcrekus.data.base.BaseData;
import dev.isnow.mcrekus.util.cuboid.RekusLocation;
import dev.isnow.mcrekus.util.serializer.database.RekusLocationSerializer;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "mcrekus_spawners")
@Getter
@Setter
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SpawnerData extends BaseData {
    @Column(name = "location")
    @Convert(converter = RekusLocationSerializer.class)
    private RekusLocation location;

    public SpawnerData(final RekusLocation location) {
        this.location = location;
    }

    public SpawnerData() {}
}
