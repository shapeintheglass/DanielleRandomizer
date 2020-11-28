[img]https://i.imgur.com/mPgYhrX.jpg[/img]
[size=6]Prey 2017 Randomizer[/size]

A simple randomizer for Prey 2017. Does not work on Mooncrash (2018) yet. Currently in pre-release form.

This is an external executable that overwrites the game level files (keeping a backup in the same directory) and generates a patch_randomizer.pak in GameSDK/Precache. Level files are backed up as "level_backup.pak" in their respective directories.

What this is currently capable of randomizing:

[list]
[*]Item spawns / loot tables
[*]Enemy spawns
[*]Voice lines
[*]Human NPC appearance
[*](new in v0.2) Neuromod skill tree (Note: If you get a tiered neuromod out of order such as Hacking II without Hacking I, that means you can ONLY open Hacking II workstations with it. Same applies for Leverage, Repair, etc.)
[*](new in v0.2) Station connectivity
[/list]

Additional useful options:

[list]
[*]Configurable item/enemy spawn settings (ex. increase weapon spawn rate, add/remove items to/from the pool, etc)
[*]Add every weapon, fab plan, chipset, etc to Morgan's apartment
[*]Unlock various doors throughout the station to make progress easier
[*](new in v0.2) Remove scan requirements for all typhon neuromods
[*](new in v0.2) Start on second day (HUD will be invisible at first- to fix this, open and close the inventory menu)
[/list]

[size=5]Getting Started[/size]

