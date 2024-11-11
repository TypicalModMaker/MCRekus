package dev.isnow.mcrekus.module.impl.model.parser.impl.object;

import me.tofaa.entitylib.meta.display.ItemDisplayMeta;

public enum DisplayType {
    NONE,
    GROUND,
    HEAD,
    THIRD_PERSON_RIGHT_HAND,
    THIRD_PERSON_LEFT_HAND,
    FIXED;

    public ItemDisplayMeta.DisplayType convert() {
        return switch(this) {
            case NONE -> ItemDisplayMeta.DisplayType.NONE;
            case GROUND -> ItemDisplayMeta.DisplayType.GROUND;
            case HEAD -> ItemDisplayMeta.DisplayType.HEAD;
            case THIRD_PERSON_RIGHT_HAND -> ItemDisplayMeta.DisplayType.THIRD_PERSON_RIGHT_HAND;
            case THIRD_PERSON_LEFT_HAND -> ItemDisplayMeta.DisplayType.THIRD_PERSON_LEFT_HAND;
            case FIXED -> ItemDisplayMeta.DisplayType.FIXED;
        };
    }
}
