package gui;

import installers.Installer;

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
import java.nio.file.Paths;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import json.GenericSpawnSettingsJson;
import json.SettingsJson;
import randomizers.cosmetic.BodyRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.LevelRandomizer;
import randomizers.gameplay.LootTableRandomizer;
import randomizers.gameplay.level.filters.EnemyFilter;
import randomizers.gameplay.level.filters.FlowgraphFilter;
import randomizers.gameplay.level.filters.ItemSpawnFilter;
import randomizers.gameplay.level.filters.MorgansApartmentFilter;
import settings.GenericFilterSettings;
import settings.Settings;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import databases.EntityDatabase;
import databases.TaggedDatabase;

/**
 * Renders the GUI interface.
 * 
 * @author Kida
 *
 */
public class RandomizerGui {

  private static final String DEFAULT_INSTALL_DIR = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";
  private static final String JSON_SETTINGS_FILE = "settings.json";

  private String installDir = DEFAULT_INSTALL_DIR;

  private Settings settings = null;
  private TaggedDatabase database = null;
  private Installer installer = null;

  private JFrame mainFrame;

  private JFileChooser fileChooser;

  private JLabel currentFile;

  private JCheckBox voiceLinesCheckBox;
  private JCheckBox bodiesCheckBox;

  private boolean randomizeVoices;
  private boolean randomizeBodies;

  private JLabel statusLabel;
  private JButton uninstallButton;

  private SettingsJson settingsJson;

  private JPanel mainPanel;
  private JPanel headerPanel;
  private JPanel installDirPanel;
  private JPanel cosmeticsPanel;
  private JPanel gameplayPanel;
  private BaseSpawnOptionsPanel itemSpawnPanel;
  private BaseSpawnOptionsPanel enemySpawnPanel;
  private JPanel buttonsPanel;
  private GenericSpawnSettingsJson[] itemSpawnSettings;
  private GenericSpawnSettingsJson[] enemySpawnSettings;

  private int itemSpawnIndex = 0;
  private int enemySpawnIndex = 0;

  private Random r;
  private long seed;
  private JTextField seedField;

