# Danielle Randomizer v0.4

A configurable randomizer for Prey 2017 (code-named "Danielle").

## Description

An external executable GUI that randomizes various entities in Prey 2017 by overwriting the level files and generating a patch_randomizer.pak in GameSDK\Precache. Before overwriting, level files are backed up in the same directory as "level_backup.pak".

### Features

Here are some of the options the randomizer offers:

**Cosmetic**

* Randomized human appearance
* Randomized voice lines
* Randomized emotions
* Randomized music
* Randomized player model
* Randomized earth/moon/sun size

**Gameplay**

* Randomized item/physics prop spawns
* Randomized enemy/NPC spawns
* Randomized neuromod upgrade tree
* Randomized station connectivity
* Randomized fab plan costs
* [New in v0.4] Randomized operator dispensers
* [New in v0.4] Randomized recyclers/fabricators
* [New in v0.4] Randomized breakables
* [New in v0.4] Randomized hackables

**Other features:**

* Start on 2nd day
* Add loot to Morgan's apartment
* Remove scan requirement for neuromods
* Unlock all doors/safes/workstations
* Get Off The Station mode (timed challenge mode)
* Enable/disable gravity (experimental and not recommended for serious runs)
* [New in v0.4] Prey Souls guns/turrets
* [New in v0.4] All corpses are alive
* [New in v0.4] Morgan's office door can lock from the inside

## Getting Started

See [Nexus Mods](https://www.nexusmods.com/prey2017/mods/67) for the latest documentation and links to the latest release. Alpha releases [can be found here](https://github.com/shapeintheglass/DanielleRandomizer/releases), but may have some issues.

## Contact

Report issues to [/u/Shape_in_the_Glass](https://reddit.com/u/shape_in_the_glass) or in our shiny new [Discord Server](https://discord.gg/MNGZjucxDE).

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
* 0.21
  * Quick fix for randomized station, where Shuttle Bay --> GUTS door was not getting properly setting
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
  * Updated skip to 2nd day option to use JerryTerry's "Natural Day 2 Start" mod
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

## Acknowledgments

A big thanks to:

* Coyote for letting me use his content from Prey Souls!													
* JerryTerry for letting me use their "Natural Day 2 Start" mod!
* Fellow Prey modders such as Rosodude, jmx777, and coyote, who have helped me understand more about the file structure
* The brave adventrous souls who have helped me playtest and debug
* The Prey reddit and discord communities for being pretty swell and listening to my terrible ideas
* Arkane Studios for being cool folks who make cool games =]
