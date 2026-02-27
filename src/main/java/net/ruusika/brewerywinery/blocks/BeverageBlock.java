package net.ruusika.brewerywinery.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class BeverageBlock extends HorizontalFacingBlock {

    private final Shape shape;
    private final Color color;
    private final Map<Direction, VoxelShape> outlineShapesByFacing;

    public BeverageBlock(Settings settings, Shape shape, Color color) {
        super(settings);
        this.shape = shape;
        this.color = color;

        this.outlineShapesByFacing = createOutlineShapes(shape);

        setDefaultState(getDefaultState().with(FACING, Direction.NORTH));
    }

    public Shape getSize() {
        return shape;
    }

    public Color getColor() {
        return color;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public @Nullable BlockState getPlacementState(ItemPlacementContext ctx) {
        return getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        return outlineShapesByFacing.getOrDefault(facing, outlineShapesByFacing.get(Direction.NORTH));
    }

    private static Map<Direction, VoxelShape> createOutlineShapes(Shape shape) {
        VoxelShape north = Block.createCuboidShape(
                shape.getMin().getX(), shape.getMin().getY(), shape.getMin().getZ(),
                shape.getMax().getX(), shape.getMax().getY(), shape.getMax().getZ()
        );

        EnumMap<Direction, VoxelShape> map = new EnumMap<>(Direction.class);
        map.put(Direction.NORTH, north);
        map.put(Direction.EAST, rotateY90(north));
        map.put(Direction.SOUTH, rotateY90(map.get(Direction.EAST)));
        map.put(Direction.WEST, rotateY90(map.get(Direction.SOUTH)));
        return map;
    }

    private static VoxelShape rotateY90(VoxelShape shape) {

        var boxes = shape.getBoundingBoxes();
        VoxelShape out = Block.createCuboidShape(0, 0, 0, 0, 0, 0);
        boolean first = true;

        for (var box : boxes) {
            // Koordinaten 0..1 -> convert to 0..16
            double minX = box.minX * 16.0;
            double minY = box.minY * 16.0;
            double minZ = box.minZ * 16.0;
            double maxX = box.maxX * 16.0;
            double maxY = box.maxY * 16.0;
            double maxZ = box.maxZ * 16.0;

            // rotate around center: (x,z) -> (16 - z, x)
            double rMinX = 16.0 - maxZ;
            double rMaxX = 16.0 - minZ;
            double rMinZ = minX;
            double rMaxZ = maxX;

            VoxelShape rotated = Block.createCuboidShape(rMinX, minY, rMinZ, rMaxX, maxY, rMaxZ);
            if (first) {
                out = rotated;
                first = false;
            } else {
                out = net.minecraft.util.shape.VoxelShapes.union(out, rotated);
            }
        }
        return out;
    }

    public enum Shape {
        SMALL("small", new Vec3i(5, 0, 5), new Vec3i(11, 8, 11)),
        LARGE("large", new Vec3i(5, 0, 5), new Vec3i(11, 10, 11));

        private final String identifier;
        private final Vec3i min;
        private final Vec3i max;

        Shape(String identifier, Vec3i min, Vec3i max){
            this.identifier = identifier;
            this.min = min;
            this.max = max;

        }

        public String getName() {
            return identifier;
        }

        public Vec3i getMax() {
            return max;
        }

        public Vec3i getMin() {
            return min;
        }
    }

    public enum Color {
        DARK("dark", 0x64491F),
        LIGHT("light", 0xBD7E37),
        RED("red", 0x5D271B),

        DARK_RED("dark_red", 0x8D021F),
        WHITE("white", 0xf7cac9),
        ROSE("rose", 0xF9E8C0),

        HONEY("honey", 0xCB8E00);

        private final String identifier;
        private final int color;

        Color(String identifier, int color){
            this.identifier = identifier;
            this.color = color;
        }

        public String getName() {
            return identifier;
        }

        public int getColor() {
            return color;
        }
    }
}
