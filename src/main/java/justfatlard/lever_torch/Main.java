package justfatlard.lever_torch;

import justfatlard.pandorical.api.BlockRegistration;
import justfatlard.pandorical.api.ItemRegistration;
import justfatlard.pandorical.api.PandoricalApi;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class Main implements ModInitializer {
	public static final String MOD_ID = "lever-torch-justfatlard";

	public static final Identifier LEVER_TORCH_ID = Identifier.fromNamespaceAndPath(MOD_ID, "lever_torch");

	public static final ResourceKey<Block> LEVER_TORCH_BLOCK_KEY = ResourceKey.create(Registries.BLOCK, LEVER_TORCH_ID);
	public static final ResourceKey<Item> LEVER_TORCH_ITEM_KEY = ResourceKey.create(Registries.ITEM, LEVER_TORCH_ID);

	// Torch settings, not lever settings: it burns at a torch's light level and breaks at a
	// torch's speed, so nothing but the click tells it apart from the real thing.
	public static final LeverTorchBlock LEVER_TORCH_BLOCK = new LeverTorchBlock(
		BlockBehaviour.Properties.of()
			.noCollision()
			.instabreak()
			.sound(SoundType.WOOD)
			.lightLevel(state -> 14)
			.pushReaction(PushReaction.POPPED)
			.setId(LEVER_TORCH_BLOCK_KEY)
	);

	public static final LeverTorchItem LEVER_TORCH_ITEM = new LeverTorchItem(
		LEVER_TORCH_BLOCK,
		new Item.Properties().setId(LEVER_TORCH_ITEM_KEY).useBlockDescriptionPrefix()
	);

	@Override
	public void onInitialize() {
		if (PandoricalApi.isAvailable()) {
			PandoricalApi.content().registerBlock(MOD_ID + ":lever_torch", new BlockRegistration()
				// A Pandorical client builds its stand-in from this block's settings, so the
				// base has to be the torch: anything else and the block a client sees is
				// unlit, or solid, or both.
				.baseBlock("minecraft:torch")
				// A right-click pulls the lever, so the client must not predict a placement.
				.interactive()
				.model(MOD_ID + ":block/lever_torch_floor"));
			PandoricalApi.content().registerItem(MOD_ID + ":lever_torch", new ItemRegistration()
				.model(MOD_ID + ":item/lever_torch"));
			PandoricalApi.content().registerModAssets(MOD_ID);
		}

		Registry.register(BuiltInRegistries.BLOCK, LEVER_TORCH_ID, LEVER_TORCH_BLOCK);
		Registry.register(BuiltInRegistries.ITEM, LEVER_TORCH_ID, LEVER_TORCH_ITEM);

		ResourceKey<CreativeModeTab> tabKey = ResourceKey.create(
			Registries.CREATIVE_MODE_TAB, Identifier.fromNamespaceAndPath(MOD_ID, "lever_torch"));
		CreativeModeTab leverTorchGroup = FabricCreativeModeTab.builder()
			.title(Component.literal("Lever Torch"))
			.icon(() -> new ItemStack(LEVER_TORCH_ITEM))
			.displayItems((context, entries) -> {
				entries.accept(new ItemStack(LEVER_TORCH_ITEM));
			})
			.build();
		Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, tabKey, leverTorchGroup);

		System.out.println("[" + MOD_ID + "] Loaded (server-side with Pandorical)");
	}
}
