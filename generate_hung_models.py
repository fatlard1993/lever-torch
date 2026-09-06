#!/usr/bin/env python3
"""The lever, and the lever torch, hung from the underside of a top slab.

A top slab's underside is half a block up from where a ceiling model expects its ceiling to be,
so a lever hung there would float. These are the ceiling models lifted eight pixels, up into the
empty half of the slab's block, so the base meets the slab. Pandorical's client swaps them in when
the block above is a top slab; the blockstate never learns about it.

One model per facing per state, the blockstate's rotation baked in, because a model chosen by
neighbour has no blockstate rotation to lean on. The lever torch's come from this mod's own
ceiling models; vanilla's lever's from vanilla's, read out of the jar, and land in the minecraft
namespace where the client looks for them. Deterministic.

Usage: python3 generate_hung_models.py [jar]
"""
import glob
import json
import os
import sys
import zipfile

HERE = os.path.dirname(os.path.abspath(__file__))
ASSETS = os.path.join(HERE, "src/main/resources/assets")
MOD_ID = "lever-torch-justfatlard"

LIFT = 8
FACES = ["north", "east", "south", "west"]
# The blockstate's y rotation for each ceiling facing.
TORCH_TURNS = {"north": 0, "east": 1, "south": 2, "west": 3}
LEVER_TURNS = {"south": 0, "west": 1, "north": 2, "east": 3}


def minecraft_version():
    for line in open(os.path.join(HERE, "gradle.properties")):
        key, sep, value = line.partition("=")
        if sep and key.strip() == "minecraft_version":
            return value.strip()


def find_jar():
    if len(sys.argv) > 1:
        return sys.argv[1]
    cache = os.path.expanduser("~/.gradle/caches/fabric-loom")
    found = []
    for name in ("minecraft-client.jar", "minecraft-merged.jar"):
        found += glob.glob(os.path.join(cache, minecraft_version() or "*", name))
    if not found:
        sys.exit("no cached Minecraft jar found: build once, or pass a jar path")
    return max(found, key=os.path.getmtime)


def flip_x(elements):
    """A blockstate x rotation of 180: upside down about the block centre."""
    out = json.loads(json.dumps(elements))
    swap = {"up": "down", "down": "up", "north": "south", "south": "north"}
    for e in out:
        (x0, y0, z0), (x1, y1, z1) = e["from"], e["to"]
        e["from"], e["to"] = [x0, 16 - y1, 16 - z1], [x1, 16 - y0, 16 - z0]
        e["faces"] = {swap.get(k, k): v for k, v in e["faces"].items()}
        for face in e["faces"].values():
            if face.get("cullface") in swap:
                face["cullface"] = swap[face["cullface"]]
        if "rotation" in e:
            r = e["rotation"]
            ox, oy, oz = r["origin"]
            r["origin"] = [ox, 16 - oy, 16 - oz]
            if r["axis"] != "x":
                r["angle"] = -r["angle"]
    return out


def turn_y(elements, quarter_turns):
    """Clockwise about the block centre, the way a blockstate y rotation turns a model."""
    out = json.loads(json.dumps(elements))
    for _ in range(quarter_turns):
        for e in out:
            (x0, y0, z0), (x1, y1, z1) = e["from"], e["to"]
            e["from"], e["to"] = [16 - z1, y0, x0], [16 - z0, y1, x1]
            e["faces"] = {(FACES[(FACES.index(k) + 1) % 4] if k in FACES else k): v for k, v in e["faces"].items()}
            for face in e["faces"].values():
                if face.get("cullface") in FACES:
                    face["cullface"] = FACES[(FACES.index(face["cullface"]) + 1) % 4]
            if "rotation" in e:
                r = e["rotation"]
                ox, oy, oz = r["origin"]
                r["origin"] = [16 - oz, oy, ox]
                if r["axis"] == "x":
                    r["axis"] = "z"
                elif r["axis"] == "z":
                    r["axis"], r["angle"] = "x", -r["angle"]
    return out


def lift(elements):
    out = json.loads(json.dumps(elements))
    for e in out:
        e["from"][1] += LIFT
        e["to"][1] += LIFT
        if "rotation" in e:
            e["rotation"]["origin"][1] += LIFT
    return out


def write(namespace, name, model):
    path = os.path.join(ASSETS, namespace, "models/block", name + ".json")
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w") as f:
        json.dump(model, f, indent=2)
        f.write("\n")


def main():
    count = 0
    # The torch: this mod's ceiling models, turned to face, lifted.
    for state, source in (("", "lever_torch_ceiling"), ("_on", "lever_torch_ceiling_on")):
        model = json.load(open(os.path.join(ASSETS, MOD_ID, "models/block", source + ".json")))
        for facing, turns in TORCH_TURNS.items():
            out = dict(model)
            out["elements"] = lift(turn_y(model["elements"], turns))
            write(MOD_ID, "lever_torch_hung_%s%s" % (facing, state), out)
            count += 1

    # Vanilla's lever: on the ceiling it is the floor model upside down, and the blockstate
    # swaps the two handle models to keep the throw reading right; the same swap here.
    with zipfile.ZipFile(find_jar()) as jar:
        for state, source in (("", "lever_on"), ("_on", "lever")):
            model = json.loads(jar.read("assets/minecraft/models/block/%s.json" % source))
            for facing, turns in LEVER_TURNS.items():
                out = dict(model)
                out["elements"] = lift(turn_y(flip_x(model["elements"]), turns))
                write("minecraft", "lever_hung_%s%s" % (facing, state), out)
                count += 1
    print("wrote %d hung models" % count)


if __name__ == "__main__":
    main()
