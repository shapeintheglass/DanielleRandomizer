package randomizers.gameplay.level.filters.rules;

import java.util.Map;
import java.util.Random;

import org.jdom2.Element;

public class StationConnectivityRule implements Rule {
  
  /*
   * 
   *   <Entity Name="Door.Door_LevelTransition_Exterior2" Archetype="ArkGameplayArchitecture.Door.Door_LevelTransition_Exterior" Pos="1711.6097,446.59161,35.661346" Rotate="0.70710671,0,0,0.70710683" EntityClass="ArkLevelTransitionDoor" EntityId="2928" EntityGuid="48493AC41D3EB947" CastShadowMinSpec="1" CastSunShadowMinSpec="3" ShadowCasterType="0" Layer="Arboretum_Observatory_Geo">
   *    <Properties2 location_Destination="1713490239386284337" textInaccessibleText="" bIsTrialGated="1" bMovePlayerOnExamine="1" bStartsInaccessible="0" bStartsLocked="0" keycard_UnlockKeycard="" bVerbose="0"/>
   * 
   * Neuromod Division 12889009724983807463
   * Door.Door_LevelTransition_Default1 --> 1713490239377285936 (lobby)
   * 
   * Lobby 1713490239377285936
   * LevelTransition_LifeSupport --> 4349723564895209499
   * LevelTransition_Hardware --> 844024417263019221
   * LevelTransition_ShuttleBay --> 1713490239386284988 (locked by 15659330456309530410)
   * LevelTransition_Psychotronics --> 11824555372632688907 (locked by 15659330456309530410)
   * LevelTransition_SimLabs --> 12889009724983807463
   * LevelTransition_Arboretum --> 1713490239386284818
   * 
   * Hardware Labs 844024417263019221
   * LevelTransitionDoor_ToLobby --> 1713490239377285936
   * Door.Door_LevelTransition_Exterior2 --> 1713490239386284337 (airlock)
   * 
   * Psychotronics 11824555372632688907
   * Door.Door_LevelTransition_Default2 --> 4349723564886052417 (guts)
   * Door.Door_LevelTransition_Default1 --> 1713490239377285936 (lobby)
   * Door.Door_LevelTransition_Exterior4 --> 1713490239386284337 (airlock)
   * 
   * Shuttle Bay 1713490239386284988
   * Door.Door_LevelTransition_Exterior3 --> 1713490239386284337 (airlock)
   * LTDoor_ToGUTs --> 4349723564886052417 (locked by 761057047955908816) (guts)
   * LTDoor_ToLobby --> 1713490239377285936 (lobby)
   * 
   * GUTS 4349723564886052417
   * Door.Door_LevelTransition_Default2 --> 1713490239386284988 (locked by 761057047955908816) (shuttle bay)
   * Door.Door_LevelTransition_Default1 --> 11824555372632688907 (psychotronics)
   * Door.Door_LevelTransition_Default3 --> 1713490239386284818 (arboretum)
   * Door.Door_LevelTransition_Exterior1 --> 15659330456296333985 (cargo bay)
   * 
   * Exterior 1713490239386284337
   * Door.Door_Transition_Exterior_ShuttleBay --> 1713490239386284988 (shuttle bay)
   * Door.Door_Transition_Exterior_Psychotronics --> 11824555372632688907 (psychotronics)
   * Door.Door_Transition_Exterior_HardwareLabs --> 844024417263019221 (hardware labs)
   * Door.Door_Transition_Exterior_PowerPlant --> 6732635291182790112 (power plant)
   * Door.Door_Transition_Exterior_Arboretum --> 1713490239386284818 (arboretum)
   * Door.Door_LevelTransition_Default7 --> 15659330456296333985 (cargo bay)
   * Door.Door_LevelTransition_Default8 --> 15659330456296333985 (cargo bay)
   * 
   * Arboretum 1713490239386284818
   * Door.Door_LevelTransition_Default5 --> 844024417275035158 (bridge)
   * Door.Door_LevelTransition_Exterior2 --> 1713490239386284337 (airlock)
   * Door.Door_LevelTransition_Close --> 14667999412570822910 (AR voice lock close)
   * Door.Door_LevelTransition_Default3 --> 4349723564886052417 (guts)
   * Door.Door_LevelTransition_Default8 --> 1713490239377738413 (deep storage open)
   * Door.Door_LevelTransition_Open --> 14667999412570822861 (AR voice lock)
   * Door.Door_LevelTransition_Default1 --> 844024417252490146 (locked by 844024417269161838) (crew quarters)
   * Door.Door_LevelTransition_Default6 --> 1713490239377285936 (lobby)
   * Door.Door_LevelTransition_AlexBunker --> "" (starts locked)
   * 
   * Crew Quarters 844024417252490146
   * Door.Door_LevelTransition_Default1 --> 1713490239386284818 (arboretum)
   * 
   * Deep Storage 1713490239377738413
   * Door.Door_LevelTransition_Default2 --> 1713490239386284818 (arboretum)
   * 
   * Bridge 844024417275035158
   * Door.Door_LevelTransition_Default1 --> 1713490239386284818 (arboretum)
   * 
   * Cargo Bay 15659330456296333985
   * Door.Door_LevelTransition_Default4 --> 1713490239386284337 (airlock)
   * Door.Door_LevelTransition_Default5 --> 1713490239386284337 (airlock)
   * Door.Door_LevelTransition_Default6 --> 1713490239386284337 (airlock)
   * Door.Door_LevelTransition_Default7 --> 15659330456296333985 (cargo bay...?) (inaccessible)
   * Door.Door_LevelTransition_Default8 --> 15659330456296333985 (cargo bay...?) (inaccessible)
   * Door.Door_LevelTransition_Exterior1 --> 4349723564886052417 (guts)
   * Door.Door_LevelTransition_Default2 --> 4349723564895209499 (life support)
   * 
   * Life Support 4349723564895209499
   * Door.Door_LevelTransition_Default4 --> 15659330456296333985 (cargo bay)
   * Door.Door_LevelTransition_Default5 --> 1713490239377285936 (lobby)
   * Door.Door_LevelTransition_Default6 --> 6732635291182790112 (power plant)
   * 
   * Power Plant 6732635291182790112
   * Door.Door_LevelTransition_Exterior2 --> 1713490239386284337 (airlock)
   * Door.Door_LevelTransition_Default1 --> 4349723564895209499 (life support)
   * 
   */
  
  
  // Map of transition door name to location 
  Map<String, String> connectivity;
  
  public StationConnectivityRule(Map<String, String> connectivity) {
    this.connectivity = connectivity;
  }

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    // TODO Auto-generated method stub
    
  }

}
