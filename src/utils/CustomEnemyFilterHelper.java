package utils;

import java.util.Random;

import org.jdom2.Element;

import databases.TaggedDatabase;
import proto.RandomizerSettings.GenericSpawnPresetFilter;
import proto.RandomizerSettings.GenericSpawnPresetRule;
import proto.RandomizerSettings.Settings;

public class CustomEnemyFilterHelper {

  private Settings settings;
  private TaggedDatabase database;

  public CustomEnemyFilterHelper(Settings settings, TaggedDatabase database) {
    this.settings = settings;
    this.database = database;
  }

  public Element getEntity(String entityName, String nameInLevel, Random r) {
    // TODO: Store rules list at construction
    GenericSpawnPresetFilter spawnPreset = settings.getNpcSettings().getEnemySpawnSettings();
    for (GenericSpawnPresetRule rule : spawnPreset.getFiltersList()) {

      GenericSpawnPresetRule.Builder copy = rule.toBuilder()
          .addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_NPC_TAGS)
          .addAllDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_NPC_TAGS);

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
    GenericSpawnPresetFilter spawnPreset = settings.getNpcSettings().getEnemySpawnSettings();
    for (GenericSpawnPresetRule rule : spawnPreset.getFiltersList()) {

      GenericSpawnPresetRule.Builder copy = rule.toBuilder()
          .addAllDoNotTouchTags(LevelConsts.DO_NOT_TOUCH_NPC_TAGS)
          .addAllDoNotOutputTags(LevelConsts.DO_NOT_OUTPUT_NPC_TAGS);

      CustomRuleHelper crh = new CustomRuleHelper(copy.build());
      if (crh.trigger(database, entityName, nameInLevel)) {
        return true;
      }
    }
    return false;
  }
}
