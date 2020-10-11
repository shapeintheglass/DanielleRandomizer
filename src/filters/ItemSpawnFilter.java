package filters;

import databases.TagHelper;
import rules.ItemSwapRule;
import settings.Settings;


public class ItemSpawnFilter extends BaseFilter {
  
  String[] doNotTouch = {TagHelper.KEYCARD_TAG, TagHelper.MISSION_ITEM_TAG, TagHelper.NOTE_TAG, TagHelper.EQUIPMENT_TAG};

  /**
   * Pre-made combination of rules that specifically filters items in certain settings.
   */
  public ItemSpawnFilter(Settings s) {
    super("ItemSpawnFilter", s);
    
    switch(s.getItemSpawnSettings().getPreset() ) {
      case ALL_JUNK:
        rules.add(new ItemSwapRule()
            .setInputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setOutputTags(TagHelper.JUNK_TAG)
            .setDoNotTouchTags(doNotTouch));
        break;
      case ALL_WRENCHES:
        rules.add(new ItemSwapRule()
            .setInputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setOutputTags(TagHelper.WRENCH_TAG)
            .setDoNotTouchTags(doNotTouch));
        break;
      case CUSTOM:
        break;
      case NONE:
        break;
      case NO_ITEMS:
        break;
      case NO_LOGIC:
        rules.add(new ItemSwapRule()
            .setInputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setOutputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setDoNotTouchTags(doNotTouch));
        break;
      case WHISKEY_AND_CIGARS:
        rules.add(new ItemSwapRule()
            .setInputTags(TagHelper.PHYSICS_PROP_TAG, TagHelper.PICKUP_TAG)
            .setOutputTags(TagHelper.WHISKEY_TAG, TagHelper.CIGAR_TAG)
            .setDoNotTouchTags(doNotTouch));
        break;
      default:
        break;
    }
  }
}
