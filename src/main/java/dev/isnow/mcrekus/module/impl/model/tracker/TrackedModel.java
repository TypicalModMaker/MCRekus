package dev.isnow.mcrekus.module.impl.model.tracker;

import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;
import dev.isnow.mcrekus.module.impl.model.util.MountableWrappedEntity;
import java.util.List;
import lombok.Data;
import me.tofaa.entitylib.wrapper.WrapperEntity;

@Data
public class TrackedModel {
    private final Model model;
    private final MountableWrappedEntity base;
    private final List<WrapperEntity> objects;
}
