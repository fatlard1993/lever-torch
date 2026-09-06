package justfatlard.lever_torch;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * A lever hung from the underside of a top slab.
 *
 * <p>Vanilla refuses it: the underside of a top slab is not a sturdy face, because it is not at
 * the block's edge. It is a flat face all the same, half a block up, and a lever wants to hang
 * from it. So a lever on the ceiling survives under a top slab, and its outline lifts half a
 * block to meet it, the way the model does on a Pandorical client.
 */
public final class SlabHung {
	private SlabHung() {}

	/** Half a block: the distance from the block's ceiling up to a top slab's underside. */
	private static final double LIFT = 0.5;

	/** Whether this ceiling-mounted state has a top slab, and only a top slab, above it. */
	public static boolean hung(BlockState state, BlockGetter level, BlockPos pos) {
		if (!state.hasProperty(FaceAttachedHorizontalDirectionalBlock.FACE)
				|| state.getValue(FaceAttachedHorizontalDirectionalBlock.FACE) != AttachFace.CEILING) {
			return false;
		}
		BlockState above = level.getBlockState(pos.above());
		return above.getBlock() instanceof SlabBlock && above.getValue(SlabBlock.TYPE) == SlabType.TOP;
	}

	public static VoxelShape raise(VoxelShape shape) {
		return shape.move(0.0, LIFT, 0.0);
	}
}
