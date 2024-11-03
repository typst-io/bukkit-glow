package io.typst.bukkit.glow;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.UUID;

public class EntityId {
    private final int id;
    private final UUID uuid;
    private final String name;
    private final EntityType entityType;

    public EntityId(int id, UUID uuid, String name, EntityType entityType) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
        this.entityType = entityType;
    }

    public static EntityId from(Entity entity) {
        return new EntityId(
                entity.getEntityId(),
                entity.getUniqueId(),
                entity instanceof Player
                        ? entity.getName()
                        : entity.getUniqueId().toString(),
                entity.getType()
        );
    }

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public EntityType getEntityType() {
        return entityType;
    }
}
