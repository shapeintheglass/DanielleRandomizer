# Danielle Randomizer v0.0

A simple randomizer for Prey 2017 (code-named "Danielle").

## Description

Randomizes various entities in Prey 2017 via an external executable that overwrites the level files and generates a patch_randomizer.pak. Before overwriting, level files are backed up in the same directory as "level_backup.pak".

## Getting Started

**IMPORTANT if you want to avoid game crashes** If you plan to start a new randomized game in an existing save slot, manually delete the old save data first (and ensure cloud save doesn't try to restore it). By default this should be in `C:\Users\<Name>\Saved Games\Arkane Studios\Prey\SaveGames`. This is because unfortunately Prey does not always cleanly delete old files before starting up a new game. If you don't do this, there's a chance your old save files will stay there and conflict with the randomized level files. The same applies if you are done with this randomized save slot and want to use it for a normal game.

### Dependencies

* Requires a valid copy of Prey 2017 on Steam or GOG (the Windows Store version of Prey 2017 cannot be modded)
* Must have Java for Windows installed

### Running the randomizer

1. Ensure danielle_randomizer.exe is in the same directory as the included data/ folder and settings.json file.
2. Run danielle_randomizer.exe to start up the GUI.
3. Specify Prey install directory.
4. Specify desired randomization settings.
5. Click "Install" to generate randomized files and install them.
6. Click "Uninstall" to revert any changes made by this randomizer.

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
* output_weights - int array of relative weights to assign to each output tags. must have the same length as output_tags.
* do_not_touch_tags - if an entity matches one of these tags, do NOT randomize it. overrides input_tags.
* do_not_output_tags - do NOT output any items that match a tag in this list. overrides output_tags.

Filters are processed sequentially for each level entity. If an entity can be transformed by multiple filters, the first one will take precidence.

If you change settings.json while the randomizer GUI is active, click "Refresh settings" to see your changes.

### Description of existing presets

####Item spawn presets - Affects entities that are spawned directly in the level

* Randomize all - Randomizes all items and replaces them with new items at a (mostly) sane spawn rate. Should not affect items that are essential for story progress.
* Randomize all, no story items - Same as randomize all, but does not spawn duplicates of critical story items (ex. you won't find arming keys randomly while walking around)
* Whiskey and cigars - Replaces all items with whiskey and cigars.
* All reployers - Replaces all items with reployers.

####NPC spawn presets - Affects entities that are spawned from an NPC spawner

* Randomize all - Randomizes all typhon and replaces them with new hostile NPCs at a (mostly) sane spawn rate. Should not affect enemies that are essential for story progression.
* All nightmares - Replaces all typhon with nightmares. Why would you use this setting? Who would think this is a good idea?
* All mimics - Replaces all typhon with mimics.
* Typhon to humans - Replaces all typhon with humans.
* All Dahl - Replaces all typhon with Dahl.

####Other options

* Randomize voice lines - Shuffles voices lines by voice actor
* Randomize NPC bodies - Scrambles body parts for every human NPC. If you see anything wrong with an NPC (ex. missing heads, bodies), let me know.
* Add loot to Morgan's apartment - Adds some starting loot to various containers in Morgan's apartment (nightstands on either side of the bed, three kitchen cabinets, refrigerator, large kitchen cabinet)
* Randomize loot tables - Scrambles items that can show up as loot in various containers.
* Open up Talos I (WIP) - Unlocks various doors. See next section for more details.

####Open up Talos I effects

The ultimate goal of this option is to make all of Talos I accessible from the get-go (and keep it that way) so that connections between maps can eventually be randomized. Currently, this is what it does:

* Neuromod Division - Unlocks the two neighboring doors in Morgan's apartment hallway (in case you can't get a wrench)
* Lobby - Moves the lift Technopath from the Arboretum level of the Lobby to the tunnel between the Lobby and Neuromod Division. Make sure to kill it here so that you can unlock the lift early!
* Psychotronics - Unlocks the second floor containment doors (in case you can't get psychoscope scans) and unlocks live exam doors (in case something goes wrong with the voltaic phantom encounter cutscene)

## Known issues

* Items that are hard-coded to spawn in certain containers cannot be randomized (changing the class type of these items results in crashing the game).
* Temporary directories will not be deleted if there was an issue during randomization/install. If these start to pile up, feel free to delete them manually.
* Game can occasionally crash when quitting to menu.
* If save files are not manually deleted before starting a new save slot, game can crash if old level files are left over from the previous save.

## Authors

Report issues to [/u/Shape_in_the_Glass](https://reddit.com/u/shape_in_the_glass).

## Version History

* 0.0
    * Early prototyping/testing
* 0.1
    * Bug fixes with settings, log files
    * Adjust presets
    * Add tooltips to GUI

## Licensing info

This project uses Jackson for json parsing, which is licensed under the Apache License.
This project uses JDOM for xml parsing, which is licensed under the JDOM license.

## Acknowledgments

Thanks to fellow Prey modders such as Rosodude, jmx777, and coyote, who have helped me understand more about the file structure. Also big thanks to the Prey reddit and discord communities for being pretty swell and listening to my terrible ideas. Biggest thanks to Arkane Studios for being cool folks who make cool games.