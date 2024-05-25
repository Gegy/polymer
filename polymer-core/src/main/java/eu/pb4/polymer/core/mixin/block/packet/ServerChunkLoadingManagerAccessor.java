package eu.pb4.polymer.core.mixin.block.packet;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ServerChunkLoadingManager;
import net.minecraft.world.chunk.WorldChunk;
import org.apache.commons.lang3.mutable.MutableObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerChunkLoadingManager.class)
public interface ServerChunkLoadingManagerAccessor {

    @Accessor("world")
    ServerWorld polymer$getWorld();

    @Accessor("watchDistance")
    int polymer$getWatchDistance();

    @Accessor("entityTrackers")
    Int2ObjectMap<ServerChunkLoadingManager.EntityTracker> polymer$getEntityTrackers();
}
