[img]https://imgur.com/taaeBI5.jpg[/img]
[size=6]Prey 2017 Randomizer[/size]

An item/enemy/neuromod/station/cosmetic randomizer for Prey 2017. Currently in pre-release form.

This is an external executable that overwrites the game level files (keeping a backup in the same directory) and generates a patch_randomizer.pak in GameSDK/Precache. Level files are backed up as "level_backup.pak" in their respective directories.

We now have a [url=https://discord.gg/MNGZjucxDE]Discord server[/url]﻿! Feel free to join to discuss the randomizer and/or provide feedback/suggestions.

[b][u]WHAT CAN BE RANDOMIZED[/u][/b]

[list]
[*]Item/prop spawns + loot tables
[*]Enemy/NPC spawns
[*]Neuromod upgrade tree
[*]Station connectivity
[*]Human appearance
[*]Voice lines
[*]Music
[*]Player model
[*]Fabrication plan costs
[*]Earth/moon/sun size
[*](new in 0.4) Operator dispensers
[*](new in 0.4) Recyclers/fabricators
[*](new in 0.4) Repairable objects (50/50 chance of requiring repair to use)
[*](new in 0.4) Hackable objects (50/50 chance of requiring hack to use)
[/list]

[b][u]OTHER OPTIONS[/u][/b]

[list]
[*]Add every weapon, fab plan, chipset, etc to Morgan's apartment
[*]Unlock various doors throughout the station to make progress easier
[*]Remove scan requirements for all typhon neuromods
[*]Start on second day (Now new and improved, thanks to JerryTerry letting me use their [url=https://www.nexusmods.com/prey2017/mods/68]"Natural Day 2 Start"[/url] mod!)
[*]More guns - Adds [url=https://www.nexusmods.com/prey2017/mods/69]guns with randomized projectiles[/url]﻿ to the item spawn pool.
[*]G.O.T.S. (Get Off The Station) mode - The station's self destruct will start as soon as you wake up in the Neuromod Division. Can you escape before time runs out?
[*]Gravity toggles - enable 0G everywhere, or 1G in the Exterior+GUTS. Not intended for serious playthroughs.
[*](new in 0.4) Living Talos mode - Changes all typhon to humans and makes all humans wander around the station. Human corpses are resurrected as well. Most of the station is unlocked.
[*](new in 0.4) Prey Souls guns - Adds 16 guns from the Prey Souls mod. Note: Some changes were made to the guns during porting, such as lack of custom skins, and 5 guns have not been ported (psi cutter, laser rifle, gauss rifle, quantum shotgun).
[*](new in 0.4) Prey Souls turrets - Adds 3 turrets from the Prey Souls mod. Friendly robot randomization must be enabled for these to spawn in place of existing turrets.
[/list]

[b][u]QUICK START INSTRUCTIONS[/u][/b]

Important note: Randomized runs will only work on a new game of Prey. There is also a known game crashing bug where the game does not cleanly delete old save files if you start a new save over an existing slot. To guarantee that this won't happen, manually delete any save progress first before starting a new randomized game in an existing slot. See "Install instructions" on how to do this.

[list=1]
[*]Unzip danielle_randomizer.zip anywhere.
[*]Run danielle_randomizer.exe to start the installer GUI.
[*]Specify Prey install directory and your desired settings.
[*]Start a new randomized game!
[/list]

[size=5]Install instructions[/size]

[spoiler][img]https://i.imgur.com/9MXNnpN.jpg[/img]

[b]IMPORTANT! This mod is intended to be used on a new game. If you plan to start a new randomized game in an existing save slot...[/b]
manually delete the old save data in that slot first (and ensure cloud save doesn't try to restore it). By default this should be in `C:\Users\<Name>\Saved Games\Arkane Studios\Prey\SaveGames\Campaign<0/1/2>`. Campaign 0 is the first slot, Campaign 1 is the second slot, and Campaign 2 is the third slot.

This is because unfortunately Prey does not always cleanly delete old files before starting up a new game. If you don't do this, there's a chance your old save files will stay there and conflict with the randomized level files, which will cause the game to crash unexpectedly when loading that level. The same applies if you are done with this randomized save slot and want to reuse it for a normal game.

[size=4]Dependencies[/size]

[list]
[*]Requires a valid copy of Prey 2017 on Steam or GOG (the Windows Store version of Prey 2017 cannot be modded) (untested on the Bethesda Launcher version)
[*]Must have [url=http://java.com/download]Java for Windows[/url] installed
[/list]

[size=4]How to install[/size]

This randomizer will directly modify the Prey 2017 level files. Any new save slots created while the randomizer is in effect will be randomized. Uninstalling the randomized mod (via the "Uninstall" button) or reinstalling with new settings/seed is not recommended unless you are done with your randomized playthrough. If you attempt to load a save slot that's incompatible with the current randomized level, the game will mark the save as "corrupt" and immediately delete it. Existing save slots will not be affected unless you try to load them. [b]tl;dr You won't be able to load save files created under a different seed/settings. Avoid changing seeds/settings until you are done with your current playthrough.[/b]

[list=1]
[*]Unzip danielle_randomizer.zip anywhere.
[*]Run danielle_randomizer.exe to start up the GUI. Ensure danielle_randomizer.exe is in the same directory as the included data.pak file and spawn_presets.json file.
[*]Specify Prey install directory.
[*]Specify desired randomization settings.
[*]Click "Install" to generate randomized files and install them.
[*]Start up Prey and begin a new randomized game!
[/list]

If the randomizer spends a long time at the "Installing..." step, check the generated log.txt file to see if it encountered any errors. Sometimes it can take a long time to generate the first time around.

[size=4]Compatibility with other mods[/size]

Will not be compatible with most mods, especially ones that modify level files (ex. Real Lights, Talos in the Dark, Prey Souls). Here is a non-exhaustive list of files that the randomizer can affect:

[list]
[*]All level.paks
[*]ark/apexvolumeconfig.xml
[*]ark/entitlementunlocklibrary.xml
[*]ark/ai/aitrees/armedhumanaitree.xml
[*]ark/ai/aitrees/humanaitree.xml
[*]ark/ai/aitrees/unarmedhumanaitree.xml
[*]ark/campaign/books.xml
[*]ark/campaign/locations.xml
[*]ark/dialog/dialoglogic/*
[*]ark/dialog/voices/*
[*]ark/items/arkitems.xml
[*]ark/items/fabricationplanlibrary.xml
[*]ark/items/loottables.xml
[*]ark/npc/npcgameeffects.xml
[*]ark/player/abilities.xml
[*]ark/player/abilitiespdalayout.xml
[*]ark/player/researchtopics.xml
[*]libs/entityarchetypes/arkgameplayarchitecture.xml
[*]libs/entityarchetypes/arkpickups.xml
[*]libs/entityarchetypes/arkprojectiles.xml
[*]libs/gameaudio/music.xml
[*]libs/globalactions/global_selfdestructsequence.xml
[*]libs/sky/arkspacesettings.xml
[*]objects/characters/humansfinal/*
[*]objects/characters/player/*
[/list]

If the other mod modifies any of these files, the randomizer will likely not be compatible. Strange things (usually crashes) may occur if you install multiple mods that overwrite the same files).

I haven't tested a lot of mod compatibility, but here are some of my other mods (mostly cosmetic) that can still work with the randomizer:

[list]
[*] [url=https://www.nexusmods.com/prey2017/mods/49]Kasma Corp transcribes[/url]
[*] [url=https://www.nexusmods.com/prey2017/mods/50]Mooncrash weapon cosmetics[/url]
[*] [url=https://www.nexusmods.com/prey2017/mods/55]Mimics with silly hats[/url]
[*] [url=https://www.nexusmods.com/prey2017/mods/60]All keycodes are 1111[/url]
[*] [url=https://www.nexusmods.com/prey2017/mods/70]Poorly translated Prey[/url]
[/list]

[/spoiler]

[size=5]Uninstall instructions[/size]
[spoiler][img]https://imgur.com/JJOh2wp.jpg[/img]

To revert the changes made by this mod, use the "Uninstall" button in the GUI. Note that any save slots created while the mod was in effect will be in a bad state as a result of this. Attempting to load those again may crash the game, or be in a weird state of partly randomized partly not randomized, depending on what settings you used.

Ideally, use the "Uninstall" function within the randomizer GUI to uninstall any generated files before deleting the randomizer installer files. Otherwise, you will have to manually uninstall the randomizer files, or just use your client's "verify game file integrity" feature to reset the game files to their default state. To manually unininstall the patch file generated by this mod, delete patch_randomizer.pak in Prey\GameSDK\Precache, if it exists. To manually uninstall the level files generated by this mod, go to Prey\GameSDK\Levels\Campaign in your game directory and replace every case of level.pak with level_backup.pak for each map, if level_backup.pak exists.

To uninstall the randomizer installer GUI itself, just delete danielle_randomizer.exe and its associated files.
[/spoiler]

[size=5]Basic presets[/size]

[spoiler][img]https://imgur.com/PkEtzIl.jpg[/img]

This randomizer offers three basic presets as a starting point. These can be customized further, or you can build up your desired settings from scratch.

[size=4]Lite[/size]

Preserves as much of the Prey experience as possible, but with a small bit of randomization. If you're trying out the randomizer for the first time, try starting out with this option and adding a few other customizations that you're comfortable with.

[size=4]Recommended[/size]

This preset is intended for people who want wacky random fun while still making the game (mostly) playable.

[size=4]Chaotic[/size]

Picks basically every option available that is hypothetically compatible. High chance of wacky situations and soft locks.

[size=4]G.O.T.S. (timed challenge mode)[/size]

Note: This mode will have involve endgame spoilers for Prey. Recommended for people who have beaten the game at least once, so that they know where to go.

Get Off The Station! The station's self-destruct sequence is activated as soon as Morgan wakes up in the neuromod division. There are two options for leaving the station- the Shuttle and the Escape Pod.

Quick guide on how to use either escape method:

[b]Shuttle[/b] - The shuttle will be waiting for you in the Shuttle Bay. However, the Shuttle Bay is locked behind the General Access keycard. January will give you the General Access keycard after you watch the second half of Morgan's video.

[b]Escape pod[/b] - Alex's escape pod requires the EP101 key card, which can be found in his cabin in Crew Quarters. The keycard for Alex's cabin is in the safe in his office.

Note that because the shuttle is at Talos I, this also means that the station is in a state where Dahl and his operators have arrived. For a more interesting challenge, try decreasing the timer values or enabling some randomization options, such as station connectivity.

[size=4]Living Talos[/size]

Replaces all typhon with humans and resurrects all human corpses. Experience a glimpse of  Talos I before the outbreak. Tip: To remove husk faces, enable body randomization.

[/spoiler]

[size=5]Other options provided by the randomizer[/size]

[spoiler][img]https://imgur.com/w6BF6Vz.jpg[/img]

Here is some more in-depth information on the other options that this randomizer provides:

[list]
[*]Randomize voice lines - Shuffles voices lines by voice actor. This could potentially contain story spoilers if you haven't played the entire game yet.
[*]Randomize NPC bodies - Scrambles body parts for every human NPC.
[*]Add loot to Morgan's apartment - Adds some starting loot to various containers in Morgan's apartment (nightstands on either side of the bed, three kitchen cabinets, refrigerator, large kitchen cabinet)
[*]Randomize player model - Replaces the first person player model with a different character.
[*]Randomize music - Shuffles the global soundtrack
[*]Unlock everything - Unlocks various doors. See next section for more details.
[*]Start on 2nd day - skips the intro to the second day. This has a known bug where the HUD will be gone until you open and close the inventory screen.
[*]Unlock all neuromod scans - Removes the scan requirement for all typhon abilities and unlocks the Psychoscope calibration door in Psychotronics.
[*]Randomize neuromod upgrade tree - Shuffles all neuromods around in the upgrade tree. Note: If you get a tiered neuromod out of order such as Hacking II without Hacking I, that means you can ONLY open Hacking II workstations with it. Same applies for Leverage, Repair, etc.
[*]Randomize station - Shuffles the connections between various parts of the station.
[*]More guns - Adds more guns to the loot table. Some exotic variants use typhon organs as ammo. Make sure not to recycle the organs by mistake!
[*]Make humans wander - Alters human AI so that their idle behavior is to wander rather than stand in place. Intended to be used with "typhon to humans" option to add some realism.
[*] Randomize emotions - Assigns a random facial expression to every line of dialog. To be honest, I don't know how effective this option actually is.
[*] Randomize fabrication plan cost - Randomizes the materials requirement for every fab plan. As of v0.4, this is now done intelligently so that low-cost items still have a low cost.
[*] Microgravity in all maps - Turns every map that would normally be 1G into a 0G zone. Probably not playable. Not recommended for serious playthroughs.
[*] Gravity in GUTS and exterior - Adds gravity to the GUTS and Exterior. If both this and "enable microgravity everywhere" are on, GUTS and Exterior will have gravity. Definitely not playable. For masochists only.
[*] Custom starting map (for testing purposes only) - A WIP feature that is a little broken but at least functions in terms of starting you in the given map. This literally just swaps the given map with the neuromod division. Saving may not work, and story triggers may not occur. Intended for testing purposes only.
[*] Game token overrides (for testing purposes only) - Specify initial starting values for any game tokens you wish (both level and global). If you don't know what game tokens are, you don't need this feature. Intended for testing purposes only.
[*] (new in 0.4) Randomize recyclers/fabricators - Redistributes non-essential recyclers/fabricators across the station with a 50/50 ratio. May sound fun in theory, although may seem less fun if you fight your way to Morgan's office with full pockets only to find two fabricators.
[*] (new in 0.4) Randomize breakable objects - If an object is breakable/repairable (ex. electrical junctions, recyclers, fabricators, etc), there is a 50% chance it is broken and will require repair to use. May sound fun in theory, although may seem less fun if you fight your way to Morgan's office with full pockets only to find two fabricators and they're now also broken.
[*] (new in 0.4) Randomize hackable objects - If an object is hackable (ex. workstations), there is a 50% chance it is locked and will require hack to use.
[*] (new in 0.4) Prey Souls guns - Adds 16 guns from Prey Souls to the item spawn pool. Some changes have been made during the porting process (removing the custom skins), and 5 weapons from the mod (psi cutter, laser rifle, unique laser rifle, gauss rifle, quantum shotgun) have not been ported.
[*] (new in 0.4) Prey Souls turrets - Adds 3 turrets from the Prey Souls mod. Friendly robot randomization must been enabled for these to spawn in place of existing turrets.
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
[*]Unlocks all doors, safes, and workstations by default (some are not affected if they are scripted to be locked, such as the doors to Psychotronics and Shuttle Bay)
[*]Unlocks all exterior airlocks, allowing them to be opened from the outside
[*]Immediately grants all voice samples required to enter Deep Storage (must pick up Zachary West's transcribe to proc the quest first, then wait a few seconds)
[*]Unfortunately this is also a death sentence for Mikhaila and the Cargo Bay survivors since they will immediately get killed by the typhon in their adjacent rooms.
[/list]

Note that this does [b]not[/b] unlock the main lift. v0.1 attempted to solve this by moving the arboretum technopath to the lobby, but this was reverted in v0.2.

[size=4]Randomize station[/size]

This option shuffles the connections between various sections of the station, resulting in a new exploration experience. If this option is selected, the book "Hands-On Electronics" in Morgan's apartment and "Too Far, Too Fast I" in Morgan's office will be replaced with a book called "Station Connectivity Debug Info" that describes the new layout of the station.

Location in Morgan's apartment: [url=https://i.imgur.com/X1SgZyc.jpg]https://i.imgur.com/X1SgZyc.jpg[/url]
Location in Morgan's office: [url=https://i.imgur.com/WaJTclk.jpg]https://i.imgur.com/WaJTclk.jpg[/url]

To prevent some soft lock situations (and reduce confusion), there are some restrictions on generated stations:

[list]
[*]Deep Storage will always be connected to the Arboretum
[*]All exterior airlocks will stay the same
[*]The Arboretum/Life Support connections to the Lobby main lift can only be swapped with each other
[*]Hardware Labs / Crew Quarters can only be swapped with each other
[/list]

Also of note is that some level transition doors will still require keycards or quest progression in order to use. These are:

[list]
[*]General Access keycard - Unlocks Lobby --> Psychotronics and Lobby --> Shuttle Bay doors. Obtained by completing "Through a Glass Darkly".
[*]Crew Quarters keycard - Unlocks Arboretum --> Crew Quarters. Obtained from Zachary West's corpse in front of the Deep Storage door.
[*]Fuel Storage keycard - Unlocks Shuttle Bay --> GUTS and GUTS --> Shuttle Bay doors. Obtained from Brittany LaValley's corpse in Fuel Storage (GUTS).
[*]Deep Storage - Requires Danielle Sho's voice samples from Crew Quarters in order to unlock.
[/list]

Other minor QoL changes made by this option:

[list]
[*]A "backup plan" - In case the main quest has been soft locked, Jada Mark's desk in the bridge will contain the fabrication plans for the arming keys and prototype nullwave inside the right drawer.
[*]You can walk through Apex kill walls without dying. This is another failsafe in case they block your way.
[*]Certain doors in Psychotronics have been unlocked to allow traversing it in either direction.
[*]The reactor door in the Power Plant and the containment chamber in Psychotronics are unlocked to allow installing the endgame items in case you get them early.
[/list]

Normally, you must obtain Morgan's arming key via Deep Storage, followed by Alex's arming key and the prototype nullwave by rebooting the reactor and dealing with Dahl. Some quick tips:

[list]
[*]Open up shortcuts whenever possible. Unlock all airlocks, pick up all keycards, and don't forget to kill the lift technopath!
[*]First stop should be the Arboretum, to enter Deep Storage and kick off the reboot sequence.
[*]While at the Lobby, try to do the "Through a Glass Darkly" sequence in order to obtain the General Access keycard. Note that Calvino may not spawn in the exterior unless you've been to Hardware Labs.
[/list]

[size=4]Enable self-destruct[/size]

This option does the following:

[list]
[*]Triggers the station's self-destruct at the beginning of the game
[*]Brings Dahl to the station and brainwashes him so that the shuttle is ready
[*]Allows you to walk through apex kill walls (because one will spawn right outside the Neuromod Division
[*]Sets the self-destruct timer and shuttle leaving timer to configurable values
[/list]

You have two options to leave the station: Dahl's Shuttle and Alex's escape pod. The shuttle is easier to get to, but has a shorter timer by default. Here are some hints on how to get to each one:

[list]
[*]The shuttle bay is locked behind the General Access keycard at the beginning of the game. You'll have to complete "Through a Glass Darkly" to get the keycard off of January.
[*]Alex's escape pod requires the EP101 keycard. December can walk you through the steps to finding it.
[*]The keycard for Alex's cabin is in the safe in his office. December will have the safe code if you meet them in person.
[/list]

If you find this challenge fun, I'd also recommend trying it out with some of the randomization options enabled!

[/spoiler]

[size=5]Configuring your own item/NPC spawn presets[/size]

[spoiler][img]https://imgur.com/26XkfL5.jpg[/img]

If you'd like to tweak the preset spawn rates for items and enemies, you can modify them yourself or add your own in spawn_presets.json.

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

If you change spawn_setings.json, you will need to restart the GUI for your changes to be applied. If there are any parsing errors, see the generated log.txt.

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
[/spoiler]

[size=5]Known issues[/size]

[spoiler][img]https://imgur.com/mkYVA8c.jpg[/img]

Issues when using "More guns" or "Prey Souls guns":

[list]
[*][b]tl;dr Avoid saving the game in the Neuromod Division at the very beginning of the game.[/b] If you have one of the new randomized guns in your inventory while in the Neuromod Division at the very beginning of the game, quit Prey entirely, then reload, you will be unable to equip any weapons. To fix this issue, simply leave the Neuromod Division, then save the game again. You may have to re-equip and re-favorite the new weapon.
[/list]

G.O.T.S. mode:

[list]
[*]Apex kill wall appears in front of the Neuromod Division - This is expected behavior. To get around this, this mode will also allow you to walk through apex kill walls unharmed.
[/list]

Some entities are not randomized:

[list]
[*]Certain items/NPCs required for progression are hard coded to not be randomized to avoid messing with the game scripting, such as Patricia Varma's wrench and the lift technopath.
[*]Certain station connections are left as-is to prevent soft lock situations.
[*]Some music tracks may not be randomized with the "Randomize music" option.
[/list]

Sidequest related issues:

[list]
[*]The Greenhouse telepath and mind controlled humans in the Arboretum may not spawn (prevents "Saving Rani").
[*]Dr. Calvino's keycard can only be obtained from the exterior if you load Hardware Labs first.
[*]If any of the Cargo Bay B typhon become unkillable or go out of bounds, this may prevent rescuing the survivors there.
[*]The nightmare quest may not trigger correctly.
[*]If you eject yourself from Deep Storage before the director lockdown happens, you will be softlocked in the exterior.
[/list]

Game crashing issues:

[list]
[*]A game crash can result if you start a randomized game on top of an existing save slot. Manually deleting old save files before starting a new game in the same slot can fix this issue.
[*]Occasional game crashes when quitting to menu.
[*]Occasional game crashes and soft locks when using the "chaotic" item randomization settings.
[*](Still investigating) Some game crashes may occur when loading quicksaves. I'm still looking into ways to reproduce this issue.
[/list]

Other issues:

[list]
[*]If installer fails while running, there may not be an indication of this in the UI. If it takes too long, check the log.txt file for more info on what happened.
[*]Temporary directories may not be deleted if there was an issue during randomization/install. If these start to pile up, feel free to delete them manually.
[/list]
[/spoiler]

[size=5]Troubleshooting[/size]

[spoiler][img]https://imgur.com/R6J7ICx.jpg[/img]

In general, check the generated log.txt file to see if there were any issues. Please report any issues you find! Ideally, also attach the log.txt file, screenshots, and the seed/settings you used.

Installer takes longer than expected

[list]
[*]Verify that mod files have been created in your Prey directory. There should be a new level.pak in each section of Prey/GameSDK/Levels/Campaign (along with a level_backup.pak) as well as a patch_randomizer.pak in Prey/GameSDK/Precache.
[*]Close Prey 2017 if it is currently running.
[*]Assert you have enough hard drive space for the generated mod files.
[*]Delete any old temporary directories created by the mod.
[*]Try closing the installer, reopening it, then running it again.
[/list]

No changes are occurring. Loot isn't showing up in Morgan's apartment, Voice lines aren't randomized, etc.

[list]
[*]Ensure you currently don't have any other mods installed that are overwriting these changes.
[*]Make sure you've started a new game and manually erased the old save files.
[/list]

Game crashes on loading into level

[list]
[*]Ensure you currently don't have any other mods installed that are overwriting these changes.
[*]Make sure you've started a new game and manually erased the old save files.
[*]Try uninstalling the randomizer to see if the game still works normally. If not, you may have to redownload your game files.
[*]Send me a copy of your last_used_settings.json and tell me which level caused the issue so I can reproduce it.
[/list]

We now have a [url=https://discord.gg/MNGZjucxDE]Discord server[/url] for the randomizer for feedback and bug reports!

Also feel free to share your ideas on how to expand the mod!
[/spoiler]

[size=5]Authors/Version History[/size]

[spoiler][img]https://imgur.com/QmNBYpL.jpg[/img]

Report issues and/or feature requests to[url=https://reddit.com/u/shape_in_the_glass]/u/Shape_in_the_Glass[/url]﻿ or [url=https://www.reddit.com/user/Tsundereployer/]/u/Tsundereployer[/url]﻿, or leave a post here.

We now have a [url=https://discord.gg/MNGZjucxDE]Discord server[/url]! Feel free to join to discuss the randomizer, provide suggestions, give bug reports, leave any kind of feedback, etc.

Source code can be found on [url=https://github.com/shapeintheglass/DanielleRandomizer]GitHub[/url]﻿.

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
* 0.21
   * Quick fix for randomized station, where Shuttle Bay --> GUTS door was not getting properly set
   * Added "More guns" option
* 0.22
   * Adjusted chaotic presets so that they crash the game less
   * Implemented better threading support for installer
   * Adjusted item quantities in containers to be more reasonable
   * Fixed exotic ammo not working for "More guns" option
   * Added randomization for Alex and Luka's appearance in "randomize bodies" option
   * Added version compatibility check to saved settings file
   * Added "Make humans wander" option
* 0.30
   * Overhauled GUI for better organization
   * Overhauled dependency retrieval to take up less hard drive space
   * Running installer now creates a "last_used_settings.json" file that reflects the last used settings
   * Renamed "settings.json" to "spawn_presets.json" to make naming more accurate
   * Added "G.O.T.S." timed mode
   * Added music, player model randomization
   * Added option to skip Jovan's death cutscene
   * Set "Randomize loot tables" as a default when randomizing items
   * Updated skip to 2nd day option to use JerryTerry's [url=https://www.nexusmods.com/prey2017/mods/68]"Natural Day 2 Start"[/url] mod
   * Tweaked recommended/chaotic/lite presets for items/enemies to better match expectations for those tiers
   * Modified neuromod skill tree randomization to randomize within category
   * Modified station randomization to allow Psychotronics, Hardware Labs, and Crew Quarters to be randomized
   * Modified station randomization so that the mod overwrites certain books in Morgan's apartment and office with the new layout of the station
   * Modified station randomization and G.O.T.S. mode so that the player can walk through Apex kill walls
   * Fixed a case where some randomly generated stations were soft locks
   * Fixed a case where item spawn multiplers were still not very reasonable
   * Fixed randomization for containers (for real this time)
* 0.31
   * Retooled JSON serialization to use protobuffers, and restructured all JSON files
   * Fixed an issue where randomized stations were not deterministic
   * Fixed an issue where randomized music would lag the game
   * Fixed an issue where some randomized stations were soft locked
   * Fixed an issue where some randomized neuromod abilities in the science tree were not visible until the psychoscope was picked up
   * Fixed an issue were item multipliers in containers could be unreasonably high
   * Removed phantom player model option as the textures are not loaded in by default
   * Modified item randomization to include items spawned on harvestables and "fruit trees" such as vending machines, gun lockers, trees, flowers, medkit holders, and neuromod holders
   * Modified item randomization to include the preorder locker
   * Changed exotic ammunition to a non-junk item so that it won't be recycled by accident
   * Added fabrication plan randomization
   * Added gravity enable/disable options (experimental and not intended for serious runs)
   * Added a start location chooser (experimental and only intended for debugging purposes)
   * Added a game token override text box (only intended for debugging purposes)
   * Added support for string seeds
* 0.31 patch
  * Fixed an issue where loot tables would contain more guns even if "more guns" was not selected
  * Renamed "Foam" guns to "Toy" guns
  * Updated textures for exotic guns so that the ammo counter is visible
  * Updated inventory and HUD icons for "more guns" guns
* 0.4
  * Fixed an issue where station randomization could lead to a soft lock during Alex's lockdown
  * Fixed an issue where body randomization could generate bald women
  * Fixed an issue with body randomization so that Jovan, Bellamy, and Ingram can have randomized heads without their head disappearing
  * Fixed an issue with fabrication plans not properly spawning in the "lite" preset
  * Fixed an issue with player model randomization where Morgan's arms would be grey
  * Fixed an issue with "More Guns" exotic guns where firing them would kill you immediately
  * Removed tentacles from the spawn presets because they were only causing trouble
  * Removed grenade guns from "More Guns"
  * Split item/enemy randomization presets into item/prop and enemy/npc
  * Optimized level randomization to decrease install time
  * Updated neuromod and new item descriptions to be more clear (ex. randomized neuromods will have accurate info, exotic guns will say what kind of ammo is needed)
  * Modified fabrication plan randomization to produce more reasonable costs
  * Modified presets for "Lite", "Recommended", and "Chaotic"
  * Modified enemy randomization so that certain enemies are no longer whitelisted (the lift phantom, the lift technopath, the water treatment technopath)
  * Modified dialog randomization to also randomize cutscene dialog
  * Modified pickup item randomization to utilize in-game randomness (ex. fruit trees can yield different random junk)
  * Added new spawn preset options, such as splitting "with nightmare" into a separate enemy randomization option, or "randomize within theme" for props
  * Added randomization options for recyclers, operator dispensers, hackable objects, and repairable objects
  * Added a "living corpses" option that makes all corpses spawn in alive (intended for "Living Talos" preset)
  * Added Prey Souls guns and turrets (icons and materials are different from the original mod, some guns such as the psi cutter, laser assault rifle, time shotgun, and gauss rifle are not present)
[/spoiler]

[size=4]Acknowledgments[/size]

A big thanks to:

* Coyote for letting me use his content from Prey Souls!
* JerryTerry for allowing the use of their [url=https://www.nexusmods.com/prey2017/mods/68]"Natural Day 2 Start"[/url] mod!
* Fellow Prey modders such as Rosodude, jmx777, and coyote, who have helped me understand more about the file structure
* The brave adventrous souls who have helped me playtest and debug
* The Prey reddit and discord communities for being pretty swell and listening to my terrible ideas
* Arkane Studios for being cool folks who make cool games =]