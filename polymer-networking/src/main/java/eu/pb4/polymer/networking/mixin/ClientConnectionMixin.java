package eu.pb4.polymer.networking.mixin;

import eu.pb4.polymer.networking.impl.ExtClientConnection;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.NbtElement;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin implements ExtClientConnection {
    @Shadow private int packetsReceivedCounter;
    @Shadow private Channel channel;

    @Unique
    private String polymerNet$version = "";
    @Unique
    private final Object2IntMap<Identifier> polymerNet$protocolMap = new Object2IntOpenHashMap<>();

    @Unique
    private final Object2ObjectMap<Identifier, NbtElement> polymerNet$metadata = new Object2ObjectOpenHashMap<>();
    @Unique
    private Consumer<Packet<?>> polymerNet$packetConsumer;
    @Override
    public boolean polymerNet$hasPolymer() {
        return !this.polymerNet$version.isEmpty();
    }

    @Override
    public String polymerNet$version() {
        return this.polymerNet$version;
    }

    @Override
    public void polymerNet$setVersion(String version) {
        this.polymerNet$version = version;
    }

    @Override
    public int polymerNet$getSupportedVersion(Identifier identifier) {
        return this.polymerNet$protocolMap.getOrDefault(identifier, -1);
    }

    @Override
    public void polymerNet$setSupportedVersion(Identifier identifier, int i) {
        this.polymerNet$protocolMap.put(identifier, i);
    }

    @Override
    public Object2IntMap<Identifier> polymerNet$getSupportMap() {
        return this.polymerNet$protocolMap;
    }

    @Override
    public Object2ObjectMap<Identifier, NbtElement> polymerNet$getMetadataMap() {
        return this.polymerNet$metadata;
    }

    @Inject(method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
    private void polymerNet$handlePacket(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
        if (this.polymerNet$packetConsumer != null) {
            this.polymerNet$packetConsumer.accept(packet);
            ci.cancel();
            this.packetsReceivedCounter++;
        }
    }

    @Override
    public void polymerNet$wrongPacketConsumer(Consumer<Packet<?>> consumer) {
        this.polymerNet$packetConsumer = consumer;
    }

    @Override
    public Channel polymerNet$getChannel() {
        return this.channel;
    }
}
