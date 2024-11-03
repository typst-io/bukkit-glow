package io.typst.bukkit.glow;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import io.typst.bukkit.glow.event.EntityDisglowEvent;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter(AccessLevel.PACKAGE)
class GlowService {
    /**
     * id of receiver player to (team player to color)
     */
    private final Map<UUID, Map<String, EntityGlowData>> views = new HashMap<>();

    public static String getTeamNameFrom(ChatColor color) {
        return "glow-" + color.getChar();
    }

    public Optional<EntityGlowData> getPlayerGlowData(String targetName, UUID receiverId) {
        return Optional.ofNullable(views.getOrDefault(receiverId, Collections.emptyMap())
                .get(targetName));
    }

    public void setGlowing(EntityId target, Player receiver, ChatColor color, String id, @Nullable Plugin plugin) {
        // update internal state
        Map<String, EntityGlowData> view = views.computeIfAbsent(receiver.getUniqueId(), k -> new HashMap<>());
        // NOTE: this name used to create team packet with player names
        view.put(target.getName(), new EntityGlowData(target.getName(), color, id, plugin));

        // send metadata packet
        PacketContainer metadataPacket = GlowPackets.createGlowingMetadataPacket(target.getId(), true);
        ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, metadataPacket);

        // send team packet
        // - create team packet
        // - add player packet - X
        List<String> teamPlayers = view.entrySet().stream()
                .flatMap(pair -> pair.getValue().getColor().equals(color)
                        ? Stream.of(pair.getKey())
                        : Stream.empty())
                .collect(Collectors.toList());
        PacketContainer teamCreationPacket = GlowPackets.createTeamCreationPacket(getTeamNameFrom(color), color, teamPlayers);
        ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, teamCreationPacket);
    }

    public void removeGlowing(EntityId target, Player receiver) {
        Map<String, EntityGlowData> view = views.getOrDefault(receiver.getUniqueId(), Collections.emptyMap());
        EntityGlowData glowData = view.get(target.getName());
        if (glowData == null) {
            return;
        }

        // event disglow
        EntityDisglowEvent event = new EntityDisglowEvent(target, receiver, glowData);
        Bukkit.getPluginManager().callEvent(event);

        // do remove
        EntityGlowData removedData = view.remove(target.getName());
        if (view.isEmpty()) {
            views.remove(receiver.getUniqueId());
        }

        // send metadata packet
        PacketContainer metadataPacket = GlowPackets.createGlowingMetadataPacket(target.getId(), false);
        ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, metadataPacket);

        // send team update packet
        // - remove team player
        // - or remove team - X
        if (removedData != null) {
            String teamName = getTeamNameFrom(removedData.getColor());
            PacketContainer teamPlayerRemovePacket = GlowPackets.createTeamRemovePlayerPacket(teamName, Collections.singletonList(target.getName()));
            ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, teamPlayerRemovePacket);
        }
    }
}
