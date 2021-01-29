package gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.google.common.base.Optional;

import gui.panels.MainPanel;
import gui.panels.TopPanel;
import json.AllPresetsJson;
import json.CosmeticSettingsJson;
import json.GameplaySettingsJson;
import json.GenericRuleJson;
import json.SettingsJson;
import json.SpawnPresetJson;

/**
 * Renders the GUI interface.
 * 
 * @author Kida
 *
 */
public class RandomizerGui {
  public static final String RELEASE_VER = "0.22";
  public static final String SAVED_SETTINGS_FILE = "saved_settings.json";

  private static final int WINDOW_HEIGHT = 300;
  private static final int WINDOW_WIDTH = 600;

  private static final String DEFAULT_INSTALL_DIR = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";
  private static final String LOG_OUTPUT_FILE = "log.txt";
  private static final String ALL_PRESETS_FILE = "settings.json";

  private JFrame mainFrame;
  private JPanel mainPanel;

  private Logger logger;
  private PrintStream fileStream;

  public RandomizerGui() {
    setupLogFile();

    logger = Logger.getLogger("randomizer_gui");
    mainFrame = new JFrame(Consts.WINDOW_TITLE + " " + RELEASE_VER);
    mainFrame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    // Attempt to read from preset definition file and saved settings file
    Optional<AllPresetsJson> allPresets = getSpawnPresets();

    if (!allPresets.isPresent()) {
      JOptionPane.showMessageDialog(mainFrame, String.format(
          "An error occurred while parsing %s. See log for more info.", ALL_PRESETS_FILE));
    }

    Optional<SettingsJson> savedSettings = getSavedSettings();

    // Use parsed files to generate initial configuration
    SettingsJson initialSettings = createInitialSettings(allPresets, savedSettings);

    // Draw the main panel
    mainPanel = new MainPanel(initialSettings, allPresets.get());
    mainFrame.add(mainPanel);
    mainFrame.pack();
  }

  private Optional<AllPresetsJson> getSpawnPresets() {
    try {
      AllPresetsJson allPresets = new AllPresetsJson(ALL_PRESETS_FILE);
      validateSpawnPresets(allPresets.getItemSpawnSettings(), "item");
      validateSpawnPresets(allPresets.getEnemySpawnSettings(), "enemy");
      return Optional.of(allPresets);
    } catch (IOException e1) {
      logger.warning(Consts.ERROR_COULD_NOT_PARSE_FILE + ALL_PRESETS_FILE);
      e1.printStackTrace();
    }
    return Optional.absent();
  }

  private Optional<SettingsJson> getSavedSettings() {
    // Parse the saved settings file and set new defaults accordingly
    File lastUsedSettingsFile = new File(SAVED_SETTINGS_FILE);
    if (lastUsedSettingsFile.exists()) {
      try {
        return Optional.of(new SettingsJson(SAVED_SETTINGS_FILE));
      } catch (IOException e) {
        logger.warning(Consts.ERROR_COULD_NOT_PARSE_FILE + SAVED_SETTINGS_FILE);
        e.printStackTrace();
      }
    }
    return Optional.absent();
  }

  private SettingsJson createInitialSettings(Optional<AllPresetsJson> spawnPresets,
      Optional<SettingsJson> savedSettings) {
    if (savedSettings.isPresent()) {
      if (savedSettings.get().getReleaseVersion().equals(RELEASE_VER)) {
        return savedSettings.get();
      } else {
        logger.warning(String.format(Consts.WARNING_SAVED_SETTINGS_VERSION_MISMATCH, savedSettings.get()
            .getReleaseVersion(), RELEASE_VER));
      }
    }

    SpawnPresetJson itemPreset = spawnPresets.isPresent() ? spawnPresets.get().getItemSpawnSettings().get(0) : null;
    SpawnPresetJson enemyPreset = spawnPresets.isPresent() ? spawnPresets.get().getEnemySpawnSettings().get(0) : null;

    long initialSeed = TopPanel.SeedPanel.getNewSeed();
    return new SettingsJson(RELEASE_VER, DEFAULT_INSTALL_DIR, initialSeed, new CosmeticSettingsJson(),
        new GameplaySettingsJson(itemPreset, enemyPreset));
  }

  private void setupLogFile() {
    try {
      File loggerFile = new File(LOG_OUTPUT_FILE);
      if (!loggerFile.exists()) {
        loggerFile.createNewFile();
      }
      if (fileStream == null) {
        fileStream = new PrintStream(loggerFile);
      }
      System.setErr(fileStream);
      System.setOut(fileStream);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  private void validateSpawnPresets(List<SpawnPresetJson> s, String name) {
    for (SpawnPresetJson gspj : s) {
      if (gspj.getRules() == null) {
        continue;
      }
      for (int i = 0; i < gspj.getRules().size(); i++) {
        GenericRuleJson gfj = gspj.getRules().get(i);

        if (gfj.getOutputTags() == null || gfj.getOutputWeights() == null) {
          continue;
        }

        if (gfj.getOutputWeights().size() != 0 && gfj.getOutputTags().size() != gfj.getOutputWeights().size()) {
          logger.info(String.format(
              "Invalid filter settings for %s spawns, preset name %s, filter %d. Output tags length (%d) and output weights length (%d) are not identical.",
              name, gspj.getName(), i, gfj.getOutputTags().size(), gfj.getOutputWeights().size()));
        }
      }
    }
  }

  public void start() {
    mainFrame.setVisible(true);
  }
}
