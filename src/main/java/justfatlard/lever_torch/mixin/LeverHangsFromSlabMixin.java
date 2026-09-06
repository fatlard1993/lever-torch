package justfatlard.lever_torch.mixin;

import justfatlard.lever_torch.SlabHung;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * A lever may hang from the underside of a top slab.
 *
 * <p>Levers only: buttons and grindstones share this class and would float under a slab with
 * nothing lifting them to meet it.
 */
@Mixin(FaceAttachedHorizontalDirectionalBlock.class)
public abstract class LeverHangsFromSlabMixin {

	@Inject(method = "canSurvive", at = @At("HEAD"), cancellable = true)
	private void leverTorch$hangFromTopSlab(BlockState state, LevelReader level, BlockPos pos,
			CallbackInfoReturnable<Boolean> cir) {
		if ((Object) this instanceof LeverBlock && SlabHung.hung(state, level, pos)) {
			cir.setReturnValue(true);
		}
	}
}
