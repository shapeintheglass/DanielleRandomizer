package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import installers.Installer;
import json.AllPresetsJson;
import json.CosmeticSettingsJson;
import json.GameplaySettingsJson;
import json.GenericRuleJson;
import json.SettingsJson;
import json.SpawnPresetJson;
import randomizers.cosmetic.BodyRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.LevelRandomizer;
import randomizers.gameplay.LootTableRandomizer;
import randomizers.gameplay.NeuromodTreeRandomizer;
import randomizers.gameplay.level.filters.EnemyFilter;
import randomizers.gameplay.level.filters.FlowgraphFilter;
import randomizers.gameplay.level.filters.ItemSpawnFilter;
import randomizers.gameplay.level.filters.MorgansApartmentFilter;
import randomizers.gameplay.level.filters.OpenStationFilter;
import randomizers.gameplay.level.filters.StationConnectivityFilter;
import utils.Utils;

/**
 * Renders the GUI interface.
 * 
 * @author Kida
 *
 */
public class RandomizerGui {

  private static final String RELEASE_VER = "0.2";

  private static final boolean DEFAULT_OPEN_STATION_VALUE = true;
  private static final boolean DEFAULT_ADD_LOOT_TO_APARTMENT_VALUE = true;
  private static final boolean DEFAULT_RANDOMIZE_LOOT_VALUE = true;
  private static final boolean DEFAULT_RANDOMIZE_VOICELINES_VALUE = true;
  private static final boolean DEFAULT_RANDOMIZE_BODIES_VALUE = true;
  private static final boolean DEFAULT_RANDOMIZE_NEUROMODS_VALUE = true;
  private static final boolean DEFAULT_UNLOCK_ALL_SCANS_VALUE = true;
  private static final boolean DEFAULT_RANDOMIZE_STATTION_VALUE = true;

  private static final String DEFAULT_INSTALL_DIR = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  private static final String LOG_OUTPUT_FILE = "log.txt";

  private static final String SAVED_SETTINGS_FILE = "last_used_settings.json";
  private static final String ALL_PRESETS_FILE = "settings.json";

  private JFrame mainFrame;

  private JLabel currentFile;
  private JFileChooser fileChooser;
  private JTextField seedField;

  private JCheckBox voiceLinesCheckBox;
  private JCheckBox bodiesCheckBox;
  private JCheckBox apartmentLootCheckBox;
  private JCheckBox lootTablesCheckBox;
  private JCheckBox openStationCheckBox;
  private JCheckBox randomizeNeuromodsCheckBox;
  private JCheckBox unlockScansCheckBox;
  private JCheckBox randomizeStationCheckBox;

  private JLabel statusLabel;
  private JButton installButton;
  private JButton uninstallButton;

  private SettingsJson currentSettings;
  private AllPresetsJson allPresets;

  private JPanel mainPanel;
  private JPanel headerPanel;
  private JPanel installDirPanel;
  private JPanel optionsPanel;
  private BaseSpawnOptionsPanel<SpawnPresetJson> itemSpawnPanel;
  private BaseSpawnOptionsPanel<SpawnPresetJson> enemySpawnPanel;
  private JPanel otherGameplayOptionsPanel;
  private JPanel buttonsPanel;
  private List<SpawnPresetJson> itemSpawnSettings;
  private List<SpawnPresetJson> enemySpawnSettings;

  private Logger logger;
  private PrintStream fileStream;

  private TaggedDatabase database;

