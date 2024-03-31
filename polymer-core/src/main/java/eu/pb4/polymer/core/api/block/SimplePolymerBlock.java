package eu.pb4.polymer.core.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;

/**
 * Minimalistic implementation of PolymerBlock
*/
public class SimplePolymerBlock extends Block implements PolymerBlock {
    private Block polymerBlock;

    public SimplePolymerBlock(Settings settings, Block polymerBlock) {
        super(settings);
        this.polymerBlock = polymerBlock;
    }

    @Override
    public BlockState getPolymerBlockState(BlockState state) {
        return this.polymerBlock.getDefaultState();
    }
}
