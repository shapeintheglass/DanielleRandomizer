package gui;

import java.awt.Component;
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
import java.util.Map;
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
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import installers.Installer;
import json.AllPresetsJson;
import json.CosmeticSettingsJson;
import json.GameplaySettingsJson;
import json.GenericRuleJson;
import json.HasOptions;
import json.SettingsJson;
import json.SpawnPresetJson;
import randomizers.cosmetic.BodyRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.LevelRandomizer;
import randomizers.gameplay.LootTableRandomizer;
import randomizers.gameplay.NeuromodTreeRandomizer;
import randomizers.gameplay.WorkstationRandomizer;
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

  private static final String DEFAULT_INSTALL_DIR =
      "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  private static final String LOG_OUTPUT_FILE = "log.txt";

  private static final String SAVED_SETTINGS_FILE = "saved_settings.json";
  private static final String ALL_PRESETS_FILE = "settings.json";

  private static final ImmutableList<String> STARTING_CHECKBOXES = ImmutableList
      .of(GameplaySettingsJson.START_ON_2ND_DAY, GameplaySettingsJson.ADD_LOOT_TO_APARTMENT);

  private static final ImmutableList<String> ITEMS_CHECKBOXES =
      ImmutableList.of(GameplaySettingsJson.RANDOMIZE_LOOT);

  private static final ImmutableList<String> CONNECTIVITY_CHECKBOXES =
      ImmutableList.of(GameplaySettingsJson.OPEN_STATION, GameplaySettingsJson.RANDOMIZE_STATION);

  private static final ImmutableList<String> NEUROMODS_CHECKBOXES = ImmutableList
      .of(GameplaySettingsJson.UNLOCK_ALL_SCANS, GameplaySettingsJson.RANDOMIZE_NEUROMODS);

  private static final ImmutableList<String> COSMETIC_CHECKBOXES = ImmutableList
      .of(CosmeticSettingsJson.RANDOMIZE_BODIES, CosmeticSettingsJson.RANDOMIZE_VOICELINES);

  private JFrame mainFrame;

  private JLabel currentFile;
  private JFileChooser fileChooser;
  private JTextField seedField;

  private BiMap<String, JCheckBox> checkBoxHolder;

  private JLabel statusLabel;
  private JButton installButton;
  private JButton uninstallButton;

  private SettingsJson currentSettings;
  private AllPresetsJson allPresets;

  private JPanel mainPanel;
  private JPanel topPanel;
  private JPanel optionsPanel;
  private BaseRadioOptionsPanel<SpawnPresetJson> itemSpawnPanel;
  private BaseRadioOptionsPanel<SpawnPresetJson> enemySpawnPanel;
  private JPanel buttonsPanel;
  private List<SpawnPresetJson> itemSpawnSettings;
  private List<SpawnPresetJson> enemySpawnSettings;

  private Logger logger;
  private PrintStream fileStream;

  private TaggedDatabase database;

  public RandomizerGui() {
    setupLogFile();

    database = EntityDatabase.getInstance();
    checkBoxHolder = HashBiMap.create();

    logger = Logger.getGlobal();
    mainFrame = new JFrame("Prey Randomizer");
    mainFrame.setSize(600, 300);
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    List<String> gameTokenValues = new ArrayList<>();

    currentSettings = new SettingsJson(RELEASE_VER, DEFAULT_INSTALL_DIR, getNewSeed(),
        new CosmeticSettingsJson(), new GameplaySettingsJson());

    try {
      allPresets = new AllPresetsJson(ALL_PRESETS_FILE);
      currentSettings.getGameplaySettings()
          .setEnemySpawnSettings(allPresets.getEnemySpawnSettings().get(0));
      currentSettings.getGameplaySettings()
          .setItemSpawnSettings(allPresets.getItemSpawnSettings().get(0));
      currentSettings.getGameplaySettings().setGameTokenValues(allPresets.getGameTokenValues());
    } catch (IOException e1) {
      logger.warning("failed to parse presets file " + ALL_PRESETS_FILE);
      e1.printStackTrace();
    }

    File lastUsedSettingsFile = new File(SAVED_SETTINGS_FILE);
    if (lastUsedSettingsFile.exists()) {
      try {
        currentSettings = new SettingsJson(SAVED_SETTINGS_FILE);
        currentSettings.getGameplaySettings().setGameTokenValues(gameTokenValues);
      } catch (IOException e) {
        logger.info(String.format(
            "An error occurred while parsing %s: %s. Falling back to default settings.",
            SAVED_SETTINGS_FILE, e.getMessage()));
        e.printStackTrace();
      }
    }

    mainPanel = new JPanel();
    topPanel = new JPanel();
    optionsPanel = new JPanel();
    buttonsPanel = new JPanel();
    statusLabel = new JLabel();

    mainFrame.add(mainPanel);

    setupMainPanel();
    setupTopPanel();
    setupOptionsPanel();
    updateOptionsPanel();
    setupButtonsPanel();

    mainFrame.pack();

  }

  private long getNewSeed() {
    return new Random().nextLong();
  }

  private void setupMainPanel() {
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    mainPanel.add(topPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
    mainPanel.add(optionsPanel);
    mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));

    mainPanel.add(buttonsPanel);
  }

  private void setupTopPanel() {
    fileChooser = new JFileChooser(currentSettings.getInstallDir());
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    JLabel currentFileLabel = new JLabel("Prey folder location:");
    currentFileLabel.setToolTipText("Game directory, ending in Prey/");

    currentFile = new JLabel(currentSettings.getInstallDir());
    currentFile.setBorder(BorderFactory.createLoweredBevelBorder());
    JButton changeInstall = new JButton("Change");
    changeInstall.setActionCommand("changeInstallDir");
    changeInstall.addActionListener(new OnChangeDirClick());

    JPanel installDirPanel = new JPanel();
    installDirPanel.setLayout(new FlowLayout());
    installDirPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);
    installDirPanel.add(currentFileLabel);
    installDirPanel.add(currentFile);
    installDirPanel.add(changeInstall);
    installDirPanel.setBorder(BorderFactory.createEtchedBorder());

    JLabel seedLabel = new JLabel("Seed:");
    seedField = new JTextField(15);
    seedField.setHorizontalAlignment(JLabel.RIGHT);
    updateSeedField();

    JButton newSeedButton = new JButton("New seed");
    newSeedButton.setToolTipText("Generate a new seed");
    newSeedButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        currentSettings.setSeed(getNewSeed());
        updateSeedField();
      }
    });
    JPanel seedPanel = new JPanel();
    seedPanel.add(seedLabel);
    seedPanel.add(seedField);
    seedPanel.add(newSeedButton);
    seedPanel.setBorder(BorderFactory.createEtchedBorder());

    topPanel.add(installDirPanel);
    topPanel.add(seedPanel);
  }

  private void updateSeedField() {
    seedField.setText(Long.toString(currentSettings.getSeed()));
  }

  private void setupOptionsPanel() {
    itemSpawnPanel = new BaseRadioOptionsPanel<>("Item spawn presets", "item", new OnRadioClick());
    enemySpawnPanel = new BaseRadioOptionsPanel<>("NPC spawn presets", "enemy", new OnRadioClick());
    JPanel otherGameplayOptionsPanel = new JPanel();
    otherGameplayOptionsPanel.setLayout(new BoxLayout(otherGameplayOptionsPanel, BoxLayout.Y_AXIS));
    otherGameplayOptionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    ItemListener cosmeticListener = new CosmeticCheckboxListener();
    JPanel cosmeticCheckboxes = createCheckboxPanel("Cosmetic Options", COSMETIC_CHECKBOXES,
        CosmeticSettingsJson.ALL_OPTIONS, cosmeticListener, currentSettings.getCosmeticSettings());

    ItemListener gameplayListener = new GameplayCheckboxListener();
    JPanel apartmentCheckboxes = createCheckboxPanel("Apartment Intro Options", STARTING_CHECKBOXES,
        GameplaySettingsJson.ALL_OPTIONS, gameplayListener, currentSettings.getGameplaySettings());
    JPanel neuromodsCheckboxes = createCheckboxPanel("Neuromod Options", NEUROMODS_CHECKBOXES,
        GameplaySettingsJson.ALL_OPTIONS, gameplayListener, currentSettings.getGameplaySettings());
    JPanel connectivityCheckboxes = createCheckboxPanel("Connectivity Options",
        CONNECTIVITY_CHECKBOXES, GameplaySettingsJson.ALL_OPTIONS, gameplayListener,
        currentSettings.getGameplaySettings());
    JPanel itemsCheckboxes = createCheckboxPanel("Item Options", ITEMS_CHECKBOXES,
        GameplaySettingsJson.ALL_OPTIONS, gameplayListener, currentSettings.getGameplaySettings());

    otherGameplayOptionsPanel.add(cosmeticCheckboxes);
    otherGameplayOptionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    otherGameplayOptionsPanel.add(apartmentCheckboxes);
    otherGameplayOptionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    otherGameplayOptionsPanel.add(itemsCheckboxes);
    otherGameplayOptionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    otherGameplayOptionsPanel.add(neuromodsCheckboxes);
    otherGameplayOptionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
    otherGameplayOptionsPanel.add(connectivityCheckboxes);

    optionsPanel.setLayout(new GridLayout(0, 3));
    optionsPanel.setAlignmentY(Component.TOP_ALIGNMENT);
    optionsPanel.setBorder(BorderFactory.createEtchedBorder());
    optionsPanel.add(itemSpawnPanel);
    optionsPanel.add(enemySpawnPanel);
    optionsPanel.add(otherGameplayOptionsPanel);
  }

  private JPanel createCheckboxPanel(String sectionName, List<String> checkboxes,
      Map<String, BaseCheckbox> checkboxInfo, ItemListener listener, HasOptions optionsGetter) {
    JPanel checkboxSection = new JPanel();
    checkboxSection.setLayout(new BoxLayout(checkboxSection, BoxLayout.Y_AXIS));
    checkboxSection.setAlignmentX(Component.LEFT_ALIGNMENT);
    checkboxSection.setAlignmentY(Component.TOP_ALIGNMENT);
    checkboxSection.add(new JLabel(sectionName));
    checkboxSection.add(Box.createRigidArea(new Dimension(0, 10)));

    for (String s : checkboxes) {
      BaseCheckbox info = checkboxInfo.get(s);
      JCheckBox checkbox = new JCheckBox(info.getName());
      checkbox.setSelected(optionsGetter.getOption(s));
      checkbox.setToolTipText(info.getDesc());
      checkbox.addItemListener(listener);
      checkBoxHolder.put(s, checkbox);
      checkboxSection.add(checkbox);
    }
    return checkboxSection;
  }

  private void updateOptionsPanel() {
    try {
      allPresets = new AllPresetsJson(ALL_PRESETS_FILE);

      itemSpawnSettings = allPresets.getItemSpawnSettings();
      itemSpawnPanel.setRadioLabels(itemSpawnSettings,
          currentSettings.getGameplaySettings().getItemSpawnSettings().getName());

      enemySpawnSettings = allPresets.getEnemySpawnSettings();
      enemySpawnPanel.setRadioLabels(enemySpawnSettings,
          currentSettings.getGameplaySettings().getEnemySpawnSettings().getName());

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
      JOptionPane.showMessageDialog(mainFrame, String
          .format("An error occurred while parsing %s: %s", ALL_PRESETS_FILE, e.getMessage()));
    }
  }

  private void setupButtonsPanel() {
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
    saveButton.setToolTipText(
        "Saves the current settings so that they'll be the default next time you load this GUI.");
    JButton refreshSettings = new JButton("Refresh options");
    refreshSettings.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateOptionsPanel();
        // Reset indeces to 0
        itemSpawnPanel.setCurrentIndex(0);
        enemySpawnPanel.setCurrentIndex(0);
        currentSettings.getGameplaySettings().setItemSpawnSettings(itemSpawnSettings.get(0));
        currentSettings.getGameplaySettings().setEnemySpawnSettings(enemySpawnSettings.get(0));
        statusLabel.setText("Options refreshed.");
        mainFrame.revalidate();
      }
    });
    refreshSettings
        .setToolTipText("Updates item/NPC spawn options if you modified " + ALL_PRESETS_FILE);
    installButton = new JButton("Install");
    installButton.setActionCommand("install");
    installButton.addActionListener(new OnInstallClick());
    installButton
        .setToolTipText("Randomizes according to above settings and installs in game directory");
    uninstallButton = new JButton("Uninstall");
    uninstallButton.setActionCommand("uninstall");
    uninstallButton.addActionListener(new OnUninstallClick());
    uninstallButton.setToolTipText(
        "Removes any mods added by this randomizer, restoring game files to previous state");
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
        installDir = fileChooser.getSelectedFile().getCanonicalPath();
        currentSettings.setInstallDir(installDir);
      } catch (IOException e1) {
        logger.warning("Unable to get canonical path for Prey install dir: " + e1.getMessage());
        statusLabel.setText("Error occurred when parsing Prey install dir");
      }
      currentFile.setText(installDir);
    }

  }

  private class CosmeticCheckboxListener implements ItemListener {

    @Override
    public void itemStateChanged(ItemEvent e) {
      Object source = e.getItemSelectable();
      String name = checkBoxHolder.inverse().get(source);
      currentSettings.getCosmeticSettings().toggleOption(name);
    }
  }

  private class GameplayCheckboxListener implements ItemListener {

    @Override
    public void itemStateChanged(ItemEvent e) {
      Object source = e.getItemSelectable();
      String name = checkBoxHolder.inverse().get(source);
      currentSettings.getGameplaySettings().toggleOption(name);
    }
  }

  private class OnRadioClick implements ActionListener {
    private static final String ENEMY_SPAWN_OPTIONS_PREFIX = "enemy";
    private static final String ITEM_SPAWN_OPTIONS_PREFIX = "item";

    @Override
    public void actionPerformed(ActionEvent e) {
      String[] tokens = e.getActionCommand().split(BaseRadioOptionsPanel.DELIMITER);
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

      if (currentSettings.getCosmeticSettings().getOption(CosmeticSettingsJson.RANDOMIZE_BODIES)) {
        new BodyRandomizer(currentSettings, tempPatchDir).randomize();
      }
      if (currentSettings.getCosmeticSettings()
          .getOption(CosmeticSettingsJson.RANDOMIZE_VOICELINES)) {
        new VoiceRandomizer(currentSettings, tempPatchDir).randomize();
      }
      if (currentSettings.getGameplaySettings()
          .getOption(GameplaySettingsJson.RANDOMIZE_NEUROMODS)) {
        new NeuromodTreeRandomizer(currentSettings, tempPatchDir).randomize();
      } else if (currentSettings.getGameplaySettings()
          .getOption(GameplaySettingsJson.UNLOCK_ALL_SCANS)) {
        new NeuromodTreeRandomizer(currentSettings, tempPatchDir).unlockAllScans();
      }

      LevelRandomizer levelRandomizer = new LevelRandomizer(currentSettings, tempLevelDir)
          .addFilter(new ItemSpawnFilter(database, currentSettings))
          .addFilter(new FlowgraphFilter(database, currentSettings))
          .addFilter(new EnemyFilter(database, currentSettings));

      if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.OPEN_STATION)
          || currentSettings.getGameplaySettings()
              .getOption(GameplaySettingsJson.RANDOMIZE_STATION)) {
        new WorkstationRandomizer(currentSettings, tempPatchDir).randomize();
      }

      if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.OPEN_STATION)) {
        levelRandomizer = levelRandomizer.addFilter(new OpenStationFilter());
      }

      if (currentSettings.getGameplaySettings()
          .getOption(GameplaySettingsJson.ADD_LOOT_TO_APARTMENT)) {
        levelRandomizer = levelRandomizer.addFilter(new MorgansApartmentFilter());
      }

      if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_STATION)) {
        levelRandomizer = levelRandomizer.addFilter(new StationConnectivityFilter(currentSettings));
      }

      if (currentSettings.getGameplaySettings().getGameTokenValuesAsMap() != null) {
        levelRandomizer = levelRandomizer
            .addGameTokenValues(currentSettings.getGameplaySettings().getGameTokenValuesAsMap());
      }

      levelRandomizer.randomize();

      try {
        if (currentSettings.getGameplaySettings().getOption(GameplaySettingsJson.RANDOMIZE_LOOT)) {
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

  private void writeLastUsedSettingsToFile(String lastUsedSettingsPath)
      throws JsonGenerationException, JsonMappingException, IOException {
    File lastUsedSettingsFile = new File(lastUsedSettingsPath);
    lastUsedSettingsFile.createNewFile();
    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writerFor(SettingsJson.class).writeValue(lastUsedSettingsFile, currentSettings);
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
      for (int i = 0; i < gspj.getRules().size(); i++) {
        GenericRuleJson gfj = gspj.getRules().get(i);

        if (gfj.getOutputTags() == null || gfj.getOutputWeights() == null) {
          continue;
        }

        if (gfj.getOutputWeights().size() != 0
            && gfj.getOutputTags().size() != gfj.getOutputWeights().size()) {
          logger.info(String.format(
              "Invalid filter settings for %s spawns, preset name %s, filter %d. Output tags length (%d) and output weights length (%d) are not identical.",
              name, gspj.getName(), i, gfj.getOutputTags().size(), gfj.getOutputWeights().size()));
        }
      }
    }
  }
}
