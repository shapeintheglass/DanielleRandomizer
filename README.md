# Danielle Randomizer

A simple randomizer for Prey 2017 (code-named "Danielle").

## Description

Randomizes various entities in Prey 2017 via an external executable that overwrites the level files and generates a patch_randomizer.pak. 

## Getting Started

### Dependencies

* Requires a valid copy of Prey 2017 on Steam or GOG (the Windows Store version of Prey 2017 cannot be modded)
* Must have Java for Windows installed

### Installing the randomizer

* Ensure danielle_randomizer.exe is in the same directory as the data/ folder.

### Uninstalling

* Ideally, use the "Uninstall" function within the randomizer to uninstall any generated files before uninstalling.
* Delete danielle_randomizer.exe and its data/ folder to uninstall.

### Executing program

* Run danielle_randomizer.exe to start up the GUI
* Specify Prey install directory
* Specify desired randomization settings
* Click "Install" to generate randomized files and install them
* Click "Uninstall" to revert any changes made by this randomizer.

### Configuring custom randomizer settings

If you aren't happy with the preset spawn rates for items and enemies, you can modify them yourself in settings.json.

This randomizer generates a list of tags for every entity in the game (ex. weapons are "Weapons", food is "ArkFood", etc). To see a list of supported entities and tags, see tagstoentities.csv (shows the entities associated with each tag) and entitiestotags.csv (shows the tags generated for each entity).

The level randomizer in this project uses a list of "filters" to process the level files. A "filter" consists of five arrays:

* input_tags - if an entity matches one of these tags, randomize it
* output_tags - acceptable tags for items to come out of this filter (first a tag is chosen randomly from this list, then an entity matching that tag is chosen randomly)
* output_weights - int array of relative weights to assign to each output tags. must have the same length as output_tags.
* do_not_touch_tags - if an entity matches one of these tags, do NOT randomize it. overrides input_tags.
* do_not_output_tags - do NOT output any items that match a tag in this list. overrides output_tags.

Filters are processed sequentially for each level entity. If an entity can be transformed by multiple filters, the first one will take precidence.

If you change settings.json while the randomizer GUI is active, click "Refresh settings" to see your changes.

## Known issues

* GUI won't properly show the status/progress of the current operation.
* Items that are hard-coded to spawn in certain containers cannot be randomized (changing the class type of these items results in breaking the game).
* Temporary directories will not be deleted if there was an issue during randomization/install.

## Authors

[@Shape in the Glass](https://twitter.com/shapeintheglass)

## Version History

* 0.0
    * Early prototyping/testing

## License

This project is licensed under the WTFPL License - see the LICENSE.md file for details

## Acknowledgments

Thanks to fellow Prey modders such as Rosodude, jmx777, and coyote, who have helped me understand more about the file structure. Also big thanks to the Prey reddit and discord communities for being pretty swell and listening to my terrible ideas. Biggest thanks to Arkane Studios for being cool dudes who make cool games.