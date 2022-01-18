package utils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.common.graph.ImmutableNetwork;
import randomizers.generators.StationGenerator;
import utils.KeycardConnectivityConsts.Keycard;
import utils.KeycardConnectivityConsts.Location;
import utils.ProgressionConsts.ProgressionMilestone;
import utils.ProgressionConsts.ProgressionNode;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;

public class ProgressionGraph {
  private static final int MAX_NUMBER_LEVEL_TRANSITIONS = 1000;
  private ImmutableNetwork<Level, Door> stationConnectivity;
  private ImmutableMap<Location, Keycard> keycardConnectivity;

  private Map<Level, LevelProgression> levelsToMilestones;

  private Level liftTechnopathLevel;
  private Door liftTechnopathDoor;
  private Door liftLowerDoor;
  private Set<ProgressionNode> progress;
  private Random r;

  public ProgressionGraph(ImmutableNetwork<Level, Door> stationConnectivity,
      ImmutableMap<Location, Keycard> keycardConnectivity, long seed) {
    this.stationConnectivity = stationConnectivity;
    this.keycardConnectivity = keycardConnectivity;
    this.r = new Random(seed);
    levelsToMilestones = Maps.newHashMap();

    // Look up which levels are connected to the lift
    liftTechnopathLevel = stationConnectivity.incidentNodes(Door.LOBBY_ARBORETUM_EXIT).nodeV();
    liftTechnopathDoor = stationConnectivity.edgeConnectingOrNull(liftTechnopathLevel, Level.LOBBY);

    Level liftLowerLevel = stationConnectivity.incidentNodes(Door.LOBBY_LIFE_SUPPORT_EXIT).nodeV();
    liftLowerDoor = stationConnectivity.edgeConnectingOrNull(liftLowerLevel, Level.LOBBY);

    for (Level l : StationGenerator.LEVELS_TO_PROCESS) {
      getLevel(l);
    }
  }

