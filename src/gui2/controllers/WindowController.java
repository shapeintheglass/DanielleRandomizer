package gui2.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.protobuf.util.JsonFormat;

import gui2.Gui2Consts;
import gui2.RandomizerService;
import gui2.SettingsHelper;
import installers.Installer;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import proto.RandomizerSettings.AllPresets;
import proto.RandomizerSettings.CheatSettings;
import proto.RandomizerSettings.CheatSettings.SpawnLocation;
import proto.RandomizerSettings.CosmeticSettings;
import proto.RandomizerSettings.ExperimentalSettings;
import proto.RandomizerSettings.GameStartSettings;
import proto.RandomizerSettings.GameplaySettings;
import proto.RandomizerSettings.GenericSpawnPresetFilter;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import proto.RandomizerSettings.MoreSettings;
import proto.RandomizerSettings.Settings;
import proto.RandomizerSettings.ToggleWithSlider;
import randomizers.generators.CustomSpawnGenerator;
import utils.Utils;

public class WindowController {

  @FXML
  private VBox mainWindow;
  @FXML
  private TextArea outputWindow;

  /* TOP BUTTONS */
  @FXML
  private TextField directoryText;
  @FXML
  private TextField seedText;
  @FXML
  private Button changeDirButton;
  @FXML
  private Button newSeedButton;

  /* PRESETS */
  @FXML
  private Button recommendedPresetButton;
  @FXML
  private Button chaoticPresetButton;
  @FXML
  private Button litePresetButton;
  @FXML
  private Button gotsPresetButton;
  @FXML
  private Button livingTalosPresetButton;

  /* COSMETICS TAB */
  @FXML
  private CheckBox cosmeticCheckboxBodies;
  @FXML
  private CheckBox cosmeticCheckboxVoices;
  @FXML
  private CheckBox cosmeticCheckboxMusic;
  @FXML
  private CheckBox cosmeticCheckboxPlayerModel;
  @FXML
  private CheckBox cosmeticCheckboxPlanetSize;
  @FXML
  private CheckBox cosmeticCheckboxEmotions;

  /* ITEMS/PROPS TAB */
  @FXML
  private VBox pickupPresetsVBox;
  private ToggleGroup pickupToggleGroup = new ToggleGroup();
  private String pickupDefault = "No item randomization";
  @FXML
  private VBox propPresetsVBox;
  private ToggleGroup propToggleGroup = new ToggleGroup();
  private String propDefault = "No prop randomization";

  /* ENEMIES/NPCS TAB */
  @FXML
  private VBox enemyPresetsVBox;
  private ToggleGroup enemyToggleGroup = new ToggleGroup();
  private String enemyDefault = "No enemy randomization";
  @FXML
  private VBox npcPresetsVBox;
  private ToggleGroup npcToggleGroup = new ToggleGroup();
  private String npcDefault = "No friendly robot randomization";

  /* GAMEPLAY TAB */
  @FXML
  private CheckBox gameplayRandomizeStation;
  @FXML
  private CheckBox gameplayRandomizeNeuromods;
  @FXML
  private CheckBox gameplayRandomizeFabPlanCosts;
  @FXML
  private CheckBox gameplayRandomizeRecyclers;
  @FXML
  private CheckBox gameplayRandomizeDispensers;
  @FXML
  private CheckBox gameplayRandomizeBreakables;
  @FXML
  private CheckBox gameplayRandomizeHackables;

  /* GAME START TAB */
  @FXML
  private CheckBox startCheckboxDay2;
  @FXML
  private CheckBox startCheckboxAddAllEquipment;
  @FXML
  private CheckBox startCheckboxSkipJovan;
  @FXML
  private CheckBox startCheckboxOutsideApartment;

  /* MORE TAB */
  @FXML
  private CheckBox moreGuns;
  @FXML
  private CheckBox morePreySoulsGuns;
  @FXML
  private CheckBox morePreySoulsChipsets;
  @FXML
  private CheckBox morePreySoulsEnemies;
  @FXML
  private CheckBox morePreySoulsTurrets;

  /* CHEATS TAB */
  @FXML
  private CheckBox cheatsCheckboxOpenStation;
  @FXML
  private CheckBox cheatsCheckboxAllScans;
  @FXML
  private CheckBox cheatsCheckboxFriendlyNpcs;
  @FXML
  private CheckBox cheatsCheckboxCustomStart;
  @FXML
  private ChoiceBox<String> cheatsChoiceBoxCustomStart;
  @FXML
  private TextField cheatsTextFieldGameTokens;

