package gui.panels;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;

import gui.Consts;
import json.AllPresetsJson;
import json.CosmeticSettingsJson;
import json.GameplaySettingsJson;
import json.HasOptions;
import json.SpawnPresetJson;

public class OptionsPanel extends JPanel {
  private static final long serialVersionUID = 8281321276860893898L;

  public BaseRadioOptionsPanel<SpawnPresetJson> itemSpawnPanel;
  public BaseRadioOptionsPanel<SpawnPresetJson> enemySpawnPanel;
  public OtherOptionsPanel otherOptionsPanel;

  public OptionsPanel(AllPresetsJson allPresets, GameplaySettingsJson gameplaySettingsJson,
      CosmeticSettingsJson cosmeticSettingsJson) {
    itemSpawnPanel = new BaseRadioOptionsPanel<>(Consts.ITEM_SPAWN_PRESETS_LABEL, allPresets.getItemSpawnSettings(),
        gameplaySettingsJson.getItemSpawnSettings().getName());
    enemySpawnPanel = new BaseRadioOptionsPanel<>(Consts.NPC_SPAWN_PRESETS_LABEL, allPresets.getEnemySpawnSettings(),
        gameplaySettingsJson.getEnemySpawnSettings().getName());
    otherOptionsPanel = new OtherOptionsPanel(gameplaySettingsJson, cosmeticSettingsJson);

    this.setLayout(new GridLayout(0, 3));
    this.setAlignmentY(Component.TOP_ALIGNMENT);
    this.setBorder(BorderFactory.createEtchedBorder());
    this.add(itemSpawnPanel);
    this.add(enemySpawnPanel);
    this.add(otherOptionsPanel);
  }

  public CosmeticSettingsJson getCosmeticSettings() {
    return new CosmeticSettingsJson(otherOptionsPanel.isChecked(CosmeticSettingsJson.RANDOMIZE_BODIES),
        otherOptionsPanel.isChecked(CosmeticSettingsJson.RANDOMIZE_VOICELINES), false);
  }

  public GameplaySettingsJson getGameplaySettings() {
    return new GameplaySettingsJson(otherOptionsPanel.isChecked(GameplaySettingsJson.RANDOMIZE_LOOT), otherOptionsPanel
        .isChecked(GameplaySettingsJson.ADD_LOOT_TO_APARTMENT), otherOptionsPanel.isChecked(
            GameplaySettingsJson.OPEN_STATION), otherOptionsPanel.isChecked(GameplaySettingsJson.RANDOMIZE_NEUROMODS),
        otherOptionsPanel.isChecked(GameplaySettingsJson.UNLOCK_ALL_SCANS), otherOptionsPanel.isChecked(
            GameplaySettingsJson.RANDOMIZE_STATION), otherOptionsPanel.isChecked(GameplaySettingsJson.START_ON_2ND_DAY),
        otherOptionsPanel.isChecked(GameplaySettingsJson.MORE_GUNS), otherOptionsPanel.isChecked(
            GameplaySettingsJson.WANDERING_HUMANS), otherOptionsPanel.isChecked(
                GameplaySettingsJson.START_OUTSIDE_LOBBY), enemySpawnPanel.getValue(), itemSpawnPanel.getValue());
  }

  public static class OtherOptionsPanel extends JPanel {

    private static final long serialVersionUID = 6623786136413466883L;

    private static final ImmutableList<String> STARTING_CHECKBOXES = ImmutableList.of(
        GameplaySettingsJson.START_ON_2ND_DAY, GameplaySettingsJson.ADD_LOOT_TO_APARTMENT,
        GameplaySettingsJson.START_OUTSIDE_LOBBY);
    private static final ImmutableList<String> ITEMS_CHECKBOXES = ImmutableList.of(GameplaySettingsJson.RANDOMIZE_LOOT,
        GameplaySettingsJson.MORE_GUNS);
    private static final ImmutableList<String> CONNECTIVITY_CHECKBOXES = ImmutableList.of(
        GameplaySettingsJson.OPEN_STATION, GameplaySettingsJson.RANDOMIZE_STATION);
    private static final ImmutableList<String> NEUROMODS_CHECKBOXES = ImmutableList.of(
        GameplaySettingsJson.UNLOCK_ALL_SCANS, GameplaySettingsJson.RANDOMIZE_NEUROMODS);
    private static final ImmutableList<String> OTHER_CHECKBOXES = ImmutableList.of(
        GameplaySettingsJson.WANDERING_HUMANS);
    private static final ImmutableList<String> COSMETIC_CHECKBOXES = ImmutableList.of(
        CosmeticSettingsJson.RANDOMIZE_BODIES, CosmeticSettingsJson.RANDOMIZE_VOICELINES);

