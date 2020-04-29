package utils;

import java.nio.file.Path;
import java.nio.file.Paths;

public class FileConsts {
  public static final String HUMANS_FILE = "data/entityarchetypes/arkhumans.xml";
  public static final String NPCS_FILE = "data/entityarchetypes/arknpcs.xml";
  public static final String PHYSICS_PROPS_FILE = "data/entityarchetypes/arkphysicsprops.xml";
  public static final String PICKUPS_FILE = "data/entityarchetypes/arkpickups.xml";
  public static final String PROJECTILES_FILE = "data/entityarchetypes/arkprojectiles.xml";
  public static final String ROBOTS_FILE = "data/entityarchetypes/arkrobots.xml";
  public static final String SPECIAL_WEAPONS_FILE = "data/entityarchetypes/arkspecialweapons.xml";

  public static final String VOICES_PATH = "data/dialog/voices";
  public static final String DIALOGIC_PATH = "data/dialog/dialoglogic";

  public static final String HUMANS_FINAL_DIR = "data/humansfinal/";

  public static final Path LOCAL_LEVELS = Paths.get("data/levels");

  public enum Archetype {
    HUMANS, TYPHON, PHYSICS_PROPS, PICKUPS, PROJECTILES, ROBOTS, SPECIAL_WEAPONS
  }

  public static String getFileForArchetype(Archetype a) {
    switch (a) {
    case HUMANS:
      return HUMANS_FILE;
    case TYPHON:
      return NPCS_FILE;
    case PHYSICS_PROPS:
      return PHYSICS_PROPS_FILE;
    case PICKUPS:
      return PICKUPS_FILE;
    case PROJECTILES:
      return PROJECTILES_FILE;
    case ROBOTS:
      return ROBOTS_FILE;
    case SPECIAL_WEAPONS:
      return SPECIAL_WEAPONS_FILE;
    default:
      return "";
    }
  }

  public static String archetypeToPrefix(Archetype a) {
    switch (a) {
    case HUMANS:
      return "ArkHumans";
    case TYPHON:
      return "ArkNpcs";
    case PHYSICS_PROPS:
      return "ArkPhysicsProps";
    case PICKUPS:
      return "ArkPickups";
    case PROJECTILES:
      return "ArkProjectiles";
    case ROBOTS:
      return "ArkRobots";
    case SPECIAL_WEAPONS:
      return "ArkSpecialWeapons";
    default:
      return "";
    }
  }
}