  public String getVerifyResult() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("Left neuromod division: %s\n",
        progress.contains(Door.NEUROMOD_DIVISION_LOBBY_EXIT)));
    sb.append(String.format("Got morgan's arming key: %s\n",
        progress.contains(ProgressionMilestone.DS_MORGANS_ARMING_KEY)));
    sb.append(String.format("Rebooted station: %s\n",
        progress.contains(ProgressionMilestone.PP_REBOOT_COMPLETED)));
    sb.append(String.format("Dahl arrived: %s\n",
        progress.contains(ProgressionMilestone.AR_DAHL_ARRIVAL)));
    sb.append(String.format("Got alex's arming key: %s\n",
        progress.contains(ProgressionMilestone.AR_ALEX_ARMING_KEY)));
    sb.append(String.format("Primed nullwave: %s\n",
        progress.contains(ProgressionMilestone.PY_NULLWAVE_PRIMED)));
    sb.append(String.format("Primed keys: %s\n",
        progress.contains(ProgressionMilestone.PP_ARMING_KEYS_PRIMED)));
    sb.append(String.format("Listened to first video: %s\n",
        progress.contains(ProgressionMilestone.LO_LG_VIDEO_PART_1)));
    sb.append(String.format("Listened to second video: %s\n",
        progress.contains(ProgressionMilestone.LO_LG_VIDEO_PART_2)));
    sb.append(String.format("Got general access keycard: %s\n",
        progress.contains(Keycard.LO_GENERAL_ACCESS)));
    sb.append(String.format("Got fuel storage keycard: %s\n",
        progress.contains(Keycard.GT_FUEL_STORAGE)));
    sb.append(String.format("Unlocked the lift: %s\n",
        progress.contains(ProgressionMilestone.LO_UNLOCKED_LIFT)));
    sb.append(progress.toString());
    sb.append("\n");
    sb.append(
        String.format("Finished game: %s\n", progress.contains(ProgressionMilestone.BR_GOAL)));

    return sb.toString();
  }

  public boolean verify() {
    // Start at neuromod division, see if we can wind up at the goal.
    progress = Sets.newHashSet();
    Level currentLevel = Level.NEUROMOD_DIVISION;
    boolean ejected = false;

    for (int i = 0; i < MAX_NUMBER_LEVEL_TRANSITIONS; i++) {
      Stack<Level> toVisit = new Stack<>();
      Set<Level> visited = Sets.newHashSet();
      toVisit.add(currentLevel);
      while (!toVisit.isEmpty()) {
        currentLevel = toVisit.pop();
        if (visited.contains(currentLevel)) {
          continue;
        }
        visited.add(currentLevel);
        List<Level> next = traverseStation(currentLevel);

        // If we are visiting Deep Storage, we are effectively teleported to Cargo Bay afterwards.
        if (!ejected && currentLevel == Level.DEEP_STORAGE) {
          // Throw out the stack and start at Cargo Bay
          currentLevel = Level.CARGO_BAY;
          ejected = true;
          break;
        }

        // Pick a random non-visited level to go to next
        List<Level> validLevels = Lists.newArrayList();
        for (Level n : next) {
          if (!visited.contains(n)) {
            validLevels.add(n);
          }
        }
        if (!validLevels.isEmpty()) {
          toVisit.add(Utils.getRandom(validLevels, r));
        }

        if (progress.contains(ProgressionMilestone.BR_GOAL)) {
          return true;
        }
      }
    }

    return progress.contains(ProgressionMilestone.BR_GOAL);
  }

  private List<Level> traverseStation(Level l) {
    // Make progress in this level
    getLevel(l).updateProgress(progress);
    List<Level> toVisitNext = Lists.newArrayList();

    // Check all doors connected to this level
    for (Door d : StationConnectivityConsts.LEVELS_TO_DOORS.get(l)) {
      // Check that this door is unlocked
      if (!progress.contains(d)) {
        continue;
      }

      // If the director lockdown has started and we have not entered the reboot, all airlocks,
      // the lift, and Cargo Bay --> Guts are down
      if (progress.contains(ProgressionMilestone.DS_DIRECTOR_LOCKDOWN)
          && !progress.contains(ProgressionMilestone.PP_REBOOT_COMPLETED)
          && (StationConnectivityConsts.AIRLOCKS.contains(d) || d == liftTechnopathDoor
              || d == liftLowerDoor || d == Door.CARGO_BAY_GUTS_EXIT)) {
        continue;
      }

      // If the apex is here, all airlocks are down
      if (progress.contains(ProgressionMilestone.AR_APEX_IS_HERE)
          && StationConnectivityConsts.AIRLOCKS.contains(d)) {
        continue;
      }

      // Add the level this door is connected to where we can still make some progress
      Level neighbor = stationConnectivity.incidentNodes(d).nodeV();

      toVisitNext.add(neighbor);
    }

    return toVisitNext;
  }

  private LevelProgression getLevel(Level l) {
    if (levelsToMilestones.containsKey(l)) {
      return levelsToMilestones.get(l);
    }

    LevelProgression levelMilestone = null;

    // Add progression
    switch (l) {
      case ARBORETUM:
        levelMilestone =
            new LevelProgression(Level.ARBORETUM).addNeighbor(ProgressionMilestone.AR_AIRLOCK)
                .addNeighbor(ProgressionMilestone.AR_WATER_PRESSURE_REGULATOR)
                .addNeighbor(ProgressionMilestone.AR_ALEX_LG_VIDEO,
                    ProgressionMilestone.PP_REBOOT_COMPLETED)
                .addNeighbor(ProgressionMilestone.AR_CORAL_SCAN_CHIPSET,
                    ProgressionMilestone.AR_ALEX_LG_VIDEO)
                .addNeighbor(ProgressionMilestone.AR_DAHL_ARRIVAL,
                    ProgressionMilestone.EX_CORAL_SCANS_COMPLETED)
                .addNeighbor(ProgressionMilestone.AR_ALEX_ARMING_KEY,
                    ProgressionMilestone.EX_KASPAR_NEUTRALIZED,
                    ProgressionMilestone.ND_KASPAR_NEUTRALIZED,
                    ProgressionMilestone.HL_KASPAR_NEUTRALIZED,
                    ProgressionMilestone.PY_KASPAR_NEUTRALIZED)
                .addNeighbor(ProgressionMilestone.AR_PROTOTYPE_NULLWAVE,
                    ProgressionMilestone.EX_KASPAR_NEUTRALIZED,
                    ProgressionMilestone.ND_KASPAR_NEUTRALIZED,
                    ProgressionMilestone.HL_KASPAR_NEUTRALIZED,
                    ProgressionMilestone.PY_KASPAR_NEUTRALIZED)
                .addNeighbor(ProgressionMilestone.AR_APEX_IS_HERE,
                    ProgressionMilestone.EX_KASPAR_NEUTRALIZED,
                    ProgressionMilestone.ND_KASPAR_NEUTRALIZED,
                    ProgressionMilestone.HL_KASPAR_NEUTRALIZED,
                    ProgressionMilestone.PY_KASPAR_NEUTRALIZED);
        break;
      case BRIDGE:
        levelMilestone = new LevelProgression(Level.BRIDGE).addNeighbor(
            ProgressionMilestone.BR_GOAL, ProgressionMilestone.PP_ARMING_KEYS_PRIMED,
            ProgressionMilestone.PY_NULLWAVE_PRIMED);
        break;
      case CARGO_BAY:
        levelMilestone = new LevelProgression(Level.CARGO_BAY);
        break;
      case CREW_QUARTERS:
        levelMilestone = new LevelProgression(Level.CREW_QUARTERS)
            .addNeighbor(ProgressionMilestone.CQ_CAFETERIA_TELEPATH_NEUTRALIZED)
            .addNeighbor(ProgressionMilestone.CQ_FREEZER_ACCESS,
                ProgressionMilestone.CQ_KITCHEN_ACCESS,
                ProgressionMilestone.AR_WATER_PRESSURE_REGULATOR)
            .addNeighbor(ProgressionMilestone.CQ_MITCHELLS_AWARD, Keycard.CQ_CABIN_MITCHELL)
            .addNeighbor(ProgressionMilestone.CQ_KITCHEN_ACCESS,
                ProgressionMilestone.CQ_MITCHELLS_AWARD)
            .addNeighbor(ProgressionMilestone.CQ_RECORDINGS_90_PERCENT)
            .addNeighbor(ProgressionMilestone.CQ_ABIGAIL_CABIN_RECORDING_10_PERCENT,
                Keycard.CQ_CABIN_FOY)
            .addNeighbor(ProgressionMilestone.CQ_MITCHELL_CABIN_RECORDING_10_PERCENT,
                Keycard.CQ_CABIN_MITCHELL)
            .addNeighbor(ProgressionMilestone.CQ_FREEZER_RECORDING_20_PERCENT,
                ProgressionMilestone.CQ_FREEZER_ACCESS)
            .addNeighbor(ProgressionMilestone.CQ_VOICE_OVERRIDE,
                ProgressionMilestone.CQ_FREEZER_RECORDING_20_PERCENT);
        break;
      case DEEP_STORAGE:
        levelMilestone = new LevelProgression(Level.DEEP_STORAGE)
            .addNeighbor(ProgressionMilestone.DS_DIRECTOR_LOCKDOWN)
            .addNeighbor(ProgressionMilestone.DS_MORGANS_ARMING_KEY);
        break;
      case EXTERIOR:
        levelMilestone = new LevelProgression(Level.EXTERIOR)
            .addNeighbor(ProgressionMilestone.EX_KASPAR_NEUTRALIZED,
                ProgressionMilestone.SB_KASPAR_IN_EX)
            .addNeighbor(ProgressionMilestone.EX_CORAL_SCANS_COMPLETED,
                ProgressionMilestone.PY_PSYCHOSCOPE, ProgressionMilestone.AR_CORAL_SCAN_CHIPSET);
        break;
      case GUTS:
        levelMilestone = new LevelProgression(Level.GUTS);
        break;
      case HARDWARE_LABS:
        levelMilestone = new LevelProgression(Level.HARDWARE_LABS)
            .addNeighbor(ProgressionMilestone.HL_VISITED_HARDWARE_LABS)
            .addNeighbor(ProgressionMilestone.HL_ARTAX_JETPACK)
            .addNeighbor(ProgressionMilestone.HL_FIXED_LG_VIDEO, Keycard.HL_CALVINO_WORKSHOP)
            .addNeighbor(ProgressionMilestone.HL_AIRLOCK, ProgressionMilestone.HL_ARTAX_JETPACK)
            .addNeighbor(ProgressionMilestone.HL_KASPAR_NEUTRALIZED,
                ProgressionMilestone.SB_KASPAR_IN_HL);
        break;
      case LIFE_SUPPORT:
        levelMilestone = new LevelProgression(Level.LIFE_SUPPORT).addNeighbor(
            ProgressionMilestone.LS_WATER_TREATMENT_TECHNOPATH,
            ProgressionMilestone.DS_DIRECTOR_LOCKDOWN);
        break;
      case LOBBY:
        levelMilestone =
            new LevelProgression(Level.LOBBY).addNeighbor(ProgressionMilestone.LO_LG_VIDEO_PART_1)
                .addNeighbor(ProgressionMilestone.LO_LG_VIDEO_PART_2,
                    ProgressionMilestone.HL_FIXED_LG_VIDEO)
                .addNeighbor(ProgressionMilestone.LO_TRAUMA_CENTER_RECORDING_20_PERCENT,
                    Keycard.LO_TRAUMA_CENTER)
                .addNeighbor(ProgressionMilestone.LO_NEUROMOD_DIVISION_PASSWORD);
        break;
      case NEUROMOD_DIVISION:
        levelMilestone = new LevelProgression(Level.NEUROMOD_DIVISION).addNeighbor(
            ProgressionMilestone.ND_KASPAR_NEUTRALIZED, ProgressionMilestone.SB_KASPAR_IN_ND);
        break;
      case POWER_PLANT:
        levelMilestone =
            new LevelProgression(Level.POWER_PLANT).addNeighbor(ProgressionMilestone.PP_AIRLOCK)
                .addNeighbor(ProgressionMilestone.PP_REBOOT_COMPLETED, Keycard.PP_COOLANT_CHAMBER,
                    ProgressionMilestone.DS_DIRECTOR_LOCKDOWN)
                .addNeighbor(ProgressionMilestone.PP_ARMING_KEYS_PRIMED,
                    ProgressionMilestone.DS_MORGANS_ARMING_KEY,
                    ProgressionMilestone.AR_ALEX_ARMING_KEY);
        break;
      case PSYCHOTRONICS:
        levelMilestone = new LevelProgression(Level.PSYCHOTRONICS)
            .addNeighbor(ProgressionMilestone.PY_PSYCHOSCOPE)
            .addNeighbor(ProgressionMilestone.PY_AIRLOCK)
            .addNeighbor(ProgressionMilestone.PY_KASPAR_NEUTRALIZED,
                ProgressionMilestone.SB_KASPAR_IN_PY)
            .addNeighbor(ProgressionMilestone.PY_NULLWAVE_PRIMED,
                ProgressionMilestone.AR_PROTOTYPE_NULLWAVE);
        break;
      case SHUTTLE_BAY:
        levelMilestone = new LevelProgression(Level.SHUTTLE_BAY)
            .addNeighbor(ProgressionMilestone.SB_KASPAR_IN_EX, ProgressionMilestone.AR_DAHL_ARRIVAL)
            .addNeighbor(ProgressionMilestone.SB_KASPAR_IN_HL, ProgressionMilestone.AR_DAHL_ARRIVAL)
            .addNeighbor(ProgressionMilestone.SB_KASPAR_IN_ND, ProgressionMilestone.AR_DAHL_ARRIVAL)
            .addNeighbor(ProgressionMilestone.SB_KASPAR_IN_PY, ProgressionMilestone.AR_DAHL_ARRIVAL)
            .addNeighbor(ProgressionMilestone.SB_AIRLOCK, ProgressionMilestone.HL_ARTAX_JETPACK);
        break;
      default:
        break;
    }

    // If this is the level from which we unlock the lift, having access to the door will let us
    // unlock the lift.
    if (l == liftTechnopathLevel) {
      levelMilestone.addNeighbor(ProgressionMilestone.LO_UNLOCKED_LIFT, liftTechnopathDoor);
    }

    // Add doors
    for (Door d : StationConnectivityConsts.LEVELS_TO_DOORS.get(l)) {
      Set<ProgressionNode> reqs = Sets.newHashSet();

      // The door connected to the lower part of the lift is locked until the lift is unlocked.
      if (d == liftLowerDoor) {
        reqs.add(ProgressionMilestone.LO_UNLOCKED_LIFT);
      }

      // TODO: Special cases for locked doors

      switch (d) {
        case ARBORETUM_CREW_QUARTERS_EXIT:
          reqs.add(Keycard.AR_CREW_QUARTERS);
          break;
        case ARBORETUM_DEEP_STORAGE_EXIT:
          reqs.add(ProgressionMilestone.CQ_VOICE_OVERRIDE);
          reqs.add(ProgressionMilestone.CQ_RECORDINGS_90_PERCENT);
          reqs.add(ProgressionMilestone.LO_TRAUMA_CENTER_RECORDING_20_PERCENT);
          reqs.add(ProgressionMilestone.CQ_ABIGAIL_CABIN_RECORDING_10_PERCENT);
          reqs.add(ProgressionMilestone.CQ_FREEZER_RECORDING_20_PERCENT);
          reqs.add(ProgressionMilestone.CQ_MITCHELL_CABIN_RECORDING_10_PERCENT);
          break;
        case ARBORETUM_AIRLOCK:
        case EXTERIOR_ARBORETUM_AIRLOCK:
          reqs.add(ProgressionMilestone.AR_AIRLOCK);
          break;
        case HARDWARE_LABS_AIRLOCK:
        case EXTERIOR_HARDWARE_LABS_AIRLOCK:
          reqs.add(ProgressionMilestone.HL_AIRLOCK);
          break;
        case POWER_PLANT_AIRLOCK:
        case EXTERIOR_POWER_PLANT_AIRLOCK:
          reqs.add(ProgressionMilestone.PP_AIRLOCK);
          break;
        case PSYCHOTRONICS_AIRLOCK:
        case EXTERIOR_PSYCHOTRONICS_AIRLOCK:
          reqs.add(ProgressionMilestone.PY_AIRLOCK);
          break;
        case SHUTTLE_BAY_AIRLOCK:
        case EXTERIOR_SHUTTLE_BAY_AIRLOCK:
          reqs.add(ProgressionMilestone.SB_AIRLOCK);
          break;
        case LOBBY_ARBORETUM_EXIT:
        case LOBBY_LIFE_SUPPORT_EXIT:
          reqs.add(ProgressionMilestone.LO_UNLOCKED_LIFT);
          break;
        case LIFE_SUPPORT_POWER_PLANT_EXIT:
          reqs.add(ProgressionMilestone.LS_WATER_TREATMENT_TECHNOPATH);
          break;
        case SHUTTLE_BAY_GUTS_EXIT:
        case GUTS_SHUTTLE_BAY_EXIT:
          reqs.add(Keycard.GT_FUEL_STORAGE);
          break;
        case LOBBY_PSYCHOTRONICS_EXIT:
        case LOBBY_SHUTTLE_BAY_EXIT:
          reqs.add(Keycard.LO_GENERAL_ACCESS);
          break;
        default:
          break;
      }
      if (reqs.isEmpty()) {
        levelMilestone.addNeighbor(d);
      } else {
        levelMilestone.addNeighbor(d, reqs);
      }

    }

    // Look up keycards that are relatively accessible
    for (Location keycardLocation : KeycardConnectivityConsts.LEVEL_TO_LOCATIONS.get(l)) {
      Keycard keycard = keycardConnectivity.get(keycardLocation);
      switch (keycardLocation) {
        // TODO: Special cases for keycards that have requirements
        case LO_NPC_DEVRIES:
          levelMilestone.addNeighbor(keycard, ProgressionMilestone.LO_UNLOCKED_LIFT);
          break;
        case LO_NPC_JANUARY:
        case LO_NPC_JANUARY_GIVEN:
          levelMilestone.addNeighbor(keycard, ProgressionMilestone.LO_LG_VIDEO_PART_2);
          break;
        case HL_NPC_CALVINO:
          levelMilestone.addNeighbor(keycard, ProgressionMilestone.HL_VISITED_HARDWARE_LABS);
          break;
        case HL_CALVINOS_SAFE:
          levelMilestone.addNeighbor(keycard, Keycard.HL_CALVINO_WORKSHOP);
          break;
        case CQ_NPC_LUKA:
          levelMilestone.addNeighbor(keycard, ProgressionMilestone.CQ_KITCHEN_ACCESS);
          break;
        case CQ_NPC_LUKA_QUEST:
          levelMilestone.addNeighbor(keycard,
              ProgressionMilestone.CQ_CAFETERIA_TELEPATH_NEUTRALIZED);
          break;
        case CB_NPC_ELAZAR:
        case CB_NPC_COOL:
          levelMilestone.addNeighbor(keycard, ProgressionMilestone.DS_DIRECTOR_LOCKDOWN);
          break;
        case PP_NPC_CROAL:
        case PP_NPC_STILLWATER:
        case PP_NPC_FAURE:
          levelMilestone.addNeighbor(keycard, Keycard.PP_COOLANT_CHAMBER);
          break;
        case ND_NPC_STEELE:
          levelMilestone.addNeighbor(keycard, ProgressionMilestone.LO_NEUROMOD_DIVISION_PASSWORD);
          break;
        case BR_NPC_JANUARY:
          levelMilestone.addNeighbor(keycard, ProgressionMilestone.PP_ARMING_KEYS_PRIMED);
          break;
        case AR_NPC_DAHL:
        case LS_NPC_DAHL:
        case ND_NPC_DAHL:
        case SB_NPC_DAHL:
          levelMilestone.addNeighbor(keycard, ProgressionMilestone.AR_DAHL_ARRIVAL);
          break;
        case LS_NPC_FOLSON:
          levelMilestone.addNeighbor(keycard, ProgressionMilestone.AR_DAHL_ARRIVAL);
          break;
        default:
          levelMilestone.addNeighbor(keycard);
          break;
      }
    }

    levelsToMilestones.put(l, levelMilestone);
    return levelMilestone;
  }

  // Represents a level in which progress can be made
  public class LevelProgression {
    // The level this represents
    public Level level;
    // Map of actions to their requirement, if it exists
    public Multimap<ProgressionNode, Gate> unlocks;

    public LevelProgression(Level level) {
      this.level = level;
      unlocks = ArrayListMultimap.create();
    }

    public LevelProgression addNeighbor(ProgressionNode value, ProgressionNode... reqs) {
      unlocks.put(value, new Gate(reqs));
      return this;
    }

    public LevelProgression addNeighbor(ProgressionNode value, Set<ProgressionNode> reqs) {
      unlocks.put(value, new Gate(reqs));
      return this;
    }

    // Attempt to make progress in this level given existing progress
    public Set<ProgressionNode> updateProgress(Set<ProgressionNode> progress) {
      Set<ProgressionNode> newProgress = Sets.newHashSet();
      int prevSize = -1;
      // Revisit this node until all possible progress is unlocked and nothing further can be done.
      while (prevSize != newProgress.size()) {
        prevSize = newProgress.size();
        for (ProgressionNode candidate : unlocks.keySet()) {
          // If we already unlocked this candidate, skip
          if (progress.contains(candidate)) {
            continue;
          }
          // Get all possible gates to this neighboring node
          Collection<Gate> gates = unlocks.get(candidate);
          for (Gate g : gates) {
            // If any of these gates pass, add the node
            if (g.pass(progress)) {
              progress.add(candidate);
              newProgress.add(candidate);
              break;
            }
          }
        }
      }
      return newProgress;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();

      sb.append(level);
      sb.append("\n");
      for (ProgressionNode node : unlocks.keySet()) {
        sb.append(String.format("\t%s: %s", node, unlocks.get(node)));
      }

      return sb.toString();
    }
  }

  // A list of milestones that are required to reach another milestone.
  public class Gate {
    // List of requirements in order to reach this milestone.
    public ImmutableSet<ProgressionNode> reqs;

    public Gate(Collection<ProgressionNode> reqs) {
      if (reqs != null) {
        this.reqs = ImmutableSet.copyOf(reqs);
      }
    }

    public Gate(ProgressionNode[] reqs) {
      if (reqs != null) {
        this.reqs = ImmutableSet.copyOf(reqs);
      }
    }

    // Given a list of visited nodes, determines if this gate is open.
    public boolean pass(Set<ProgressionNode> progress) {
      for (ProgressionNode r : reqs) {
        if (!progress.contains(r)) {
          return false;
        }
      }
      return true;
    }

    @Override
    public String toString() {
      return reqs.toString();
    }
  }

  public static void main(String[] args) {
    ImmutableBiMap<Level, String> levelsToIds = new ImmutableBiMap.Builder<Level, String>()
        .put(Level.ARBORETUM, "1713490239386284818").put(Level.BRIDGE, "844024417275035158")
        .put(Level.CARGO_BAY, "15659330456296333985").put(Level.CREW_QUARTERS, "844024417252490146")
        .put(Level.DEEP_STORAGE, "1713490239377738413").put(Level.GUTS, "4349723564886052417")
        .put(Level.HARDWARE_LABS, "844024417263019221")
        .put(Level.LIFE_SUPPORT, "4349723564895209499").put(Level.LOBBY, "1713490239377285936")
        .put(Level.NEUROMOD_DIVISION, "12889009724983807463")
        .put(Level.POWER_PLANT, "6732635291182790112")
        .put(Level.PSYCHOTRONICS, "11824555372632688907")
        .put(Level.SHUTTLE_BAY, "1713490239386284988").put(Level.EXTERIOR, "1713490239386284337")
        .build();

    for (int i = 0; i < 1; i++) {
      Random r = new Random();
      long seed = 0L;// r.nextLong();
      System.out.println(seed);
      StationGenerator sg = new StationGenerator(seed, levelsToIds);

      ImmutableNetwork<Level, Door> stationConnectivity = sg.getNetwork();
          //StationConnectivityConsts.getDefaultNetwork();

      ProgressionGraph pg = new ProgressionGraph(stationConnectivity,
          KeycardConnectivityConsts.DEFAULT_CONNECTIVITY, 0L);

      boolean result = pg.verify();
      System.out.println(result);
      if (!result) {
        System.out.println(seed);
        System.out.println(pg.getVerifyResult());
        break;
      }
    }

  }
}
