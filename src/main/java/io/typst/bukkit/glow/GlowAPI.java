package io.typst.bukkit.glow;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class GlowAPI {
    private final Plugin plugin;

    public GlowAPI(Plugin plugin) {
        this.plugin = plugin;
    }

    public GlowAPI() {
        this(null);
    }

    private static GlowService getServiceOrThrow() {
        GlowService service = JavaPlugin.getPlugin(GlowPlugin.class).getGlow();
        return Objects.requireNonNull(service);
    }

    private static boolean checkModelEngine() {
        return Bukkit.getPluginManager().isPluginEnabled("ModelEngine");
    }

    // TODO: should be removed
    public void setGlowing(Player target, Player receiver) {
        setGlowing((Entity) target, receiver);
    }

    public void setGlowing(Entity target, Player receiver) {
        setGlowing(target, receiver, ChatColor.WHITE);
    }

    // TODO: should be removed
    public void setGlowing(Player target, Player receiver, ChatColor color) {
        setGlowing((Entity) target, receiver, color);
    }

    public void setGlowing(Entity target, Player receiver, ChatColor color, String id) {
        GlowService service = getServiceOrThrow();
        service.setGlowing(EntityId.from(target), receiver, color, id, this.plugin);
        if (checkModelEngine()) {
            for (EntityId boneId : ModelEngineAccessor.getBoneEntities(target.getUniqueId())) {
                service.setGlowing(boneId, receiver, color, id, this.plugin);
            }
        }
    }

    public void setGlowing(Entity target, Player receiver, ChatColor color) {
        setGlowing(target, receiver, color, "");
    }

    // TODO: should be removed
    public void removeGlowing(Player target, Player receiver) {
        removeGlowing((Entity) target, receiver);
    }

    public void removeGlowing(Entity target, Player receiver, String id) {
        GlowService service = getServiceOrThrow();

        // skip if the id is defined and the id of previous glow doesn't match
        if (id != null && !id.isEmpty()) {
            EntityGlowData glowData = service.getPlayerGlowData(target.getName(), receiver.getUniqueId()).orElse(null);
            if (glowData == null || !glowData.getId().equals(id)) {
                return;
            }
        }

        service.removeGlowing(EntityId.from(target), receiver);
        if (checkModelEngine()) {
            for (EntityId boneId : ModelEngineAccessor.getBoneEntities(target.getUniqueId())) {
                service.removeGlowing(boneId, receiver);
            }
        }
    }

    public void removeGlowing(Entity target, Player receiver) {
        removeGlowing(target, receiver, "");
    }

    public boolean hasGlowingView(UUID receiverId) {
        GlowService service = getServiceOrThrow();
        Map<String, EntityGlowData> view = service.getViews().getOrDefault(receiverId, Collections.emptyMap());
        return !view.isEmpty();
    }

    /**
     * @param targetName this is: their username if player, uuid if other entities
     * @param receiverId the id of receiver player
     * @return the glow data contains color and id and ...
     */
    public Optional<EntityGlowData> getGlowingData(String targetName, UUID receiverId) {
        GlowService service = getServiceOrThrow();
        return service.getPlayerGlowData(targetName, receiverId);
    }

    public Optional<EntityGlowData> getGlowingData(Entity target, UUID receiverId) {
        String targetName = target instanceof Player
                ? target.getName()
                : target.getUniqueId().toString();
        return getGlowingData(targetName, receiverId);
    }

    /**
     * @param targetName this is: their username if player, uuid if other entities
     * @param receiverId the id of receiver player
     * @return whether the target has glow
     */
    public boolean checkGlowing(String targetName, UUID receiverId) {
        return getGlowingData(targetName, receiverId).isPresent();
    }

    public boolean checkGlowing(Entity target, UUID receiverId) {
        String targetName = target instanceof Player
                ? target.getName()
                : target.getUniqueId().toString();
        return checkGlowing(targetName, receiverId);
    }
}
