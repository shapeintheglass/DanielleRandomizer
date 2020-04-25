package utils;

import java.util.Map;

import com.google.auto.value.AutoValue;

import filters.ItemSpawnFilter.ItemType;

@AutoValue
public abstract class ItemSpawnSettings {
  abstract ItemType[] items();
  abstract int[] percents();

  public static Builder builder() {
    return new AutoValue_ItemSpawnSettings.Builder();
  }

  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setItems(ItemType[] items);
    public abstract Builder setPercents(int[] percents);

    public abstract ItemSpawnSettings build();
  }
}