package json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CosmeticSettingsJson {
  private boolean randomizeBodies;
  private boolean randomizeVoicelines;
  
  @JsonCreator
  public CosmeticSettingsJson(@JsonProperty("randomize_bodies") boolean randomizeBodies,
      @JsonProperty("randomize_voicelines") boolean randomizeVoicelines) {
    this.setRandomizeBodies(randomizeBodies);
    this.setRandomizeVoicelines(randomizeVoicelines);
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
