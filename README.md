# Lever Torch

A Fabric mod that adds one block: a lever wearing a torch.

## Features

- **A lever, exactly**: placement, attachment, the click, the signal and the pull are vanilla's, inherited whole from the lever block. Anything a lever does, this does.
- **A torch, exactly**: while off it is the vanilla torch, pixel for pixel, in whichever placement it sits: upright on the floor, angled out from a wall, hanging from a ceiling.
- **The tell is the swing**: pulling it swings the torch over toward the way it faces, the way a lever's handle throws.
- **Lit either way**: it burns at a torch's light level in both states, so the light never gives the state away.

## Crafting

A lever and a torch, shapeless.

## Pandorical

Lever Torch registers its block and item models through Pandorical's content sync.

**The Pandorical mod must be installed client-side** to see the Lever Torch rendered as a torch. Without it the block still works (a connecting client can pull it and it still powers what it is wired to), but it does not render correctly.

## Installation

Install server-side alongside its declared dependencies (see `fabric.mod.json`); connecting clients need only Pandorical. Version targets live in `gradle.properties` (Minecraft, loader, Fabric API) and `fabric.mod.json` (Java).

## Art

`generate_icon.py` and `generate_textures.py` cut the mod's icon and item sprite out of the vanilla jar. Both are deterministic; re-run either after a Minecraft version bump.

## License

MIT, see [LICENSE](LICENSE).
