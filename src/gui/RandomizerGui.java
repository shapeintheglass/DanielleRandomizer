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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import databases.EntityDatabase;
import databases.TaggedDatabase;
import installers.Installer;
import json.GenericFilterJson;
import json.GenericSpawnPresetJson;
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
import settings.GenericFilterSettings;
import settings.Settings;

/**
 * Renders the GUI interface.
 * 
 * @author Kida
 *
 */
public class RandomizerGui {

  private static final String DEFAULT_INSTALL_DIR = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";
  private static final String JSON_SETTINGS_FILE = "settings.json";
  private static final String LOG_OUTPUT_FILE = "log.txt";

  private String installDir = DEFAULT_INSTALL_DIR;

  private JFrame mainFrame;

  private JFileChooser fileChooser;

  private JLabel currentFile;

  private JCheckBox voiceLinesCheckBox;
  private JCheckBox bodiesCheckBox;
  private JCheckBox apartmentLootCheckBox;
  private JCheckBox lootTablesCheckBox;
  private JCheckBox openStationCheckBox;

  private boolean randomizeVoices = true;
  private boolean randomizeBodies = true;
  private boolean addApartmentLoot = true;
  private boolean randomizeLootTables = true;
  private boolean openUpStation = true;

  private JLabel statusLabel;
  private JButton uninstallButton;

  private SpawnPresetsJson settingsJson;

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

  private int itemSpawnIndex = 0;
  private int enemySpawnIndex = 0;

  private Random r;
  private long seed;
  private JTextField seedField;

  PrintStream logStream;
  private JButton installButton;

  private Logger logger;
  private PrintStream fileStream;

