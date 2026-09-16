# Lever Torch

A Fabric mod that adds one block: a lever wearing a torch.

## Features

- **A lever, exactly**: placement, attachment, the click, the signal and the pull are vanilla's, inherited whole from the lever block. Anything a lever does, this does.
- **A torch, exactly**: while off it is the vanilla torch, pixel for pixel, in whichever placement it sits: upright on the floor, angled out from a wall, hanging from a ceiling.
- **The tell is the swing**: pulling it swings the torch over toward the way it faces, the way a lever's handle throws.
- **Lit either way**: it burns at a torch's light level in both states, so the light never gives the state away.

## Hangs From A Slab

Vanilla will not hang a lever from the underside of a top slab, because that face is not at the
block's edge. It is a flat face all the same, so here a lever hangs from it - the lever torch and
vanilla's own lever alike - and on a Pandorical client the model lifts half a block to meet the
slab rather than floating under it. A vanilla client sees it hang at the block's ceiling line.

## Crafting

A lever and a torch, shapeless.

## Pandorical

Lever Torch runs server-side, and Pandorical is required: the server will not load this mod without it. It registers its block and item models through Pandorical's content sync.

**The Pandorical mod must be installed client-side** to see the Lever Torch rendered as a torch. Without it the block still works (a connecting client can pull it and it still powers what it is wired to), but it does not render correctly.

## Development

Installing and the art pipeline are in [DEVELOPMENT.md](DEVELOPMENT.md).

## License

MIT, see [LICENSE](LICENSE).
