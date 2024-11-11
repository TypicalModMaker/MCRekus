package dev.isnow.mcrekus.module.impl.model.tracker;

import dev.isnow.mcrekus.module.impl.model.parser.impl.Model;
import java.util.List;
import lombok.Data;
import me.tofaa.entitylib.wrapper.WrapperEntity;

@Data
public class TrackedModel {
    private final Model model;
    private final WrapperEntity base;
    private final List<WrapperEntity> objects;
}
