package io.typst.bukkit.glow;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter(AccessLevel.PACKAGE)
class GlowService {
    /**
     * id of receiver player to (team player to color)
     */
    private final Map<UUID, Map<String, PlayerGlowData>> views = new HashMap<>();

    public static String getTeamNameFrom(ChatColor color) {
        return "glow-" + color.getChar();
    }

    public Optional<PlayerGlowData> getPlayerGlowData(String targetName, UUID receiverId) {
        return Optional.ofNullable(views.getOrDefault(receiverId, Collections.emptyMap())
                        .get(targetName));
    }

    public void setGlowing(Player target, Player receiver, ChatColor color) {
        // update internal state
        Map<String, PlayerGlowData> view = views.computeIfAbsent(receiver.getUniqueId(), k -> new HashMap<>());
        view.put(target.getName(), new PlayerGlowData(target.getName(), color));

        // send metadata packet
        PacketContainer metadataPacket = GlowPackets.createGlowingMetadataPacket(target, true);
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

    public void removeGlowing(Player target, Player receiver) {
        // update internal state
        Map<String, PlayerGlowData> view = views.getOrDefault(receiver.getUniqueId(), Collections.emptyMap());
        PlayerGlowData removedData = view.remove(target.getName());
        if (view.isEmpty()) {
            views.remove(receiver.getUniqueId());
        }

        // send metadata packet
        PacketContainer metadataPacket = GlowPackets.createGlowingMetadataPacket(target, false);
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
