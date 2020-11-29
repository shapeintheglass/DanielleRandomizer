package json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import gui.BaseCheckbox;

public class CosmeticSettingsJson implements HasOptions {
  public static final String RANDOMIZE_BODIES = "randomize_bodies";
  public static final String RANDOMIZE_VOICELINES = "randomize_voicelines";
  public static final String RANDOMIZE_WEAPON_APPEARANCE = "randomize_weapon_appearance";

  public static final ImmutableMap<String, BaseCheckbox> ALL_OPTIONS =
      new ImmutableMap.Builder<String, BaseCheckbox>()
          .put(RANDOMIZE_BODIES,
              new BaseCheckbox("Randomize bodies", "Randomizes the appearance of all human NPCs",
                  true))
          .put(RANDOMIZE_VOICELINES,
              new BaseCheckbox("Randomize voicelines", "Shuffles all voice lines by voice actor",
                  true))
          .put(RANDOMIZE_WEAPON_APPEARANCE,
              new BaseCheckbox("Randomize weapon appearance",
                  "Randomizes what weapons can look like", false))
          .build();


  private Map<String, Boolean> booleanSettings;

  @JsonCreator
  public CosmeticSettingsJson(@JsonProperty(RANDOMIZE_BODIES) boolean randomizeBodies,
      @JsonProperty(RANDOMIZE_VOICELINES) boolean randomizeVoicelines,
      @JsonProperty(RANDOMIZE_WEAPON_APPEARANCE) boolean randomizeWeaponAppearance) {
    booleanSettings = Maps.newHashMap();
    booleanSettings.put(RANDOMIZE_BODIES, randomizeBodies);
    booleanSettings.put(RANDOMIZE_VOICELINES, randomizeVoicelines);
    booleanSettings.put(RANDOMIZE_WEAPON_APPEARANCE, randomizeWeaponAppearance);
  }

  public CosmeticSettingsJson() {
    booleanSettings = new HashMap<>();
    for (String s : ALL_OPTIONS.keySet()) {
      booleanSettings.put(s, ALL_OPTIONS.get(s)
          .getDefaultValue());
    }
  }

  public CosmeticSettingsJson(JsonNode node) {
    booleanSettings = new HashMap<>();
    booleanSettings.put(RANDOMIZE_BODIES, node.get(RANDOMIZE_BODIES)
        .asBoolean());
    booleanSettings.put(RANDOMIZE_VOICELINES, node.get(RANDOMIZE_VOICELINES)
        .asBoolean());
    booleanSettings.put(RANDOMIZE_WEAPON_APPEARANCE, node.get(RANDOMIZE_WEAPON_APPEARANCE)
        .asBoolean());
  }

  public boolean getOption(String name) {
    return booleanSettings.get(name);
  }

  public void toggleOption(String name) {
    booleanSettings.put(name, !booleanSettings.get(name));
  }

  @JsonProperty(RANDOMIZE_BODIES)
  public boolean getRandomizeBodies() {
    return getOption(RANDOMIZE_BODIES);
  }

  @JsonProperty(RANDOMIZE_VOICELINES)
  public boolean getRandomizeVoiceLines() {
    return getOption(RANDOMIZE_VOICELINES);
  }

  @JsonProperty(RANDOMIZE_WEAPON_APPEARANCE)
  public boolean getRandomizeWeaponAppearance() {
    return getOption(RANDOMIZE_WEAPON_APPEARANCE);
  }
}
