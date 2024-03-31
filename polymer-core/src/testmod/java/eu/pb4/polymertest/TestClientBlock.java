package eu.pb4.polymertest;

import eu.pb4.polymer.core.api.block.PolymerBlock;
import eu.pb4.polymer.core.api.utils.PolymerClientDecoded;
import eu.pb4.polymer.core.api.utils.PolymerKeepModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;

public class TestClientBlock extends Block implements PolymerBlock, PolymerKeepModel, PolymerClientDecoded {
    public TestClientBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return Blocks.DISPENSER.getDefaultState().with(DispenserBlock.FACING, Direction.UP);
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state, ServerPlayerEntity player) {
        return getPolymerBlockState(state);
    }
}
