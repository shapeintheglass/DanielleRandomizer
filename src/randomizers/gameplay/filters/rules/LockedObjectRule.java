package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

import com.google.common.collect.ImmutableSet;

/*
 * Randomly makes a hackable object locked.
 * 
 * Original state --> New State
 * Locked --> Locked= OK
 * Not Locked --> Not Locked = OK
 * Locked --> Not Locked = OK
 * Not Locked --> Locked = Iffy
 */
public class LockedObjectRule implements Rule {

  private static ImmutableSet<String> GUIDS_NOT_TO_RANDOMIZE = ImmutableSet.of();
  private static final float LOCKED_RATIO = 0.5f;

  private static final ImmutableSet<String> HACKABLE_ARCHETYPES = ImmutableSet.of(
      "ArkGameplayObjects.Keypad.Keypad_Default", "ArkGameplayObjects.InteractiveScreens.Workstation",
      "ArkGameplayObjects.InteractiveScreens.SecurityStation", "ArkGameplayObjects.Keypad.Keypad_NoFrame",
      "ArkGameplayObjects.OperatorDispenser.OperatorDispenser_Medical",
      "ArkGameplayObjects.OperatorDispenser.OperatorDispenser_Science",
      "ArkGameplayObjects.OperatorDispenser.OperatorDispenser_Military",
      "ArkGameplayObjects.OperatorDispenser.OperatorDispenser_Engineering",
      "ArkGameplayObjects.Keypad.CargoBay.Keypad_CargoBay");

  /*private static final ImmutableSet<String> HACKABLE_DOOR_ARCHETYPES = ImmutableSet.of(
      "ArkGameplayObjects.Door.Door_Sliding_Double_Default", "ArkGameplayObjects.Door.Door_Sliding_Double_Large",
      "ArkGameplayObjects.Door.Door_SecurityBooth_A", "ArkGameplayObjects.Door.Door_Standard_A_Dmg_Open",
      "ArkGameplayObjects.Door.Door_Standard_A_Dmg_Closed", "ArkGameplayObjects.Door.Door_Standard_A_Large_Dmg_Opened",
      "ArkGameplayObjects.Door.Door_Standard_A_Large_Dmg_Closed", "ArkGameplayObjects.Door.DoorFrame_NeoDeco_A_Large",
      "ArkGameplayObjects.Door.DoorFrame_NeoDeco_A_Small", "ArkGameplayObjects.Door.Turnstile_Securty_A",
      "ArkGameplayObjects.Door.Door_Greenhouse_Side", "ArkGameplayObjects.Door.Door_Greenhouse_Front",
      "ArkGameplayObjects.Door.BlastDoor_Medium", "ArkGameplayObjects.Door.BlastDoor_Small",
      "ArkGameplayObjects.Door.BlastDoor_Large", "ArkGameplayObjects.Door.Door_CargoContainer",
      "ArkGameplayObjects.Door.BlastDoor_Medium_NoAuto", "ArkGameplayObjects.Door.DoorFrame_Lab_A_Small",
      "ArkGameplayObjects.Door.DoorFrame_Lab_A_Large", "ArkGameplayObjects.Door.CargoCrate_Door",
      "ArkGameplayObjects.Door.DoorFrame_Security_Arch", "ArkGameplayObjects.Door.DoorFrame_NavMesh",
      "ArkGameplayObjects.Door.Bridge_Shuttle_Bridge_A");*/

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    String archetype = e.getAttributeValue("Archetype");
    String guid = e.getAttributeValue("EntityGuid");
    // If a hackable entity has a fixed keycode or password, do not touch it.
    //boolean hasKeycode = !e.getChild("Properties2").getAttributeValue("keycode_UnlockCode").isEmpty();
    return guid != null && archetype != null && !GUIDS_NOT_TO_RANDOMIZE.contains(guid) && HACKABLE_ARCHETYPES.contains(archetype);
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    boolean isLocked = r.nextFloat() < LOCKED_RATIO;
    e.getChild("Properties2").setAttribute("bStartsUnlocked", isLocked ? "0" : "1");
    e.getChild("Properties2").setAttribute("bStartsLocked", isLocked ? "1" : "0");
  }

}