  /* EXPERIMENTAL TAB */
  @FXML
  private CheckBox expCheckboxWander;
  @FXML
  private CheckBox expLivingCorpses;
  @FXML
  private CheckBox expCheckboxGravity;
  @FXML
  private CheckBox expCheckboxEnableGravity;
  @FXML
  private CheckBox expCheckboxSelfDestruct;
  @FXML
  private TextField expTextFieldTimer;
  @FXML
  private TextField expTextFieldShuttleTimer;

  /* LOWER BUTTONS */
  @FXML
  private Button installButton;
  @FXML
  private Button uninstallButton;
  @FXML
  private Button clearButton;
  @FXML
  private Button saveSettingsButton;
  @FXML
  private Button closeButton;

  private Stage stage;
  private Settings settings;
  private AllPresets allPresets;
  private Logger logger;
  private List<Node> toDisable;
  private List<CheckBox> checkboxes;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
  }

  @FXML
  protected void initialize() {
    checkboxes = ImmutableList.of(cosmeticCheckboxBodies, cosmeticCheckboxVoices, cosmeticCheckboxEmotions,
        cosmeticCheckboxMusic, cosmeticCheckboxPlayerModel, cosmeticCheckboxPlanetSize, gameplayRandomizeStation,
        gameplayRandomizeNeuromods, gameplayRandomizeFabPlanCosts, gameplayRandomizeRecyclers,
        gameplayRandomizeDispensers, gameplayRandomizeBreakables, gameplayRandomizeHackables, startCheckboxDay2,
        startCheckboxAddAllEquipment, startCheckboxSkipJovan, startCheckboxOutsideApartment, moreGuns,
        morePreySoulsGuns, morePreySoulsChipsets, morePreySoulsEnemies, morePreySoulsTurrets, cheatsCheckboxAllScans,
        cheatsCheckboxCustomStart, cheatsCheckboxOpenStation, expCheckboxEnableGravity, expCheckboxGravity,
        expCheckboxSelfDestruct, expCheckboxWander, expLivingCorpses);

    logger = Logger.getLogger("randomizer_gui");

    setTooltips();

    cheatsChoiceBoxCustomStart.getItems().add(SpawnLocation.RANDOM.name());
    for (SpawnLocation l : CustomSpawnGenerator.SUPPORTED_SPAWNS) {
      cheatsChoiceBoxCustomStart.getItems().add(l.name());
    }

    // Attempt to read from preset definition file and saved settings file
    try {
      allPresets = getAllPresets();
    } catch (FileNotFoundException e) {
      String loggerWarning = String.format("Could not find presets file %s.\n", Gui2Consts.ALL_PRESETS_FILE);
      logger.warning(loggerWarning);
      outputWindow.appendText(loggerWarning);
      e.printStackTrace();
    } catch (IOException e) {
      outputWindow.appendText(String.format("Unable to parse %s\n", Gui2Consts.ALL_PRESETS_FILE));
      logger.info(String.format("An error occurred while parsing %s.", Gui2Consts.ALL_PRESETS_FILE));
      e.printStackTrace();
      allPresets = AllPresets.getDefaultInstance();
    }

    Settings savedSettings = Settings.getDefaultInstance();
    try {
      savedSettings = getSavedSettings();
    } catch (FileNotFoundException e) {
      String loggerWarning = String.format("Could not find saved settings file %s.\n", Gui2Consts.SAVED_SETTINGS_FILE);
      logger.warning(loggerWarning);
      e.printStackTrace();
    } catch (IOException e) {
      String loggerWarning = Gui2Consts.ERROR_COULD_NOT_PARSE_FILE + Gui2Consts.SAVED_SETTINGS_FILE + "\n";
      logger.warning(loggerWarning);
      outputWindow.appendText(loggerWarning);
      e.printStackTrace();
    }

    this.settings = createInitialSettings(allPresets, savedSettings);

    seedText.setText(settings.getSeed());
    directoryText.setText(settings.getInstallDir());

    toDisable = Lists.newArrayList(changeDirButton, newSeedButton, installButton, uninstallButton, clearButton,
        saveSettingsButton, closeButton, recommendedPresetButton, chaoticPresetButton, litePresetButton,
        gotsPresetButton, cheatsTextFieldGameTokens, expTextFieldShuttleTimer, expTextFieldTimer, seedText,
        directoryText);

    outputWindow.appendText("Loaded with settings:\n" + SettingsHelper.settingsToString(settings));

    updateUI();

    cheatsCheckboxCustomStart.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (event.getSource() instanceof CheckBox) {
          boolean isCustomStart = cheatsCheckboxCustomStart.isSelected();
          cheatsChoiceBoxCustomStart.setDisable(!isCustomStart);
        }
      }
    });

    expCheckboxSelfDestruct.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (event.getSource() instanceof CheckBox) {
          boolean isSelfDestruct = expCheckboxSelfDestruct.isSelected();
          expTextFieldTimer.setDisable(!isSelfDestruct);
          expTextFieldShuttleTimer.setDisable(!isSelfDestruct);
        }
      }
    });
  }

  private void setTooltips() {
    directoryText.setTooltip(new Tooltip("The directory where Prey is installed"));
    changeDirButton.setTooltip(new Tooltip("Open a file chooser to select the Prey install directory"));
    seedText.setTooltip(new Tooltip("Seed to use for random generation. Must be a number (int64)"));
    newSeedButton.setTooltip(new Tooltip("Randomly picks a new seed"));

    recommendedPresetButton.setTooltip(new Tooltip("Updates this UI to the preset \"recommended\" experience."));
    chaoticPresetButton.setTooltip(new Tooltip("Updates this UI to the preset \"basic\" experience."));
    litePresetButton.setTooltip(new Tooltip("Updates this UI to the preset \"lite\" experience."));
    gotsPresetButton.setTooltip(new Tooltip("Updates this UI to the preset \"timed\" experience."));

    moreGuns.setTooltip(new Tooltip("Adds additional weapons from \"More Guns\" to the item pool."));
    gameplayRandomizeFabPlanCosts.setTooltip(new Tooltip(
        "Sets the materials costs for every fab plan to random values."));

    installButton.setTooltip(new Tooltip("Generates randomized game and installs to the game directory."));
    uninstallButton.setTooltip(new Tooltip(
        "Removes any mods added by this installer and reverts changes to the game directory."));
    clearButton.setTooltip(new Tooltip("Clears the output window."));
    saveSettingsButton.setTooltip(new Tooltip(
        "Saves the current settings to a file so that they are the default the next time this UI is opened."));
    closeButton.setTooltip(new Tooltip("Closes this UI."));
  }

  private void updateUI() {
    updateAllPresets();
    cosmeticCheckboxBodies.setSelected(settings.getCosmeticSettings().getRandomizeBodies());
    cosmeticCheckboxVoices.setSelected(settings.getCosmeticSettings().getRandomizeVoicelines());
    cosmeticCheckboxMusic.setSelected(settings.getCosmeticSettings().getRandomizeMusic());
    cosmeticCheckboxPlayerModel.setSelected(settings.getCosmeticSettings().getRandomizePlayerModel());
    cosmeticCheckboxPlanetSize.setSelected(settings.getCosmeticSettings().getRandomizePlanetSize());
    cosmeticCheckboxEmotions.setSelected(settings.getCosmeticSettings().getRandomizeEmotions());

    gameplayRandomizeStation.setSelected(settings.getGameplaySettings().getRandomizeStation());
    gameplayRandomizeNeuromods.setSelected(settings.getGameplaySettings().getRandomizeNeuromods());
    gameplayRandomizeFabPlanCosts.setSelected(settings.getGameplaySettings().getRandomizeFabPlanCosts());
    gameplayRandomizeRecyclers.setSelected(settings.getGameplaySettings().getRandomizeRecyclers().getIsEnabled());
    gameplayRandomizeDispensers.setSelected(settings.getGameplaySettings().getRandomizeDispensers().getIsEnabled());
    gameplayRandomizeBreakables.setSelected(settings.getGameplaySettings().getRandomizeBreakables().getIsEnabled());
    gameplayRandomizeHackables.setSelected(settings.getGameplaySettings().getRandomizeHackables().getIsEnabled());

    startCheckboxDay2.setSelected(settings.getGameStartSettings().getStartOnSecondDay());
    startCheckboxAddAllEquipment.setSelected(settings.getGameStartSettings().getAddLootToApartment());
    startCheckboxSkipJovan.setSelected(settings.getGameStartSettings().getSkipJovanCutscene());
    startCheckboxOutsideApartment.setSelected(settings.getGameStartSettings().getStartOutsideApartment());

    moreGuns.setSelected(settings.getMoreSettings().getMoreGuns());
    morePreySoulsGuns.setSelected(settings.getMoreSettings().getPreySoulsGuns());
    morePreySoulsChipsets.setSelected(settings.getMoreSettings().getPreySoulsChipsets());
    morePreySoulsEnemies.setSelected(settings.getMoreSettings().getPreySoulsEnemies());
    morePreySoulsTurrets.setSelected(settings.getMoreSettings().getPreySoulsTurrets());

    cheatsCheckboxOpenStation.setSelected(settings.getCheatSettings().getOpenStation());
    cheatsCheckboxAllScans.setSelected(settings.getCheatSettings().getUnlockAllScans());
    cheatsCheckboxFriendlyNpcs.setSelected(settings.getCheatSettings().getFriendlyNpcs());
    cheatsCheckboxCustomStart.setSelected(settings.getCheatSettings().getUseCustomSpawn());
    cheatsChoiceBoxCustomStart.setDisable(!settings.getCheatSettings().getUseCustomSpawn());
    cheatsChoiceBoxCustomStart.setValue(settings.getCheatSettings().getCustomSpawnLocation().name());
    cheatsTextFieldGameTokens.setText(settings.getCheatSettings().getGameTokenOverrides());

    expCheckboxWander.setSelected(settings.getExpSettings().getWanderingHumans());
    expCheckboxGravity.setSelected(settings.getExpSettings().getZeroGravityEverywhere());
    expCheckboxEnableGravity.setSelected(settings.getExpSettings().getEnableGravityInExtAndGuts());
    expCheckboxSelfDestruct.setSelected(settings.getExpSettings().getStartSelfDestruct());
    expTextFieldTimer.setText(settings.getExpSettings().getSelfDestructTimer());
    expTextFieldShuttleTimer.setText(settings.getExpSettings().getSelfDestructShuttleTimer());
    expTextFieldTimer.setDisable(!expCheckboxSelfDestruct.isSelected());
    expTextFieldShuttleTimer.setDisable(!expCheckboxSelfDestruct.isSelected());
  }

  private void resetUI() {
    for (CheckBox c : checkboxes) {
      c.setSelected(false);
    }
    resetPickupPreset();
    resetPropPreset();
    resetEnemyPreset();
    resetNpcPreset();
    cheatsChoiceBoxCustomStart.setValue(SpawnLocation.RANDOM.name());
    cheatsChoiceBoxCustomStart.setDisable(true);
    cheatsTextFieldGameTokens.clear();
    expTextFieldTimer.setText(Gui2Consts.DEFAULT_SELF_DESTRUCT_TIMER);
    expTextFieldShuttleTimer.setText(Gui2Consts.DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER);
  }

  /*
   * UPPER BUTTONS
   */

  @FXML
  protected void onNewSeedClicked(ActionEvent event) {
    seedText.setText(Long.toString(Utils.getNewSeed()));
  }

  @FXML
  protected void onChangeDirClicked(ActionEvent event) {
    DirectoryChooser dirChooser = new DirectoryChooser();
    dirChooser.setInitialDirectory(new File(settings.getInstallDir()));
    dirChooser.setTitle("Choose Prey Directory");
    File chosenDir = dirChooser.showDialog(stage);
    try {
      directoryText.setText(chosenDir.getCanonicalPath());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * PRESETS
   */

  @FXML
  protected void onPresetsRecommendedClicked(ActionEvent event) {
    resetUI();
    cosmeticCheckboxBodies.setSelected(true);
    cosmeticCheckboxVoices.setSelected(true);
    cosmeticCheckboxMusic.setSelected(true);
    cosmeticCheckboxPlayerModel.setSelected(true);
    cosmeticCheckboxEmotions.setSelected(true);
    cosmeticCheckboxPlanetSize.setSelected(true);
    setPickupPreset("Randomize all items (no story items)");
    setPropPreset("Randomize props within leverage tier");
    setEnemyPreset("Randomize all enemies");
    setNpcPreset("Randomize all friendly robots");
    gameplayRandomizeFabPlanCosts.setSelected(true);
    startCheckboxDay2.setSelected(true);
    outputWindow.clear();
    outputWindow.appendText("Recommended preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, SettingsHelper.settingsToString(getSettings())));
  }

  @FXML
  protected void onPresetsChaoticClicked(ActionEvent event) {
    resetUI();
    cosmeticCheckboxBodies.setSelected(true);
    cosmeticCheckboxVoices.setSelected(true);
    cosmeticCheckboxEmotions.setSelected(true);
    cosmeticCheckboxMusic.setSelected(true);
    cosmeticCheckboxPlayerModel.setSelected(true);
    cosmeticCheckboxPlanetSize.setSelected(true);
    setPickupPreset("Randomize all items");
    setPropPreset("Randomize all props");
    setEnemyPreset("Randomize all enemies");
    setNpcPreset("Randomize all friendly robots");
    gameplayRandomizeStation.setSelected(true);
    gameplayRandomizeFabPlanCosts.setSelected(true);
    gameplayRandomizeNeuromods.setSelected(true);
    gameplayRandomizeRecyclers.setSelected(true);
    gameplayRandomizeDispensers.setSelected(true);
    gameplayRandomizeBreakables.setSelected(true);
    gameplayRandomizeHackables.setSelected(true);
    startCheckboxDay2.setSelected(true);
    moreGuns.setSelected(true);
    outputWindow.clear();
    outputWindow.appendText("Chaotic preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, SettingsHelper.settingsToString(getSettings())));
  }

  @FXML
  protected void onPresetsLiteClicked(ActionEvent event) {
    resetUI();
    setPickupPreset("Randomize items within type");
    setEnemyPreset("Randomize enemies within type");
    setNpcPreset("Randomize friendly robots within type");
    startCheckboxDay2.setSelected(true);
    outputWindow.clear();
    outputWindow.appendText("Lite preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, SettingsHelper.settingsToString(getSettings())));
  }

  @FXML
  protected void onPresetsGotsClicked(ActionEvent event) {
    resetUI();
    startCheckboxDay2.setSelected(true);
    startCheckboxSkipJovan.setSelected(true);
    expCheckboxSelfDestruct.setSelected(true);
    expTextFieldTimer.setText(Gui2Consts.DEFAULT_SELF_DESTRUCT_TIMER);
    expTextFieldShuttleTimer.setText(Gui2Consts.DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER);
    outputWindow.clear();
    outputWindow.appendText("G.O.T.S. preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, SettingsHelper.settingsToString(getSettings())));
  }

  @FXML
  protected void onPresetsLivingTalosClicked(ActionEvent event) {
    resetUI();
    setEnemyPreset("Typhon to humans");
    expCheckboxWander.setSelected(true);
    expLivingCorpses.setSelected(true);
    cheatsCheckboxFriendlyNpcs.setSelected(true);
    cheatsCheckboxOpenStation.setSelected(true);
    outputWindow.clear();
    outputWindow.appendText("Living Talos preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, SettingsHelper.settingsToString(getSettings())));
  }

  /*
   * LOWER BUTTONS
   */

  @FXML
  protected void handleInstallClicked(ActionEvent event) {
    Settings finalSettings = getSettings();

    // TODO: Add a check here to see if nothing will be installed
    validateSpawnPresets(finalSettings.getGameplaySettings().getItemSpawnSettings(), "items");
    validateSpawnPresets(finalSettings.getGameplaySettings().getEnemySpawnSettings(), "enemies");

    try {
      writeLastUsedSettingsToFile(Gui2Consts.LAST_USED_SETTINGS_FILE, finalSettings);
    } catch (IOException e) {
      e.printStackTrace();
    }
    RandomizerService installService = new RandomizerService(outputWindow, finalSettings);
    installService.setOnRunning(new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent arg0) {
        disableAllButtons(true);
      }
    });
    EventHandler<WorkerStateEvent> reenableButtons = new EventHandler<WorkerStateEvent>() {
      @Override
      public void handle(WorkerStateEvent arg0) {
        disableAllButtons(false);
      }
    };
    installService.setOnCancelled(reenableButtons);
    installService.setOnFailed(reenableButtons);
    installService.setOnSucceeded(reenableButtons);
    installService.start();
  }

  private void disableAllButtons(boolean disable) {
    for (Node b : toDisable) {
      b.setDisable(disable);
    }
    mainWindow.setDisable(disable);
  }

  @FXML
  protected void onUninstallClicked(ActionEvent event) {
    Installer.uninstall(Paths.get(directoryText.getText()), logger);
    outputWindow.appendText("Uninstall complete.\n");
  }

  @FXML
  protected void onClearClicked(ActionEvent event) {
    outputWindow.clear();
  }

  @FXML
  protected void onCloseClicked(ActionEvent event) {
    Platform.exit();
  }

  @FXML
  protected void onSaveSettingsClicked(ActionEvent event) {
    Settings toSave = getSettings();
    try {
      writeLastUsedSettingsToFile(Gui2Consts.SAVED_SETTINGS_FILE, toSave);
      outputWindow.appendText(String.format(Gui2Consts.SAVING_INFO, Gui2Consts.SAVED_SETTINGS_FILE));
      outputWindow.appendText(SettingsHelper.settingsToString(toSave));
    } catch (IOException e) {
      outputWindow.appendText(Gui2Consts.SAVING_FAILED);
      e.printStackTrace();
    }
  }

  private void writeLastUsedSettingsToFile(String savedSettingsFilePath, Settings toSave) throws IOException {
    File savedSettingsFile = new File(savedSettingsFilePath);
    savedSettingsFile.createNewFile();
    String serialized = JsonFormat.printer().includingDefaultValueFields().preservingProtoFieldNames().print(toSave);
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(savedSettingsFile));) {
      bw.write(serialized);
    }
  }

  private void updateAllPresets() {
    initPresetRadioButtons(pickupPresetsVBox, allPresets.getPickupSpawnSettingsList(), pickupToggleGroup, settings
        .getGameplaySettings()
        .getPickupPresetName(), pickupDefault);
    initPresetRadioButtons(propPresetsVBox, allPresets.getPropSpawnSettingsList(), propToggleGroup, settings
        .getGameplaySettings()
        .getPropPresetName(), propDefault);
    initPresetRadioButtons(enemyPresetsVBox, allPresets.getEnemySpawnSettingsList(), enemyToggleGroup, settings
        .getGameplaySettings()
        .getEnemyPresetName(), enemyDefault);
    initPresetRadioButtons(npcPresetsVBox, allPresets.getNpcSpawnSettingsList(), npcToggleGroup, settings
        .getGameplaySettings()
        .getNpcPresetName(), npcDefault);
  }

  private void setPickupPreset(String name) {
    setSelectedPreset(pickupToggleGroup, name);
  }

  private void setPropPreset(String name) {
    setSelectedPreset(propToggleGroup, name);
  }

  private void setEnemyPreset(String name) {
    setSelectedPreset(enemyToggleGroup, name);
  }

  private void setNpcPreset(String name) {
    setSelectedPreset(npcToggleGroup, name);
  }

  private void resetPickupPreset() {
    setSelectedPreset(pickupToggleGroup, pickupDefault);
  }

  private void resetPropPreset() {
    setSelectedPreset(propToggleGroup, propDefault);
  }

  private void resetEnemyPreset() {
    setSelectedPreset(enemyToggleGroup, enemyDefault);
  }

  private void resetNpcPreset() {
    setSelectedPreset(npcToggleGroup, npcDefault);
  } 
 
  private String getPickupPreset() {
    return getSelectedPreset(pickupToggleGroup, pickupDefault);
  }

  private String getPropPreset() {
    return getSelectedPreset(propToggleGroup, propDefault);
  }

  private String getEnemyPreset() { 
    return getSelectedPreset(enemyToggleGroup, enemyDefault);
  }

  private String getNpcPreset() {
    return getSelectedPreset(npcToggleGroup, npcDefault);
  } 
 
  // Adds all options from the given preset list into the given vbox
  private void initPresetRadioButtons(VBox vbox, List<GenericSpawnPresetFilter> presets, ToggleGroup toggleGroup,
      String selected, String defaultSelected) {
    if (Strings.isNullOrEmpty(selected)) { 
      selected = defaultSelected;
    }
    for (GenericSpawnPresetFilter preset : presets) {
      RadioButton rb = new RadioButton(preset.getName());
      rb.setToggleGroup(toggleGroup);
      rb.setTooltip(new Tooltip(preset.getDesc()));
      if (selected.equals(preset.getName())) {
        rb.setSelected(true);
      }
      vbox.getChildren().add(rb);
    }
  }

  private String getSelectedPreset(ToggleGroup toggleGroup, String defaultValue) {
    RadioButton selected = (RadioButton) toggleGroup.getSelectedToggle();
    String toSelect = selected == null ? "" : selected.getText();
    return toSelect;
  }

  private void setSelectedPreset(ToggleGroup toggleGroup, String name) {
    for (Toggle t : toggleGroup.getToggles()) {
      RadioButton rb = (RadioButton) t;
      if (rb.getText().equals(name)) {
        rb.setSelected(true);
      } else {
        rb.setSelected(false);
      }
    }
  }

  private CosmeticSettings getCosmeticSettings() {
    return CosmeticSettings.newBuilder()
        .setRandomizeBodies(cosmeticCheckboxBodies.isSelected())
        .setRandomizeVoicelines(cosmeticCheckboxVoices.isSelected())
        .setRandomizeMusic(cosmeticCheckboxMusic.isSelected())
        .setRandomizePlayerModel(cosmeticCheckboxPlayerModel.isSelected())
        .setRandomizePlanetSize(cosmeticCheckboxPlanetSize.isSelected())
        .setRandomizeEmotions(cosmeticCheckboxEmotions.isSelected())
        .build();
  }

  private GenericSpawnPresetFilter getPresetFilterByName(List<GenericSpawnPresetFilter> presets, String presetName) {
    for (GenericSpawnPresetFilter preset : presets) {
      if (preset.getName().equals(presetName)) {
        return preset;
      }
    }
    return null;
  }

  // Create an item spawn filter by merging the selected item and prop spawn
  // presets
  private GenericSpawnPresetFilter mergeItemSpawnPresets() {
    GenericSpawnPresetFilter pickupPresetFilter = getPresetFilterByName(allPresets.getPickupSpawnSettingsList(),
        getPickupPreset());
    GenericSpawnPresetFilter propPresetFilter = getPresetFilterByName(allPresets.getPropSpawnSettingsList(),
        getPropPreset());
    return GenericSpawnPresetFilter.newBuilder()
        .setName(pickupPresetFilter.getName() + " + " + propPresetFilter.getName())
        .setDesc(pickupPresetFilter.getDesc() + " + " + propPresetFilter.getDesc())
        .addAllFilters(pickupPresetFilter.getFiltersList())
        .addAllFilters(propPresetFilter.getFiltersList())
        .build();
  }

//Create an enemy spawn filter by merging the selected enemy and npc spawn presets
  private GenericSpawnPresetFilter mergeEnemySpawnPresets() {
    GenericSpawnPresetFilter enemyPresetFilter = getPresetFilterByName(allPresets.getEnemySpawnSettingsList(),
        getEnemyPreset());
    GenericSpawnPresetFilter npcPresetFilter = getPresetFilterByName(allPresets.getNpcSpawnSettingsList(),
        getNpcPreset());
    return GenericSpawnPresetFilter.newBuilder()
        .setName(enemyPresetFilter.getName() + " + " + npcPresetFilter.getName())
        .setDesc(enemyPresetFilter.getDesc() + " + " + npcPresetFilter.getDesc())
        .addAllFilters(enemyPresetFilter.getFiltersList())
        .addAllFilters(npcPresetFilter.getFiltersList())
        .build();
  }

  private GameplaySettings getGameplaySettings() {
    GenericSpawnPresetFilter itemSpawnSettings = mergeItemSpawnPresets();
    GenericSpawnPresetFilter enemySpawnSettings = mergeEnemySpawnPresets();

    return GameplaySettings.newBuilder()
        .setItemSpawnSettings(itemSpawnSettings)
        .setEnemySpawnSettings(enemySpawnSettings)
        .setRandomizeStation(gameplayRandomizeStation.isSelected())
        .setRandomizeNeuromods(gameplayRandomizeNeuromods.isSelected())
        .setRandomizeFabPlanCosts(gameplayRandomizeFabPlanCosts.isSelected())
        .setRandomizeRecyclers(ToggleWithSlider.newBuilder().setIsEnabled(gameplayRandomizeRecyclers.isSelected()))
        .setRandomizeDispensers(ToggleWithSlider.newBuilder().setIsEnabled(gameplayRandomizeDispensers.isSelected()))
        .setRandomizeBreakables(ToggleWithSlider.newBuilder().setIsEnabled(gameplayRandomizeBreakables.isSelected()))
        .setRandomizeHackables(ToggleWithSlider.newBuilder().setIsEnabled(gameplayRandomizeHackables.isSelected()))
        .setPickupPresetName(getPickupPreset())
        .setPropPresetName(getPropPreset())
        .setEnemyPresetName(getEnemyPreset())
        .setNpcPresetName(getNpcPreset())
        .build();
  }

  private GameStartSettings getGameStartSettings() {
    return GameStartSettings.newBuilder()
        .setAddLootToApartment(startCheckboxAddAllEquipment.isSelected())
        .setStartOnSecondDay(startCheckboxDay2.isSelected())
        .setSkipJovanCutscene(startCheckboxSkipJovan.isSelected())
        .setStartOutsideApartment(startCheckboxOutsideApartment.isSelected())
        .build();
  }

  private MoreSettings getMoreSettings() {
    return MoreSettings.newBuilder()
        .setMoreGuns(moreGuns.isSelected())
        .setPreySoulsGuns(morePreySoulsGuns.isSelected())
        .setPreySoulsChipsets(morePreySoulsChipsets.isSelected())
        .setPreySoulsEnemies(morePreySoulsEnemies.isSelected())
        .setPreySoulsTurrets(morePreySoulsTurrets.isSelected())
        .build();
  }

  private CheatSettings getCheatSettings() {
    return CheatSettings.newBuilder()
        .setOpenStation(cheatsCheckboxOpenStation.isSelected())
        .setUnlockAllScans(cheatsCheckboxAllScans.isSelected())
        .setFriendlyNpcs(cheatsCheckboxFriendlyNpcs.isSelected())
        .setUseCustomSpawn(cheatsCheckboxCustomStart.isSelected())
        .setCustomSpawnLocation(SpawnLocation.valueOf(cheatsChoiceBoxCustomStart.getValue()))
        .setGameTokenOverrides(cheatsTextFieldGameTokens.getText())
        .build();
  }

  private ExperimentalSettings getExperimentalSettings() {
    return ExperimentalSettings.newBuilder()
        .setWanderingHumans(expCheckboxWander.isSelected())
        .setLivingCorpses(expLivingCorpses.isSelected())
        .setStartSelfDestruct(expCheckboxSelfDestruct.isSelected())
        .setSelfDestructTimer(expTextFieldTimer.getText())
        .setSelfDestructShuttleTimer(expTextFieldShuttleTimer.getText())
        .setZeroGravityEverywhere(expCheckboxGravity.isSelected())
        .setEnableGravityInExtAndGuts(expCheckboxEnableGravity.isSelected())
        .build();
  }

  private Settings getSettings() {
    return Settings.newBuilder()
        .setReleaseVersion(Gui2Consts.VERSION)
        .setInstallDir(directoryText.getText())
        .setSeed(seedText.getText())
        .setCosmeticSettings(getCosmeticSettings())
        .setGameplaySettings(getGameplaySettings())
        .setGameStartSettings(getGameStartSettings())
        .setMoreSettings(getMoreSettings())
        .setCheatSettings(getCheatSettings())
        .setExpSettings(getExperimentalSettings())
        .build();
  }

  private AllPresets getAllPresets() throws IOException {
    FileReader fr = new FileReader(Gui2Consts.ALL_PRESETS_FILE);
    AllPresets.Builder builder = AllPresets.newBuilder();
    JsonFormat.parser().ignoringUnknownFields().merge(fr, builder);
    return builder.build();
  }

  private Settings getSavedSettings() throws IOException {
    // Parse the saved settings file and set new defaults accordingly
    FileReader fr = new FileReader(Gui2Consts.SAVED_SETTINGS_FILE);
    Settings.Builder builder = Settings.newBuilder();
    JsonFormat.parser().ignoringUnknownFields().merge(fr, builder);
    return builder.build();
  }

  private Settings createInitialSettings(AllPresets allPresets, Settings savedSettings) {
    if (savedSettings.getReleaseVersion().equals(Gui2Consts.VERSION)) {
      return savedSettings;
    } else {
      String loggerWarning = String.format(Gui2Consts.WARNING_SAVED_SETTINGS_VERSION_MISMATCH, savedSettings
          .getReleaseVersion(), Gui2Consts.VERSION);
      logger.warning(loggerWarning);
      if (savedSettings.getReleaseVersion().isEmpty()) {
        outputWindow.appendText("No saved settings found.\n");
      } else {
        outputWindow.appendText("Saved settings version mismatch.\n");
      }
      outputWindow.appendText("Falling back to default settings.\n");
    }

    long initialSeed = Utils.getNewSeed();
    return Settings.newBuilder()
        .setReleaseVersion(Gui2Consts.VERSION)
        .setInstallDir(Gui2Consts.DEFAULT_INSTALL_DIR)
        .setSeed(Long.toString(initialSeed))
        .setCosmeticSettings(CosmeticSettings.getDefaultInstance())
        .setGameplaySettings(GameplaySettings.getDefaultInstance())
        .setGameStartSettings(GameStartSettings.getDefaultInstance())
        .setMoreSettings(MoreSettings.getDefaultInstance())
        .setCheatSettings(CheatSettings.getDefaultInstance())
        .setExpSettings(ExperimentalSettings.getDefaultInstance())
        .build();
  }

  private void validateSpawnPresets(GenericSpawnPresetFilter gspj, String name) {
    if (gspj.getFiltersList() == null) {
      return;
    }
    for (int i = 0; i < gspj.getFiltersList().size(); i++) {
      GenericSpawnPresetRule gfj = gspj.getFilters(i);

      if (gfj.getOutputTagsList() == null || gfj.getOutputWeightsList() == null) {
        continue;
      }

      if (gfj.getOutputWeightsList().size() != 0 && gfj.getOutputTagsList().size() != gfj.getOutputWeightsList()
          .size()) {
        logger.info(String.format(
            "Invalid filter settings for %s spawns, preset name %s, filter %d. Output tags length (%d) and output weights length (%d) are not identical.",
            name, gspj.getName(), i, gfj.getOutputTagsList().size(), gfj.getOutputWeightsList().size()));
      }
    }

  }

}
