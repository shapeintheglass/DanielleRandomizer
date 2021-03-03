package randomizers.generators;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import proto.RandomizerSettings.StoryProgressionSettings;
import proto.RandomizerSettings.StoryProgressionSettings.SpawnLocation;
import utils.StationConnectivityConsts;
import utils.StationConnectivityConsts.Level;
import utils.ZipHelper;

public class CustomSpawnGenerator {

  public static final ImmutableList<StoryProgressionSettings.SpawnLocation> SUPPORTED_SPAWNS = ImmutableList.of(
      SpawnLocation.LOBBY, SpawnLocation.HARDWARE_LABS, SpawnLocation.SHUTTLE_BAY, SpawnLocation.PSYCHOTRONICS,
      SpawnLocation.GUTS, SpawnLocation.ARBORETUM, SpawnLocation.BRIDGE, SpawnLocation.CREW_QUARTERS,
      SpawnLocation.DEEP_STORAGE, SpawnLocation.CARGO_BAY, SpawnLocation.LIFE_SUPPORT, SpawnLocation.POWER_PLANT,
      SpawnLocation.EXTERIOR, SpawnLocation.ENDGAME, SpawnLocation.GENDER_SELECT);

  public static final ImmutableMap<StoryProgressionSettings.SpawnLocation, Level> SPAWN_LOCATION_TO_LEVEL = new ImmutableMap.Builder<StoryProgressionSettings.SpawnLocation, Level>()
      .put(SpawnLocation.LOBBY, Level.LOBBY)
      .put(SpawnLocation.HARDWARE_LABS, Level.HARDWARE_LABS)
      .put(SpawnLocation.SHUTTLE_BAY, Level.SHUTTLE_BAY)
      .put(SpawnLocation.PSYCHOTRONICS, Level.PSYCHOTRONICS)
      .put(SpawnLocation.GUTS, Level.GUTS)
      .put(SpawnLocation.ARBORETUM, Level.ARBORETUM)
      .put(SpawnLocation.BRIDGE, Level.BRIDGE)
      .put(SpawnLocation.CREW_QUARTERS, Level.CREW_QUARTERS)
      .put(SpawnLocation.DEEP_STORAGE, Level.DEEP_STORAGE)
      .put(SpawnLocation.CARGO_BAY, Level.CARGO_BAY)
      .put(SpawnLocation.LIFE_SUPPORT, Level.LIFE_SUPPORT)
      .put(SpawnLocation.POWER_PLANT, Level.POWER_PLANT)
      .put(SpawnLocation.EXTERIOR, Level.EXTERIOR)
      .put(SpawnLocation.ENDGAME, Level.ENDGAME)
      .put(SpawnLocation.GENDER_SELECT, Level.GENDER_SELECT)
      .build();

  private BiMap<Level, String> levelsToIds;

  private SpawnLocation location;
  private ZipHelper zipHelper;

  public CustomSpawnGenerator(SpawnLocation l, ZipHelper zipHelper, long seed) {
    this.zipHelper = zipHelper;
    Random r = new Random(seed);
    if (l == SpawnLocation.RANDOM) {
      l = SUPPORTED_SPAWNS.get(r.nextInt(SUPPORTED_SPAWNS.size()));
      Logger.getGlobal().info(String.format("Random spawn set to %s", l.name()));
    }
    this.location = l;
    levelsToIds = HashBiMap.create();
    levelsToIds.put(Level.ARBORETUM, "1713490239386284818");
    levelsToIds.put(Level.BRIDGE, "844024417275035158");
    levelsToIds.put(Level.CARGO_BAY, "15659330456296333985");
    levelsToIds.put(Level.CREW_QUARTERS, "844024417252490146");
    levelsToIds.put(Level.DEEP_STORAGE, "1713490239377738413");
    levelsToIds.put(Level.GUTS, "4349723564886052417");
    levelsToIds.put(Level.HARDWARE_LABS, "844024417263019221");
    levelsToIds.put(Level.LIFE_SUPPORT, "4349723564895209499");
    levelsToIds.put(Level.LOBBY, "1713490239377285936");
    levelsToIds.put(Level.NEUROMOD_DIVISION, "12889009724983807463");
    levelsToIds.put(Level.POWER_PLANT, "6732635291182790112");
    levelsToIds.put(Level.PSYCHOTRONICS, "11824555372632688907");
    levelsToIds.put(Level.SHUTTLE_BAY, "1713490239386284988");
    levelsToIds.put(Level.EXTERIOR, "1713490239386284337");
    levelsToIds.put(Level.ENDGAME, "13680621263401479941");
    levelsToIds.put(Level.GENDER_SELECT, "3149325216909839564");
  }

  public SpawnLocation getLocation() {
    return location;
  }

  public String getNewSpawnLocation() {
    if (location != SpawnLocation.NONE) {
      return StationConnectivityConsts.LEVELS_TO_NAMES.get(SPAWN_LOCATION_TO_LEVEL.get(location));
    } else {
      return null;
    }
  }

  public ImmutableBiMap<Level, String> getLevelsToIds() {
    return new ImmutableBiMap.Builder<Level, String>().putAll(levelsToIds).build();
  }

  public void swapLocationId() {
    try {
      Document document = zipHelper.getDocument(ZipHelper.LOCATIONS_XML);

      boolean neuromodDivisionSwitched = false;
      boolean customMapSwitched = false;

      Level level = SPAWN_LOCATION_TO_LEVEL.get(location);
      String customLocationId = levelsToIds.get(level);
      String neuromodDivisionId = levelsToIds.get(Level.NEUROMOD_DIVISION);

      levelsToIds.remove(Level.NEUROMOD_DIVISION);
      levelsToIds.remove(level);
      levelsToIds.put(Level.NEUROMOD_DIVISION, customLocationId);
      levelsToIds.put(level, neuromodDivisionId);

      List<Element> locations = document.getRootElement().getChild("Locations").getChildren();
      for (Element e : locations) {
        if (neuromodDivisionSwitched && customMapSwitched) {
          break;
        }

        String id = e.getAttributeValue("ID");
        if (id.equals(customLocationId)) {
          e.setAttribute("ID", neuromodDivisionId);
          customMapSwitched = true;
        } else if (id.equals(neuromodDivisionId)) {
          e.setAttribute("ID", customLocationId);
          neuromodDivisionSwitched = true;
        }
      }
      zipHelper.copyToPatch(document, ZipHelper.LOCATIONS_XML);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }
}
