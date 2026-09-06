package justfatlard.lever_torch.mixin;

import justfatlard.lever_torch.SlabHung;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** Vanilla's lever, hung from a top slab, has its outline where the lifted model is. */
@Mixin(LeverBlock.class)
public abstract class LeverOutlineMixin {

	@Inject(method = "getShape", at = @At("RETURN"), cancellable = true)
	private void leverTorch$liftToSlab(BlockState state, BlockGetter level, BlockPos pos,
			CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if (SlabHung.hung(state, level, pos)) {
			cir.setReturnValue(SlabHung.raise(cir.getReturnValue()));
		}
	}
}