  public RandomizerGui() {
    setupLogFile();

    database = EntityDatabase.getInstance();

    logger = Logger.getGlobal();
    mainFrame = new JFrame("Prey Randomizer");
    mainFrame.setSize(600, 300);
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    SpawnPresetJson defaultEnemySpawnPreset = new SpawnPresetJson("", "", new ArrayList<>());
    SpawnPresetJson defaultItemSpawnPreset = new SpawnPresetJson("", "", new ArrayList<>());

    try {
      allPresets = new AllPresetsJson(ALL_PRESETS_FILE);
      defaultEnemySpawnPreset = allPresets.getEnemySpawnSettings()
                                          .get(0);
      defaultItemSpawnPreset = allPresets.getItemSpawnSettings()
                                         .get(0);
    } catch (IOException e1) {
      logger.warning("failed to parse presets file " + ALL_PRESETS_FILE);
      e1.printStackTrace();
    }

    currentSettings = new SettingsJson(RELEASE_VER, DEFAULT_INSTALL_DIR, new Random().nextLong(),
        new CosmeticSettingsJson(DEFAULT_RANDOMIZE_BODIES_VALUE, DEFAULT_RANDOMIZE_VOICELINES_VALUE),
        new GameplaySettingsJson(DEFAULT_RANDOMIZE_LOOT_VALUE, DEFAULT_ADD_LOOT_TO_APARTMENT_VALUE,
            DEFAULT_OPEN_STATION_VALUE, DEFAULT_RANDOMIZE_NEUROMODS_VALUE, DEFAULT_UNLOCK_ALL_SCANS_VALUE,
            DEFAULT_RANDOMIZE_STATTION_VALUE, defaultEnemySpawnPreset, defaultItemSpawnPreset));
    File lastUsedSettingsFile = new File(SAVED_SETTINGS_FILE);
    if (lastUsedSettingsFile.exists()) {
      try {
        currentSettings = new SettingsJson(SAVED_SETTINGS_FILE);
      } catch (IOException e) {
        logger.info(String.format("An error occurred while parsing %s: %s. Falling back to default settings.",
            SAVED_SETTINGS_FILE, e.getMessage()));
        e.printStackTrace();
      }
    }

    mainPanel = new JPanel();
    headerPanel = new JPanel();
    installDirPanel = new JPanel();
    optionsPanel = new JPanel();
    buttonsPanel = new JPanel();
    statusLabel = new JLabel();

    mainFrame.add(mainPanel);

    setupMainPanel();
    setupHeaderPanel();
    setupInstallDirPanel();
    setupOptionsPanel();
    updateOptionsPanel();
    setupButtonsPanel();

    mainFrame.pack();

  }

  private void setupMainPanel() {
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(headerPanel);
    mainPanel.add(installDirPanel);
    mainPanel.add(optionsPanel);
    mainPanel.add(buttonsPanel);
  }

  private void setupHeaderPanel() {
    JLabel headerLabel = new JLabel("Prey Randomizer", JLabel.LEFT);
    headerLabel.setSize(600, 10);

    headerPanel.setLayout(new FlowLayout());
    headerPanel.add(headerLabel);
    headerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
  }

  private void setupInstallDirPanel() {
    fileChooser = new JFileChooser(currentSettings.getInstallDir());
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    JLabel currentFileLabel = new JLabel("Prey folder location:");
    currentFileLabel.setToolTipText("Game directory, ending in Prey/");

    currentFile = new JLabel(currentSettings.getInstallDir());
    currentFile.setBorder(BorderFactory.createRaisedSoftBevelBorder());
    JButton changeInstall = new JButton("Change");
    changeInstall.setActionCommand("changeInstallDir");
    changeInstall.addActionListener(new OnChangeDirClick());

    JLabel seedLabel = new JLabel("Seed:");
    seedField = new JTextField(Long.toString(currentSettings.getSeed()), 15);
    seedField.setHorizontalAlignment(JLabel.RIGHT);

    // TODO: Add "new seed" button

    installDirPanel.setLayout(new FlowLayout());
    installDirPanel.add(currentFileLabel);
    installDirPanel.add(currentFile);
    installDirPanel.add(changeInstall);
    installDirPanel.add(Box.createRigidArea(new Dimension(5, 0)));
    installDirPanel.add(seedLabel);
    installDirPanel.add(seedField);
    installDirPanel.setBorder(BorderFactory.createLineBorder(Color.black));
  }

