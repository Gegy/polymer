package eu.pb4.polymer.mixin.block;

import eu.pb4.polymer.block.BlockHelper;
import eu.pb4.polymer.interfaces.ChunkDataS2CPacketInterface;
import eu.pb4.polymer.interfaces.VirtualObject;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.packet.s2c.play.ChunkDataS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mixin(ChunkDataS2CPacket.class)
public class ChunkDataS2CPacketMixin implements ChunkDataS2CPacketInterface {
    @Unique
    private WorldChunk worldChunk;

    @Inject(method = "<init>(Lnet/minecraft/world/chunk/WorldChunk;)V", at = @At("TAIL"))
    private void polymer_storeWorldChunk(WorldChunk chunk, CallbackInfo ci) {
        this.worldChunk = chunk;
    }

    @Redirect(method = "<init>(Lnet/minecraft/world/chunk/WorldChunk;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;entrySet()Ljava/util/Set;"))
    private Set<Map.Entry<BlockPos, BlockEntity>> polymer_dontAddVirtualBlockEntities(Map<BlockPos, BlockEntity> map) {
        Set<Map.Entry<BlockPos, BlockEntity>> blockEntities = new HashSet<>();

        for (var entry : map.entrySet()) {
            if (!(entry.getValue() instanceof VirtualObject) && !BlockHelper.isVirtualBlockEntity(entry.getValue().getType())) {
                blockEntities.add(entry);
            }
        }

        return blockEntities;
    }

    public WorldChunk polymer_getWorldChunk() {
        return this.worldChunk;
    }
}
