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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import installers.Installer;
import json.CosmeticSettingsJson;
import json.GameplaySettingsJson;
import json.GenericRuleJson;
import json.GenericSpawnPresetJson;
import json.SettingsJson;
import json.SpawnPresetsJson;
import randomizers.cosmetic.BodyRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.LevelRandomizer;
import randomizers.gameplay.LootTableRandomizer;
import randomizers.gameplay.level.filters.EnemyFilter;
import randomizers.gameplay.level.filters.FlowgraphFilter;
import randomizers.gameplay.level.filters.ItemSpawnFilter;
import randomizers.gameplay.level.filters.MorgansApartmentFilter;
import randomizers.gameplay.level.filters.OpenStationFilter;
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

  private static final String DEFAULT_INSTALL_DIR = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";
  private static final String JSON_SETTINGS_FILE = "settings.json";
  private static final String LOG_OUTPUT_FILE = "log.txt";
  private static final String LAST_USED_SETTINGS_FILE = "last_used_settings.json";

  private JFrame mainFrame;

  private JLabel currentFile;
  private JFileChooser fileChooser;
  private JTextField seedField;

  private JCheckBox voiceLinesCheckBox;
  private JCheckBox bodiesCheckBox;
  private JCheckBox apartmentLootCheckBox;
  private JCheckBox lootTablesCheckBox;
  private JCheckBox openStationCheckBox;

  private JLabel statusLabel;
  private JButton installButton;
  private JButton uninstallButton;

  private SettingsJson settings;
  private SpawnPresetsJson spawnSettings;

  private JPanel mainPanel;
  private JPanel headerPanel;
  private JPanel installDirPanel;
  private JPanel optionsPanel;
  private BaseSpawnOptionsPanel<GenericSpawnPresetJson> itemSpawnPanel;
  private BaseSpawnOptionsPanel<GenericSpawnPresetJson> enemySpawnPanel;
  private JPanel otherGameplayOptionsPanel;
  private JPanel buttonsPanel;
  private GenericSpawnPresetJson[] itemSpawnSettings;
  private GenericSpawnPresetJson[] enemySpawnSettings;

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

    GenericSpawnPresetJson defaultEnemySpawnPreset = new GenericSpawnPresetJson("", "", new GenericRuleJson[0]);
    GenericSpawnPresetJson defaultItemSpawnPreset = new GenericSpawnPresetJson("", "", new GenericRuleJson[0]);

    try {
      spawnSettings = getSettingsFromFile(JSON_SETTINGS_FILE);
      defaultEnemySpawnPreset = spawnSettings.getEnemySpawnSettings()[0];
      defaultItemSpawnPreset = spawnSettings.getItemSpawnSettings()[0];
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    settings = new SettingsJson(RELEASE_VER, DEFAULT_INSTALL_DIR, new Random().nextLong(),
        new CosmeticSettingsJson(DEFAULT_RANDOMIZE_BODIES_VALUE, DEFAULT_RANDOMIZE_VOICELINES_VALUE),
        new GameplaySettingsJson(DEFAULT_RANDOMIZE_LOOT_VALUE, DEFAULT_ADD_LOOT_TO_APARTMENT_VALUE,
            DEFAULT_OPEN_STATION_VALUE, defaultEnemySpawnPreset, defaultItemSpawnPreset));
    File lastUsedSettingsFile = new File(LAST_USED_SETTINGS_FILE);
    if (lastUsedSettingsFile.exists()) {
      try {
        settings = getLastUsedSettingsFromFile(LAST_USED_SETTINGS_FILE);
      } catch (IOException e) {
        logger.info(String.format("An error occurred while parsing %s: %s. Falling back to default settings.",
            LAST_USED_SETTINGS_FILE, e.getMessage()));
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
    fileChooser = new JFileChooser(settings.getInstallDir());
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    JLabel currentFileLabel = new JLabel("Prey folder location:");
    currentFileLabel.setToolTipText("Game directory, ending in Prey/");

    currentFile = new JLabel(settings.getInstallDir());
    currentFile.setBorder(BorderFactory.createRaisedSoftBevelBorder());
    JButton changeInstall = new JButton("Change");
    changeInstall.setActionCommand("changeInstallDir");
    changeInstall.addActionListener(new OnChangeDirClick());

    JLabel seedLabel = new JLabel("Seed:");
    seedField = new JTextField(Long.toString(settings.getSeed()), 15);
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
    voiceLinesCheckBox = new JCheckBox("Randomize voice lines", settings.getCosmeticSettings()
                                                                        .isRandomizeVoicelines());
    voiceLinesCheckBox.setToolTipText("Shuffles voice lines within voice actor");
    voiceLinesCheckBox.addItemListener(listener);
    bodiesCheckBox = new JCheckBox("Randomize NPC bodies", settings.getCosmeticSettings()
                                                                   .isRandomizeBodies());
    bodiesCheckBox.setToolTipText("Randomizes human NPC body parts");
    bodiesCheckBox.addItemListener(listener);
    apartmentLootCheckBox = new JCheckBox("Add loot to Morgan's apartment", settings.getGameplaySettings()
                                                                                    .isAddLootToApartment());
    apartmentLootCheckBox.setToolTipText("Adds useful equipment in containers around Morgan's apartment");
    apartmentLootCheckBox.addItemListener(listener);
    lootTablesCheckBox = new JCheckBox("Randomize loot tables", settings.getGameplaySettings()
                                                                        .isRandomizeLoot());
    lootTablesCheckBox.setToolTipText("Randomizes contents of loot tables according to item spawn settings");
    lootTablesCheckBox.addItemListener(listener);
    openStationCheckBox = new JCheckBox("Open up Talos I (WIP)", settings.getGameplaySettings()
                                                                         .isOpenStation());
    openStationCheckBox.setToolTipText("Unlocks doors around Talos I to make traversal easier");
    openStationCheckBox.addItemListener(listener);
    otherGameplayOptionsPanel.add(voiceLinesCheckBox);
    otherGameplayOptionsPanel.add(bodiesCheckBox);
    otherGameplayOptionsPanel.add(apartmentLootCheckBox);
    otherGameplayOptionsPanel.add(lootTablesCheckBox);
    otherGameplayOptionsPanel.add(openStationCheckBox);

    optionsPanel.setLayout(new GridLayout(1, 3));
    optionsPanel.add(itemSpawnPanel);
    optionsPanel.add(enemySpawnPanel);
    optionsPanel.add(otherGameplayOptionsPanel);
  }

  private void updateOptionsPanel() {
    try {
      spawnSettings = getSettingsFromFile(JSON_SETTINGS_FILE);

      itemSpawnSettings = spawnSettings.getItemSpawnSettings();
      itemSpawnPanel.setRadioLabels(itemSpawnSettings, settings.getGameplaySettings()
                                                               .getItemSpawnSettings()
                                                               .getName());

      enemySpawnSettings = spawnSettings.getEnemySpawnSettings();
      enemySpawnPanel.setRadioLabels(enemySpawnSettings, settings.getGameplaySettings()
                                                                 .getEnemySpawnSettings()
                                                                 .getName());

      validateSettings();
    } catch (Exception e) {
      statusLabel.setText("Error occurred while parsing " + JSON_SETTINGS_FILE);
      JOptionPane.showMessageDialog(mainFrame,
          String.format("An error occurred while parsing %s: %s", JSON_SETTINGS_FILE, e.getMessage()));
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
          writeLastUsedSettingsToFile(LAST_USED_SETTINGS_FILE);
          statusLabel.setText("Settings saved to " + LAST_USED_SETTINGS_FILE);
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
        settings.setInstallDir(installDir);
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
        settings.getCosmeticSettings()
                .toggleRandomizeVoicelines();
      } else if (source == bodiesCheckBox) {
        settings.getCosmeticSettings()
                .toggleRandomizeBodies();
      } else if (source == apartmentLootCheckBox) {
        settings.getGameplaySettings()
                .toggleAddLootToApartment();
      } else if (source == lootTablesCheckBox) {
        settings.getGameplaySettings()
                .toggleRandomizeLoot();
      } else if (source == openStationCheckBox) {
        settings.getGameplaySettings()
                .toggleOpenStation();
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
          settings.getGameplaySettings()
                  .setItemSpawnSettings(itemSpawnPanel.getSettingsByName(tokens[1]));
          break;
        case ENEMY_SPAWN_OPTIONS_PREFIX:
          settings.getGameplaySettings()
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
        settings.setSeed(seed);
      } catch (NumberFormatException e) {
        statusLabel.setText("Failed to parse seed.");
        uninstallButton.setEnabled(true);
        return;
      }

      // Log settings info
      logger.info("Current settings:");
      try {
        logger.info(new ObjectMapper().writeValueAsString(settings));
      } catch (JsonProcessingException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      Path installDir = Paths.get(settings.getInstallDir());
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

      if (settings.getCosmeticSettings()
                  .isRandomizeBodies()) {
        new BodyRandomizer(settings, tempPatchDir).randomize();
      }
      if (settings.getCosmeticSettings()
                  .isRandomizeVoicelines()) {
        new VoiceRandomizer(settings, tempPatchDir).randomize();
      }

      LevelRandomizer levelRandomizer = new LevelRandomizer(settings, tempLevelDir)
                                                                                   .addFilter(new ItemSpawnFilter(
                                                                                       database, settings))
                                                                                   .addFilter(new FlowgraphFilter(
                                                                                       database, settings))
                                                                                   .addFilter(new EnemyFilter(database,
                                                                                       settings));

      if (settings.getGameplaySettings()
                  .isOpenStation()) {
        levelRandomizer = levelRandomizer.addFilter(new OpenStationFilter());
      }

      if (settings.getGameplaySettings()
                  .isAddLootToApartment()) {
        levelRandomizer = levelRandomizer.addFilter(new MorgansApartmentFilter());
      }

      levelRandomizer.randomize();

      try {
        if (settings.getGameplaySettings()
                    .isRandomizeLoot()) {
          new LootTableRandomizer(database, settings, tempPatchDir).randomize();
        } else {
          new LootTableRandomizer(database, settings, tempPatchDir).copyFile();
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

  private SpawnPresetsJson getSettingsFromFile(String settingsPath)
      throws JsonParseException, JsonMappingException, IOException {
    File settingsFile = new File(settingsPath);
    if (!settingsFile.exists()) {
      throw new FileNotFoundException("Could not find settings.json file");
    }
    SpawnPresetsJson settings = new ObjectMapper().readerFor(SpawnPresetsJson.class)
                                                  .readValue(settingsFile, SpawnPresetsJson.class);
    return settings;
  }

  private SettingsJson getLastUsedSettingsFromFile(String lastUsedSettingsPath) throws IOException {
    File settingsFile = new File(lastUsedSettingsPath);
    if (!settingsFile.exists()) {
      throw new FileNotFoundException("Could not find settings.json file");
    }
    SettingsJson settings = new ObjectMapper().readerFor(SettingsJson.class)
                                              .readValue(settingsFile, SettingsJson.class);
    return settings;
  }

  private void writeLastUsedSettingsToFile(String lastUsedSettingsPath)
      throws JsonGenerationException, JsonMappingException, IOException {
    File lastUsedSettingsFile = new File(lastUsedSettingsPath);
    lastUsedSettingsFile.createNewFile();
    new ObjectMapper().writerFor(SettingsJson.class)
                      .writeValue(lastUsedSettingsFile, settings);
  }

  private class OnUninstallClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      setupLogFile();
      statusLabel.setText("Uninstalling...");
      installButton.setEnabled(false);

      Installer.uninstall(Paths.get(settings.getInstallDir()), Logger.getGlobal());
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
    for (GenericSpawnPresetJson gspj : enemySpawnSettings) {
      if (gspj.getRules() == null) {
        continue;
      }
      for (int i = 0; i < gspj.getRules().length; i++) {
        GenericRuleJson gfj = gspj.getRules()[i];

        if (gfj.getOutputTags() == null || gfj.getOutputWeights() == null) {
          continue;
        }

        if (gfj.getOutputTags().length != gfj.getOutputWeights().length) {
          logger.info(String.format(
              "Invalid filter settings for enemy spawns, preset name %s, filter %d. Output tags length (%d) and output weights length (%d) are not identical.",
              gspj.getName(), i, gfj.getOutputTags().length, gfj.getOutputWeights().length));
        }
      }
    }

    for (GenericSpawnPresetJson gspj : itemSpawnSettings) {
      if (gspj.getRules() == null) {
        continue;
      }
      for (int i = 0; i < gspj.getRules().length; i++) {
        GenericRuleJson gfj = gspj.getRules()[i];

        if (gfj.getOutputTags() == null || gfj.getOutputWeights() == null) {
          continue;
        }

        if (gfj.getOutputTags().length != gfj.getOutputWeights().length) {
          logger.info(String.format(
              "Invalid filter settings for item spawns, preset name %s, filter %d. Output tags length (%d) and output weights length (%d) are not identical.",
              gspj.getName(), i, gfj.getOutputTags().length, gfj.getOutputWeights().length));
        }
      }
    }
  }
}
