package net.stras.dimmablelamps.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.stras.dimmablelamps.item.ModItems;

import javax.annotation.Nullable;

public class LampBlock extends Block {

    public static final IntegerProperty LIT = IntegerProperty.create("lit", 0, 15);

    public LampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LIT, 0));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                Block block, BlockPos fromPos, boolean isMoving) {

        if (!level.isClientSide) {
            int power = level.getBestNeighborSignal(pos);
            level.setBlock(pos, state.setValue(LIT, power), 3);
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int power = context.getLevel().getBestNeighborSignal(context.getClickedPos());
        return this.defaultBlockState().setValue(LIT, power);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos,
                                 Player player, InteractionHand hand, BlockHitResult result) {

        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            ItemStack stack = player.getItemInHand(hand);

            if (stack.is(ModItems.WRENCH.get())) {
                level.setBlock(pos, state.cycle(LIT), 3);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT);
    }
}
