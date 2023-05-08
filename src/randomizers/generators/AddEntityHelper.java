package randomizers.generators;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.jdom2.Document;
import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import proto.RandomizerSettings.Settings;
import proto.RandomizerSettings.StartItem;
import utils.LevelConsts;
import utils.MimicSliderUtils;
import utils.ZipHelper;

public class AddEntityHelper {
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
  
  private static Element createFlowgraphStarter(String entityId, String entityGuid) {
    Element entity = new Element("Entity").setAttribute("Name", "AddItemsFlowgraph")
        .setAttribute("Pos", "0,0,0")
        .setAttribute("Rotate", "0,0,0,1")
        .setAttribute("EntityClass", "FlowgraphEntity")
        .setAttribute("EntityId", "2592734589")
        .setAttribute("EntityGuid", "162362214")
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
    flowgraph.addContent(new Element("Nodes"));
    flowgraph.addContent(new Element("Edges"));
    flowgraph.addContent(new Element("GraphTokens"));
    entity.addContent(properties);
    entity.addContent(flowgraph);
    return entity;
  }

  private static Element getNeuromodDivisionAddItemsFlowgraph(List<StartItem> archetypes) {
    Element entity = createFlowgraphStarter("482317647", "91874238"); // Just needs to be unique within level
    Element nodes = entity.getChild("FlowGraph").getChild("Nodes");
    Element edges = entity.getChild("FlowGraph").getChild("Edges");
    
    String gameStartNodeId = "1";
    Element gameStartNode = new Element("Node").setAttribute("Id", gameStartNodeId)
        .setAttribute("Class", "Game:Start")
        .setAttribute("pos", "-127,19,0")
        .addContent(new Element("Inputs").setAttribute("InGame", "1")
            .setAttribute("InEditor", "1")
            .setAttribute("InEditorPlayFromHere", "0"));
    String playerNodeId = "2";
    Element playerNode = new Element("Node").setAttribute("Id", playerNodeId)
        .setAttribute("Class", "Actor:LocalPlayer")
        .setAttribute("pos", "620,570,0")
        .addContent(new Element("Inputs"));
    nodes.addContent(gameStartNode);
    nodes.addContent(playerNode);
    
    int idCounter = 3;
    for (StartItem s : archetypes) {
      String newItemNodeId = Integer.toString(idCounter);
      Element newItemNode = new Element("Node").setAttribute("Id", newItemNodeId)
          .setAttribute("Class", "Inventory:ItemAdd")
          .setAttribute("pos", "1360,520,0")
          .addContent(new Element("Inputs")
              .setAttribute("entityId", "0")
              .setAttribute("archetype", s.getArchetype())
              .setAttribute("quantity", Integer.toString(s.getQuantity()))
              .setAttribute("playfanfare", "0"));
      nodes.addContent(newItemNode);
      
      Element newItemTriggerEdge = new Element("Edge")
          .setAttribute("nodeIn", newItemNodeId)
          .setAttribute("nodeOut", gameStartNodeId)
          .setAttribute("portIn", "add")
          .setAttribute("portOut", "output")
          .setAttribute("enabled", "1");
      Element newItemEntityEdge = new Element("Edge")
          .setAttribute("nodeIn", newItemNodeId)
          .setAttribute("nodeOut", playerNodeId)
          .setAttribute("portIn", "entityId")
          .setAttribute("portOut", "entityId")
          .setAttribute("enabled", "1");
      edges.addContent(newItemTriggerEdge);
      edges.addContent(newItemEntityEdge);
      idCounter++;
    }
    
    return entity;
  }
  
