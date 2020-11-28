# Danielle Randomizer v0.2

A simple randomizer for Prey 2017 (code-named "Danielle").

## Description

Randomizes various entities in Prey 2017 via an external executable that overwrites the level files and generates a patch_randomizer.pak. Before overwriting, level files are backed up in the same directory as "level_backup.pak".

Randomizer features:

* Randomized item/physics prop spawns
* Randomized enemy/NPC spawns
* Randomized human appearance
* Randomized voice lines
* Randomized loot tables
* Randomized neuromod upgrade tree
* Randomized station connectivity

Other features:

* Start on 2nd day
* Add loot to Morgan's apartment
* Remove scan requirement for neuromods
* Unlock all doors/safes/workstations (doesn't work for certain scripted doors or the main lift)
* Configurable spawn rates for item/enemy randomization

## Getting Started

**IMPORTANT if you want to avoid game crashes** If you plan to start a new randomized game in an existing save slot, manually delete the old save data first (and ensure cloud save doesn't try to restore it). By default this should be in `C:\Users\<Name>\Saved Games\Arkane Studios\Prey\SaveGames`. This is because unfortunately Prey does not always cleanly delete old files before starting up a new game. If you don't do this, there's a chance your old save files will stay there and conflict with the randomized level files. The same applies if you are done with this randomized save slot and want to use it for a normal game.

### Dependencies

* Requires a valid copy of Prey 2017 on Steam or GOG (the Windows Store version of Prey 2017 cannot be modded).
* Must have [Java for Windows](http://java.com/download) installed.

### Running the randomizer

1. Unzip danielle_randomizer.zip anywhere.
2. Ensure danielle_randomizer.exe is in the same directory as the included data/ folder and settings.json file.
3. Run danielle_randomizer.exe to start up the GUI.
4. Specify Prey install directory.
5. Specify desired randomization settings.
6. Click "Install" to generate randomized files and install them.
7. Start a new randomized game!

When done with your randomized game, use the "Uninstall" button to revert any changes made by this randomizer. Note that once the mod is uninstalled, all save files created while the randomizer was in effect will become unplayable.

If the randomizer spends a long time at the "Installing..." step, check the generated log.txt file to see if it encountered any errors. Sometimes it can just take a long time to generate the first time around.

### Uninstalling the randomizer

* Ideally, use the "Uninstall" function within the randomizer GUI to uninstall any generated files before uninstalling. If this isn't possible, you can use the GOG or Steam client to redownload any missing or modified files.
* Delete patch_randomizer.pak in Prey\GameSDK\Precache, if it exists.
* To uninstall the randomizer, delete danielle_randomizer.exe and its associated files.

### Configuring custom randomizer settings

If you'd like to tweak the preset spawn rates for items and enemies, you can specify them yourself in settings.json.

This randomizer has assigned multiple tags to every entity in the game (ex. weapons are "Weapons", food is "ArkFood", etc). To see a list of supported entities and tags, see tagstoentities.csv (shows the entities associated with each tag) and entitiestotags.csv (shows the tags generated for each entity).

The level randomizer in this project uses a list of "filters" to process the level files. A "filter" consists of five arrays:

* input_tags - if an entity matches one of these tags, randomize it
* output_tags - acceptable tags for items to come out of this filter (first a tag is chosen randomly from this list, then an entity matching that tag is chosen randomly)
* output_weights - int array of relative weights to assign to each output tags. Must have the same length as output_tags.
* do_not_touch_tags - if an entity matches one of these tags, do NOT randomize it. overrides input_tags.
* do_not_output_tags - do NOT output any items that match a tag in this list. overrides output_tags.

Filters are processed sequentially for each level entity. If an entity can be transformed by multiple filters, the first one will take precidence and the rest will be ignored.

If you change settings.json while the randomizer GUI is active, click "Refresh settings" to see your changes.

### Item spawn presets - Affects entities that are spawned directly in the level

* Randomize all - Randomizes all items and replaces them with new items at a (mostly) sane spawn rate. Should not affect items that are essential for story progress.
* Randomize all (chaotic)- Similar to randomize all, but has a chance of spawning story progression items (ex. the arming keys) early, as well as turning items into enemies.
* Randomize within type - An experimental setting where objects are randomized within their type. Ex. weapons are replaced with other weapons, food is replaced with other food, fab plans are replaced with other fab plans, etc.
* Whiskey and cigars - Replaces all items with whiskey and cigars.
* All reployers - Replaces all items with reployers.
* Oops! All eels - Replaces all items with eels

### NPC spawn presets - Affects entities that are spawned from an NPC spawner

* Randomize all - Randomizes all typhon and replaces them with new hostile NPCs at a (mostly) sane spawn rate. Should not affect enemies that are essential for story progression.
* Randomize all (chaotic) - Similar to randomize all, but has a chance of spawning unkillable entities such as tentacles and turrets.
* Randomize within type - Swaps out enemies for an enemy of roughly the same difficulty level. Ex. Mimics and base phantoms can become other mimics/base phantoms.
* All nightmares - Replaces all typhon with nightmares. Why would you use this setting? Who would think this is a good idea?
* All mimics - Replaces all typhon with mimics.
* Typhon to humans - Replaces all typhon with humans.
* All Dahl - Replaces all typhon with Dahl.

### Other options

* Randomize voice lines - Shuffles voices lines by voice actor
* Randomize NPC bodies - Scrambles body parts for every human NPC.
* Start on 2nd day - Starts the game at the beginning of the 2nd day of the intro. There is a known bug where the HUD will be invisible at first. To fix this, open and close the inventory menu.
* Add loot to Morgan's apartment - Adds some starting loot to various containers in Morgan's apartment (nightstands on either side of the bed, three kitchen cabinets, refrigerator, large kitchen cabinet)
* Randomize loot tables - Scrambles items that can show up as loot in various containers.
* Unlock all neuromod scans - Removes research requirements for all typhon neuromods. You'll still have to purchase them via neuromods though!
* Randomize Neuromod upgrade tree - Scrambles the neuromod skill tree so that the order of unlocking every neuromod will be a little different.
* Unlock everything - Unlocks (almost) every door, airlock, workstation, safe, etc on board the station. Does not unlock the main lift or doors that are scripted to be locked (ex. the door to Psychoronics in the Lobby).
* Randomize station connections - Scrambles connections between maps. Basic checks are done to ensure that the game is still completable.

## Known issues

* If starting on the 2nd day, the HUD will not be visible unless you open and close your inventory menu.
* If save files are not manually deleted before starting a new save slot, game can crash if old level files are left over from the previous save.
* Items that are hard-coded to spawn in certain containers cannot be randomized (changing the class type of these items results in crashing the game).
* Temporary directories will not be deleted if there was an issue during randomization/install. If these start to pile up, feel free to delete them manually.
* Game can occasionally crash when quitting to menu.
* Randomized neuromods - If "Unlock all scans" is not selected, typhon neuromods will not initially be available until the Psychoscope is obtained.
* Randomized station - To prevent soft locking scenarios, some doors are not randomized. These include the doors to Crew Quarters, Hardware Labs, Deep Storage, and Psychotronics. The Exterior airlocks are also not randomized.

## Authors

Report issues to [/u/Shape_in_the_Glass](https://reddit.com/u/shape_in_the_glass).

## Version History

* 0.0
    * Early prototyping/testing
* 0.1
    * Bug fixes with settings, log files
    * Adjust presets
    * Add tooltips to GUI
* 0.2 (beta)
    * Fix bug with multiple custom filters
    * Adjust presets
    * Add neuromod randomization
    * Add station randomization
    * Add "start on 2nd day"
    * Add game token override support
    * Add save settings support
    * Add "new seed" support
* 0.2
	* Adjust station randomization to reduce unplayable scenarios
	* Adjust costs for randomized neuromods so that they are proportional to their attainment order
	* Remove station randomization dependency on "open talos"
	* Change "open talos" option to "unlock everything"
	* Update item/enemy spawn presets to have a "recommended", "chaotic", and "lite" option
	* Various minor bug fixes, refactors, and improvements

## Dependencies info

This project uses:
* Jackson for json parsing, which is licensed under the Apache license.
* JDOM for xml parsing, which is licensed under the JDOM license.
* Guava, which is licensed under the Apache license.
* JGraphT for graph visualization, which is dual-licensed under LGPL 2.1 and EPL 2.0.

## Acknowledgments

Thanks to fellow Prey modders such as Rosodude, jmx777, and coyote, who have helped me understand more about the file structure. Also big thanks to the Prey reddit and discord communities for being pretty swell and listening to my terrible ideas. Biggest thanks to Arkane Studios for being cool folks who make cool games. =]
