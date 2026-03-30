package net.ruusika.brewerywinery.blocks;

import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.ruusika.brewerywinery.init.BreweryWineryItems;
import net.ruusika.brewerywinery.init.BreweryWineryProperties;


public class HopsBlock extends Block implements Fertilizable {
    public static final int MAX_HEIGHT_WITH_SUPPORT = 10;
    public static final int MAX_HEIGHT_WITHOUT_SUPPORT = 2;
    public static final IntProperty HOP_AGE = Properties.AGE_2;

    public HopsBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getStateManager().getDefaultState().with(HOP_AGE, 0)
                .with(BreweryWineryProperties.HAS_PLANT, true));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        BlockState below = world.getBlockState(pos.down());
        if (below.isOf(this)) return true;
        return below.isOf(Blocks.FARMLAND);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState,
                                                WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (!this.canPlaceAt(state, world, pos)) {
            return Blocks.AIR.getDefaultState();
        }

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

        if (!isTop(world, pos)) return;
        if (!isBaseMoist(world, pos)) return;
        if (world.getBaseLightLevel(pos, 0) < 9) return;
        if (random.nextInt(5) != 0) return;

        tryGrow(world, pos, state);
    }

    private void tryGrow(ServerWorld world, BlockPos topPos, BlockState topState) {
        int height = getColumnHeight(world, topPos);
        int maxHeight = getMaxHeight(world, topPos);
        int age = topState.get(HOP_AGE);

        if (age < 2) {
            world.setBlockState(topPos, topState.with(HOP_AGE, age + 1), Block.NOTIFY_LISTENERS);
            return;
        }

        if (height >= maxHeight) return;

        BlockPos above = topPos.up();
        if (!world.getBlockState(above).isAir()) return;

        world.setBlockState(above, this.getDefaultState().with(HOP_AGE, 0), Block.NOTIFY_LISTENERS);
        world.setBlockState(topPos, topState.with(HOP_AGE, 0), Block.NOTIFY_LISTENERS);
    }

    private boolean isTop(WorldView world, BlockPos pos) {
        return !world.getBlockState(pos.up()).isOf(this);
    }

    private BlockPos findBase(WorldView world, BlockPos anyPos) {
        BlockPos base = anyPos;
        while (world.getBlockState(base.down()).isOf(this)) {
            base = base.down();
        }
        return base;
    }

    private BlockPos findTop(WorldView world, BlockPos pos) {
        BlockPos top = pos;
        while (world.getBlockState(top.up()).isOf(this)) {
            top = top.up();
        }
        return top;
    }

    private boolean isBaseMoist(WorldView world, BlockPos anyPos) {
        BlockPos base = findBase(world, anyPos);
        BlockPos farmlandPos = base.down();
        BlockState farmland = world.getBlockState(farmlandPos);

        if (!farmland.isOf(Blocks.FARMLAND)) return false;
        return farmland.get(FarmlandBlock.MOISTURE) > 0;
    }

    private int getColumnHeight(WorldView world, BlockPos anyPosInColumn) {
        BlockPos base = anyPosInColumn;
        while (world.getBlockState(base.down()).isOf(this)) {
            base = base.down();
        }

        int i = 1;
        BlockPos cursor = base;
        while (world.getBlockState(cursor.up()).isOf(this)) {
            cursor = cursor.up();
            i++;
            if (i > MAX_HEIGHT_WITH_SUPPORT) break;
        }
        return i;
    }

    private int getMaxHeight(WorldView world, BlockPos anyPosInColumn) {
        BlockPos base = anyPosInColumn;
        while (world.getBlockState(base.down()).isOf(this)) {
            base = base.down();
        }

        for (int i = 1; i <= MAX_HEIGHT_WITH_SUPPORT; i++) {
            BlockPos p = base.up(i);
            BlockState s = world.getBlockState(p);
            if (s.isIn(BlockTags.WOODEN_FENCES)) {
                return MAX_HEIGHT_WITH_SUPPORT;
            }
        }
        return MAX_HEIGHT_WITHOUT_SUPPORT;
    }

    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state, boolean isClient) {
        if (!state.isOf(this)) return false;
        if (!state.get(BreweryWineryProperties.HAS_PLANT)) return false;
        if (!isBaseMoist(world, pos)) return false;

        BlockPos topPos = findTop(world, pos);
        BlockState topState = world.getBlockState(topPos);

        if (!topState.isOf(this)) return false;
        if (!topState.get(BreweryWineryProperties.HAS_PLANT)) return false;

        int height = getColumnHeight(world, pos);
        int maxHeight = getMaxHeight(world, pos);
        if (height < maxHeight) return true;

        return state.get(HOP_AGE) < 2;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return isFertilizable(world, pos, state, world.isClient);
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (!state.isOf(this)) return;
        if (!state.get(BreweryWineryProperties.HAS_PLANT)) return;
        if (!isBaseMoist(world, pos)) return;

        BlockPos topPos = findTop(world, pos);
        BlockState topState = world.getBlockState(topPos);
        if (!topState.isOf(this) || !topState.get(BreweryWineryProperties.HAS_PLANT)) return;

        tryGrow(world, topPos, topState);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HOP_AGE, BreweryWineryProperties.HAS_PLANT);
    }

    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!state.isOf(this)) return ActionResult.PASS;
        if (!state.get(BreweryWineryProperties.HAS_PLANT)) return ActionResult.PASS;

        ItemStack heldStack = player.getStackInHand(hand);
        if (heldStack.isOf(Items.BONE_MEAL)) return ActionResult.PASS;

        BlockPos topPos = findTop(world, pos);
        BlockState topState = world.getBlockState(topPos);

        if (!topState.isOf(this)) return ActionResult.PASS;
        if (!topState.get(BreweryWineryProperties.HAS_PLANT)) return ActionResult.PASS;

        if (topState.get(HOP_AGE) < 2) return ActionResult.PASS;
        if (!world.isClient) {
            int dropCount = 1 + world.random.nextInt(2);
            ItemStack drop = new ItemStack(BreweryWineryItems.HOPS, dropCount);
            Block.dropStack(world, topPos, drop);

            world.setBlockState(topPos, topState.with(HOP_AGE, 0), Block.NOTIFY_LISTENERS);
            world.playSound(null, topPos, SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, SoundCategory.BLOCKS,
                    1.0F, 0.8F + world.random.nextFloat() * 0.4F);
        }

        return ActionResult.success(world.isClient);
    }
}
