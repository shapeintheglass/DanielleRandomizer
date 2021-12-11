package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import com.google.common.collect.ImmutableSet;

public class RecyclerRule implements Rule {

  private static ImmutableSet<String> GUIDS_NOT_TO_RANDOMIZE =
      ImmutableSet.of("401B2E795ACF72BB", "4499B0CE8C142E9F", "4499B0CE8C142E9F",
          "4D6155B8A1C85557", "45645A62A8D284E9", "49A4E77E3949EB7E", "4C0DD215352BAF82",
          "401B2E795ACF72BB", "48A3188ACC350D82", "4DEA7789369062CD", "4E01DBAA425A3725",
          "4A1C6E76180BF096", "4EEF20B4F686C269", "4499F74C19471880", "428F5B9AEBF21E39",
          "4AFB60EDA6DC6643", "4FD16C51804A6666", "44D9A18ECB12AEF9");

  private static final String RECYCLER_ARCHETYPE =
      "ArkGameplayObjects.Fabricator.Fabricator_Default";
  private static final String FABRICATOR_ARCHETYPE = "ArkGameplayObjects.Recycler.Recycler_Default";

  private float recyclerChance;

  public RecyclerRule(float recyclerChance) {
    this.recyclerChance = recyclerChance;
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    String archetype = e.getAttributeValue("Archetype");
    String guid = e.getAttributeValue("EntityGuid");
    if (guid != null && archetype != null && !GUIDS_NOT_TO_RANDOMIZE.contains(guid)
        && (archetype.equals(RECYCLER_ARCHETYPE) || archetype.equals(FABRICATOR_ARCHETYPE))) {
      return true;
    }
    return false;
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    boolean isRecycler = r.nextFloat() < recyclerChance;
    String newArchetype = isRecycler ? RECYCLER_ARCHETYPE : FABRICATOR_ARCHETYPE;
    e.setAttribute("Archetype", newArchetype);
  }
}
