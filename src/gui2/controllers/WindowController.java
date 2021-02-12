package gui2.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import gui2.Gui2Consts;
import gui2.InstallService;
import installers.Installer;
import javafx.application.Platform;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import json.AllPresetsJson;
import json.CosmeticSettingsJson;
import json.GameplaySettingsJson;
import json.GenericRuleJson;
import json.SettingsJson;
import json.SpawnPresetJson;
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

  /* COSMETICS TAB */
  @FXML
  private CheckBox cosmeticCheckboxBodies;
  @FXML
  private CheckBox cosmeticCheckboxVoices;
  @FXML
  private CheckBox cosmeticCheckboxMusic;

  /* ITEMS TAB */
  @FXML
  private CheckBox itemsCheckboxMoreGuns;
  @FXML
  private CheckBox itemsCheckboxLootTables;
  @FXML
  private VBox itemsOptions;
  private ToggleGroup itemSpawnToggleGroup = new ToggleGroup();

  /* ENEMIES TAB */
  @FXML
  private VBox enemiesOptions;
  private ToggleGroup enemySpawnToggleGroup = new ToggleGroup();

  /* LOADOUTS TAB */
  @FXML
  private CheckBox startCheckboxDay2;
  @FXML
  private CheckBox startCheckboxAddAllEquipment;

  /* NEUROMODS TAB */
  @FXML
  private CheckBox neuromodsCheckboxRandomize;

  /* STORY/PROGRESSION TAB */
  @FXML
  private CheckBox storyCheckboxRandomStation;

  /* CHEATS TAB */
  @FXML
  private CheckBox cheatsCheckboxAllScans;
  @FXML
  private CheckBox cheatsCheckboxUnlockAll;
  @FXML
  private CheckBox cheatsCheckboxWander;
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
  private SettingsJson settings;
  private Optional<AllPresetsJson> allPresets;
  private Logger logger;
  private List<Node> allEntities;

  public void setStage(Stage stage) {
    this.stage = stage;
  }

  public void setSettings(SettingsJson settings) {
    this.settings = settings;
  }

  @FXML
  protected void initialize() {
    logger = Logger.getLogger("randomizer_gui");

    // Attempt to read from preset definition file and saved settings file
    allPresets = getSpawnPresets();

    if (!allPresets.isPresent()) {
      logger.info(String.format("An error occurred while parsing %s.", Gui2Consts.ALL_PRESETS_FILE));
    }

    Optional<SettingsJson> savedSettings = getSavedSettings();

    this.settings = createInitialSettings(allPresets, savedSettings);

    seedText.setText(Long.toString(settings.getSeed()));
    directoryText.setText(settings.getInstallDir());

    allEntities = Lists.newArrayList(changeDirButton, newSeedButton, installButton, uninstallButton, clearButton,
        saveSettingsButton, closeButton, recommendedPresetButton, chaoticPresetButton, litePresetButton);

    updateUI();

    initCustomSpawnCheckboxes(allPresets, settings);

    cheatsCheckboxSelfDestruct.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        if (event.getSource() instanceof CheckBox) {
          CheckBox c = (CheckBox) event.getSource();
          boolean isSelfDestruct = c.isSelected();
          cheatsTextFieldTimer.setDisable(!isSelfDestruct);
          cheatsTextFieldShuttleTimer.setDisable(!isSelfDestruct);
        }
      }
    });
  }

  private void updateUI() {
    cosmeticCheckboxBodies.setSelected(settings.getCosmeticSettings().getRandomizeBodies());
    cosmeticCheckboxVoices.setSelected(settings.getCosmeticSettings().getRandomizeVoiceLines());
    cosmeticCheckboxMusic.setSelected(settings.getCosmeticSettings().getRandomizeMusic());

    itemsCheckboxMoreGuns.setSelected(settings.getGameplaySettings().getMoreGuns());
    itemsCheckboxLootTables.setSelected(settings.getGameplaySettings().getRandomizeLoot());

    startCheckboxDay2.setSelected(settings.getGameplaySettings().getStartOn2ndDay());
    startCheckboxAddAllEquipment.setSelected(settings.getGameplaySettings().getAddLootToApartment());

    neuromodsCheckboxRandomize.setSelected(settings.getGameplaySettings().getRandomizeNeuromods());

    storyCheckboxRandomStation.setSelected(settings.getGameplaySettings().getRandomizeStation());

    cheatsCheckboxUnlockAll.setSelected(settings.getGameplaySettings().getOpenStation());
    cheatsCheckboxAllScans.setSelected(settings.getGameplaySettings().getUnlockAllScans());
    cheatsCheckboxWander.setSelected(settings.getGameplaySettings().getWanderingHumans());
    cheatsCheckboxSelfDestruct.setSelected(settings.getGameplaySettings().getStartSelfDestruct());
    cheatsTextFieldTimer.setText(settings.getGameplaySettings().getSelfDestructTimer());
    cheatsTextFieldShuttleTimer.setText(settings.getGameplaySettings().getSelfDestructShuttleTimer());
  }

  private void initCustomSpawnCheckboxes(Optional<AllPresetsJson> allPresets, SettingsJson settings) {
    // Add custom presets
    if (!allPresets.isPresent()) {
      return;
    }

    for (SpawnPresetJson spj : allPresets.get().getItemSpawnSettings()) {
      RadioButton rb = new RadioButton(spj.getName());
      rb.setToggleGroup(itemSpawnToggleGroup);
      rb.setTooltip(new Tooltip(spj.getDesc()));
      if (settings.getGameplaySettings().getItemSpawnSettings().getName().equals(spj.getName())) {
        rb.setSelected(true);
      }
      itemsOptions.getChildren().add(rb);
    }

    for (SpawnPresetJson spj : allPresets.get().getEnemySpawnSettings()) {
      RadioButton rb = new RadioButton(spj.getName());
      rb.setToggleGroup(enemySpawnToggleGroup);
      rb.setTooltip(new Tooltip(spj.getDesc()));
      if (settings.getGameplaySettings().getEnemySpawnSettings().getName().equals(spj.getName())) {
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
    // TODO: Make this less gross
    cosmeticCheckboxBodies.setSelected(true);
    cosmeticCheckboxVoices.setSelected(true);
    cosmeticCheckboxMusic.setSelected(true);
    itemsCheckboxLootTables.setSelected(true);
    itemsCheckboxMoreGuns.setSelected(false);
    setSpawnCheckbox(itemSpawnToggleGroup, "Randomize items");
    setSpawnCheckbox(enemySpawnToggleGroup, "Randomize enemies");
    startCheckboxDay2.setSelected(false);
    startCheckboxAddAllEquipment.setSelected(false);
    neuromodsCheckboxRandomize.setSelected(false);
    storyCheckboxRandomStation.setSelected(false);
    cheatsCheckboxAllScans.setSelected(false);
    cheatsCheckboxUnlockAll.setSelected(false);
    cheatsCheckboxWander.setSelected(false);
    cheatsCheckboxSelfDestruct.setSelected(false);
    cheatsTextFieldTimer.setText(GameplaySettingsJson.DEFAULT_SELF_DESTRUCT_TIMER);
    cheatsTextFieldShuttleTimer.setText(GameplaySettingsJson.DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER);
    outputWindow.clear();
    outputWindow.appendText("Recommended preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, getSettings().toString()));
  }

  @FXML
  protected void onPresetsChaoticClicked(ActionEvent event) {
    // TODO: Make this less gross
    cosmeticCheckboxBodies.setSelected(true);
    cosmeticCheckboxVoices.setSelected(true);
    cosmeticCheckboxMusic.setSelected(true);
    itemsCheckboxLootTables.setSelected(true);
    itemsCheckboxMoreGuns.setSelected(true);
    setSpawnCheckbox(itemSpawnToggleGroup, "Randomize items (chaotic)");
    setSpawnCheckbox(enemySpawnToggleGroup, "Randomize enemies (chaotic)");
    startCheckboxDay2.setSelected(false);
    startCheckboxAddAllEquipment.setSelected(false);
    neuromodsCheckboxRandomize.setSelected(true);
    storyCheckboxRandomStation.setSelected(true);
    cheatsCheckboxAllScans.setSelected(false);
    cheatsCheckboxUnlockAll.setSelected(false);
    cheatsCheckboxWander.setSelected(false);
    cheatsCheckboxSelfDestruct.setSelected(false);
    cheatsTextFieldTimer.setText(GameplaySettingsJson.DEFAULT_SELF_DESTRUCT_TIMER);
    cheatsTextFieldShuttleTimer.setText(GameplaySettingsJson.DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER);
    outputWindow.clear();
    outputWindow.appendText("Chaotic preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, getSettings().toString()));
  }

  @FXML
  protected void onPresetsLiteClicked(ActionEvent event) {
    // TODO: Make this less gross
    cosmeticCheckboxBodies.setSelected(false);
    cosmeticCheckboxVoices.setSelected(false);
    cosmeticCheckboxMusic.setSelected(false);
    itemsCheckboxLootTables.setSelected(true);
    itemsCheckboxMoreGuns.setSelected(false);
    setSpawnCheckbox(itemSpawnToggleGroup, "Randomize items within type");
    setSpawnCheckbox(enemySpawnToggleGroup, "Randomize enemies within type");
    startCheckboxDay2.setSelected(false);
    startCheckboxAddAllEquipment.setSelected(false);
    neuromodsCheckboxRandomize.setSelected(false);
    storyCheckboxRandomStation.setSelected(false);
    cheatsCheckboxAllScans.setSelected(false);
    cheatsCheckboxUnlockAll.setSelected(false);
    cheatsCheckboxWander.setSelected(false);
    cheatsCheckboxSelfDestruct.setSelected(false);
    cheatsTextFieldTimer.setText(GameplaySettingsJson.DEFAULT_SELF_DESTRUCT_TIMER);
    cheatsTextFieldShuttleTimer.setText(GameplaySettingsJson.DEFAULT_SELF_DESTRUCT_SHUTTLE_TIMER);
    outputWindow.clear();
    outputWindow.appendText("Lite preset selected.\n");
    outputWindow.appendText(String.format(Gui2Consts.PRESET_INFO, getSettings().toString()));
  }

  /*
   * LOWER BUTTONS
   */

  @FXML
  protected void handleInstallClicked(ActionEvent event) {
    SettingsJson finalSettings = getSettings();
    try {
      writeLastUsedSettingsToFile(Gui2Consts.LAST_USED_SETTINGS_FILE, finalSettings);
    } catch (IOException e) {
      e.printStackTrace();
    }
    InstallService installService = new InstallService(outputWindow, finalSettings);
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
    SettingsJson toSave = getSettings();
    try {
      writeLastUsedSettingsToFile(Gui2Consts.SAVED_SETTINGS_FILE, toSave);
      outputWindow.appendText(String.format(Gui2Consts.SAVING_INFO, Gui2Consts.SAVED_SETTINGS_FILE));
    } catch (IOException e) {
      outputWindow.appendText(Gui2Consts.SAVING_FAILED);
      e.printStackTrace();
    }
  }

  private void writeLastUsedSettingsToFile(String savedSettingsFilePath, SettingsJson toSave)
      throws JsonGenerationException, JsonMappingException, IOException {
    File savedSettingsFile = new File(savedSettingsFilePath);
    savedSettingsFile.createNewFile();
    ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    mapper.writerFor(SettingsJson.class).writeValue(savedSettingsFile, toSave);
  }

  private CosmeticSettingsJson getCosmeticSettings() {
    return new CosmeticSettingsJson(cosmeticCheckboxBodies.isSelected(), cosmeticCheckboxVoices.isSelected(),
        cosmeticCheckboxMusic.isSelected());
  }

  private GameplaySettingsJson getGameplaySettings() {
    RadioButton selectedItemSpawn = (RadioButton) itemSpawnToggleGroup.getSelectedToggle();
    String itemToSelect = selectedItemSpawn == null ? "" : selectedItemSpawn.getText();
    SpawnPresetJson itemSpawnPreset = getSpawnPresetFromList(allPresets.get().getItemSpawnSettings(), itemToSelect);

    RadioButton selectedEnemySpawn = (RadioButton) enemySpawnToggleGroup.getSelectedToggle();
    String enemyToSelect = selectedEnemySpawn == null ? "" : selectedEnemySpawn.getText();
    SpawnPresetJson enemySpawnPreset = getSpawnPresetFromList(allPresets.get().getEnemySpawnSettings(), enemyToSelect);

    return new GameplaySettingsJson(itemsCheckboxLootTables.isSelected(), startCheckboxAddAllEquipment.isSelected(),
        cheatsCheckboxUnlockAll.isSelected(), neuromodsCheckboxRandomize.isSelected(), cheatsCheckboxAllScans
            .isSelected(), storyCheckboxRandomStation.isSelected(), startCheckboxDay2.isSelected(),
        itemsCheckboxMoreGuns.isSelected(), cheatsCheckboxWander.isSelected(), cheatsCheckboxSelfDestruct.isSelected(),
        cheatsTextFieldTimer.getText(), cheatsTextFieldShuttleTimer.getText(), enemySpawnPreset, itemSpawnPreset);
  }

  private static SpawnPresetJson getSpawnPresetFromList(List<SpawnPresetJson> presetList, String name) {
    if (presetList == null || presetList.isEmpty()) {
      return null;
    }
    for (SpawnPresetJson s : presetList) {
      if (s.getName().equals(name)) {
        return s;
      }
    }
    return null;
  }

  private SettingsJson getSettings() {
    SettingsJson settings = new SettingsJson(Gui2Consts.VERSION, directoryText.getText(), Long.parseLong(seedText
        .getText()), getCosmeticSettings(), getGameplaySettings());
    return settings;
  }

  private Optional<AllPresetsJson> getSpawnPresets() {
    try {
      AllPresetsJson allPresets = new AllPresetsJson(Gui2Consts.ALL_PRESETS_FILE);
      validateSpawnPresets(allPresets.getItemSpawnSettings(), "item");
      validateSpawnPresets(allPresets.getEnemySpawnSettings(), "enemy");
      return Optional.of(allPresets);
    } catch (IOException e1) {
      logger.warning(Gui2Consts.ERROR_COULD_NOT_PARSE_FILE + Gui2Consts.ALL_PRESETS_FILE);
      e1.printStackTrace();
    }
    return Optional.absent();
  }

  private Optional<SettingsJson> getSavedSettings() {
    // Parse the saved settings file and set new defaults accordingly
    File lastUsedSettingsFile = new File(Gui2Consts.SAVED_SETTINGS_FILE);
    if (lastUsedSettingsFile.exists()) {
      try {
        return Optional.of(new SettingsJson(Gui2Consts.SAVED_SETTINGS_FILE));
      } catch (Exception e) {
        logger.warning(Gui2Consts.ERROR_COULD_NOT_PARSE_FILE + Gui2Consts.SAVED_SETTINGS_FILE);
        e.printStackTrace();
      }
    }
    return Optional.absent();
  }

  private SettingsJson createInitialSettings(Optional<AllPresetsJson> spawnPresets,
      Optional<SettingsJson> savedSettings) {
    if (savedSettings.isPresent()) {
      if (savedSettings.get().getReleaseVersion().equals(Gui2Consts.VERSION)) {
        return savedSettings.get();
      } else {
        logger.warning(String.format(Gui2Consts.WARNING_SAVED_SETTINGS_VERSION_MISMATCH, savedSettings.get()
            .getReleaseVersion(), Gui2Consts.VERSION));
      }
    }

    SpawnPresetJson itemPreset = spawnPresets.isPresent() ? spawnPresets.get().getItemSpawnSettings().get(0) : null;
    SpawnPresetJson enemyPreset = spawnPresets.isPresent() ? spawnPresets.get().getEnemySpawnSettings().get(0) : null;

    long initialSeed = Utils.getNewSeed();
    return new SettingsJson(Gui2Consts.VERSION, Gui2Consts.DEFAULT_INSTALL_DIR, initialSeed, new CosmeticSettingsJson(),
        new GameplaySettingsJson(itemPreset, enemyPreset));
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

}
