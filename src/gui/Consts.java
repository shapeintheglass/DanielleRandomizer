package gui;

public class Consts {

  public static String WINDOW_TITLE = "Prey Randomizer";

  /* ERROR MESSAGES */
  public static String ERROR_COULD_NOT_PARSE_FILE = "Failed to parse file: ";
  public static String WARNING_SAVED_SETTINGS_VERSION_MISMATCH = "Found saved settings at version %s. This application is only compatible with version %s. Falling back to default settings.";
  public static String ERROR_COULD_NOT_PARSE_GUI = "Failed to process input.";
  public static String ERROR_COULD_NOT_PARSE_JSON = "Failed to parse json settings!";
  public static String ERROR_COULD_NOT_RUN_PREY = "Error occurred when launching Prey.";

  /* TOP PANEL */
  public static String CURRENT_FILE_LABEL = "Prey folder location:";
  public static String CURRENT_FILE_TTT = "Game directory, ending in Prey/";
  public static String CHANGE_CURRENT_FILE_BUTTON_LABEL = "Change";
  public static String CURRENT_SEED_LABEL = "Seed:";
  public static String NEW_SEED_LABEL = "New seed";
  public static String NEW_SEED_TTT = "Generate a new seed";
  public static String INSTALL_DIR_HACKY_ERROR_MSG = "Error occurred while parsing install directory";

  /* OPTIONS PANEL */
  public static String ITEM_SPAWN_PRESETS_LABEL = "Item spawn presets";
  public static String NPC_SPAWN_PRESETS_LABEL = "NPC spawn presets";
  public static String COSMETIC_OPTIONS_HEADER = "Cosmetic Options";
  public static String APARTMENT_OPTIONS_HEADER = "Apartment Options";
  public static String NEUROMOD_OPTIONS_HEADER = "Neuromod Options";
  public static String CONNECTIVITY_OPTIONS_HEADER = "Connectivity Options";
  public static String ITEM_OPTIONS_HEADER = "Item Options";
  public static String OTHER_OPTIONS_HEADER = "Other Options";

  /* BUTTONS PANEL */
  public static String RUN_BUTTON_LABEL = "Run Prey";
  public static String RUN_BUTTON_TTT = "Starts Prey using the file location provided";
  public static String SAVE_BUTTON_LABEL = "Save settings";
  public static String SAVE_BUTTON_TTT = "Saves the current settings so that they'll be the default next time you load this GUI.";
  public static String SAVE_STATUS_COMPLETE = "Current settings saved to %s.";
  public static String INSTALL_BUTTON_LABEL = "Install";
  public static String INSTALL_BUTTON_TTT = "Randomizes according to above settings and installs in game directory";
  public static String UNINSTALL_BUTTON_LABEL = "Uninstall";
  public static String UNINSTALL_BUTTON_TTT = "Removes any mods added by this randomizer, restoring game files to previous state";
  public static String UNINSTALLING_STATUS_TEXT = "Uninstalling...";
  public static String UNINSTALLING_STATUS_COMPLETE_TEXT = "Done uninstalling.";
  public static String CLOSE_BUTTON_LABEL = "Close";
  public static String CLOSE_BUTTON_TTT = "Closes this GUI";

  /* INSTALLATION */
  public static String INSTALL_STATUS_TEXT = "Installing...";
  public static String INSTALL_STATUS_COMPLETE_TEXT = "Finished installing in %d seconds.";
  public static String INSTALL_STATUS_FAILED_TEXT = "Install failed. See log for more info.";
  public static String INSTALL_ERROR_DATA_NOT_FOUND = "Could not find data/ folder";
  public static String INSTALL_ERROR_INVALID_INSTALL_FOLDER = "Prey install directory not valid.";
  public static String INSTALL_ERROR_CANNOT_WRITE = "Unable to write to file. Is Prey running?";

  public static String INSTALL_PROGRESS_BODIES = "Randomizing bodies...";
  public static String INSTALL_PROGRESS_VOICELINES = "Randomizing voicelines...";
  public static String INSTALL_PROGRESS_NEUROMOD = "Randomizing neuromod upgrade tree...";
  public static String INSTALL_PROGRESS_LOOT = "Randomizing loot tables...";
  public static String INSTALL_PROGRESS_LEVELS = "Processing level data...";
  public static String INSTALL_PROGRESS_WRITING = "Copying mod files to game directory...";
}