  public RandomizerGui() {
    logger = Logger.getGlobal();
    mainFrame = new JFrame("Prey Randomizer");
    mainFrame.setSize(600, 300);
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    r = new Random();
    seed = r.nextLong();

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
    fileChooser = new JFileChooser(installDir);
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    JLabel currentFileLabel = new JLabel("Prey folder location:");
    currentFileLabel.setToolTipText("Game directory, ending in Prey/");

    currentFile = new JLabel(installDir);
    currentFile.setBorder(BorderFactory.createRaisedSoftBevelBorder());
    JButton changeInstall = new JButton("Change");
    changeInstall.setActionCommand("changeInstallDir");
    changeInstall.addActionListener(new OnChangeDirClick());

    JLabel seedLabel = new JLabel("Seed:");
    seedField = new JTextField(Long.toString(seed), 15);
    seedField.setHorizontalAlignment(JLabel.RIGHT);

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
    voiceLinesCheckBox = new JCheckBox("Randomize voice lines", randomizeVoices);
    voiceLinesCheckBox.setToolTipText("Shuffles voice lines within voice actor");
    voiceLinesCheckBox.addItemListener(listener);
    bodiesCheckBox = new JCheckBox("Randomize NPC bodies", randomizeBodies);
    bodiesCheckBox.setToolTipText("Randomizes human NPC body parts");
    bodiesCheckBox.addItemListener(listener);
    apartmentLootCheckBox = new JCheckBox("Add loot to Morgan's apartment", addApartmentLoot);
    apartmentLootCheckBox.setToolTipText("Adds useful equipment in containers around Morgan's apartment");
    apartmentLootCheckBox.addItemListener(listener);
    lootTablesCheckBox = new JCheckBox("Randomize loot tables", randomizeLootTables);
    lootTablesCheckBox.setToolTipText("Randomizes contents of loot tables according to item spawn settings");
    lootTablesCheckBox.addItemListener(listener);
    openStationCheckBox = new JCheckBox("Open up Talos I (WIP)", openUpStation);
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
      settingsJson = getSettingsFromFile(JSON_SETTINGS_FILE);

      itemSpawnSettings = settingsJson.getItemSpawnSettings();
      itemSpawnPanel.setRadioLabels(itemSpawnSettings);

      enemySpawnSettings = settingsJson.getEnemySpawnSettings();
      enemySpawnPanel.setRadioLabels(enemySpawnSettings);

      validateSettings();
    } catch (Exception e) {
      statusLabel.setText("Error occurred while parsing " + JSON_SETTINGS_FILE);
      JOptionPane.showMessageDialog(mainFrame,
          String.format("An error occurred while parsing %s: %s", JSON_SETTINGS_FILE, e.getMessage()));
    }
  }

  private void setupButtonsPanel() {
    statusLabel = new JLabel("", JLabel.LEFT);
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
      try {
        installDir = fileChooser.getSelectedFile()
                                .getCanonicalPath();
      } catch (IOException e1) {
        // TODO Auto-generated catch block
        installDir = "Error occurred while parsing install directory";
      }
      currentFile.setText(installDir);
    }

  }

  private class OnCheckBoxClick implements ItemListener {

    @Override
    public void itemStateChanged(ItemEvent e) {
      Object source = e.getItemSelectable();

      if (source == voiceLinesCheckBox) {
        randomizeVoices = !randomizeVoices;
      } else if (source == bodiesCheckBox) {
        randomizeBodies = !randomizeBodies;
      } else if (source == apartmentLootCheckBox) {
        addApartmentLoot = !addApartmentLoot;
      } else if (source == lootTablesCheckBox) {
        randomizeLootTables = !randomizeLootTables;
      } else if (source == openStationCheckBox) {
        openUpStation = !openUpStation;
      }
    }

  }

  private class OnRadioClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String[] tokens = e.getActionCommand()
                         .split("_");
      switch (tokens[0]) {
        case "item":
          itemSpawnIndex = Integer.parseInt(tokens[1]);
          break;
        case "enemy":
          enemySpawnIndex = Integer.parseInt(tokens[1]);
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

    @Override
    public void actionPerformed(ActionEvent arg0) {
      setupLogFile();

      uninstallButton.setEnabled(false);
      statusLabel.setText("Installing...");

      // Validation checks

      try {
        seed = Long.parseLong(seedField.getText());
      } catch (NumberFormatException e) {
        statusLabel.setText("Failed to parse seed.");
        uninstallButton.setEnabled(true);
        return;
      }

      GenericSpawnPresetJson itemSettingsJson = itemSpawnPanel.getSettingsForId(itemSpawnIndex);
      GenericFilterSettings itemSpawnSettings = new GenericFilterSettings(r, itemSettingsJson);
      GenericSpawnPresetJson enemySettingsJson = enemySpawnPanel.getSettingsForId(enemySpawnIndex);
      GenericFilterSettings enemySettings = new GenericFilterSettings(r, enemySettingsJson);

      TaggedDatabase database = EntityDatabase.getInstance();
      Settings settings = new Settings.Builder().setInstallDir(Paths.get(installDir))
                                                .setItemSpawnSettings(itemSpawnSettings)
                                                .setEnemySettings(enemySettings)
                                                .setRand(r)
                                                .setSeed(seed)
                                                .build();

      // Log settings info
      logger.info(String.format("Install location: %s", installDir));
      logger.info(String.format("Seed: %d", seed));
      logger.info(String.format("Randomize voice lines: %b", randomizeVoices));
      logger.info(String.format("Randomize bodies: %b", randomizeBodies));
      logger.info(String.format("Apartment loot: %b", addApartmentLoot));
      logger.info(String.format("Randomize loot tables: %b", randomizeLootTables));
      logger.info(String.format("Open Talos I: %b", openUpStation));
      logger.info(String.format("Item spawn settings: %s", itemSettingsJson));
      logger.info(String.format("Enemy spawn settings: %s", enemySettingsJson));

      Installer installer = new Installer(settings);

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

      if (randomizeBodies) {
        new BodyRandomizer(settings).randomize();
      }
      if (randomizeVoices) {
        new VoiceRandomizer(settings).randomize();
      }

      LevelRandomizer levelRandomizer = new LevelRandomizer(settings).addFilter(new ItemSpawnFilter(database, settings))
                                                                     .addFilter(new FlowgraphFilter(database, settings))
                                                                     .addFilter(new EnemyFilter(database, settings));

      if (openUpStation) {
        levelRandomizer = levelRandomizer.addFilter(new OpenStationFilter(settings));
      }

      if (addApartmentLoot) {
        levelRandomizer = levelRandomizer.addFilter(new MorgansApartmentFilter(settings));
      }

      levelRandomizer.randomize();

      if (randomizeLootTables) {
        new LootTableRandomizer(database, settings).randomize();
      } else {
        try {
          new LootTableRandomizer(database, settings).copyFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
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
    setupLogFile();
    File settingsFile = new File(settingsPath);
    if (!settingsFile.exists()) {
      throw new FileNotFoundException("Could not find settings.json file");
    }
    SpawnPresetsJson settings = new ObjectMapper().readerFor(SpawnPresetsJson.class)
                                                  .readValue(settingsFile, SpawnPresetsJson.class);
    return settings;
  }

  private class OnUninstallClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      setupLogFile();
      statusLabel.setText("Uninstalling...");
      installButton.setEnabled(false);

      Installer.uninstall(Paths.get(installDir), Logger.getGlobal());
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
      if (gspj.getFilters() == null) {
        continue;
      }
      for (int i = 0; i < gspj.getFilters().length; i++) {
        GenericFilterJson gfj = gspj.getFilters()[i];

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
      if (gspj.getFilters() == null) {
        continue;
      }
      for (int i = 0; i < gspj.getFilters().length; i++) {
        GenericFilterJson gfj = gspj.getFilters()[i];

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
