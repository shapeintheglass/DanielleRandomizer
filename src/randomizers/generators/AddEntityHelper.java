package randomizers.generators;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.jdom2.Document;
import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;
import proto.RandomizerSettings.Settings;
import utils.LevelConsts;
import utils.ZipHelper;

public class AddEntityHelper {

  private static final float MIMIC_POSITION_FUDGE = 1.0f;
  private static final ImmutableMap<String, String> LEVEL_TO_MAIN_SCRIPTING_LAYER =
      new ImmutableMap.Builder<String, String>()
          .put(LevelConsts.NEUROMOD_DIVISION, "Simulation_Scripting_MAIN")
          .put(LevelConsts.LOBBY, "Lobby_Scripting")
          .put(LevelConsts.HARDWARE_LABS, "Prototype_Scripting")
          .put(LevelConsts.PSYCHOTRONICS, "Pyschotronics_Scripting")
          .put(LevelConsts.SHUTTLE_BAY, "ShuttleBay_Scripting")
          .put(LevelConsts.GUTS, "Guts_Scripting")
          .put(LevelConsts.ARBORETUM, "Arboretum_Scripting")
          .put(LevelConsts.CREW_QUARTERS, "CrewFacilities_Scripting")
          .put(LevelConsts.DEEP_STORAGE, "CorporateIT_Scripting")
          .put(LevelConsts.BRIDGE, "Bridge_scripting")
          .put(LevelConsts.CARGO_BAY, "CargoBay_Scripting")
          .put(LevelConsts.LIFE_SUPPORT, "LifeSupport_Scripting_Central")
          .put(LevelConsts.POWER_PLANT, "PowerSource_Scripting")
          .put(LevelConsts.EXTERIOR, "Exterior_Scripting")
          .build();

  private static Element getNeuromodDivisionSelfDestructFlowgraph() {
    Element entity = new Element("Entity").setAttribute("Name", "Shenanigans")
        .setAttribute("Pos", "0,0,0")
        .setAttribute("Rotate", "0,0,0,1")
        .setAttribute("EntityClass", "FlowgraphEntity")
        .setAttribute("EntityId", "5808")
        .setAttribute("EntityGuid", "0451")
        .setAttribute("CastShadowViewDistRatio", "0")
        .setAttribute("CastShadowMinSpec", "1")
        .setAttribute("CastSunShadowMinSpec", "8")
        .setAttribute("ShadowCasterType", "0")
        .setAttribute("Layer", "AlwaysLoaded")
        .setAttribute("mod", "1");
    Element properties = new Element("Properties").setAttribute("bDrawThroughWalls", "0")
        .setAttribute("bHideInEditor", "0")
        .setAttribute("bHideInGame", "0")
        .setAttribute("clrMainColor", "0")
        .setAttribute("clrMainColor", "1,0.5,0")
        .setAttribute("nMainDist", "100")
        .setAttribute("MainText", "")
        .setAttribute("nMaxNoteHeight", "0")
        .setAttribute("nMaxNoteWidth", "30")
        .setAttribute("fSize", "1.2")
        .setAttribute("clrSummaryColor", "1,0.5,0")
        .setAttribute("nSummaryDist", "20")
        .setAttribute("SummaryText", "");
    Element flowgraph = new Element("FlowGraph").setAttribute("Description", "")
        .setAttribute("Group", "Utilities")
        .setAttribute("enabled", "1")
        .setAttribute("MultiPlayer", "ClientServer");
    Element nodes = new Element("Nodes");
    Element node1 = new Element("Node").setAttribute("Id", "1")
        .setAttribute("Class", "Game:Start")
        .setAttribute("pos", "-127,19,0")
        .addContent(new Element("Inputs").setAttribute("InGame", "1")
            .setAttribute("InEditor", "1")
            .setAttribute("InEditorPlayFromHere", "0"));
    Element node2 = new Element("Node").setAttribute("Id", "2")
        .setAttribute("Class", "Ark:SendRemoteEvent")
        .setAttribute("pos", "484,20,0")
        .setAttribute("mod", "1")
        .addContent(new Element("Inputs").setAttribute("remoteEvent_Event", "844024417287297631"));
    Element node3 = new Element("Node").setAttribute("Id", "3")
        .setAttribute("Class", "Mission:GameTokenCheck")
        .setAttribute("pos", "-126,105,0")
        .setAttribute("mod", "1")
        .addContent(new Element("Inputs").setAttribute("gametokenid_Token", "101193842")
            .setAttribute("CheckValue", "From_Nowhere"));
    Element node4 = new Element("Node").setAttribute("Id", "4")
        .setAttribute("Class", "Ark:SendRemoteEvent")
        .setAttribute("pos", "484,20,0")
        .setAttribute("mod", "1")
        .addContent(
            new Element("Inputs").setAttribute("remoteEvent_Event", "14667999412552670772"));
    nodes.addContent(node1);
    nodes.addContent(node2);
    nodes.addContent(node3);
    nodes.addContent(node4);
    Element edges = new Element("Edges");
    Element edge1 = new Element("Edge").setAttribute("nodeIn", "3")
        .setAttribute("nodeOut", "1")
        .setAttribute("portIn", "Trigger")
        .setAttribute("portOut", "output")
        .setAttribute("enabled", "1")
        .setAttribute("mod", "1");
    Element edge2 = new Element("Edge").setAttribute("nodeIn", "2")
        .setAttribute("nodeOut", "3")
        .setAttribute("portIn", "Send")
        .setAttribute("portOut", "True")
        .setAttribute("enabled", "1")
        .setAttribute("mod", "1");
    Element edge3 = new Element("Edge").setAttribute("nodeIn", "4")
        .setAttribute("nodeOut", "3")
        .setAttribute("portIn", "Send")
        .setAttribute("portOut", "True")
        .setAttribute("enabled", "1")
        .setAttribute("mod", "1");
    edges.addContent(edge1);
    edges.addContent(edge2);
    edges.addContent(edge3);
    flowgraph.addContent(nodes);
    flowgraph.addContent(edges);
    entity.addContent(properties);
    entity.addContent(flowgraph);
    return entity;
  }

