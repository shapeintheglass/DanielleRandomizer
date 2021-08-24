package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import com.google.common.collect.ImmutableSet;

/*
 * Randomly breaks a repairable object.
 * 
 * Original state --> New State
 * Broken --> Broken = OK
 * Not Broken --> Not Broken = OK
 * Broken --> Not Broken = OK
 * Not Broken --> Broken = Iffy
 */
public class BrokenObjectRule implements Rule {
  
  private static ImmutableSet<String> GUIDS_NOT_TO_RANDOMIZE = ImmutableSet.of();
  
  private static final float BROKEN_RATIO = 0.5f;

  private static final ImmutableSet<String> BREAKABLE_ARCHETYPES = ImmutableSet.of(
      "ArkGameplayArchitecture.ElectricalBox.ElectricBox_DurationIndefinite",
      "ArkGameplayObjects.Recycler.Recycler_Default", "ArkGameplayObjects.Fabricator.Fabricator_Default",
      "ArkGameplayObjects.RepairableObject.RepairBox", "ArkGameplayObjects.RepairableObject.RepairBox_Level2",
      "ArkGameplayObjects.RepairableObject.RepairBox_Level3",
      "ArkGameplayObjects.RepairableObject.BrokenGroundingResistor",
      "ArkGameplayObjects.RepairableObject.BrokenDivertor", "ArkGameplayObjects.RepairableObject.WaterRegulator",
      "ArkGameplayObjects.RepairableObject.HeatExchangeMonitor");

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    String archetype = e.getAttributeValue("Archetype");
    String guid = e.getAttributeValue("EntityGuid");
    return guid != null && archetype != null && !GUIDS_NOT_TO_RANDOMIZE.contains(guid) && BREAKABLE_ARCHETYPES.contains(archetype);
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    boolean isBroken = r.nextFloat() < BROKEN_RATIO;
    e.getChild("Properties2").setAttribute("bStartsBroken", isBroken ? "1" : "0");
  }
}
