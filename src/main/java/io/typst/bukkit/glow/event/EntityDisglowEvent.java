package io.typst.bukkit.glow.event;

import io.typst.bukkit.glow.EntityGlowData;
import io.typst.bukkit.glow.EntityId;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * The use case
 */
public class EntityDisglowEvent extends Event {
    private static HandlerList handlers = new HandlerList();
    private final EntityId target;
    private final Player receiver;
    private final EntityGlowData glowData;

    /**
     * @param target   the target will be dis-glowed
     * @param receiver the receiver to see the target
     * @param glowData the glow data contains color and id and ...
     */
    public EntityDisglowEvent(@NotNull EntityId target, Player receiver, EntityGlowData glowData) {
        this.target = target;
        this.receiver = receiver;
        this.glowData = glowData;
    }

    /**
     * @return the receiver to see the target
     */
    public Player getReceiver() {
        return receiver;
    }

    /**
     * @return the glow data contains color and id and ...
     */
    public EntityGlowData getGlowData() {
        return glowData;
    }

    /**
     * @return the target will be dis-glowed
     */
    @NotNull
    public EntityId getTarget() {
        return target;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
