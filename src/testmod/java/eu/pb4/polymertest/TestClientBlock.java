package eu.pb4.polymertest;

import eu.pb4.polymer.api.block.PlayerAwarePolymerBlock;
import eu.pb4.polymer.api.client.PolymerClientDecoded;
import eu.pb4.polymer.api.client.PolymerKeepModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Direction;

public class TestClientBlock extends Block implements PlayerAwarePolymerBlock, PolymerKeepModel, PolymerClientDecoded {
    public TestClientBlock(Settings settings) {
        super(settings);
    }

    @Override
    public Block getPolymerBlock(BlockState state) {
        return Blocks.DISPENSER;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return Blocks.DISPENSER.getDefaultState().with(DispenserBlock.FACING, Direction.UP);
    }

    @Override
    public BlockState getPolymerBlockState(ServerPlayerEntity player, BlockState state) {
        return player.getServer().isHost(player.getGameProfile()) ? state : Blocks.DIAMOND_BLOCK.getDefaultState();
    }
}