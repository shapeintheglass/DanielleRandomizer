package utils;

import com.google.common.collect.ImmutableMap;

public class FileConsts {  
  public static final String ENTITY_ARCHETYPES_DEST_DIR = "libs/entityarchetypes/";

  public enum Archetype {
    HUMANS,
    TYPHON,
    PHYSICS_PROPS,
    PICKUPS,
    PROJECTILES,
    ROBOTS,
    SPECIAL_WEAPONS,
    GAMEPLAY_ARCHITECTURE,
    GAMEPLAY_OBJECTS,
  }

  public static final ImmutableMap<Archetype, String> ARCHETYPE_TO_FILENAME =
      new ImmutableMap.Builder<Archetype, String>().put(Archetype.HUMANS, "arkhumans.xml")
          .put(Archetype.TYPHON, "arknpcs.xml")
          .put(Archetype.PHYSICS_PROPS, "arkphysicsprops.xml")
          .put(Archetype.PICKUPS, "arkpickups.xml")
          .put(Archetype.PROJECTILES, "arkprojectiles.xml")
          .put(Archetype.ROBOTS, "arkrobots.xml")
          .put(Archetype.SPECIAL_WEAPONS, "arkspecialweapons.xml")
          .put(Archetype.GAMEPLAY_ARCHITECTURE, "arkgameplayarchitecture.xml")
          .put(Archetype.GAMEPLAY_OBJECTS, "arkgameplayobjects.xml")
          .build();

  public static final ImmutableMap<Archetype, String> ARCHETYPE_TO_LIBRARY_NAME =
      new ImmutableMap.Builder<Archetype, String>().put(Archetype.HUMANS, "ArkHumans")
          .put(Archetype.TYPHON, "ArkNpcs")
          .put(Archetype.PHYSICS_PROPS, "ArkPhysicsProps")
          .put(Archetype.PICKUPS, "ArkPickups")
          .put(Archetype.PROJECTILES, "ArkProjectiles")
          .put(Archetype.ROBOTS, "ArkRobots")
          .put(Archetype.SPECIAL_WEAPONS, "ArkSpecialWeapons")
          .put(Archetype.GAMEPLAY_ARCHITECTURE, "ArkGameplayArchitecture")
          .put(Archetype.GAMEPLAY_OBJECTS, "ArkGameplayObjects")
          .build();

  public static String getFileForArchetype(Archetype a) {
    return ZipHelper.ENTITY_ARCHETYPES_SOURCE_DIR + ARCHETYPE_TO_FILENAME.get(a);
  }
}
