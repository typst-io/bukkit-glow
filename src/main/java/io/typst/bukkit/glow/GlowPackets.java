package io.typst.bukkit.glow;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.InternalStructure;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

class GlowPackets {
    public static final int CREATE_TEAM = 0;
    public static final int REMOVE_TEAM = 1;
    public static final int ADD_PLAYER = 3;
    public static final int REMOVE_PLAYER = 4;
    public static final byte METADATA_GLOWING = 0b1000000;

    public static byte modifyGlowing(byte b, boolean glow) {
        if (glow) {
            return (byte) (b | METADATA_GLOWING);
        } else {
            return (byte) (b & ~METADATA_GLOWING);
        }
    }

    static PacketContainer createGlowingMetadataPacket(int entityId, boolean glow) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entityId);
        byte newHeadByte = modifyGlowing((byte) 0, glow);
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        packet.getDataValueCollectionModifier().write(0, new ArrayList<>(Arrays.asList(
                new WrappedDataValue(0, serializer, newHeadByte)
        )));
        return packet;
    }

    static PacketContainer createDisplayMetadataPacket(int entityId, boolean glow, ChatColor color) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA);
        packet.getIntegers().write(0, entityId);
        byte newHeadByte = modifyGlowing((byte) 0, glow);
        WrappedDataWatcher.Serializer serializer = WrappedDataWatcher.Registry.get(Byte.class);
        packet.getDataValueCollectionModifier().write(0, new ArrayList<>(Arrays.asList(
                new WrappedDataValue(0, serializer, newHeadByte)
        )));
        return packet;
    }

    static PacketContainer createTeamRemovalPacket(String teamName) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getStrings().write(0, teamName);
        packet.getIntegers().write(0, REMOVE_TEAM);
        return packet;
    }

    static PacketContainer createTeamCreationPacket(String teamName, ChatColor color, List<String> teamPlayerNames) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getStrings().write(0, teamName);
        packet.getIntegers().write(0, CREATE_TEAM);
        packet.getSpecificModifier(Collection.class)
                .write(0, new ArrayList<>(teamPlayerNames));
        packet.getModifier().writeDefaults();
        InternalStructure teamParam = packet.getOptionalStructures().read(0).orElse(null);
        if (teamParam != null) {
            teamParam.getChatComponents().write(0, WrappedChatComponent.fromText(teamName));
            teamParam.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat"))
                    .write(0, color);
            teamParam.getStrings().write(0, "always");
        }
        return packet;
    }

    static PacketContainer createTeamAddPlayerPacket(String teamName, List<String> addingPlayers) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getStrings().write(0, teamName);
        packet.getIntegers().write(0, ADD_PLAYER);
        packet.getSpecificModifier(Collection.class)
                .write(0, new ArrayList<>(addingPlayers));
        return packet;
    }

    static PacketContainer createTeamRemovePlayerPacket(String teamName, List<String> removingPlayers) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.SCOREBOARD_TEAM);
        packet.getStrings().write(0, teamName);
        packet.getIntegers().write(0, REMOVE_PLAYER);
        packet.getSpecificModifier(Collection.class)
                .write(0, new ArrayList<>(removingPlayers));
        return packet;
    }
}