  private void setupOptionsPanel() {
    itemSpawnPanel = new BaseSpawnOptionsPanel<>("Item spawn presets", "item", new OnRadioClick());
    enemySpawnPanel = new BaseSpawnOptionsPanel<>("NPC spawn presets", "enemy", new OnRadioClick());
    otherGameplayOptionsPanel = new JPanel();
    otherGameplayOptionsPanel.add(new JLabel("Other options"));
    otherGameplayOptionsPanel.setLayout(new GridLayout(6, 1));

    ItemListener listener = new OnCheckBoxClick();
    voiceLinesCheckBox = new JCheckBox("Randomize voice lines", currentSettings.getCosmeticSettings()
                                                                               .isRandomizeVoicelines());
    voiceLinesCheckBox.setToolTipText("Shuffles voice lines within voice actor");
    voiceLinesCheckBox.addItemListener(listener);
    bodiesCheckBox = new JCheckBox("Randomize NPC bodies", currentSettings.getCosmeticSettings()
                                                                          .isRandomizeBodies());
    bodiesCheckBox.setToolTipText("Randomizes human NPC body parts");
    bodiesCheckBox.addItemListener(listener);
    apartmentLootCheckBox = new JCheckBox("Add loot to Morgan's apartment", currentSettings.getGameplaySettings()
                                                                                           .isAddLootToApartment());
    apartmentLootCheckBox.setToolTipText("Adds useful equipment in containers around Morgan's apartment");
    apartmentLootCheckBox.addItemListener(listener);
    lootTablesCheckBox = new JCheckBox("Randomize loot tables", currentSettings.getGameplaySettings()
                                                                               .isRandomizeLoot());
    lootTablesCheckBox.setToolTipText("Randomizes contents of loot tables according to item spawn settings");
    lootTablesCheckBox.addItemListener(listener);
    openStationCheckBox = new JCheckBox("Open up Talos I (WIP)", currentSettings.getGameplaySettings()
                                                                                .isOpenStation());
    openStationCheckBox.setToolTipText("Unlocks doors around Talos I to make traversal easier");
    openStationCheckBox.addItemListener(listener);
    randomizeNeuromodsCheckBox = new JCheckBox("Randomize Neuromod upgrade tree", currentSettings.getGameplaySettings()
                                                                                                 .isRandomizeNeuromods());
    randomizeNeuromodsCheckBox.addItemListener(listener);
    randomizeNeuromodsCheckBox.setToolTipText("Shuffles the neuromods in the skill upgrade tree");
    unlockScansCheckBox = new JCheckBox("Unlock all neuromods scans", currentSettings.getGameplaySettings()
                                                                                     .isUnlockAllScans());
    unlockScansCheckBox.addItemListener(listener);
    unlockScansCheckBox.setToolTipText("Removes scan requirement for all typhon neuromods");

    randomizeStationCheckBox = new JCheckBox("Randomize station connections", currentSettings.getGameplaySettings()
                                                                                             .isRandomizeStation());
    randomizeStationCheckBox.addItemListener(listener);
    randomizeStationCheckBox.setToolTipText("Shuffles connections between levels");

    otherGameplayOptionsPanel.add(voiceLinesCheckBox);
    otherGameplayOptionsPanel.add(bodiesCheckBox);
    otherGameplayOptionsPanel.add(apartmentLootCheckBox);
    otherGameplayOptionsPanel.add(lootTablesCheckBox);
    otherGameplayOptionsPanel.add(openStationCheckBox);
    otherGameplayOptionsPanel.add(randomizeNeuromodsCheckBox);
    otherGameplayOptionsPanel.add(unlockScansCheckBox);
    otherGameplayOptionsPanel.add(randomizeStationCheckBox);

    optionsPanel.setLayout(new GridLayout(1, 3));
    optionsPanel.add(itemSpawnPanel);
    optionsPanel.add(enemySpawnPanel);
    optionsPanel.add(otherGameplayOptionsPanel);
  }

