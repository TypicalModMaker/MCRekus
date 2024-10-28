package dev.isnow.mcrekus.module.impl.customevents.event;

import lombok.Getter;

@Getter
public abstract class BukkitEvent {
    protected final CustomEvent customEvent;

    public BukkitEvent(final CustomEvent customEvent) {
        this.customEvent = customEvent;
    }
}
