package utils;

import java.util.Arrays;

import databases.EntityDatabase.EntityType;

public class ItemSpawnSettings {
  private EntityType[] items;
  private int[] percents;
  private boolean includePhysicsProps;
  
  private ItemSpawnSettings(EntityType[] items, int[] percents, boolean includePhysicsProps) {
    this.items = items;
    this.percents = percents;
    this.includePhysicsProps = includePhysicsProps;
  }
  
  public EntityType[] getItems() {
    return items;
  }
  public int[] getPercents() {
    return percents;
  }
  public boolean getIncludePhysicsProps() {
    return includePhysicsProps;
  }

  public static Builder builder() {
    return new ItemSpawnSettings.Builder();
  }

  public String toString() {
    return String.format("%s\n%s", Arrays.toString(items), Arrays.toString(percents));
  }
  
  public static class Builder {
    private EntityType[] items;
    private int[] percents;
    private boolean includePhysicsProps;

    public Builder setItems(EntityType[] items) {
      this.items = items;
      return this;
    }
    public Builder setPercents(int[] percents) {
      this.percents = percents;
      return this;
    }
    public Builder setIncludePhysicsProps(boolean includePhysicsProps) {
      this.includePhysicsProps = includePhysicsProps;
      return this;
    }

    public ItemSpawnSettings build() {
      return new ItemSpawnSettings(items, percents, includePhysicsProps);
    }
  }
}