  private static Element gravityBox(double gravityVector) {
    BigInteger id = new BigInteger("1000000");
    Element entity =
        new Element("Entity").setAttribute("Name", String.format("GravityBox%s", id.toString()))
            .setAttribute("Pos", "0.0,0.0,0.0")
            .setAttribute("Rotate", "0.0,0,0,0")
            .setAttribute("EntityClass", "GravityBox")
            .setAttribute("EntityId", id.toString())
            .setAttribute("EntityGuid", id.toString(16)
                .toUpperCase())
            .setAttribute("CastShadowViewDistRatio", "0")
            .setAttribute("CastShadowMinSpec", "1")
            .setAttribute("CastSunShadowMinSpec", "8")
            .setAttribute("ShadowCasterType", "0")
            .setAttribute("Layer", "AlwaysLoaded");
    Element properties = new Element("Properties").setAttribute("bActive", "1")
        .setAttribute("bAffectsParticleEmitterPosition", "0")
        .setAttribute("FalloffInner", "0")
        .setAttribute("vector_ImpulseActivate", "0,0,0")
        .setAttribute("vector_ImpulseDeactivate", "0,0,0")
        .setAttribute("bUniform", "1");
    properties.addContent(new Element("BoxMax").setAttribute("x", "100000")
        .setAttribute("y", "100000")
        .setAttribute("z", "100000"));
    properties.addContent(new Element("BoxMin").setAttribute("x", "0")
        .setAttribute("y", "0")
        .setAttribute("z", "0"));
    properties.addContent(new Element("Gravity").setAttribute("x", "0")
        .setAttribute("y", "0")
        .setAttribute("z", Double.toString(gravityVector)));

    entity.addContent(properties);
    return entity;
  }

  private static String getNewPos(String originalPos, Random r) {
    // Parse position into coordinates
    String[] tokens = originalPos.split(",");
    // Fudge x and y by a certain threshold
    float xFudge = r.nextFloat() * 2 * MIMIC_POSITION_FUDGE;
    float yFudge = r.nextFloat() * 2 * MIMIC_POSITION_FUDGE;
    float newX = Float.parseFloat(tokens[0]) - MIMIC_POSITION_FUDGE + xFudge;
    float newY = Float.parseFloat(tokens[1]) - MIMIC_POSITION_FUDGE + yFudge;
    return String.format("%.5f,%.5f,%s", newX, newY, tokens[2]);
  }

