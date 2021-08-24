package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import utils.Utils;

public class OperatorDispenserRule implements Rule {

  private static ImmutableSet<String> GUIDS_NOT_TO_RANDOMIZE = ImmutableSet.of();

  private static final int ENGINEERING_CHANCE = 1;
  private static final int MEDICAL_CHANCE = 1;
  private static final int SCIENCE_CHANCE = 1;
  private static final int MILITARY_CHANCE = 1;
  private static ImmutableList<Integer> ARCHETYPE_WEIGHTS = ImmutableList.of(ENGINEERING_CHANCE, MEDICAL_CHANCE,
      SCIENCE_CHANCE, MILITARY_CHANCE);

  private static final String ENGINEERING_ARCHETYPE = "ArkGameplayObjects.OperatorDispenser.OperatorDispenser_Engineering";
  private static final String MEDICAL_ARCHETYPE = "ArkGameplayObjects.OperatorDispenser.OperatorDispenser_Medical";
  private static final String SCIENCE_ARCHETYPE = "ArkGameplayObjects.OperatorDispenser.OperatorDispenser_Science";
  private static final String MILITARY_ARCHETYPE = "ArkGameplayObjects.OperatorDispenser.OperatorDispenser_Military";
  private static ImmutableSet<String> VALID_ARCHETYPES = ImmutableSet.of(ENGINEERING_ARCHETYPE, MEDICAL_ARCHETYPE,
      SCIENCE_ARCHETYPE, MILITARY_ARCHETYPE);
  private static ImmutableList<String> VALID_ARCHETYPES_LIST = ImmutableList.of(ENGINEERING_ARCHETYPE,
      MEDICAL_ARCHETYPE, SCIENCE_ARCHETYPE, MILITARY_ARCHETYPE);

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    String archetype = e.getAttributeValue("Archetype");
    String guid = e.getAttributeValue("EntityGuid");
    return guid != null && archetype != null && !GUIDS_NOT_TO_RANDOMIZE.contains(guid) && VALID_ARCHETYPES.contains(archetype);
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    // Pick one archetype
    String nextArchetype = Utils.getRandomWeighted(VALID_ARCHETYPES_LIST, ARCHETYPE_WEIGHTS, r);
    e.setAttribute("Archetype", nextArchetype);
  }

}
