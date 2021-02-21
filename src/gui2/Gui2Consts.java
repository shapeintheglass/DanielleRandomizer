package gui2;

public class Gui2Consts {
  public static final String WINDOW_TITLE = "Danielle Randomizer v%s";
  public static final String VERSION = "0.31";

  public static final String DEFAULT_SELF_DESTRUCT_TIMER = "60.000000";
  public static final String DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER = "30.000000";

  public static final String DEFAULT_INSTALL_DIR = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  public static final String SAVED_SETTINGS_FILE = "saved_settings.json";
  public static final String LAST_USED_SETTINGS_FILE = "last_used_settings.json";
  public static final String LOG_OUTPUT_FILE = "log.txt";
  public static final String ALL_PRESETS_FILE = "spawn_presets.json";

  /* OUTPUT WINDOW INFO */
  public static final String PRESET_INFO = "Settings:\n%s\n";
  public static final String SAVING_INFO = "Wrote current settings to %s\n";

  /* ERROR MESSAGES */
  public static final String ERROR_COULD_NOT_PARSE_FILE = "Failed to parse file: ";
  public static final String WARNING_SAVED_SETTINGS_VERSION_MISMATCH = "Found saved settings at version %s. This application is only compatible with version %s. Falling back to default settings.";
  public static final String SAVING_FAILED = "Failed to save. See log for more info.\n";

  /* INSTALLATION */
  public static String INSTALL_STATUS_TEXT = "Installing...";
  public static String INSTALL_STATUS_COMPLETE_TEXT = "Finished installing in %d seconds.";
  public static String INSTALL_STATUS_FAILED_TEXT = "Install failed. See log for more info.";
  public static String INSTALL_ERROR_DATA_NOT_FOUND = "Could not find data/ folder";
  public static String INSTALL_ERROR_INVALID_INSTALL_FOLDER = "Prey install directory not valid.";
  public static String INSTALL_ERROR_CANNOT_WRITE = "Unable to write to file. Is Prey running?";

  public static String INSTALL_PROGRESS_BODIES = "Randomizing bodies...";
  public static String INSTALL_PROGRESS_VOICELINES = "Randomizing voicelines...";
  public static String INSTALL_PROGRESS_MUSIC = "Randomizing music...";
  public static String INSTALL_PROGRESS_PLAYER_MODEL = "Randomizing player model...";
  public static String INSTALL_PROGRESS_NEUROMOD = "Randomizing neuromod upgrade tree...";
  public static String INSTALL_PROGRESS_LOOT = "Randomizing loot tables...";
  public static String INSTALL_PROGRESS_LEVELS = "Processing level data... (can take a while)";
  public static String INSTALL_PROGRESS_WRITING = "Copying mod files to game directory...";
}
