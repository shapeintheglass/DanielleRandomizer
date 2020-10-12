package gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import filters.EnemyFilter;
import filters.ItemSpawnFilter;
import filters.MorgansApartmentFilter;
import installers.Installer;
import randomizers.cosmetic.BodyRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.LevelRandomizer;
import randomizers.gameplay.LootTableRandomizer;
import settings.EnemySettings;
import settings.ItemSpawnSettings;
import settings.Settings;

/**
 * Renders the GUI interface.
 * 
 * @author Kida
 *
 */
public class RandomizerGui {
  private JFrame mainFrame;

  private Settings settings = null;
  private Installer installer = null;

  private JLabel statusLabel;
  private JFileChooser fileChooser;

  private JLabel currentFile;
  private String installDir = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  private JCheckBox voiceLinesCheckBox;

  private JCheckBox bodiesCheckBox;

  private boolean randomizeVoices;
  private boolean randomizeBodies;

  private ItemSpawnSettings.Preset itemSpawnPreset = ItemSpawnSettings.Preset.NONE;
  private EnemySettings.Preset enemySpawnPreset = EnemySettings.Preset.NONE;

  private JButton uninstallButton;

  private static final String[] ITEM_SPAWN_PRESET_NAMES = { "Do not randomize", "Randomize all", "Whiskey and cigars" };
  private static final ItemSpawnSettings.Preset[] ITEM_SPAWN_PRESETS = { ItemSpawnSettings.Preset.NONE,
      ItemSpawnSettings.Preset.NO_LOGIC, ItemSpawnSettings.Preset.WHISKEY_AND_CIGARS };

  private static final String[] ENEMY_SPAWN_PRESET_NAMES = { "Do not randomize", "Randomize all typhon",
      "All nightmares" };
  private static final EnemySettings.Preset[] ENEMY_SPAWN_PRESETS = { EnemySettings.Preset.NONE,
      EnemySettings.Preset.NO_LOGIC, EnemySettings.Preset.ALL_NIGHTMARES };

  public RandomizerGui() {
    setup();
  }