  public RandomizerGui() {
    r = new Random();
    seed = r.nextLong();

    mainFrame = new JFrame("Prey Randomizer");
    mainFrame.setSize(600, 300);
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });
    mainPanel = new JPanel();
    headerPanel = new JPanel();
    installDirPanel = new JPanel();
    cosmeticsPanel = new JPanel();
    gameplayPanel = new JPanel();
    itemSpawnPanel = new BaseSpawnOptionsPanel("Item spawn options", "item",
        new OnRadioClick());
    enemySpawnPanel = new BaseSpawnOptionsPanel("Enemy spawn options", "enemy",
        new OnRadioClick());
    gameplayPanel.setLayout(new GridLayout(1, 2));
    gameplayPanel.add(itemSpawnPanel);
    gameplayPanel.add(enemySpawnPanel);
    buttonsPanel = new JPanel();
    mainFrame.add(mainPanel);

    setupMainPanel();
    setupHeaderPanel();
    setupInstallDirPanel();
    setupCosmeticsPanel();
    updateGameplayPanel();
    setupButtonsPanel();

    mainFrame.pack();
  }

  private void setupMainPanel() {
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    mainPanel.add(headerPanel);
    mainPanel.add(installDirPanel);
    mainPanel.add(new JLabel("Cosmetics"));
    mainPanel.add(cosmeticsPanel);
    mainPanel.add(new JLabel("Gameplay"));
    mainPanel.add(gameplayPanel);
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

  private void setupCosmeticsPanel() {
    voiceLinesCheckBox = new JCheckBox("Randomize voice lines", false);
    voiceLinesCheckBox.addItemListener(new OnCheckBoxClick());
    bodiesCheckBox = new JCheckBox("Randomize NPC bodies", false);
    bodiesCheckBox.addItemListener(new OnCheckBoxClick());

    cosmeticsPanel.setLayout(new FlowLayout());
    cosmeticsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    cosmeticsPanel.add(voiceLinesCheckBox);
    cosmeticsPanel.add(bodiesCheckBox);
  }

  private void updateGameplayPanel() {
    try {
      settingsJson = getSettingsFromFile(JSON_SETTINGS_FILE);

      itemSpawnSettings = settingsJson.getItemSpawnSettings();
      itemSpawnPanel.setRadioLabels(itemSpawnSettings);

      enemySpawnSettings = settingsJson.getEnemySpawnSettings();
      enemySpawnPanel.setRadioLabels(enemySpawnSettings);
    } catch (Exception e) {
      statusLabel.setText("Error occurred while parsing " + JSON_SETTINGS_FILE);
      e.printStackTrace();
    }
  }

  private void setupButtonsPanel() {
    statusLabel = new JLabel("", JLabel.LEFT);
    JButton refreshSettings = new JButton("Refresh options");
    refreshSettings.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        updateGameplayPanel();
        statusLabel.setText("Options refreshed.");
        mainFrame.revalidate();
      }
    });
    JButton installButton = new JButton("Install");
    installButton.setActionCommand("install");
    installButton.addActionListener(new OnInstallClick());
    uninstallButton = new JButton("Uninstall");
    uninstallButton.setActionCommand("uninstall");
    uninstallButton.addActionListener(new OnUninstallClick());
    JButton closeButton = new JButton("Close");
    closeButton.setActionCommand("close");
    closeButton.addActionListener(new OnCloseClick());
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
        installDir = fileChooser.getSelectedFile().getCanonicalPath();
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
      }
    }

  }

  private class OnRadioClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      String[] tokens = e.getActionCommand().split("_");
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

  private class OnInstallClick implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      uninstallButton.setEnabled(false);
      statusLabel.setText("Installing...");

      // TODO: Add check to assert that Prey is not currently running.

      try {
        seed = Long.parseLong(seedField.getText());
        r.setSeed(seed);
      } catch (NumberFormatException e) {
        statusLabel.setText("Failed to parse seed.");
        uninstallButton.setEnabled(true);
        return;
      }

      if (itemSpawnIndex == 0 && enemySpawnIndex == 0 && !randomizeVoices
          && !randomizeBodies) {
        statusLabel.setText("Nothing to install.");
        uninstallButton.setEnabled(true);
        return;
      }

      GenericFilterSettings itemSpawnSettings = new GenericFilterSettings(r,
          itemSpawnPanel.getSettingsForId(itemSpawnIndex));
      GenericFilterSettings enemySettings = new GenericFilterSettings(r,
          enemySpawnPanel.getSettingsForId(enemySpawnIndex));

      database = EntityDatabase.getInstance();
      settings = new Settings.Builder().setInstallDir(Paths.get(installDir))
          .setItemSpawnSettings(itemSpawnSettings)
          .setEnemySettings(enemySettings).setRand(r).build();

      installer = new Installer(settings);

      if (!installer.verifyExeDir()) {
        statusLabel.setText("Could not find data/ folder in exe directory.");
        uninstallButton.setEnabled(true);
        return;
      }
      if (!installer.verifyInstallDir()) {
        statusLabel.setText("Prey install directory not valid.");
        uninstallButton.setEnabled(true);
        return;
      }

      if (randomizeVoices) {
        statusLabel.setText("Randomizing voices...");
        r.setSeed(seed);
        new VoiceRandomizer(settings).randomize();
        statusLabel.setText("Done randomizing voices.");
      }
      if (randomizeBodies) {
        statusLabel.setText("Randomizing bodies...");
        r.setSeed(seed);
        new BodyRandomizer(settings).randomize();
        statusLabel.setText("Done randomizing bodies.");
      }

      if (itemSpawnIndex != 0 || enemySpawnIndex != 0) {
        statusLabel.setText("Randomizing levels...");
        LevelRandomizer lr = new LevelRandomizer(settings)
            .addFilter(new MorgansApartmentFilter(settings))
            .addFilter(new ItemSpawnFilter(database, settings))
            .addFilter(new EnemyFilter(database, settings))
            .addFilter(new FlowgraphFilter(database, settings));
        r.setSeed(seed);
        lr.randomize();
        LootTableRandomizer ltr = new LootTableRandomizer(database, settings);
        r.setSeed(seed);
        ltr.randomize();
        statusLabel.setText("Done randomizing levels.");
      }

      try {
        installer.install();
        statusLabel.setText("Done installing.");
      } catch (IOException e) {
        statusLabel.setText("Error occurred during install.");
        e.printStackTrace();
      }
      uninstallButton.setEnabled(true);
    }
  }

  private SettingsJson getSettingsFromFile(String settingsPath)
      throws JsonParseException, JsonMappingException, IOException {
    File settingsFile = new File(settingsPath);
    if (!settingsFile.exists()) {
      throw new FileNotFoundException("Could not find settings.json file");
    }
    SettingsJson settings = new ObjectMapper().readerFor(SettingsJson.class)
        .readValue(settingsFile, SettingsJson.class);
    return settings;
  }

  private class OnUninstallClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent arg0) {
      uninstall();
    }
  }

  private void uninstall() {
    statusLabel.setText("Uninstalling...");
    if (installer == null) {
      settings = new Settings.Builder().setInstallDir(
          Paths.get(DEFAULT_INSTALL_DIR)).build();
      installer = new Installer(settings);
    }
    installer.uninstall();
    statusLabel.setText("Done uninstalling.");
  }

  private class OnCloseClick implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      System.exit(0);
    }

  }
}
