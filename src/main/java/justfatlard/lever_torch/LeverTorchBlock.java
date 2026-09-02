package justfatlard.lever_torch;

import java.util.Map;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A lever wearing a torch. Every mechanic is vanilla's, inherited whole from
 * {@link LeverBlock}: placement, attachment, the pull, the click, the signal.
 *
 * <p>Only the outline is restated, because the model no longer sits where a lever's does.
 * Each shape below is the bounding box of the matching model in
 * {@code assets/.../models/block}, so the box a player clicks tracks the torch through its
 * swing instead of standing where the lever handle used to be.
 */
public class LeverTorchBlock extends LeverBlock {
	private static final VoxelShape FLOOR_OFF = Block.box(6, 0, 6, 10, 10, 10);
	private static final VoxelShape CEILING_OFF = Block.box(6, 6, 6, 10, 16, 10);

	private static final Map<Direction, VoxelShape> FLOOR_ON = byFacing(
		Block.box(6, 0, 0, 10, 8, 9),
		Block.box(7, 0, 6, 16, 8, 10),
		Block.box(6, 0, 7, 10, 8, 16),
		Block.box(0, 0, 6, 9, 8, 10));

	private static final Map<Direction, VoxelShape> CEILING_ON = byFacing(
		Block.box(6, 8, 0, 10, 16, 9),
		Block.box(7, 8, 6, 16, 16, 10),
		Block.box(6, 8, 7, 10, 16, 16),
		Block.box(0, 8, 6, 9, 16, 10));

	private static final Map<Direction, VoxelShape> WALL_OFF = byFacing(
		Block.box(5.5, 3, 11, 10.5, 13.5, 16),
		Block.box(0, 3, 5.5, 5, 13.5, 10.5),
		Block.box(5.5, 3, 0, 10.5, 13.5, 5),
		Block.box(11, 3, 5.5, 16, 13.5, 10.5));

	private static final Map<Direction, VoxelShape> WALL_ON = byFacing(
		Block.box(5.5, 0.5, 8, 10.5, 9.5, 16),
		Block.box(0, 0.5, 5.5, 8, 9.5, 10.5),
		Block.box(5.5, 0.5, 0, 10.5, 9.5, 8),
		Block.box(8, 0.5, 5.5, 16, 9.5, 10.5));

	public LeverTorchBlock(Properties settings) {
		super(settings);
	}

	private static Map<Direction, VoxelShape> byFacing(VoxelShape north, VoxelShape east, VoxelShape south, VoxelShape west) {
		return Map.of(
			Direction.NORTH, north,
			Direction.EAST, east,
			Direction.SOUTH, south,
			Direction.WEST, west);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Direction facing = state.getValue(FACING);
		boolean powered = state.getValue(POWERED);

		return switch (state.getValue(FACE)) {
			case FLOOR -> powered ? FLOOR_ON.get(facing) : FLOOR_OFF;
			case CEILING -> powered ? CEILING_ON.get(facing) : CEILING_OFF;
			case WALL -> powered ? WALL_ON.get(facing) : WALL_OFF.get(facing);
		};
	}
}
