package utils;

import java.util.List;
import java.util.Random;
import org.jdom2.Element;
import com.google.common.collect.Lists;
import databases.TaggedDatabase;
import proto.RandomizerSettings.GenericSpawnPresetFilter;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import proto.RandomizerSettings.Settings;

public class CustomItemFilterHelper {

  private Settings settings;
  private TaggedDatabase database;

  List<String> doNotOutput;

  public CustomItemFilterHelper(Settings settings, TaggedDatabase database) {
    this.settings = settings;
    this.database = database;
    doNotOutput = Lists.newArrayList();
  }

  public void addDoNotOutput(List<String> doNotOutput) {
    this.doNotOutput.addAll(doNotOutput);
  }

  public Element getEntity(String entityName, String nameInLevel, Random r) {
    // TODO: Store rules list at construction
    GenericSpawnPresetFilter spawnPreset = settings.getGameplaySettings().getItemSpawnSettings();
    for (GenericSpawnPresetRule rule : spawnPreset.getFiltersList()) {

      GenericSpawnPresetRule.Builder copy = rule.toBuilder()
          .addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS)
          .addAllDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS)
          .addAllDoNotOutputTags(doNotOutput);
      if (!settings.getMoreSettings().getMoreGuns()) {
        copy.addDoNotOutputTags("Randomizer");
      }

      CustomRuleHelper ruleHelper = new CustomRuleHelper(copy.build());

      if (ruleHelper.trigger(database, entityName, nameInLevel)) {
        return ruleHelper.getEntityToSwap(database, r);
      }
    }
    return null;
  }

  public String getEntityStr(String entityName, String nameInLevel, Random r) {
    Element entityToSwap = getEntity(entityName, nameInLevel, r);
    if (entityToSwap == null) {
      return null;
    }
    return Utils.getNameForEntity(entityToSwap);
  }

  public boolean trigger(String entityName, String nameInLevel) {
    GenericSpawnPresetFilter spawnPreset = settings.getGameplaySettings().getItemSpawnSettings();
    for (GenericSpawnPresetRule rule : spawnPreset.getFiltersList()) {

      GenericSpawnPresetRule.Builder copy = rule.toBuilder()
          .addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_ITEM_TAGS)
          .addAllDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_ITEM_TAGS);
      if (!settings.getMoreSettings().getMoreGuns()) {
        copy.addDoNotOutputTags("Randomizer");
      }

      CustomRuleHelper crh = new CustomRuleHelper(copy.build());
      if (crh.trigger(database, entityName, nameInLevel)) {
        return true;
      }
    }
    return false;
  }
}
