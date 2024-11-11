package dev.isnow.mcrekus.module.impl.model.parser.impl;

import dev.isnow.mcrekus.module.impl.model.parser.impl.object.Transformable;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class Model {
    private final String name;
    private final String additionalNBT;
    private final Transformable defaultTransform;
    private final List<Transformable> children;
}