  private void updateOptionsPanel() {
    try {
      allPresets = new AllPresetsJson(ALL_PRESETS_FILE);

      itemSpawnSettings = allPresets.getItemSpawnSettings();
      itemSpawnPanel.setRadioLabels(itemSpawnSettings, currentSettings.getGameplaySettings()
                                                                      .getItemSpawnSettings()
                                                                      .getName());

      enemySpawnSettings = allPresets.getEnemySpawnSettings();
      enemySpawnPanel.setRadioLabels(enemySpawnSettings, currentSettings.getGameplaySettings()
                                                                        .getEnemySpawnSettings()
                                                                        .getName());

      // Update current settings in case presets have changed underneath
      int itemSpawnIndex = itemSpawnPanel.getCurrentIndex();
      int enemySpawnIndex = enemySpawnPanel.getCurrentIndex();

      currentSettings.getGameplaySettings()
                     .setItemSpawnSettings(itemSpawnSettings.get(itemSpawnIndex));
      currentSettings.getGameplaySettings()
                     .setEnemySpawnSettings(enemySpawnSettings.get(enemySpawnIndex));

      validateSettings();
      mainFrame.pack();
    } catch (Exception e) {
      statusLabel.setText("Error occurred while parsing " + ALL_PRESETS_FILE);
      JOptionPane.showMessageDialog(mainFrame, String.format("An error occurred while parsing %s: %s", ALL_PRESETS_FILE,
          e.getMessage()));
    }
  }

