package gui;

import java.awt.Color;
import java.awt.FlowLayout;
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
import installers.Installer;
import randomizers.cosmetic.BodyRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.LevelRandomizer;
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

  public RandomizerGui() {
    setup();
  }

  private void setup() {
    mainFrame = new JFrame("Java SWING Examples");
    mainFrame.setSize(800, 800);
    mainFrame.setLayout(new GridLayout(5, 1));

    mainFrame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent windowEvent) {
        System.exit(0);
      }
    });

    /** HEADER */
    JLabel headerLabel = new JLabel("Prey Randomizer", JLabel.LEFT);
    JPanel headerPanel = new JPanel();
    headerPanel.setLayout(new FlowLayout());
    headerPanel.add(headerLabel);
    headerPanel.setBorder(BorderFactory.createLineBorder(Color.black));

    /** GET INSTALL DIR */

    fileChooser = new JFileChooser(installDir);
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.setMultiSelectionEnabled(false);
    JLabel currentFileLabel = new JLabel("Install location:");

    currentFile = new JLabel(installDir);
    JButton changeInstall = new JButton("Change");
    changeInstall.setActionCommand("changeInstallDir");
    changeInstall.addActionListener(new OnChangeDirClick());

    JPanel getInstallDirPanel = new JPanel();
    getInstallDirPanel.setLayout(new FlowLayout());
    getInstallDirPanel.add(currentFileLabel);
    getInstallDirPanel.add(currentFile);
    getInstallDirPanel.add(changeInstall);
    getInstallDirPanel.setBorder(BorderFactory.createLineBorder(Color.black));

    voiceLinesCheckBox = new JCheckBox("Randomize voice lines", false);
    voiceLinesCheckBox.addItemListener(new OnCheckBoxClick());
    bodiesCheckBox = new JCheckBox("Randomize NPC bodies", false);
    bodiesCheckBox.addItemListener(new OnCheckBoxClick());

    JPanel cosmeticsPanel = new JPanel();
    cosmeticsPanel.setLayout(new FlowLayout());
    cosmeticsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
    cosmeticsPanel.add(voiceLinesCheckBox);
    cosmeticsPanel.add(bodiesCheckBox);

    /** GAMEPLAY */
    JPanel gameplayPanel = new JPanel();
    JPanel itemSpawnPanel = new JPanel();
    JPanel enemySpawnPanel = new JPanel();

    JLabel itemSpawnLabel = new JLabel("Item spawn presets");
    ActionListener itemSpawnActionListener = new OnItemSpawnRadioClick();
    JRadioButton itemSpawnNoRandom = new JRadioButton("Do not randomize");
    itemSpawnNoRandom.setActionCommand(ItemSpawnSettings.Preset.NONE.toString());
    itemSpawnNoRandom.addActionListener(itemSpawnActionListener);
    itemSpawnNoRandom.setSelected(true);
    JRadioButton itemSpawnRandomizeAll = new JRadioButton("Randomize all");
    itemSpawnRandomizeAll.setActionCommand(ItemSpawnSettings.Preset.NO_LOGIC.toString());
    itemSpawnRandomizeAll.addActionListener(itemSpawnActionListener);
    JRadioButton itemSpawnWhiskeyAndCigars = new JRadioButton("Whiskey and cigars");
    itemSpawnWhiskeyAndCigars.setActionCommand(ItemSpawnSettings.Preset.WHISKEY_AND_CIGARS.toString());
    itemSpawnWhiskeyAndCigars.addActionListener(itemSpawnActionListener);
    ButtonGroup itemSpawnGroup = new ButtonGroup();
    itemSpawnGroup.add(itemSpawnNoRandom);
    itemSpawnGroup.add(itemSpawnRandomizeAll);
    itemSpawnGroup.add(itemSpawnWhiskeyAndCigars);

    itemSpawnPanel.add(itemSpawnLabel);
    itemSpawnPanel.add(itemSpawnNoRandom);
    itemSpawnPanel.add(itemSpawnRandomizeAll);
    itemSpawnPanel.add(itemSpawnWhiskeyAndCigars);

    JLabel enemySpawnLabel = new JLabel("Enemy spawn presets");
    ActionListener enemySpawnActionListener = new OnEnemySpawnRadioClick();
    JRadioButton enemySpawnNoRandom = new JRadioButton("Do not randomize");
    enemySpawnNoRandom.setActionCommand(EnemySettings.Preset.NONE.toString());
    enemySpawnNoRandom.addActionListener(enemySpawnActionListener);
    enemySpawnNoRandom.setSelected(true);
    JRadioButton enemySpawnRandomizeTyphon = new JRadioButton("Randomize all typhon");
    enemySpawnRandomizeTyphon.setActionCommand(EnemySettings.Preset.NO_LOGIC.toString());
    enemySpawnRandomizeTyphon.addActionListener(enemySpawnActionListener);
    JRadioButton enemySpawnAllNightmare = new JRadioButton("All nightmares");
    enemySpawnAllNightmare.setActionCommand(EnemySettings.Preset.ALL_NIGHTMARES.toString());
    enemySpawnAllNightmare.addActionListener(enemySpawnActionListener);
    ButtonGroup enemySpawnGroup = new ButtonGroup();
    enemySpawnGroup.add(enemySpawnNoRandom);
    enemySpawnGroup.add(enemySpawnRandomizeTyphon);
    enemySpawnGroup.add(enemySpawnAllNightmare);

    enemySpawnPanel.add(enemySpawnLabel);
    enemySpawnPanel.add(enemySpawnNoRandom);
    enemySpawnPanel.add(enemySpawnRandomizeTyphon);
    enemySpawnPanel.add(enemySpawnAllNightmare);

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
    JButton uninstallButton = new JButton("Uninstall");
    uninstallButton.setActionCommand("uninstall");
    uninstallButton.addActionListener(new OnUninstallClick());
    JButton closeButton = new JButton("Close");
    closeButton.setActionCommand("close");
    closeButton.addActionListener(new OnCloseClick());
    JPanel buttonsPanel = new JPanel();
    buttonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
    buttonsPanel.add(statusLabel);
    buttonsPanel.add(installButton);
    buttonsPanel.add(uninstallButton);
    buttonsPanel.add(closeButton);
    buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.black));

    mainFrame.add(headerPanel);
    mainFrame.add(getInstallDirPanel);
    mainFrame.add(cosmeticsPanel);
    mainFrame.add(gameplayPanel);
    mainFrame.add(buttonsPanel);
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
      statusLabel.setText("Installing...");

      // Uninstall previous version if it exists
      if (installer != null || settings != null) {
        uninstall();
      }

      EnemySettings enemySettings = new EnemySettings.Builder().setRandomizeMode(enemySpawnPreset).build();
      ItemSpawnSettings itemSpawnSettings = new ItemSpawnSettings.Builder().setRandomizeMode(itemSpawnPreset).build();

      settings = new Settings.Builder().setInstallDir(Paths.get(installDir)).setItemSpawnSettings(itemSpawnSettings)
          .setEnemySettings(enemySettings).build();

      installer = new Installer(settings);

      if (randomizeVoices) {
        new VoiceRandomizer(settings).randomize();
      }
      if (randomizeBodies) {
        new BodyRandomizer(settings).randomize();
      }
      LevelRandomizer lr = new LevelRandomizer(settings);
      lr.addFilter(new ItemSpawnFilter(settings));
      lr.addFilter(new EnemyFilter(settings));
      lr.randomize();

      try {
        installer.install();
        statusLabel.setText("Done installing.");
      } catch (IOException e) {
        statusLabel.setText("Error occurred during install.");
      }
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