    // Map of unique checkbox name to its checkbox object
    private BiMap<String, JCheckBox> checkBoxHolder;

    public OtherOptionsPanel(GameplaySettingsJson gameplaySettingsJson, CosmeticSettingsJson cosmeticSettingsJson) {
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

      checkBoxHolder = HashBiMap.create();

      CheckboxPanel cosmeticCheckboxes = new CheckboxPanel(Consts.COSMETIC_OPTIONS_HEADER, COSMETIC_CHECKBOXES,
          CosmeticSettingsJson.ALL_OPTIONS, cosmeticSettingsJson, checkBoxHolder);

      CheckboxPanel apartmentCheckboxes = new CheckboxPanel(Consts.APARTMENT_OPTIONS_HEADER, STARTING_CHECKBOXES,
          GameplaySettingsJson.ALL_OPTIONS, gameplaySettingsJson, checkBoxHolder);
      CheckboxPanel neuromodsCheckboxes = new CheckboxPanel(Consts.NEUROMOD_OPTIONS_HEADER, NEUROMODS_CHECKBOXES,
          GameplaySettingsJson.ALL_OPTIONS, gameplaySettingsJson, checkBoxHolder);
      CheckboxPanel connectivityCheckboxes = new CheckboxPanel(Consts.CONNECTIVITY_OPTIONS_HEADER,
          CONNECTIVITY_CHECKBOXES, GameplaySettingsJson.ALL_OPTIONS, gameplaySettingsJson, checkBoxHolder);
      CheckboxPanel itemsCheckboxes = new CheckboxPanel(Consts.ITEM_OPTIONS_HEADER, ITEMS_CHECKBOXES,
          GameplaySettingsJson.ALL_OPTIONS, gameplaySettingsJson, checkBoxHolder);
      CheckboxPanel otherCheckboxes = new CheckboxPanel(Consts.OTHER_OPTIONS_HEADER, OTHER_CHECKBOXES,
          GameplaySettingsJson.ALL_OPTIONS, gameplaySettingsJson, checkBoxHolder);

      this.add(cosmeticCheckboxes);
      this.add(Box.createRigidArea(new Dimension(0, 20)));
      this.add(apartmentCheckboxes);
      this.add(Box.createRigidArea(new Dimension(0, 20)));
      this.add(itemsCheckboxes);
      this.add(Box.createRigidArea(new Dimension(0, 20)));
      this.add(neuromodsCheckboxes);
      this.add(Box.createRigidArea(new Dimension(0, 20)));
      this.add(connectivityCheckboxes);
      this.add(Box.createRigidArea(new Dimension(0, 20)));
      this.add(otherCheckboxes);
    }

    public boolean isChecked(String name) {
      return checkBoxHolder.containsKey(name) ? checkBoxHolder.get(name).isSelected() : false;
    }
  }

  public static class CheckboxPanel extends JPanel {
    private static final long serialVersionUID = -8193554674901911402L;

    public CheckboxPanel(String sectionName, List<String> checkboxes, Map<String, BaseCheckbox> checkboxReference,
        HasOptions initialOptions, BiMap<String, JCheckBox> checkBoxHolder) {
      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      this.setAlignmentX(Component.LEFT_ALIGNMENT);
      this.setAlignmentY(Component.TOP_ALIGNMENT);
      this.add(new JLabel(sectionName));
      this.add(Box.createRigidArea(new Dimension(0, 10)));

      for (String s : checkboxes) {
        BaseCheckbox info = checkboxReference.get(s);
        JCheckBox checkbox = new JCheckBox(info.getName());
        checkbox.setSelected(initialOptions.getOption(s));
        checkbox.setToolTipText(info.getDesc());
        checkBoxHolder.put(s, checkbox);
        this.add(checkbox);
      }
    }
  }
}
