package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import com.google.common.collect.ImmutableSet;

/*
 * Randomly breaks a repairable object.
 * 
 * Original state --> New State Broken --> Broken = OK Not Broken --> Not Broken = OK Broken --> Not
 * Broken = OK Not Broken --> Broken = Iffy
 */
public class BrokenObjectRule implements Rule {

  // Fabricator/Recycler in psychotronics
  private static ImmutableSet<String> GUIDS_NOT_TO_RANDOMIZE = ImmutableSet.of("48A3188ACC350D82", "4FD16C51804A6666");

  private static final ImmutableSet<String> BREAKABLE_ARCHETYPES =
      ImmutableSet.of("ArkGameplayObjects.Recycler.Recycler_Default",
          "ArkGameplayObjects.Fabricator.Fabricator_Default",
          "ArkGameplayObjects.RepairableObject.BrokenGroundingResistor",
          "ArkGameplayObjects.RepairableObject.BrokenDivertor",
          "ArkGameplayObjects.RepairableObject.WaterRegulator",
          "ArkRobots.Turrets.Turret_Default");

  private float brokenObjectRatio;

  public BrokenObjectRule(float brokenObjectRatio) {
    this.brokenObjectRatio = brokenObjectRatio;
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    String archetype = e.getAttributeValue("Archetype");
    String guid = e.getAttributeValue("EntityGuid");
    return guid != null && archetype != null && !GUIDS_NOT_TO_RANDOMIZE.contains(guid)
        && BREAKABLE_ARCHETYPES.contains(archetype);
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    boolean isBroken = r.nextFloat() < brokenObjectRatio;
    e.getChild("Properties2").setAttribute("bStartsBroken", isBroken ? "1" : "0");
  }
}
