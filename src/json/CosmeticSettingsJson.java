package json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

public class CosmeticSettingsJson {
  private static final String RANDOMIZE_VOICELINES = "randomize_voicelines";
  private static final String RANDOMIZE_BODIES = "randomize_bodies";
  @JsonProperty(RANDOMIZE_BODIES)
  private boolean randomizeBodies;
  @JsonProperty(RANDOMIZE_VOICELINES) 
  private boolean randomizeVoicelines;

  @JsonCreator
  public CosmeticSettingsJson(@JsonProperty(RANDOMIZE_BODIES) boolean randomizeBodies,
      @JsonProperty(RANDOMIZE_VOICELINES) boolean randomizeVoicelines) {
    this.setRandomizeBodies(randomizeBodies);
    this.setRandomizeVoicelines(randomizeVoicelines);
  }

  public CosmeticSettingsJson(JsonNode node) {
    this.randomizeBodies = node.get(RANDOMIZE_BODIES).asBoolean();
    this.randomizeVoicelines = node.get(RANDOMIZE_VOICELINES).asBoolean();
  }

  public boolean isRandomizeBodies() {
    return randomizeBodies;
  }

  public void setRandomizeBodies(boolean randomizeBodies) {
    this.randomizeBodies = randomizeBodies;
  }

  public boolean toggleRandomizeBodies() {
    randomizeBodies = !randomizeBodies;
    return randomizeBodies;
  }

  public boolean isRandomizeVoicelines() {
    return randomizeVoicelines;
  }

  public void setRandomizeVoicelines(boolean randomizeVoicelines) {
    this.randomizeVoicelines = randomizeVoicelines;
  }

  public void toggleRandomizeVoicelines() {
    randomizeVoicelines = !randomizeVoicelines;
  }
}
