package net.stras.dimmablelamps.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.stras.dimmablelamps.item.ModItems;

import javax.annotation.Nullable;

public class LampBlock extends Block {

    public static final IntegerProperty LIT = IntegerProperty.create("lit", 0, 15);
    public static final BooleanProperty TOGGLE_MODE = BooleanProperty.create("toggle_mode");
    public static final IntegerProperty BRIGHTNESS = IntegerProperty.create("brightness", 0, 15);

    private static final int TURN_OFF_DELAY = 4;

    public LampBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(LIT, 0)
                .setValue(TOGGLE_MODE, false)
                .setValue(BRIGHTNESS, 15));
    }

    private int targetLit(BlockState state, int power) {
        if (state.getValue(TOGGLE_MODE)) {
            return power > 0 ? state.getValue(BRIGHTNESS) : 0;
        }
        return power;
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos,
                                 Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide) return;

        int power = level.getBestNeighborSignal(pos);
        int target = targetLit(state, power);
        int current = state.getValue(LIT);

        if (target == current) return;

        if (target > current) {
            level.setBlock(pos, state.setValue(LIT, target), 3);
        } else {
            if (!level.getBlockTicks().hasScheduledTick(pos, this)) {
                level.scheduleTick(pos, this, TURN_OFF_DELAY);
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, net.minecraft.util.RandomSource random) {
        int power = level.getBestNeighborSignal(pos);
        int target = targetLit(state, power);
        if (target != state.getValue(LIT)) {
            level.setBlock(pos, state.setValue(LIT, target), 3);
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
        if (level.isClientSide || hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        if (!stack.is(ModItems.WRENCH.get())) return InteractionResult.PASS;

        if (player.isShiftKeyDown()) {
            BlockState newState = state.setValue(TOGGLE_MODE, false);
            int power = level.getBestNeighborSignal(pos);
            level.setBlock(pos, newState.setValue(LIT, power), 3);
        } else {
            int capturedBrightness = state.getValue(LIT) > 0 ? state.getValue(LIT) : 15;
            BlockState newState = state.setValue(TOGGLE_MODE, true)
                    .setValue(BRIGHTNESS, capturedBrightness);
            int power = level.getBestNeighborSignal(pos);
            level.setBlock(pos, newState.setValue(LIT, power > 0 ? capturedBrightness : 0), 3);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LIT, TOGGLE_MODE, BRIGHTNESS);
    }
}