  public static void addEntities(Element objects, String filename, Settings settings,
      ZipHelper zipHelper, List<Element> mimicEntities, Random r) {
    if (settings.getExpSettings()
        .getStartSelfDestruct() && filename.equals("research/simulationlabs")) {
      objects.addContent(getNeuromodDivisionSelfDestructFlowgraph());
    }

    if (settings.getGameStartSettings()
        .getStartOnSecondDay() && filename.equals("research/simulationlabs")) {
      try {
        Document document = zipHelper.getDocument(ZipHelper.NATURAL_DAY_2_START_FILE);
        Element root = document.getRootElement()
            .clone();

        objects.addContent(root);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (settings.getExpSettings()
        .getZeroGravityEverywhere() && !filename.equals("station/exterior")) {
      if (filename.equals("research/zerog_utilitytunnels") && settings.getExpSettings()
          .getEnableGravityInExtAndGuts()) {
        return;
      }
      objects.addContent(gravityBox(0));
    }

    if (settings.getGameplaySettings()
        .getRandomizeMimics()
        .getIsEnabled() && LEVEL_TO_MAIN_SCRIPTING_LAYER.containsKey(filename)) {
      // Generate a flowgraph entity
      String flowgraphEntityId = "999999999"; // Must be unique per level file
      String entityGuid = "AAAAAAAA"; // Must be unique per level file
      Element flowgraph = new Element("Entity").setAttribute("Name", "FlowgraphMimics." + filename)
          .setAttribute("Pos", "0,0,0")
          .setAttribute("EntityClass", "FlowgraphEntity")
          .setAttribute("EntityId", flowgraphEntityId)
          .setAttribute("EntityGuid", entityGuid)
          .setAttribute("CastShadowViewDistRatio", "0")
          .setAttribute("CastShadowMinSpec", "1")
          .setAttribute("CastSunShadowMinSpec", "8")
          .setAttribute("ShadowCasterType", "0")
          .setAttribute("Layer", LEVEL_TO_MAIN_SCRIPTING_LAYER.getOrDefault(filename, ""))
          .addContent(new Element("Properties").setAttribute("bDrawThroughWalls", "0")
              .setAttribute("bHideInEditor", "0")
              .setAttribute("bHideInGame", "0")
              .setAttribute("clrMainColor", "1,0.5,0")
              .setAttribute("nMainDist", "100")
              .setAttribute("MainText", "")
              .setAttribute("nMaxNoteHeight", "0")
              .setAttribute("nMaxNoteWidth", "30")
              .setAttribute("fSize", "1.2")
              .setAttribute("clrSummaryColor", "1,0.5,0")
              .setAttribute("nSummaryDist", "20")
              .setAttribute("SummaryText", "Adds hidden mimics"));
      Element flowGraphNodes = new Element("Nodes");
      Element flowGraphEdges = new Element("Edges");
      int nodeIndexCounter = 1;

      for (Element e : mimicEntities) {
        // Nab the Entity GUID of the object to mimic and its coordinates
        String objectGuid = e.getAttributeValue("EntityGuid");
        String objectPosition = e.getAttributeValue("Pos");
        String objectName = e.getAttributeValue("Name");

        if (objectGuid == null || objectPosition == null || objectName == null) {
          continue;
        }

        // Generate a GUID for the object node
        String objectNodeGuid = String.format("{%s}", UUID.randomUUID()
            .toString()
            .toUpperCase());

        // Generate a GUID for the mimic
        String mimicGuid = Integer.toHexString(r.nextInt())
            .toUpperCase();

        // Generate a GUID for the mimic node
        String mimicNodeGuid = String.format("{%s}", UUID.randomUUID()
            .toString()
            .toUpperCase());
        String mimicId = Integer.toString(r.nextInt() / 2);

        String mimicPos = getNewPos(objectPosition, r);

        // Create the flowgraph nodes and edges
        String spawnerNodeId = Integer.toString(nodeIndexCounter++);
        String entityIdNodeId = Integer.toString(nodeIndexCounter++);
        String startMimickingNodeId = Integer.toString(nodeIndexCounter++);
        Element spawnerNode = new Element("Node").setAttribute("Id", spawnerNodeId)
            .setAttribute("Class", "entity:ArkNpcSpawner")
            .setAttribute("pos", "0,0,0")
            .setAttribute("EntityGUID", mimicNodeGuid)
            .setAttribute("EntityGUID_64", mimicGuid)
            .addContent(new Element("Inputs").setAttribute("entityId", "0")
                .setAttribute("Spawn", "0"));
        Element entityIdNode = new Element("Node").setAttribute("Id", entityIdNodeId)
            .setAttribute("Class", "Entity:EntityId")
            .setAttribute("pos", "0,0,0")
            .setAttribute("EntityGUID", objectNodeGuid)
            .setAttribute("EntityGUID_64", objectGuid)
            .addContent(new Element("Inputs").setAttribute("entityId", "0"));
        Element startMimickingNode = new Element("Node").setAttribute("Id", startMimickingNodeId)
            .setAttribute("Class", "Ark:NPC:Mimic:StartMimicking")
            .setAttribute("pos", "0,0,0")
            .addContent(new Element("Inputs").setAttribute("entityId", "0")
                .setAttribute("EntityToMimic", "0")
                .setAttribute("Reason", "3")
                .setAttribute("Replace", "0"));
        // Indicates that the mimicking should start once spawning has succeeded
        Element startEdge = new Element("Edge").setAttribute("nodeIn", startMimickingNodeId)
            .setAttribute("nodeOut", spawnerNodeId)
            .setAttribute("portIn", "Start")
            .setAttribute("portOut", "Succeeded")
            .setAttribute("enabled", "1");
        // Indicates that the entity spawned is the one that should do the mimicking
        Element entityIdEdge = new Element("Edge").setAttribute("nodeIn", startMimickingNodeId)
            .setAttribute("nodeOut", spawnerNodeId)
            .setAttribute("portIn", "entityId")
            .setAttribute("portOut", "SpawnedEntityId")
            .setAttribute("enabled", "1");
        // Indicates that the entity id of the object is the id to be mimicked
        Element entityToMimicEdge = new Element("Edge").setAttribute("nodeIn", startMimickingNodeId)
            .setAttribute("nodeOut", entityIdNodeId)
            .setAttribute("portIn", "EntityToMimic")
            .setAttribute("portOut", "Id")
            .setAttribute("enabled", "1");
        flowGraphNodes.addContent(spawnerNode)
            .addContent(entityIdNode)
            .addContent(startMimickingNode);
        flowGraphEdges
            // .addContent(gamestartEdge)
            .addContent(startEdge)
            .addContent(entityIdEdge)
            .addContent(entityToMimicEdge);

        // Generate the mimic entity
        Element mimicEntity = new Element("Entity").setAttribute("Name", "Mimic." + objectName)
            .setAttribute("Pos", mimicPos)
            .setAttribute("EntityClass", "ArkNpcSpawner")
            .setAttribute("EntityId", mimicId)
            .setAttribute("EntityGuid", mimicGuid)
            .setAttribute("CastShadowViewDistRatio", "30")
            .setAttribute("CastShadowMinSpec", "1")
            .setAttribute("CastSunShadowMinSpec", "1")
            .setAttribute("ShadowCasterType", "4")
            .setAttribute("Layer", LEVEL_TO_MAIN_SCRIPTING_LAYER.get(filename));
        Element mimicProperties =
            new Element("Properties").setAttribute("sNpcArchetype", "ArkNpcs.Mimics.Mimic")
                .setAttribute("pose_PoseAnim", "")
                .setAttribute("bRigorMortis", "0")
                .setAttribute("bSpawnAlwaysUpdate", "0")
                .setAttribute("bSpawnBroken", "0")
                .setAttribute("bSpawnCorpse", "0")
                .setAttribute("bSpawnDormant", "0")
                .setAttribute("bSpawnOnGameStart", "1");
        Element mimicProperties2 = new Element("Properties2")
            .setAttribute("roomContainer_wanderRoomsContainer", "")
            .addContent(new Element("ArkDialogOverride").setAttribute("fPlayerApproachCDFar", "-1")
                .setAttribute("fPlayerApproachCDMedium", "-1")
                .setAttribute("fPlayerApproachCDNear", "-1")
                .setAttribute("fPlayerApproachDistanceFar", "-1")
                .setAttribute("fPlayerApproachDistanceMedium", "-1")
                .setAttribute("fPlayerApproachDistanceNear", "-1")
                .setAttribute("fPlayerLoiterCD", "-1")
                .setAttribute("fPlayerLoiterDistance", "-1"));
        mimicEntity.addContent(mimicProperties)
            .addContent(mimicProperties2)
            .addContent(new Element("RenderProxy"));

        // Add the mimic to the level
        objects.addContent(mimicEntity);
      }

      flowgraph.addContent(new Element("FlowGraph").setAttribute("Description", "")
          .setAttribute("Group", "")
          .setAttribute("enabled", "1")
          .setAttribute("MultiPlayer", "ClientServer")
          .addContent(flowGraphNodes)
          .addContent(flowGraphEdges));

      // Add the flowgraph to the level
      objects.addContent(flowgraph);
    }
  }
}
