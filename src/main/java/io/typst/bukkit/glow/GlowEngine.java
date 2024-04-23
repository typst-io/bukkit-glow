package io.typst.bukkit.glow;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;

import java.util.List;
import java.util.stream.Collectors;

public class GlowEngine {

    public static void register(Plugin plugin) {
        GlowService glowService = Bukkit.getServicesManager().load(GlowService.class);
        if (glowService == null) {
            GlowService newService = new GlowService();
            Bukkit.getServicesManager().register(
                    GlowService.class,
                    newService,
                    plugin,
                    ServicePriority.Normal
            );
            glowService = newService;
        }
        GlowAPI glow = new GlowAPI(plugin);
        // glow info appender
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_METADATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                if (event.getPacketType() != PacketType.Play.Server.ENTITY_METADATA) return;
                Player receiver = event.getPlayer();
                if (!glow.hasGlowingView(receiver.getUniqueId())) return;
                PacketContainer packet = event.getPacket().shallowClone();
                Entity target = event.getPacket().getEntityModifier(event).read(0);
                if (target == null) return;
                if (!glow.checkGlowing(target.getName(), receiver.getUniqueId())) return;
                List<WrappedDataValue> dataValues = packet.getDataValueCollectionModifier().read(0).stream()
                        .map(a ->
                                a.getIndex() == 0
                                        ? new WrappedDataValue(a.getIndex(), a.getSerializer(), a.getValue())
                                        : a
                        )
                        .collect(Collectors.toList());
                WrappedDataValue head = dataValues.stream()
                        .filter(a -> a.getIndex() == 0)
                        .findFirst()
                        .orElse(null);
                if (head != null) {
                    byte headByte = (byte) head.getValue();
                    byte newByte = GlowPackets.modifyGlowing(headByte, true);
                    head.setValue(newByte);
                } else {
                    dataValues.add(new WrappedDataValue(0, WrappedDataWatcher.Registry.get(Byte.class), GlowPackets.METADATA_GLOWING));
                }
                packet.getDataValueCollectionModifier().write(0, dataValues);
                event.setPacket(packet);
            }
        });
    }
}