  private void setup() {
    mainFrame = new JFrame("Prey Randomizer");
    mainFrame.setSize(600, 300);
    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    JPanel mainPanel = new JPanel();
    mainFrame.add(mainPanel);
    mainPanel.setLayout(new GridBagLayout());

    GridBagConstraints c = new GridBagConstraints();
    c.fill = GridBagConstraints.HORIZONTAL;

    JPanel headerPanel = new JPanel();
    c.gridx = 0;
    c.gridy = 0;
    mainPanel.add(headerPanel, c);
    JPanel getInstallDirPanel = new JPanel();
    c.gridx = 0;
    c.gridy = 1;
    mainPanel.add(getInstallDirPanel, c);
    JPanel cosmeticsPanel = new JPanel();
    c.gridx = 0;
    c.gridy = 2;
    mainPanel.add(cosmeticsPanel, c);
    JPanel gameplayPanel = new JPanel();
    c.gridx = 0;
    c.gridy = 3;
    mainPanel.add(gameplayPanel, c);
    JPanel buttonsPanel = new JPanel();
    c.gridx = 0;
    c.gridy = 4;
    mainPanel.add(buttonsPanel, c);

    /** HEADER */
    JLabel headerLabel = new JLabel("Prey Randomizer", JLabel.LEFT);
    headerLabel.setSize(600, 10);

    headerPanel.setLayout(new FlowLayout());
    headerPanel.add(headerLabel);
    headerPanel.setBorder(BorderFactory.createLineBorder(Color.black));

    /** GET INSTALL DIR */

    fileChooser = new JFileChooser(installDir);
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    JLabel currentFileLabel = new JLabel("Prey folder location:");

    currentFile = new JLabel(installDir);
    currentFile.setBorder(BorderFactory.createRaisedSoftBevelBorder());
    JButton changeInstall = new JButton("Change");
    changeInstall.setActionCommand("changeInstallDir");
    changeInstall.addActionListener(new OnChangeDirClick());

    getInstallDirPanel.setLayout(new FlowLayout());
    getInstallDirPanel.add(currentFileLabel);
    getInstallDirPanel.add(currentFile);
    getInstallDirPanel.add(changeInstall);
    getInstallDirPanel.setBorder(BorderFactory.createLineBorder(Color.black));

    voiceLinesCheckBox = new JCheckBox("Randomize voice lines", false);
    voiceLinesCheckBox.addItemListener(new OnCheckBoxClick());
    bodiesCheckBox = new JCheckBox("Randomize NPC bodies", false);
    bodiesCheckBox.addItemListener(new OnCheckBoxClick());

    cosmeticsPanel.setLayout(new FlowLayout());
    cosmeticsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    cosmeticsPanel.add(voiceLinesCheckBox);
    cosmeticsPanel.add(bodiesCheckBox);

    /** GAMEPLAY */

    JPanel itemSpawnPanel = new JPanel();
    itemSpawnPanel.setLayout(new GridLayout(0, 1));
    JPanel enemySpawnPanel = new JPanel();
    enemySpawnPanel.setLayout(new GridLayout(0, 1));

    JLabel itemSpawnLabel = new JLabel("Item spawn presets");
    ActionListener itemSpawnActionListener = new OnItemSpawnRadioClick();
    ButtonGroup itemSpawnGroup = new ButtonGroup();
    itemSpawnPanel.add(itemSpawnLabel);
    for (int i = 0; i < ITEM_SPAWN_PRESET_NAMES.length; i++) {
      JRadioButton itemSpawnOption = new JRadioButton(ITEM_SPAWN_PRESET_NAMES[i]);
      itemSpawnOption.setActionCommand(ITEM_SPAWN_PRESETS[i].toString());
      itemSpawnOption.addActionListener(itemSpawnActionListener);
      itemSpawnGroup.add(itemSpawnOption);
      itemSpawnPanel.add(itemSpawnOption);
      if (i == 0) {
        itemSpawnOption.setSelected(true);
      }
    }

    JLabel enemySpawnLabel = new JLabel("Enemy spawn presets");
    ActionListener enemySpawnActionListener = new OnEnemySpawnRadioClick();
    ButtonGroup enemySpawnGroup = new ButtonGroup();

    enemySpawnPanel.add(enemySpawnLabel);
    for (int i = 0; i < ENEMY_SPAWN_PRESET_NAMES.length; i++) {
      JRadioButton enemySpawnOption = new JRadioButton(ENEMY_SPAWN_PRESET_NAMES[i]);
      enemySpawnOption.setActionCommand(ENEMY_SPAWN_PRESETS[i].toString());
      enemySpawnOption.addActionListener(enemySpawnActionListener);
      enemySpawnGroup.add(enemySpawnOption);
      enemySpawnPanel.add(enemySpawnOption);
      if (i == 0) {
        enemySpawnOption.setSelected(true);
      }
    }

    gameplayPanel.setLayout(new GridLayout(1, 2));
    gameplayPanel.add(itemSpawnPanel);
    gameplayPanel.add(enemySpawnPanel);
    itemSpawnPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    enemySpawnPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    gameplayPanel.setBorder(BorderFactory.createLineBorder(Color.black));

    /** BUTTONS */
    statusLabel = new JLabel("", JLabel.LEFT);
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
    buttonsPanel.add(installButton);
    buttonsPanel.add(uninstallButton);
    buttonsPanel.add(closeButton);
    buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.black));

  }

  public void start() {
    mainFrame.pack();
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

  private class OnItemSpawnRadioClick implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      itemSpawnPreset = ItemSpawnSettings.Preset.valueOf(e.getActionCommand());
    }

  }

  private class OnEnemySpawnRadioClick implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      enemySpawnPreset = EnemySettings.Preset.valueOf(e.getActionCommand());
    }

  }

  private class OnInstallClick implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent arg0) {
      uninstallButton.setEnabled(false);
      statusLabel.setText("Installing...");

      if (enemySpawnPreset == EnemySettings.Preset.NONE && itemSpawnPreset == ItemSpawnSettings.Preset.NONE
          && !randomizeVoices && !randomizeBodies) {
        statusLabel.setText("Nothing to install.");
        uninstallButton.setEnabled(true);
        return;
      }

      EnemySettings enemySettings = new EnemySettings.Builder().setPreset(enemySpawnPreset).build();
      ItemSpawnSettings itemSpawnSettings = new ItemSpawnSettings.Builder().setRandomizeMode(itemSpawnPreset).build();

      settings = new Settings.Builder().setInstallDir(Paths.get(installDir)).setItemSpawnSettings(itemSpawnSettings)
          .setEnemySettings(enemySettings).build();

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
        new VoiceRandomizer(settings).randomize();
        statusLabel.setText("Done randomizing voices.");
      }
      if (randomizeBodies) {
        statusLabel.setText("Randomizing bodies...");
        new BodyRandomizer(settings).randomize();
        statusLabel.setText("Done randomizing bodies.");
      }

      if (enemySpawnPreset != EnemySettings.Preset.NONE || itemSpawnPreset != ItemSpawnSettings.Preset.NONE) {
        statusLabel.setText("Randomizing levels...");
        LevelRandomizer lr = new LevelRandomizer(settings)
            .addFilter(new MorgansApartmentFilter(settings))
            .addFilter(new ItemSpawnFilter(settings))
            .addFilter(new EnemyFilter(settings));
        lr.randomize();
        LootTableRandomizer ltr = new LootTableRandomizer(settings);
        ltr.randomize();
        statusLabel.setText("Done randomizing levels.");
      }

      try {
        installer.install();
        statusLabel.setText("Done installing.");
      } catch (IOException e) {
        statusLabel.setText("Error occurred during install.");
      }
      uninstallButton.setEnabled(true);
    }
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
      settings = new Settings.Builder().setInstallDir(Paths.get(installDir)).build();
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