[b]IMPORTANT! This mod is intended to be used on a new game. If you plan to start a new randomized game in an existing save slot...[/b]
manually delete the old save data in that slot first (and ensure cloud save doesn't try to restore it). By default this should be in `C:\Users\<Name>\Saved Games\Arkane Studios\Prey\SaveGames\Campaign<0/1/2>`.  Campaign 0 is the first slot, Campaign 1 is the second slot, and Campaign 2 is the third slot.

This is because unfortunately Prey does not always cleanly delete old files before starting up a new game. If you don't do this, there's a chance your old save files will stay there and conflict with the randomized level files, which will cause the game to crash unexpectedly when loading that level. The same applies if you are done with this randomized save slot and want to reuse it for a normal game.

[size=4]Dependencies[/size]

[list]
[*]Requires a valid copy of Prey 2017 on Steam or GOG (the Windows Store version of Prey 2017 cannot be modded)
[*]Must have Java for Windows installed
[/list]

[size=4]Running the randomizer[/size]

The installer will directly modify the game files so that any new save files created while the randomized levels are in effect will be randomized. Uninstalling the randomized mod (via the "Uninstall" button) is not recommended until you are done with your randomized playthrough. Existing save slots will not be affected unless you try to load them.

[list=1]
[*]Unzip the files anywhere. Ensure danielle_randomizer.exe is in the same directory as the included data/ folder and settings.json file.
[*]Run danielle_randomizer.exe to start up the GUI.
[*]Specify Prey install directory.
[*]Specify desired randomization settings.
[*]Click "Install" to generate randomized files and install them.
[*]Start up Prey and begin a new randomized game!
[*]To revert the changes made by this mod, click "Uninstall". Note that any save slots created while the mod was in effect will be in a bad state as a result of this (attempting to load those again may crash the game, or be in a weird state of partly randomized partly not randomized).
[/list]

If the randomizer spends a long time at the "Installing..." step, check the generated log.txt file to see if it encountered any errors. Sometimes it can just take a long time to generate the first time around.

[size=4]Uninstalling[/size]

[list]
[*]Ideally, use the "Uninstall" function within the randomizer GUI to uninstall any generated files before uninstalling. If this isn't possible, you can use the GOG or Steam client to redownload any missing or modified files.
[*]Delete patch_randomizer.pak in Prey\GameSDK\Precache, if it exists.
[*]To uninstall the randomizer, delete danielle_randomizer.exe and its associated files.
[/list]

[size=5]New in v0.2[/size]

[list]
[*]Ability to save current settings
[*]Ability to randomly generate a new seed
[*]Randomize neuromods option
[*]Unlock all neuromod scans options
[*]Randomize station connectivity option
[*]"Open up Talos I" option changed to "Unlock everything", no longer interferes with the lift
[*]Adjusted randomizer presets
[/list]

[size=5]Randomizer presets[/size]

The item and enemy spawn randomizers come with a set of presets, but these can be modified or new ones can be added in settings.json.

[size=4]Item spawn presets[/size]

Affects entities that are spawned directly in the level. These can be configured or changed in settings.json.

[img]https://imgur.com/bHJEhq1.jpg[/img]

[list]
[*]Randomize all - Randomizes all items and replaces them with new items at a (mostly) sane spawn rate. Should not affect items that are essential for story progress.
[*]Randomize all (chaotic)- Similar to randomize all, but has a chance of spawning story progression items (ex. the arming keys) early, as well as turning items into enemies.
[*]Randomize within type - An experimental setting where objects are randomized within their type. Ex. weapons are replaced with other weapons, food is replaced with other food, fab plans are replaced with other fab plans, etc.
[*]Whiskey and cigars - Replaces all items with whiskey and cigars.
[*]All reployers - Replaces all items with reployers.
[*]Oops! All eels - Replaces all items with eels.
[/list]

[size=4]NPC spawn presets[/size]

Affects entities that are spawned from an NPC spawner. These can be configured or changed in settings.json.

[img]https://i.imgur.com/jkNmnAA.jpg[/img]

[list]
[*]Randomize all - Randomizes all typhon and replaces them with new hostile NPCs at a (mostly) sane spawn rate. Should not affect enemies that are essential for story progression.
[*]Randomize all (chaotic) - Similar to randomize all, but has a chance of spawning unkillable entities such as tentacles and turrets.
[*]Randomize within type - Swaps out enemies for an enemy of roughly the same difficulty level. Ex. Mimics and base phantoms can become other mimics/base phantoms.
[*]All nightmares - Replaces all typhon with nightmares. Why would you use this setting? Who would think this is a good idea?
[*]All mimics - Replaces all typhon with mimics.
[*]Typhon to humans - Replaces all typhon with humans.
[*]All Dahl - Replaces all typhon with Dahl.
[/list]

[size=5]Other options[/size]

The randomizer provides some additional options that can be toggled on or off.

[list]
[*]Randomize voice lines - Shuffles voices lines by voice actor. This could potentially contain story spoilers if you haven't played the entire game yet.
[*]Randomize NPC bodies - Scrambles body parts for every human NPC.
[*]Add loot to Morgan's apartment - Adds some starting loot to various containers in Morgan's apartment (nightstands on either side of the bed, three kitchen cabinets, refrigerator, large kitchen cabinet)
[*]Randomize loot tables - Scrambles items that can show up as loot in various containers.
[*]Unlock everything - Unlocks various doors. See next section for more details.
[*](new in v0.2) Start on 2nd day - skips the intro to the second day. This has a known bug where the HUD will be gone until you open and close the inventory screen.
[*](new in v0.2) Unlock all neuromod scans - Removes the scan requirement for all typhon abilities and unlocks the Psychoscope calibration door in Psychotronics.
[*](new in v0.2) Randomize neuromod upgrade tree - Shuffles all neuromods around in the upgrade tree. Note: If you get a tiered neuromod out of order such as Hacking II without Hacking I, that means you can ONLY open Hacking II workstations with it. Same applies for Leverage.
[*](new in v0.2) Randomize station - Shuffles the connections between various parts of the station.
[/list]

[size=4]Loot in Morgan's Apartment[/size]

Here's a quick rundown of what should spawn in Morgan's apartment if you select this option:

[list]
[*]Nightstands on either side of Morgan's bed - Jetpack, weapons, and some ammo (sans the Q-Beam)
[*]Refrigerator in Morgan's kitchen - Psychoscope, neuromods, and various resources
[*]Kitchen floor cabinets (3 of them) - All chipsets.
[*]Kitchen cabinet to the right of the refrigerator -  All fabrication plans.
[*]Countertop (where the "Congrats" note would be) - The General Access keycard (lets you into Psychotronics and several other areas).
[/list]

Note that if you pick up these items and complete the intro, you will lose all items in your inventory. However, they should show up in these containers again on the second day.


[size=4]Unlock everything[/size]

The "Unlock everything" option will force most doors, workstations, etc to be unlocked by default, as well as set various global variables to allow skipping some sidequests.

[list]
[*]Unlocks all doors and workstations by default (some are not affected if they are scripted to be locked, such as the doors to Psychotronics and Shuttle Bay)
[*]Unlocks all exterior airlocks, allowing them to be opened from the outside
[*]Immediately grants all voice samples required to enter Deep Storage (must pick up Zachary West's transcribe to proc the quest first, then wait a few seconds)
[/list]

Note that this does not unlock the main lift. v0.1 attempted to solve this by moving the arboretum technopath to the lobby, but this was reverted in v0.2.

[size=4]Randomize neuromods[/size]

This option shuffles all neuromods in the upgrade tree, regardless of category of type. New costs are assigned to the neuromods according to their position in the new skill tree. Note that if "Unlock all neuromod scans" is not selected, typhon neuromods will not be shown in the skill tree until the psychoscope is obtained.

If this option is selected, a simple graph of the new skill tree is generated as neuromod_output.png.

[size=4]Randomize station[/size]

This option shuffles the connections between various sections of the station, resulting in a new exploration experience. If this option is selected, a simple graph of the new station is generated as station_connectivity.png.

To prevent some soft lock situations, some connections are not randomized. This includes:

[list]
[*]Doors to/from Deep Storage
[*]Doors to/from Crew Quarters
[*]Doors to/from Hardware Labs
[*]Doors to/from Psychotronics
[*]All exterior airlocks
[/list]

You may be able to use this knowledge to your advantage. In addition, the Arboretum/Life Support connections to the Lobby main lift can only be swapped with each other.

Also of note is that some level transition doors will still require keycards or quest progression in order to use. This includes:

[list]
[*]General Access keycard - Unlocks Lobby --> Psychotronics and Lobby --> Shuttle Bay doors. Obtained by completing "Through a Glass Darkly".
[*]Crew Quarters keycard - Unlocks Arboretum --> Crew Quarters. Obtained from Zachary West's corpse in front of the Deep Storage door.
[*]Fuel Storage keycard - Unlocks Shuttle Bay --> GUTS and GUTS --> Shuttle Bay doors. Obtained from Brittany LaValley's corpse in Fuel Storage (GUTS).
[*]Deep Storage - Still requires Danielle Sho's voice samples from Crew Quarters in order to unlock.
[/list]

This option includes two "backup plans" in case the main quest has been soft locked by sequence breaking.

[list]
[*]Crew Quarters - Morgan's apartment will contain Morgan's arming key fabrication plan, and Alex's apartment will contain Alex's arming key fabrication plan, as well as the Prototype Nullwave fabrication plan.
[*]Bridge - Jada Mark's desk will contain the fabrication plans for the arming keys and prototype nullwave inside the right drawer.
[/list]

Normally, you must obtain Morgan's arming key via Deep Storage, followed by Alex's arming key and the prototype nullwave by rebooting the reactor and dealing with Dahl. Some quick tips:

[list]
[*]Open up shortcuts whenever possible. Unlock all airlocks, pick up all keycards, and don't forget to kill the lift technopath!
[*]First stop should be the Arboretum, to enter Deep Storage and kick off the reboot sequence.
[*]While at the Lobby, try to do the "Through a Glass Darkly" sequence in order to obtain the General Access keycard. Note that Calvino won't spawn in the exterior unless you've been to Hardware Labs.
[/list]

[size=5]Compatibility with other mods[/size]

[list]
[*]Will not be compatible with any mods that modify level files (ex. Real Lights, Talos in the Dark, Prey Souls).
[*]Should be compatible with light cosmetic mods, for now (ex. PRIC, Mooncrash weapon skins).
[/list]

[size=5]Configuring your own item/NPC spawn presets[/size]

[img]https://imgur.com/HvH35rc.jpg[/img]

If you'd like to tweak the preset spawn rates for items and enemies, you can specify them yourself in settings.json.

This randomizer has assigned multiple tags to every entity in the game (ex. weapons are "Weapons", food is "ArkFood", etc). To see a list of supported entities and tags, see the included files tagstoentities.csv (shows the entities associated with each tag) and entitiestotags.csv (shows the tags generated for each entity).

The level randomizer in this project uses a list of "filters" to process the level files. A "filter" consists of five arrays:


[list]
[*]input_tags - if an entity matches one of these tags, randomize it
[*]output_tags - acceptable tags for items to come out of this filter (first a tag is chosen randomly from this list, then an entity matching that tag is chosen randomly)
[*]output_weights - int array of relative probability weights to assign to each output tags. must have the same length as output_tags. a higher weight value means a higher likelihood of showing up.
[*]do_not_touch_tags - if an entity matches one of these tags, do NOT randomize it. overrides input_tags.
[*]do_not_output_tags - do NOT output any items that match a tag in this list. overrides output_tags.
[/list]

Here is a example NPC preset that would replace all typhon with tentacles:

[code]    {
      "name": "More tentacles",
      "desc": "Example preset",
      "filters": [
        {
          "input_tags": [ "ArkNpcs" ],
          "output_tags": [ "ApexTentacle" ],
        }
      ]
    },
[/code]

The "ArkNpcs" tag is used as input, which includes all typhon in the game. This means when the randomizer parses a level entity and sees that it spawns something tagged with "ArkNpcs", it will swap it out for something in the output tags list. Here the output tags are only "ApexTentacle", which includes all tentacle type enemies in the game.

Here is an example item preset that uses all of these options:

[code]    {
      "name": "Food to alcohol and cigars",
      "desc": "Example preset",
      "filters": [
        {
          "input_tags": [ "Food" ],
          "output_tags": [ "Alcohol", "UsedCigar" ],
          "output_weights": [ 2, 1],
          "do_not_touch_tags": [ "Banana" ],
          "do_not_output_tags": [ "DuckBeer" ]
        }
      ]
    },
[/code]

This filter will:

[list=1]
[*]Act on all items that are tagged as "Food" (includes Food.Bag.BigBangCandy, Food.Bag.Glucassist, Food.Bag.SunDriedTomatoJerky...)
[*]Replace them with either an item tagged as "Alcohol" or "UsedCigar".
[*]Alcohol items will have a spawn rate twice that of used cigars (66% alcohol, 33% cigars)
[*]Not touch any items that are tagged as "Banana" (so bananas will be left alone)
[*]Not spawn any items that are tagged as "DuckBeer" (so the alcohol will not include duck beer)
[/list]

Filters are configured behind the scenes to not affect entities that would prevent story progression (ex. the wrench in Morgan's apartment, the lift technopath, keycards, key items, etc).

Filters are processed sequentially for each level entity. If an entity can be transformed by multiple filters, the first one should take precedence.

If you change settings.json while the randomizer GUI is active, click "Refresh settings" to see your changes. If there are any parsing errors, the GUI should show them in a dialog box.

To find out what tags are valid, consult the included files tagstoentities.csv  and entitiestotags.csv. Here are some basic tags that may be helpful to know:

[list]
[*]GLOBAL - all known entities (use with EXTREME caution- if this doesn't cause a game crash, it will be a miracle)
[*]ArkNpcs - all typhon
[*]ArkHumans - all humans
[*]ArkPickups - all items that can be added to the inventory
[*]ArkPhysicsProps - all props (generally, items that can be picked up)
[*]_CARRYABLE - items that can be picked up and carried (props, leverage items)
[*]_CONSUMABLE - items that can be "consumed" (invoked via the 'G' key) (ex. food, medicine, patch kits, etc)
[*]_IMPORTANT - items that were marked as "important" by the game (chipsets, plot items, psychoscope, jetpack, etc)
[*]_LEVERAGE_I, _LEVERAGE_II, _LEVERAGE_III - items that can only be picked up by leverage I, II, or III
[*]_MIMICABLE - items that can be mimicked
[*]_PLOT_CRITICAL - items required in order to progress the plot or sidequests (ex. keycards, Annalise's thumb drive, the coral scanning chipset, self destruct keys, etc)
[*]_TRASH - Junk that has no purpose other than recycling for materials
[*]_USABLE - Entities that can be "used" (invoked via the 'F' key). Note that this includes both items like meds/food and robots that can be spoken to.
[/list]

[size=5]Known issues[/size]

[img]https://imgur.com/RKFNYxJ.jpg[/img]

Some entities are not randomized:

[list]
[*]Some items that are scripted to spawn in certain containers/locations cannot be randomized (changing the class type of these items results in crashing the game, so they can only be replaced with a different entity of the same class (ex. Food will be replaced with other food)).
[*]Voice lines in certain cutscenes are not randomized.
[*]Certain items/NPCs required for progression are hard coded to not be randomized to avoid messing with the game scripting, such as Patricia Varma's wrench and the lift technopath.
[/list]

Sidequest related issues:

[list]
[*]The Greenhouse telepath and mind controlled humans in the Arboretum may not spawn (prevents "Saving Rani").
[*]Dr. Calvino's keycard can only be obtained from the exterior if you load Hardware Labs first.
[*]If any of the Cargo Bay B typhon become unkillable or go out of bounds, this may prevent rescuing the survivors there.
[/list]

Potential soft locking issues:

[list]
[*]Ejecting from the station in Deep Storage before Alex has enforced his lockdown can result in a soft lock where you are unable to return to the station. This should no longer be possible, but is still something to watch out for.
[/list]

Game crashing issues:

[list]
[*]A game crash can result if you start a randomized game on top of an existing save slot. Manually deleting old save files before starting a new game in the same slot can fix this issue.
[*]Occasional game crashes when quitting to menu.
[/list]

Other issues:

[list]
[*]If randomizing the neuromod skill tree, ability descriptions may no longer be accurate (ex. toughness III tells you it'll raise your HP to 300, but that assumes that you've already gotten toughness I and II).
[*]If installer fails while running, there may not be an indication of this in the UI. If it takes too long, check the log.txt file for more info on what happened.
[*]Temporary directories will not be deleted if there was an issue during randomization/install. If these start to pile up, feel free to delete them manually.
[/list]

[size=5]Troubleshooting[/size]

In general, check the generated log.txt file to see if there were any issues. Please report any issues you find! Ideally, also attach the log.txt file, screenshots, and the seed/settings you used.

Installer takes longer than expected -

[list]
[*]Verify that mod files have been created in your Prey directory. There should be a new level.pak in each section of Prey/GameSDK/Levels/Campaign (along with a level_backup.pak) as well as a patch_randomizer.pak in Prey/GameSDK/Precache.
[*]Close Prey 2017 if it is currently running.
[*]Assert you have enough hard drive space for the generated mod files.
[*]Delete any old temporary directories created by the mod.
[/list]

HUD is missing when starting on day 2 - 
[list]
[*]Open and close the inventory to fix this.
[/list]

Can't get out of Simulation labs - 
[list]
[*]It's possible to get out of Sim Labs quickly by hopping on a small electrical box just outside the debriefing room, then scaling over the wall (https://i.imgur.com/9bA8ueU.jpg).
[/list]

(v0.1) Lift technopath is missing
[list]
[*](v0.1) If you selected the "Open Talos I (WIP)" option when randomizing, this moves the technopath down to the lobby so that you can kill it early. Unfortunately this means it also won't be available on the Arboretum level.
[*]This should no longer occur on v0.2.
[/list]

Loot isn't showing up in Morgan's apartment, Voice lines aren't randomized, etc.
[list]
[*]Ensure you currently don't have any other mods installed that are overwriting these changes. If you want, you can manually merge the files by modifying data/ark/loottables.xml. Just make sure to keep the "RANDOMIZER_" loot tables at the end if you want to keep your loot.
[/list]

Please report issues to [url=https://reddit.com/u/shape_in_the_glass]/u/Shape_in_the_Glass[/url]﻿ or [url=https://www.reddit.com/user/Tsundereployer/]/u/Tsundereployer[/url]﻿!

Also feel free to share your ideas on how to expand the mod! Here are some features that are tentatively planned down the line:
[list]
[*]Tweaked neuromod abilities (ex. expanded range of typhon created by Phantom Genesis)
[*]Randomized weapon models (cosmetic)
[*]Randomized weapon ammo
[*]Randomized enemy attacks
[*]Randomized keycards
[*]Randomized recyclers/fabricators
[*]Mooncrash compatibility
[*]All items are explosives
[*]Randomized gravity
[*]Randomize within level
[/list]

[size=4]Authors[/size]

Report issues and/or feature requests to[url=https://reddit.com/u/shape_in_the_glass] /u/Shape_in_the_Glass[/url]﻿ or Tsundereployer ([url=https://www.reddit.com/user/Tsundereployer/]/u/Tsundereployer[/url]﻿), or leave a post here.

Source code can be found on [url=https://github.com/shapeintheglass/DanielleRandomizer]GitHub[/url]﻿.

[size=4]Version History[/size]

0.0 - Early prototyping/testing
0.1 - Quick bug fixes, adjusted presets to remove potentially game breaking scenarios
0.2 (beta) - Bug fixes, adjusted presets, save settings, neuromod randomization, station randomization
0.2 - Fixed issues from beta

[size=4]Acknowledgments[/size]
Thanks to fellow Prey modders such as Rosodude, jmx777, and coyote, who have helped me understand more about the file structure. Also big thanks to the Prey reddit and discord communities for being pretty swell and listening to my terrible ideas. Biggest thanks to Arkane Studios for being cool folks who make cool games. =]