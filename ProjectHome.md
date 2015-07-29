# AndEngine - TMX TiledMap Artifact Fixer #

This tool was created to fix artifacts of TMXTiledMaps in [AndEngine](http://code.google.com/p/andengine/).

Those artifacts usually express themselves in horziontal and/or vertical lines appearing between the tiles. Example:

![http://andenginetmxtiledmapartifactfixer.googlecode.com/files/tiledmap_with_artifacts.png](http://andenginetmxtiledmapartifactfixer.googlecode.com/files/tiledmap_with_artifacts.png)

## Usage ##
  * -f X  | the filename of the tileset to fix.
  * -o X  | the output filename of the fixed tileset.
  * -m X  | the margin of the supplied tileset (default: 0).
  * -s X  | the spacing of the supplied tileset (default: 0).
  * -w X  | the width of a tile in the supplied tileset.
  * -h X  | the height of a tile in the supplied tileset.
```
-f C:\Users\nico\Desktop\test\desert_tiled_raw.png -o C:\Users\nico\Desktop\test\desert_tiled_fixed.png -w 32 -h 32 -m 1 -s 1
```

## Example ##
Before and After fixing a TileSet:
![http://andenginetmxtiledmapartifactfixer.googlecode.com/files/desert_tiled.png](http://andenginetmxtiledmapartifactfixer.googlecode.com/files/desert_tiled.png)
![http://andenginetmxtiledmapartifactfixer.googlecode.com/files/fixed_desert_tiled.png](http://andenginetmxtiledmapartifactfixer.googlecode.com/files/fixed_desert_tiled.png)