  private void setupButtonsPanel() {

    // TODO: Split into two panels, add "clear saved settings" option

    statusLabel = new JLabel("", JLabel.LEFT);
    JButton saveButton = new JButton("Save settings");
    saveButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        try {
          writeLastUsedSettingsToFile(SAVED_SETTINGS_FILE);
          statusLabel.setText("Settings saved to " + SAVED_SETTINGS_FILE);
        } catch (IOException e) {
          logger.warning("Error occurred when saving last used settings.");
          e.printStackTrace();
        }
      }
    });
    saveButton.setToolTipText("Saves the current settings so that they'll be the default next time you load this GUI.");
    JButton refreshSettings = new JButton("Refresh options");
    refreshSettings.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateOptionsPanel();
        // Reset indeces to 0
        itemSpawnPanel.setCurrentIndex(0);
        enemySpawnPanel.setCurrentIndex(0);
        currentSettings.getGameplaySettings()
                       .setItemSpawnSettings(itemSpawnSettings.get(0));
        currentSettings.getGameplaySettings()
                       .setEnemySpawnSettings(enemySpawnSettings.get(0));
        statusLabel.setText("Options refreshed.");
        mainFrame.revalidate();
      }
    });
    refreshSettings.setToolTipText("Updates item/NPC spawn options if you modified settings.json");
    installButton = new JButton("Install");
    installButton.setActionCommand("install");
    installButton.addActionListener(new OnInstallClick());
    installButton.setToolTipText("Randomizes according to above settings and installs in game directory");
    uninstallButton = new JButton("Uninstall");
    uninstallButton.setActionCommand("uninstall");
    uninstallButton.addActionListener(new OnUninstallClick());
    uninstallButton.setToolTipText("Removes any mods added by this randomizer, restoring game files to previous state");
    JButton closeButton = new JButton("Close");
    closeButton.setActionCommand("close");
    closeButton.addActionListener(new OnCloseClick());
    closeButton.setToolTipText("Closes this GUI");
    buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

    buttonsPanel.add(statusLabel);
    buttonsPanel.add(saveButton);
    buttonsPanel.add(refreshSettings);
    buttonsPanel.add(installButton);
    buttonsPanel.add(uninstallButton);
    buttonsPanel.add(closeButton);
    buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
  }

  public void start() {
    mainFrame.setVisible(true);
  }

  private class OnChangeDirClick implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      fileChooser.showOpenDialog(null);
      String installDir = "Error occurred while parsing install directory";
      try {
        installDir = fileChooser.getSelectedFile()
                                .getCanonicalPath();
        currentSettings.setInstallDir(installDir);
      } catch (IOException e1) {
        logger.warning("Unable to get canonical path for Prey install dir: " + e1.getMessage());
        statusLabel.setText("Error occurred when parsing Prey install dir");
      }
      currentFile.setText(installDir);
    }

  }

  private class OnCheckBoxClick implements ItemListener {

    @Override
    public void itemStateChanged(ItemEvent e) {
      Object source = e.getItemSelectable();

      if (source == voiceLinesCheckBox) {
        currentSettings.getCosmeticSettings()
                       .toggleRandomizeVoicelines();
      } else if (source == bodiesCheckBox) {
        currentSettings.getCosmeticSettings()
                       .toggleRandomizeBodies();
      } else if (source == apartmentLootCheckBox) {
        currentSettings.getGameplaySettings()
                       .toggleAddLootToApartment();
      } else if (source == lootTablesCheckBox) {
        currentSettings.getGameplaySettings()
                       .toggleRandomizeLoot();
      } else if (source == openStationCheckBox) {
        currentSettings.getGameplaySettings()
                       .toggleOpenStation();
      } else if (source == randomizeNeuromodsCheckBox) {
        currentSettings.getGameplaySettings()
                       .toggleRandomizeNeuromods();
      } else if (source == unlockScansCheckBox) {
        currentSettings.getGameplaySettings()
                       .toggleUnlockAllScans();
      } else if (source == randomizeStationCheckBox) {
        currentSettings.getGameplaySettings()
                       .toggleRandomizeStation();
      }
    }

  }

  private class OnRadioClick implements ActionListener {
    private static final String ENEMY_SPAWN_OPTIONS_PREFIX = "enemy";
    private static final String ITEM_SPAWN_OPTIONS_PREFIX = "item";

    @Override
    public void actionPerformed(ActionEvent e) {
      String[] tokens = e.getActionCommand()
                         .split(BaseSpawnOptionsPanel.DELIMITER);
      switch (tokens[0]) {
        case ITEM_SPAWN_OPTIONS_PREFIX:
          currentSettings.getGameplaySettings()
                         .setItemSpawnSettings(itemSpawnPanel.getSettingsByName(tokens[1]));
          break;
        case ENEMY_SPAWN_OPTIONS_PREFIX:
          currentSettings.getGameplaySettings()
                         .setEnemySpawnSettings(enemySpawnPanel.getSettingsByName(tokens[1]));
          break;
        default:
          break;
      }
    }
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

  private class OnInstallClick implements ActionListener {

    private static final String TEMP_FOLDER_SUFFIX = "deleteme";
    private static final String TEMP_PATCH_DIR_NAME = "patch";
    private static final String TEMP_LEVEL_DIR_NAME = "level";
    private static final String DEFAULT_WORKING_DIR = ".";

    @Override
    public void actionPerformed(ActionEvent arg0) {
      uninstallButton.setEnabled(false);
      statusLabel.setText("Installing...");

      // Validation checks

      try {
        long seed = Long.parseLong(seedField.getText());
        currentSettings.setSeed(seed);
      } catch (NumberFormatException e) {
        statusLabel.setText("Failed to parse seed.");
        uninstallButton.setEnabled(true);
        return;
      }

      // Log settings info
      logger.info("Current settings:");
      try {
        logger.info(new ObjectMapper().writeValueAsString(currentSettings));
      } catch (JsonProcessingException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      Path installDir = Paths.get(currentSettings.getInstallDir());
      Path workingDir = Paths.get(DEFAULT_WORKING_DIR);
      Path tempDir = Utils.createTempDir(workingDir, TEMP_FOLDER_SUFFIX);
      Path tempLevelDir = tempDir.resolve(TEMP_LEVEL_DIR_NAME);
      Path tempPatchDir = tempDir.resolve(TEMP_PATCH_DIR_NAME);

      Installer installer = new Installer(installDir, tempDir, tempLevelDir, tempPatchDir);

      if (!installer.verifyDataExists()) {
        statusLabel.setText("Could not find data/ folder");
        uninstallButton.setEnabled(true);
        return;
      }
      if (!installer.verifyInstallDir()) {
        statusLabel.setText("Prey install directory not valid.");
        uninstallButton.setEnabled(true);
        return;
      }
      if (!installer.testInstall()) {
        statusLabel.setText("Unable to write to file. Is Prey running?");
        uninstallButton.setEnabled(true);
        return;
      }

      // Randomization

      // TODO: Use worker threads

      if (currentSettings.getCosmeticSettings()
                         .isRandomizeBodies()) {
        new BodyRandomizer(currentSettings, tempPatchDir).randomize();
      }
      if (currentSettings.getCosmeticSettings()
                         .isRandomizeVoicelines()) {
        new VoiceRandomizer(currentSettings, tempPatchDir).randomize();
      }
      if (currentSettings.getGameplaySettings()
                         .isRandomizeNeuromods()) {
        new NeuromodTreeRandomizer(currentSettings, tempPatchDir).randomize();
      } else if (currentSettings.getGameplaySettings()
                                .isUnlockAllScans()) {
        new NeuromodTreeRandomizer(currentSettings, tempPatchDir).unlockAllScans();
      }

      LevelRandomizer levelRandomizer = new LevelRandomizer(currentSettings, tempLevelDir).addFilter(
          new ItemSpawnFilter(database, currentSettings))
                                                                                          .addFilter(
                                                                                              new FlowgraphFilter(
                                                                                                  database,
                                                                                                  currentSettings))
                                                                                          .addFilter(new EnemyFilter(
                                                                                              database,
                                                                                              currentSettings));

      if (currentSettings.getGameplaySettings()
                         .isOpenStation()) {
        levelRandomizer = levelRandomizer.addFilter(new OpenStationFilter());
      }

      if (currentSettings.getGameplaySettings()
                         .isAddLootToApartment()) {
        levelRandomizer = levelRandomizer.addFilter(new MorgansApartmentFilter());
      }

      if (currentSettings.getGameplaySettings()
                         .isRandomizeStation()) {
        levelRandomizer = levelRandomizer.addFilter(new StationConnectivityFilter(currentSettings));
      }

      levelRandomizer.randomize();

      try {
        if (currentSettings.getGameplaySettings()
                           .isRandomizeLoot()) {
          new LootTableRandomizer(database, currentSettings, tempPatchDir).randomize();
        } else {
          new LootTableRandomizer(database, currentSettings, tempPatchDir).copyFile();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

      try {
        installer.install();
        statusLabel.setText("Done installing.");
      } catch (IOException | InterruptedException e) {
        statusLabel.setText("Error occurred during install.");
        e.printStackTrace();
      }
      uninstallButton.setEnabled(true);
    }

  }

  private void writeLastUsedSettingsToFile(String lastUsedSettingsPath) throws JsonGenerationException,
      JsonMappingException, IOException {
    File lastUsedSettingsFile = new File(lastUsedSettingsPath);
    lastUsedSettingsFile.createNewFile();
    new ObjectMapper().writerFor(SettingsJson.class)
                      .writeValue(lastUsedSettingsFile, currentSettings);
  }

  private class OnUninstallClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      setupLogFile();
      statusLabel.setText("Uninstalling...");
      installButton.setEnabled(false);

      Installer.uninstall(Paths.get(currentSettings.getInstallDir()), Logger.getGlobal());
      statusLabel.setText("Done uninstalling.");
      installButton.setEnabled(true);
    }
  }

  private class OnCloseClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.exit(0);
    }

  }

  private void validateSettings() {
    validateSpawnPresets(enemySpawnSettings, "enemy");
    validateSpawnPresets(enemySpawnSettings, "item");
  }

  private void validateSpawnPresets(List<SpawnPresetJson> s, String name) {
    for (SpawnPresetJson gspj : s) {
      if (gspj.getRules() == null) {
        continue;
      }
      for (int i = 0; i < gspj.getRules()
                              .size(); i++) {
        GenericRuleJson gfj = gspj.getRules()
                                  .get(i);

        if (gfj.getOutputTags() == null || gfj.getOutputWeights() == null) {
          continue;
        }

        if (gfj.getOutputWeights()
               .size() != 0 && gfj.getOutputTags()
                                  .size() != gfj.getOutputWeights()
                                                .size()) {
          logger.info(String.format(
              "Invalid filter settings for %s spawns, preset name %s, filter %d. Output tags length (%d) and output weights length (%d) are not identical.",
              name, gspj.getName(), i, gfj.getOutputTags()
                                          .size(), gfj.getOutputWeights()
                                                      .size()));
        }
      }
    }
  }
}
