package rules;

import utils.XmlEntity;

public class ShitloadsOfResourcesInApartmentRule extends BaseRule {

  /*
   * 
   * 
   * 
   * Nightstand left - 12 slots
   * 
   * 
   * 
   * Nightstand right - 12 slots
   * 
   * Bathroom trashcan - 12 slots
   * 
   * Wall cabinet - 12 slots
   * 
   * Bathroom cabinet - 12 slots
   * 
   * Floor cabinet left - 24 slots
   * 
   * Floor cabinet center - 24 slots
   * 
   * Floor cabinet right - 24 slots
   * 
   * Fridge - 24 slots
   * 
   * 
   * 
   * Standing cabinet - 48 slots
   * 
   * 
   */

  public enum OPTIONS {
    START_ON_2ND_DAY,
    PSYCHOSCOPE, // Replace champagne w/ psychoscope
    ARTAX, // Replace note w/ jetpack
    FAB_PLANS, // Place every fab plan in nightstand left
    WEAPONS, // Place weapons in fridge, ammo/charges in cabinets
    CHIPSETS, // Place every chipset in nightstand right
    NEUROMODS, // Place 377 neuromods in cabinet + psi hypos
    MISC_ITEMS, // Place spare parts, weapon upgrade kits, suit upgrade kits in ???
    KEYCARDS, // Place every keycard in the game in ???
  }

  @Override
  public boolean trigger(XmlEntity e, String levelDir) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void apply(XmlEntity e, String levelDir) {
    // TODO Auto-generated method stub

  }
}
