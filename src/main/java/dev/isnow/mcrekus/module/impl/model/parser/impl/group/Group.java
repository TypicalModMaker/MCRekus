package dev.isnow.mcrekus.module.impl.model.parser.impl.group;

import dev.isnow.mcrekus.module.impl.model.parser.impl.object.Transformable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Group extends Transformable {
    private final String name;
    private final List<Transformable> children;

    public Group(final float[] matrix, final String name, final List<Transformable> children) {
        super(matrix);

        this.name = name;
        this.children = children;
    }
}