  private static Element getNeuromodDivisionSelfDestructFlowgraph() {
    Element entity = createFlowgraphStarter("5808", "0451"); // Just needs to be unique within level
    Element nodes = entity.getChild("FlowGraph").getChild("Nodes");
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
    Element edges = entity.getChild("FlowGraph").getChild("Edges");
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
        .setAttribute("FalloffInner", "1")
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
  
  // Generates mimics to be added to the level
  private static List<Element> createMimicsInLevel(List<Element> entitiesToMimic, Random r, String levelDir) {
    List<Element> mimicElements = Lists.newArrayList();
    for (Element e : entitiesToMimic) {
      // Modify the rotation of the original object
      e.setAttribute("Rotate", getNewRot(r));
      // Generate a GUID for the mimic
      String mimicGuid = Integer.toHexString(r.nextInt())
          .toUpperCase();
      // Generate a GUID for the mimic node
      String mimicId = Integer.toString(r.nextInt() / 2);
      // Slightly change the position of the mimic
      String mimicPos = fudgeLocation(e.getAttributeValue("Pos"), r);
      Element mimicInLevel = createMimicElement(e.getAttributeValue("Name"), mimicPos, mimicId, mimicGuid, levelDir);
      // Add the mimic to the level
      mimicElements.add(mimicInLevel);
    }
    return mimicElements;
  }
  
  // Generates element that represents a single added mimic
  private static Element createMimicElement(String objectName, String mimicPos, String mimicId, String mimicGuid, String levelDir) {
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
        .setAttribute("Layer", LEVEL_TO_MAIN_SCRIPTING_LAYER.get(levelDir));
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
    return mimicEntity;
  }
  
  // Generates flowgraph script telling all hidden mimics to hide
  private static Element createMimicFlowgraphScript(String levelDir, List<Element> objectsToMimic, List<Element> mimicsInLevel, Random r) {
    String flowgraphEntityId = "999999999"; // Must be unique per level file
    String entityGuid = "AAAAAAAAAA"; // Must be unique per level file
    Element flowgraph = new Element("Entity").setAttribute("Name", "FlowgraphMimics." + levelDir)
        .setAttribute("Pos", "0,0,0")
        .setAttribute("EntityClass", "FlowgraphEntity")
        .setAttribute("EntityId", flowgraphEntityId)
        .setAttribute("EntityGuid", entityGuid)
        .setAttribute("CastShadowViewDistRatio", "0")
        .setAttribute("CastShadowMinSpec", "1")
        .setAttribute("CastSunShadowMinSpec", "8")
        .setAttribute("ShadowCasterType", "0")
        .setAttribute("Layer", LEVEL_TO_MAIN_SCRIPTING_LAYER.getOrDefault(levelDir, ""))
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

    for (int i = 0; i < objectsToMimic.size(); i++) {
      Element objectToMimic = objectsToMimic.get(i);
      Element mimic = mimicsInLevel.get(i);
      // Generate a GUID for the object node
      String objectNodeGuid = String.format("{%s}", UUID.randomUUID().toString().toUpperCase());
      // Generate a GUID for the mimic node
      String mimicNodeGuid = String.format("{%s}", UUID.randomUUID().toString().toUpperCase());
      // Get the GUID for the mimic
      String mimicGuid = mimic.getAttributeValue("EntityGuid");
      // Get the GUID for the object
      String objectGuid = objectToMimic.getAttributeValue("EntityGuid");

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
      
      // Flip a coin to decide whether this mimic should replace or duplicate the item.
      boolean replaceTheItem = r.nextBoolean();
      
      Element startMimickingNode = createMimicNode(startMimickingNodeId, replaceTheItem);
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
    }

    flowgraph.addContent(new Element("FlowGraph").setAttribute("Description", "")
        .setAttribute("Group", "")
        .setAttribute("enabled", "1")
        .setAttribute("MultiPlayer", "ClientServer")
        .addContent(flowGraphNodes)
        .addContent(flowGraphEdges));
    return flowgraph;
  }

  private static String fudgeLocation(String originalPos, Random r) {
    // Parse position into coordinates
    String[] tokens = originalPos.split(",");
    // Fudge x and y by a certain threshold
    float xFudge = r.nextFloat() * 2 * MimicSliderUtils.MIMIC_POSITION_FUDGE;
    float yFudge = r.nextFloat() * 2 * MimicSliderUtils.MIMIC_POSITION_FUDGE;
    float newX = Float.parseFloat(tokens[0]) - MimicSliderUtils.MIMIC_POSITION_FUDGE + xFudge;
    float newY = Float.parseFloat(tokens[1]) - MimicSliderUtils.MIMIC_POSITION_FUDGE + yFudge;
    return String.format("%.5f,%.5f,%s", newX, newY, tokens[2]);
  }

  private static String getNewRot(Random r) {
    // First coordinate of quaternion can be -1 <= w <= 1
    float w = (r.nextFloat() * 2) - 1;
    // Last coordinate is the complement so that w^2 + z^2 = 1
    // Therefore, z = sqrt( 1 - w^2 )
    float z = (float) Math.sqrt(1 - Math.pow(w, 2));
    return String.format("%.5f,%.5f,%.5f,%.5f", z, 0.0f, 0.0f, w);
  }
  
  private static Element createMimicNode(String startMimickingNodeId, boolean replace) {
    return new Element("Node").setAttribute("Id", startMimickingNodeId)
        .setAttribute("Class", "Ark:NPC:Mimic:StartMimicking")
        .setAttribute("pos", "0,0,0")
        .addContent(new Element("Inputs").setAttribute("entityId", "0")
            .setAttribute("EntityToMimic", "0")
            .setAttribute("Reason", "1") // 1 and 3 = no timeout, 2 and 4 = has timeout. 1 = not jumpy, 3 = jumpy
            .setAttribute("Replace", replace ? "1" : "0"));
  }

  /**
   * Appends new entities into a level map.
   * @param objects Objects xml node of level map
   * @param levelDir levelDir representing the name of the map
   * @param settings Randomizer settings
   * @param zipHelper Handle to assist writing to zip
   * @param mimicEntities Entities to transform into hidden mimics
   * @param r seeded random number generator
   */
  public static void addEntities(Element objects, String levelDir, Settings settings,
      ZipHelper zipHelper, List<Element> mimicEntities, Random r) {
    
    // Neuromod Division-specific settings (usually game start stuff)
    if (levelDir.equals(LevelConsts.NEUROMOD_DIVISION)) {
      if (settings.getExpSettings().getStartSelfDestruct()) {
        objects.addContent(getNeuromodDivisionSelfDestructFlowgraph());
      }
      
      List<StartItem> gameStartItems = Lists.newArrayList();
      // Add any items specified in settings
      gameStartItems.addAll(settings.getStartingItemsList());
      
      // If starting outside apartment, shift to second day and add some starting equipment
      if (settings.getGameStartSettings().getStartOutsideApartment()) {
        try {
          Document document = zipHelper.getDocument(ZipHelper.NATURAL_DAY_2_START_FILE);
          Element root = document.getRootElement()
              .clone();

          objects.addContent(root);
        } catch (Exception e) {
          e.printStackTrace();
        }
        gameStartItems.add(StartItem.newBuilder()
            .setArchetype("ArkPickups.Weapons.Wrench")
            .setQuantity(1)
            .build());
        gameStartItems.add(StartItem.newBuilder()
            .setArchetype("ArkPickups.Ammo.EMPGrenades")
            .setQuantity(2)
            .build());
      }
      
      if (settings.getGameStartSettings().getAddJetpack()) {
        gameStartItems.add(StartItem.newBuilder()
            .setArchetype("ArkPickups.Player.ZeroGSuit")
            .setQuantity(1)
            .build());
      }
      if (settings.getGameStartSettings().getAddPsychoscope()) {
        gameStartItems.add(StartItem.newBuilder()
            .setArchetype("ArkPickups.Player.Psychoscope")
            .setQuantity(1)
            .build());
      }
      
      if (!gameStartItems.isEmpty()) {
        // Add requested starting items to player
        objects.addContent(getNeuromodDivisionAddItemsFlowgraph(gameStartItems)); 
      }
    }

    // Gravity boxes: Zero Gravity Everywhere  will add a gravity box.
    if (settings.getExpSettings().getZeroGravityEverywhere() 
        && !levelDir.equals(LevelConsts.EXTERIOR) 
        && !levelDir.equals(LevelConsts.GUTS)) {
      objects.addContent(gravityBox(0));
    }

    // Mimic slider: Generate the actual mimics for the items labelled as entities to mimic
    if (settings.getGameplaySettings().getRandomizeMimics().getIsEnabled() && LEVEL_TO_MAIN_SCRIPTING_LAYER.containsKey(levelDir)) {
      // Generate mimics to add to level
      List<Element> mimicsInLevel = createMimicsInLevel(mimicEntities, r, levelDir);
      Element flowgraph = createMimicFlowgraphScript(levelDir, mimicEntities, mimicsInLevel, r);
      // Add mimics to the level
      for (Element e : mimicsInLevel) {
        objects.addContent(e);
      }
      // Add the flowgraph to the level
      objects.addContent(flowgraph);
    }
  }
}
