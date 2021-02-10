package gui2;

import com.google.common.collect.Lists;

import json.CosmeticSettingsJson;
import json.GameplaySettingsJson;
import json.GenericRuleJson;
import json.SpawnPresetJson;

public class Gui2Consts {
  public static final String WINDOW_TITLE = "Danielle Randomizer v%s";
  public static final String VERSION = "0.30";

  public static final String DEFAULT_INSTALL_DIR = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  public static final String SAVED_SETTINGS_FILE = "saved_settings.json";
  public static final String LAST_USED_SETTINGS_FILE = "last_used_settings.json";
  public static final String LOG_OUTPUT_FILE = "log.txt";
  public static final String ALL_PRESETS_FILE = "spawn_presets.json";

  /* DEFAULT PRESETS */
  public static final SpawnPresetJson ITEMS_RANDOMIZE_ALL = new SpawnPresetJson("Randomize all", "", Lists.newArrayList(
      new GenericRuleJson(Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Lists
          .newArrayList())));
  public static final CosmeticSettingsJson RECOMMENDED_COSMETIC = new CosmeticSettingsJson(true, true);
  public static final GameplaySettingsJson RECOMMENDED_GAMEPLAY = new GameplaySettingsJson(true, false, false, false,
      false, false, false, false, false, false, null, null);
  public static final GameplaySettingsJson CHAOTIC_GAMEPLAY = new GameplaySettingsJson(true, false, true, true, false,
      true, true, true, false, false, null, null);

  /* OUTPUT WINDOW INFO */
  public static final String PRESET_INFO = "Settings:\n%s\n";
  public static final String SAVING_INFO = "Wrote current settings to %s\n";

  /* ERROR MESSAGES */
  public static final String ERROR_COULD_NOT_PARSE_FILE = "Failed to parse file: ";
  public static final String WARNING_SAVED_SETTINGS_VERSION_MISMATCH = "Found saved settings at version %s. This application is only compatible with version %s. Falling back to default settings.";
  public static final String SAVING_FAILED = "Failed to save. See log for more info.\n";
}
