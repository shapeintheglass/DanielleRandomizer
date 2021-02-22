package gui2.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
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
import proto.RandomizerSettings.CosmeticSettings;
import proto.RandomizerSettings.GameStartSettings;
import proto.RandomizerSettings.GenericSpawnPresetFilter;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import proto.RandomizerSettings.ItemSettings;
import proto.RandomizerSettings.NeuromodSettings;
import proto.RandomizerSettings.NpcSettings;
import proto.RandomizerSettings.Settings;
import proto.RandomizerSettings.StoryProgressionSettings;
import proto.RandomizerSettings.StoryProgressionSettings.SpawnLocation;
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

  /* COSMETICS TAB */
  @FXML
  private CheckBox cosmeticCheckboxBodies;
  @FXML
  private CheckBox cosmeticCheckboxVoices;
  @FXML
  private CheckBox cosmeticCheckboxMusic;
  @FXML
  private CheckBox cosmeticCheckboxPlayerModel;

  /* ITEMS TAB */
  @FXML
  private CheckBox itemsCheckboxMoreGuns;
  @FXML
  private CheckBox itemsCheckboxFabPlanCosts;
  @FXML
  private VBox itemsOptions;
  private ToggleGroup itemSpawnToggleGroup = new ToggleGroup();

  /* ENEMIES TAB */
  @FXML
  private CheckBox npcsCheckBoxNightmare;
  @FXML
  private CheckBox npcsCheckBoxCystoidNests;
  @FXML
  private CheckBox npcsCheckBoxWeavers;
  @FXML
  private VBox enemiesOptions;
  private ToggleGroup enemySpawnToggleGroup = new ToggleGroup();

  /* LOADOUTS TAB */
  @FXML
  private CheckBox startCheckboxDay2;
  @FXML
  private CheckBox startCheckboxAddAllEquipment;
  @FXML
  private CheckBox startCheckboxSkipJovan;

  /* NEUROMODS TAB */
  @FXML
  private CheckBox neuromodsCheckboxRandomize;

  /* STORY/PROGRESSION TAB */
  @FXML
  private CheckBox storyCheckboxRandomStation;
  @FXML
  private CheckBox storyCheckboxCustomStart;
  @FXML
  private ChoiceBox<String> storyChoiceBoxCustomStart;

  /* CHEATS TAB */
  @FXML
  private CheckBox cheatsCheckboxAllScans;
  @FXML
  private CheckBox cheatsCheckboxUnlockAll;
  @FXML
  private CheckBox cheatsCheckboxWander;
  @FXML
  private CheckBox cheatsCheckboxGravity;
  @FXML
  private CheckBox cheatsCheckboxEnableGravity;
  @FXML
  private CheckBox cheatsCheckboxSelfDestruct;
  @FXML
  private TextField cheatsTextFieldTimer;
  @FXML
  private TextField cheatsTextFieldShuttleTimer;

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
  private List<Node> allEntities;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setSettings(Settings settings) {
    this.settings = settings;
  }

  @FXML
  protected void initialize() {
    logger = Logger.getLogger("randomizer_gui");

    setTooltips();

    storyChoiceBoxCustomStart.getItems().add(SpawnLocation.RANDOM.name());
    for (SpawnLocation l : CustomSpawnGenerator.SUPPORTED_SPAWNS) {
      storyChoiceBoxCustomStart.getItems().add(l.name());
    }

    // Attempt to read from preset definition file and saved settings file

    try {
      allPresets = getAllPresets();
    } catch (IOException e) {
      outputWindow.appendText(String.format("Unable to parse %s\n", Gui2Consts.ALL_PRESETS_FILE));
      logger.info(String.format("An error occurred while parsing %s.", Gui2Consts.ALL_PRESETS_FILE));
      e.printStackTrace();
      allPresets = AllPresets.getDefaultInstance();
    }

    Settings savedSettings = Settings.getDefaultInstance();
    try {
      savedSettings = getSavedSettings();
    } catch (IOException e) {
      String loggerWarning = Gui2Consts.ERROR_COULD_NOT_PARSE_FILE + Gui2Consts.SAVED_SETTINGS_FILE + "\n";
      logger.warning(loggerWarning);
      outputWindow.appendText(loggerWarning);
      e.printStackTrace();
    }

    this.settings = createInitialSettings(allPresets, savedSettings);

    seedText.setText(settings.getSeed());
    directoryText.setText(settings.getInstallDir());

    allEntities = Lists.newArrayList(changeDirButton, newSeedButton, installButton, uninstallButton, clearButton,
        saveSettingsButton, closeButton, recommendedPresetButton, chaoticPresetButton, litePresetButton,
        gotsPresetButton);

    initCustomSpawnCheckboxes(allPresets, settings);

    outputWindow.appendText("Loaded with settings:\n" + SettingsHelper.settingsToString(settings));

    updateUI();

    storyCheckboxCustomStart.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (event.getSource() instanceof CheckBox) {
          boolean isCustomStart = storyCheckboxCustomStart.isSelected();
          storyChoiceBoxCustomStart.setDisable(!isCustomStart);
        }
      }
    });

    cheatsCheckboxSelfDestruct.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (event.getSource() instanceof CheckBox) {
          boolean isSelfDestruct = cheatsCheckboxSelfDestruct.isSelected();
          cheatsTextFieldTimer.setDisable(!isSelfDestruct);
          cheatsTextFieldShuttleTimer.setDisable(!isSelfDestruct);
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
    chaoticPresetButton.setTooltip(new Tooltip("Updates this UI to the preset \"chaotic\" experience."));
    litePresetButton.setTooltip(new Tooltip("Updates this UI to the preset \"lite\" experience."));
    gotsPresetButton.setTooltip(new Tooltip("Updates this UI to the preset \"timed\" experience."));

    itemsCheckboxMoreGuns.setTooltip(new Tooltip("Adds additional weapons from \"More Guns\" to the item pool."));

    installButton.setTooltip(new Tooltip("Generates randomized game and installs to the game directory."));
    uninstallButton.setTooltip(new Tooltip(
        "Removes any mods added by this installer and reverts changes to the game directory."));
    clearButton.setTooltip(new Tooltip("Clears the output window."));
    saveSettingsButton.setTooltip(new Tooltip(
        "Saves the current settings to a file so that they are the default the next time this UI is opened."));
    closeButton.setTooltip(new Tooltip("Closes this UI."));
  }

  private void updateUI() {
    setSpawnCheckbox(itemSpawnToggleGroup, settings.getItemSettings().getItemSpawnSettings().getName());
    setSpawnCheckbox(enemySpawnToggleGroup, settings.getNpcSettings().getEnemySpawnSettings().getName());

    cosmeticCheckboxBodies.setSelected(settings.getCosmeticSettings().getRandomizeBodies());
    cosmeticCheckboxVoices.setSelected(settings.getCosmeticSettings().getRandomizeVoicelines());
    cosmeticCheckboxMusic.setSelected(settings.getCosmeticSettings().getRandomizeMusic());
    cosmeticCheckboxPlayerModel.setSelected(settings.getCosmeticSettings().getRandomizePlayerModel());

    itemsCheckboxMoreGuns.setSelected(settings.getItemSettings().getMoreGuns());
    itemsCheckboxFabPlanCosts.setSelected(settings.getItemSettings().getRandomizeFabPlanCosts());

    npcsCheckBoxCystoidNests.setSelected(settings.getNpcSettings().getRandomizeCystoidNests());
    npcsCheckBoxNightmare.setSelected(settings.getNpcSettings().getRandomizeNightmare());
    npcsCheckBoxWeavers.setSelected(settings.getNpcSettings().getRandomizeWeaverCystoids());

    startCheckboxDay2.setSelected(settings.getGameStartSettings().getStartOnSecondDay());
    startCheckboxAddAllEquipment.setSelected(settings.getGameStartSettings().getAddLootToApartment());
    startCheckboxSkipJovan.setSelected(settings.getGameStartSettings().getSkipJovanCutscene());

    neuromodsCheckboxRandomize.setSelected(settings.getNeuromodSettings().getRandomizeNeuromods());

    storyCheckboxRandomStation.setSelected(settings.getStoryProgressionSettings().getRandomizeStation());
    storyCheckboxCustomStart.setSelected(settings.getStoryProgressionSettings().getUseCustomSpawn());
    storyChoiceBoxCustomStart.setDisable(!settings.getStoryProgressionSettings().getUseCustomSpawn());
    storyChoiceBoxCustomStart.setValue(settings.getStoryProgressionSettings().getCustomSpawnLocation().name());

    cheatsCheckboxUnlockAll.setSelected(settings.getCheatSettings().getOpenStation());
    cheatsCheckboxAllScans.setSelected(settings.getCheatSettings().getUnlockAllScans());
    cheatsCheckboxWander.setSelected(settings.getCheatSettings().getWanderingHumans());
    cheatsCheckboxGravity.setSelected(settings.getCheatSettings().getZeroGravityEverywhere());
    cheatsCheckboxEnableGravity.setSelected(settings.getCheatSettings().getEnableGravityInExtAndGuts());
    cheatsCheckboxSelfDestruct.setSelected(settings.getCheatSettings().getStartSelfDestruct());
    cheatsTextFieldTimer.setText(settings.getCheatSettings().getSelfDestructTimer());
    cheatsTextFieldShuttleTimer.setText(settings.getCheatSettings().getSelfDestructShuttleTimer());

    cheatsTextFieldTimer.setDisable(!cheatsCheckboxSelfDestruct.isSelected());
    cheatsTextFieldShuttleTimer.setDisable(!cheatsCheckboxSelfDestruct.isSelected());
  }

  private void resetUI() {
    cosmeticCheckboxBodies.setSelected(false);
    cosmeticCheckboxVoices.setSelected(false);
    cosmeticCheckboxMusic.setSelected(false);
    cosmeticCheckboxPlayerModel.setSelected(false);
    itemsCheckboxMoreGuns.setSelected(false);
    itemsCheckboxFabPlanCosts.setSelected(false);
    setSpawnCheckbox(itemSpawnToggleGroup, "No item randomization");
    npcsCheckBoxNightmare.setSelected(false);
    npcsCheckBoxCystoidNests.setSelected(false);
    npcsCheckBoxWeavers.setSelected(false);
    setSpawnCheckbox(enemySpawnToggleGroup, "No NPC randomization");
    startCheckboxDay2.setSelected(false);
    startCheckboxAddAllEquipment.setSelected(false);
    startCheckboxSkipJovan.setSelected(false);
    neuromodsCheckboxRandomize.setSelected(false);
    storyCheckboxRandomStation.setSelected(false);
    storyCheckboxCustomStart.setSelected(false);
    storyChoiceBoxCustomStart.setValue(SpawnLocation.NONE.name());
    cheatsCheckboxAllScans.setSelected(false);
    cheatsCheckboxUnlockAll.setSelected(false);
    cheatsCheckboxWander.setSelected(false);
    cheatsCheckboxGravity.setSelected(false);
    cheatsCheckboxEnableGravity.setSelected(false);
    cheatsCheckboxSelfDestruct.setSelected(false);
    cheatsTextFieldTimer.setText(Gui2Consts.DEFAULT_SELF_DESTRUCT_TIMER);
    cheatsTextFieldShuttleTimer.setText(Gui2Consts.DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER);
  }

  private void initCustomSpawnCheckboxes(AllPresets allPresets, Settings settings) {
    // Add custom presets

    for (GenericSpawnPresetFilter spj : allPresets.getItemSpawnSettingsList()) {
      RadioButton rb = new RadioButton(spj.getName());
      rb.setToggleGroup(itemSpawnToggleGroup);
      rb.setTooltip(new Tooltip(spj.getDesc()));
      if (settings.getItemSettings().getItemSpawnSettings().getName().equals(spj.getName())) {
        rb.setSelected(true);
      }
      itemsOptions.getChildren().add(rb);
    }

    for (GenericSpawnPresetFilter spj : allPresets.getEnemySpawnSettingsList()) {
      RadioButton rb = new RadioButton(spj.getName());
      rb.setToggleGroup(enemySpawnToggleGroup);
      rb.setTooltip(new Tooltip(spj.getDesc()));
      if (settings.getNpcSettings().getEnemySpawnSettings().getName().equals(spj.getName())) {
        rb.setSelected(true);
      }
      enemiesOptions.getChildren().add(rb);
    }
  }

  private void setSpawnCheckbox(ToggleGroup tg, String name) {
    for (Toggle t : tg.getToggles()) {
      RadioButton rb = (RadioButton) t;
      if (rb.getText().equals(name)) {
        rb.setSelected(true);
      } else {
        rb.setSelected(false);
      }
    }
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
    setSpawnCheckbox(itemSpawnToggleGroup, "Randomize items");
    setSpawnCheckbox(enemySpawnToggleGroup, "Randomize enemies");
    neuromodsCheckboxRandomize.setSelected(true);
    outputWindow.clear();
    outputWindow.appendText("Recommended preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, SettingsHelper.settingsToString(getSettings())));
  }

  @FXML
  protected void onPresetsChaoticClicked(ActionEvent event) {
    resetUI();
    cosmeticCheckboxBodies.setSelected(true);
    cosmeticCheckboxVoices.setSelected(true);
    cosmeticCheckboxMusic.setSelected(true);
    cosmeticCheckboxPlayerModel.setSelected(true);
    itemsCheckboxMoreGuns.setSelected(true);
    setSpawnCheckbox(itemSpawnToggleGroup, "Randomize items (chaotic)");
    setSpawnCheckbox(enemySpawnToggleGroup, "Randomize enemies (chaotic)");
    neuromodsCheckboxRandomize.setSelected(true);
    storyCheckboxRandomStation.setSelected(true);
    outputWindow.clear();
    outputWindow.appendText("Chaotic preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, SettingsHelper.settingsToString(getSettings())));
  }

  @FXML
  protected void onPresetsLiteClicked(ActionEvent event) {
    resetUI();
    setSpawnCheckbox(itemSpawnToggleGroup, "Randomize items within type");
    setSpawnCheckbox(enemySpawnToggleGroup, "Randomize enemies within type");
    outputWindow.clear();
    outputWindow.appendText("Lite preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, SettingsHelper.settingsToString(getSettings())));
  }

  @FXML
  protected void onPresetsGotsClicked(ActionEvent event) {
    resetUI();
    startCheckboxDay2.setSelected(true);
    startCheckboxSkipJovan.setSelected(true);
    cheatsCheckboxSelfDestruct.setSelected(true);
    cheatsTextFieldTimer.setText(Gui2Consts.DEFAULT_SELF_DESTRUCT_TIMER);
    cheatsTextFieldShuttleTimer.setText(Gui2Consts.DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER);
    outputWindow.clear();
    outputWindow.appendText("G.O.T.S. preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, SettingsHelper.settingsToString(getSettings())));
  }

  /*
   * LOWER BUTTONS
   */

  @FXML
  protected void handleInstallClicked(ActionEvent event) {
    Settings finalSettings = getSettings();

    // TODO: Add a check here to see if nothing will be installed
    validateSpawnPresets(finalSettings.getItemSettings().getItemSpawnSettings(), "items");
    validateSpawnPresets(finalSettings.getNpcSettings().getEnemySpawnSettings(), "enemies");

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
    for (Node b : allEntities) {
      b.setDisable(disable);
    }
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

  private void writeLastUsedSettingsToFile(String savedSettingsFilePath, Settings toSave)
      throws JsonGenerationException, JsonMappingException, IOException {
    File savedSettingsFile = new File(savedSettingsFilePath);
    savedSettingsFile.createNewFile();
    String serialized = JsonFormat.printer().includingDefaultValueFields().preservingProtoFieldNames().print(toSave);
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(savedSettingsFile));) {
      bw.write(serialized);
    }
  }

  private CosmeticSettings getCosmeticSettings() {
    return CosmeticSettings.newBuilder()
        .setRandomizeBodies(cosmeticCheckboxBodies.isSelected())
        .setRandomizeVoicelines(cosmeticCheckboxVoices.isSelected())
        .setRandomizeMusic(cosmeticCheckboxMusic.isSelected())
        .setRandomizePlayerModel(cosmeticCheckboxPlayerModel.isSelected())
        .build();
  }

  private ItemSettings getItemSettings() {
    GenericSpawnPresetFilter itemSpawnPreset = GenericSpawnPresetFilter.getDefaultInstance();
    RadioButton selectedItemSpawn = (RadioButton) itemSpawnToggleGroup.getSelectedToggle();
    String itemToSelect = selectedItemSpawn == null ? "" : selectedItemSpawn.getText();
    itemSpawnPreset = getSpawnPresetFromList(allPresets.getItemSpawnSettingsList(), itemToSelect);

    return ItemSettings.newBuilder()
        .setItemSpawnSettings(itemSpawnPreset)
        .setMoreGuns(itemsCheckboxMoreGuns.isSelected())
        .setRandomizeFabPlanCosts(itemsCheckboxFabPlanCosts.isSelected())
        .build();
  }

  private NpcSettings getNpcSettings() {
    GenericSpawnPresetFilter enemySpawnPreset = GenericSpawnPresetFilter.getDefaultInstance();
    RadioButton selectedEnemySpawn = (RadioButton) enemySpawnToggleGroup.getSelectedToggle();
    String enemyToSelect = selectedEnemySpawn == null ? "" : selectedEnemySpawn.getText();
    enemySpawnPreset = getSpawnPresetFromList(allPresets.getEnemySpawnSettingsList(), enemyToSelect);

    return NpcSettings.newBuilder()
        .setEnemySpawnSettings(enemySpawnPreset)
        .setRandomizeNightmare(npcsCheckBoxNightmare.isSelected())
        .setRandomizeCystoidNests(npcsCheckBoxCystoidNests.isSelected())
        .setRandomizeWeaverCystoids(npcsCheckBoxWeavers.isSelected())
        .build();
  }

  private NeuromodSettings getNeuromodSettings() {
    return NeuromodSettings.newBuilder().setRandomizeNeuromods(neuromodsCheckboxRandomize.isSelected()).build();
  }

  private StoryProgressionSettings getStoryProgressionSettings() {
    return StoryProgressionSettings.newBuilder()
        .setRandomizeStation(storyCheckboxRandomStation.isSelected())
        .setUseCustomSpawn(storyCheckboxCustomStart.isSelected())
        .setCustomSpawnLocation(SpawnLocation.valueOf(storyChoiceBoxCustomStart.getValue()))
        .build();
  }

  private GameStartSettings getGameStartSettings() {
    return GameStartSettings.newBuilder()
        .setAddLootToApartment(startCheckboxAddAllEquipment.isSelected())
        .setStartOnSecondDay(startCheckboxDay2.isSelected())
        .setSkipJovanCutscene(startCheckboxSkipJovan.isSelected())
        .build();
  }

  private CheatSettings getCheatSettings() {
    return CheatSettings.newBuilder()
        .setOpenStation(cheatsCheckboxUnlockAll.isSelected())
        .setUnlockAllScans(cheatsCheckboxAllScans.isSelected())
        .setWanderingHumans(cheatsCheckboxWander.isSelected())
        .setStartSelfDestruct(cheatsCheckboxSelfDestruct.isSelected())
        .setSelfDestructTimer(cheatsTextFieldTimer.getText())
        .setSelfDestructShuttleTimer(cheatsTextFieldShuttleTimer.getText())
        .setZeroGravityEverywhere(cheatsCheckboxGravity.isSelected())
        .setEnableGravityInExtAndGuts(cheatsCheckboxEnableGravity.isSelected())
        .build();
  }

  private static GenericSpawnPresetFilter getSpawnPresetFromList(List<GenericSpawnPresetFilter> presetList,
      String name) {
    if (presetList == null || presetList.isEmpty()) {
      return null;
    }
    for (GenericSpawnPresetFilter s : presetList) {
      if (s.getName().equals(name)) {
        return s;
      }
    }
    return presetList.get(0);
  }

  private Settings getSettings() {
    return Settings.newBuilder()
        .setReleaseVersion(Gui2Consts.VERSION)
        .setInstallDir(directoryText.getText())
        .setSeed(seedText.getText())
        .setCosmeticSettings(getCosmeticSettings())
        .setItemSettings(getItemSettings())
        .setNpcSettings(getNpcSettings())
        .setNeuromodSettings(getNeuromodSettings())
        .setStoryProgressionSettings(getStoryProgressionSettings())
        .setGameStartSettings(getGameStartSettings())
        .setCheatSettings(getCheatSettings())
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
      if (settings.getReleaseVersion().isEmpty()) {
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
        .setItemSettings(ItemSettings.getDefaultInstance())
        .setNpcSettings(NpcSettings.getDefaultInstance())
        .setNeuromodSettings(NeuromodSettings.getDefaultInstance())
        .setStoryProgressionSettings(StoryProgressionSettings.getDefaultInstance())
        .setGameStartSettings(GameStartSettings.getDefaultInstance())
        .setCheatSettings(CheatSettings.getDefaultInstance())
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
