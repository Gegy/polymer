package eu.pb4.polymer.mixin.block;

import eu.pb4.polymer.block.VirtualBlock;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.network.packet.s2c.play.PlayerActionResponseS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerActionResponseS2CPacket.class)
public class PlayerActionResponseS2CPacketMixin {
    @Shadow @Final private BlockState state;

    @ModifyArg(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getRawIdFromState(Lnet/minecraft/block/BlockState;)I"))
    private BlockState replaceWithVirtualBlockState(BlockState state) {
        if (state.getBlock() instanceof VirtualBlock) {
            return ((VirtualBlock) state.getBlock()).getVirtualBlockState(state);
        }
        return state;
    }


    @Environment(EnvType.CLIENT)
    @Inject(method = "getBlockState", at = @At("HEAD"), cancellable = true)
    public void replaceWithVirtualState(CallbackInfoReturnable<BlockState> cir) {
        if (this.state.getBlock() instanceof VirtualBlock virtualBlock) {
            cir.setReturnValue(virtualBlock.getVirtualBlockState(this.state));
        }
    }
}