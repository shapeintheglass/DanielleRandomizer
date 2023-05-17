package randomizers.generators;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.jdom2.Document;
import org.jdom2.Element;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import flowgraph.Edge;
import flowgraph.Node;
import proto.RandomizerSettings.Settings;
import proto.RandomizerSettings.StartItem;
import utils.LevelConsts;
import utils.MimicSliderUtils;
import utils.StationConnectivityConsts;
import utils.StationConnectivityConsts.Door;
import utils.StationConnectivityConsts.Level;
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
        .setAttribute("EntityId", entityId)
        .setAttribute("EntityGuid", entityGuid)
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
  
  private static Element getTeleportFlowgraph(String levelDir, String spawnDoor) {
    Element entity = createFlowgraphStarter("928759283", "29823482");
    Element nodes = entity.getChild("FlowGraph").getChild("Nodes");
    Element edges = entity.getChild("FlowGraph").getChild("Edges");
    
    int nodeId = 1;
    
    String gameStartNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(gameStartNodeId, "Game:Start", 
        ImmutableMap.of("InGame", "1", "InEditor", "1", "InEditorPlayFromHere", "0")).get());
    
    // Repurpose an unused token for Trauma Center, PlayerHasExogeuousContrastingAgent
    // to determine if we have already executed this script
    String gameTokenCheckNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(gameTokenCheckNodeId, "Mission:GameTokenCheck",
        ImmutableMap.of("gametokenid_Token", "1131953825", "CheckValue", "false")).get());
    
    // Set the token for the door to spawn by
    String gameTokenSetSpawnDoorNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(gameTokenSetSpawnDoorNodeId,  "Mission:GameTokenSet",
        ImmutableMap.of("gametokenid_Token", "101193842", "Value", spawnDoor)).get());
    
    // Trigger loading a different level
    String loadNextLevelNodeId = Integer.toString(nodeId++);
    String levelName = LevelConsts.LEVEL_PATH_TO_NAME.get(levelDir);
    nodes.addContent(new Node(loadNextLevelNodeId, "Mission:LoadNextLevel",
        ImmutableMap.of("NextLevel", levelName)).get());

    // Set day2 token
    String gameTokenSetDay2NodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(gameTokenSetDay2NodeId, "Mission:GameTokenSet",
        ImmutableMap.of("gametokenid_Token", "1304802513", "Value", "true")).get());
    
    // Enable saving
    String gameTokenSetEnableSavingNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(gameTokenSetEnableSavingNodeId, "Mission:GameTokenSet",
        ImmutableMap.of("gametokenid_Token", "2067707504", "Value", "true")).get());
    
    // Enable health/armor in HUD
    String healthNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(healthNodeId, "Ark:UI:EnableHUDHealth", ImmutableMap.of()).get());
    String armorNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(armorNodeId, "Ark:UI:EnableHUDArmor", ImmutableMap.of()).get());
    String scopeNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(scopeNodeId, "Ark:EnableScope", ImmutableMap.of()).get());
    String flashlightNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(flashlightNodeId, "Ark:Flashlight", ImmutableMap.of()).get());
    
    // Enemy health meters
    String enemyHealthNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(enemyHealthNodeId, "Ark:Player:EnableEnemyHealthMeter", ImmutableMap.of()).get());
    
    // Enable PDA pages
    String pdaAccessNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaAccessNodeId, "Ark:PDA:SetPDAAccess", ImmutableMap.of()).get());
    String pdaDateTimeNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaDateTimeNodeId, "Ark:PDA:EnableDateAndTime", ImmutableMap.of()).get());
    String pdaAbilitiesNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaAbilitiesNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "Abilities")).get());
    String pdaCodesNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaCodesNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "Codes")).get());
    String pdaFabPlansNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaFabPlansNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "FabricationPlans")).get());
    String pdaStatusNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaStatusNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "Status")).get());
    String pdaObjectivesNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaObjectivesNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "Objectives")).get());
    String pdaLevelMapNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaLevelMapNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "LevelMap")).get());
    String pdaStationMapNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaStationMapNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "StationMap")).get());
    String pdaNotesNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaNotesNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "Notes")).get());
    String pdaMetadataNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaMetadataNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "Metadata")).get());
    String pdaSuitModsNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaSuitModsNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "SuitMods")).get());
    String pdaScopeModsNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(pdaScopeModsNodeId, "Ark:PDA:EnablePDAPage",
        ImmutableMap.of("bEnable", "1", "Page", "Status")).get());

    // set location name
    String setAltNameForSimLabsNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(setAltNameForSimLabsNodeId, "Ark:Locations:SetAlternateName",
        ImmutableMap.of("location_Location", "12889009724983807463", "text_AlternateName", "@ui_simulationlabs")).get());

    // Send sim lab cleanup event
    String sendRemoteEventCleanSimLabsNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(sendRemoteEventCleanSimLabsNodeId, "Ark:SendRemoteEvent",
        ImmutableMap.of("remoteEvent_Event", "12889009725049448174")).get());
    
    String timeDelayNodeId = Integer.toString(nodeId++);
    nodes.addContent(new Node(timeDelayNodeId, "Time:Delay",
        ImmutableMap.of("delay", "1", "resetOnInput", "0")).get());

    // Game:Start In: ??? Out: output, LevelStateRestored
    // Mission:GameTokenCheck In: Trigger Out: True/False
    // Mission:GameTokenSet In: Trigger Out: OutValue
    // Time:Delay In: in Out: out
    // Mission:LoadNextLevel In: Trigger
    // Ark:UI:EnableHUDHealth Ark:UI:EnableHUDArmor Ark:EnableScope Ark:Flashlight Ark:Player:EnableEnemyHealthMeter Ark:PDA:SetPDAAccess In: Enable
    // Ark:PDA:EnablePDAPage In: Trigger
    // Ark:PDA:EnableDateAndTime  In: Show/Hide?
    // Ark:Locations:SetAlternateName In: Trigger
    // Ark:SendRemoteEvent In: Send
    // Ark:Player:LearnNameForResearchTopic In: Trigger
    
    // Game Start --> Token Check
    edges.addContent(new Edge(gameTokenCheckNodeId, gameStartNodeId, "Trigger", "output").get());
    // Token Check --> Set to Day 2 (to skip intro)
    edges.addContent(new Edge(gameTokenSetDay2NodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    // Token Check --> Time Delay
    edges.addContent(new Edge(timeDelayNodeId, gameTokenCheckNodeId, "in", "True").get());
    // Time Delay --> Set Spawn and Teleport
    edges.addContent(new Edge(gameTokenSetSpawnDoorNodeId, timeDelayNodeId, "Trigger", "out").get());
    edges.addContent(new Edge(loadNextLevelNodeId, gameTokenSetSpawnDoorNodeId, "Trigger", "OutValue").get());
    
    // Token Check --> Everything else
    edges.addContent(new Edge(gameTokenSetEnableSavingNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(healthNodeId, gameTokenCheckNodeId, "Enable", "True").get());
    edges.addContent(new Edge(armorNodeId, gameTokenCheckNodeId, "Enable", "True").get());
    edges.addContent(new Edge(scopeNodeId, gameTokenCheckNodeId, "Enable", "True").get());
    edges.addContent(new Edge(flashlightNodeId, gameTokenCheckNodeId, "Enable", "True").get());
    edges.addContent(new Edge(enemyHealthNodeId, gameTokenCheckNodeId, "Enable", "True").get());
    edges.addContent(new Edge(pdaAccessNodeId, gameTokenCheckNodeId, "Enable", "True").get());
    edges.addContent(new Edge(pdaDateTimeNodeId, gameTokenCheckNodeId, "Show", "True").get()); 
    edges.addContent(new Edge(pdaAbilitiesNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaCodesNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaFabPlansNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaStatusNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaObjectivesNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaLevelMapNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaStationMapNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaNotesNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaMetadataNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaSuitModsNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(pdaScopeModsNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(setAltNameForSimLabsNodeId, gameTokenCheckNodeId, "Trigger", "True").get());
    edges.addContent(new Edge(sendRemoteEventCleanSimLabsNodeId, gameTokenCheckNodeId, "Send", "True").get());
    return entity;
  }

  private static Element getAddItemsFlowgraph(List<StartItem> archetypes) {
    Element entity = createFlowgraphStarter("482317647", "91874238"); // Just needs to be unique within level
    Element nodes = entity.getChild("FlowGraph").getChild("Nodes");
    Element edges = entity.getChild("FlowGraph").getChild("Edges");
    
    int idCounter = 1;
    String gameStartNodeId = Integer.toString(idCounter++);
    nodes.addContent(new Node(gameStartNodeId, "Game:Start", ImmutableMap.of("InGame", "1", "InEditor", "1", "InEditorPlayFromHere", "0")).get());
  
    // Repurpose an unused token for Trauma Center, PlayerHasExogeuousContrastingAgent
    // to determine if we have already executed this script
    String tokenCheckNodeId = Integer.toString(idCounter++);
    nodes.addContent(new Node(tokenCheckNodeId, "Mission:GameTokenCheck", ImmutableMap.of("gametokenid_Token", "1131953825", "CheckValue", "false")).get());
    
    // Node pointing to the entity we want to add items into (the player)
    String playerNodeId = Integer.toString(idCounter++);
    nodes.addContent(new Node(playerNodeId, "Actor:LocalPlayer", ImmutableMap.of()).get());
    
    // Set the token to indicate the setup has completed successfully
    String gameTokenSetNodeId = Integer.toString(idCounter++);
    nodes.addContent(new Node(gameTokenSetNodeId, "Mission:GameTokenSet", ImmutableMap.of("gametokenid_Token", "1131953825", "Value", "true")).get());
    
    // Add welcome note
    String welcomeNodeId = Integer.toString(idCounter++);
    nodes.addContent(new Node(welcomeNodeId, "Ark:Notes:GiveNote", ImmutableMap.of("note_Note", WelcomeNoteHelper.WELCOME_NOTE_ID)).get());
    
    // Time delay for setting the token to make sure we don't run this script again
    String timeDelayNodeId = Integer.toString(idCounter++);
    nodes.addContent(new Node(timeDelayNodeId, "Time:Delay", ImmutableMap.of("delay", "1", "resetOnInput", "0")).get());
    
    // Game Start --> Token Check
    edges.addContent(new Edge(tokenCheckNodeId, gameStartNodeId, "Trigger", "output").get());

    // Token Check --> Add Welcome note
    edges.addContent(new Edge(welcomeNodeId, tokenCheckNodeId, "Trigger", "OutValue").get());
    
    // Token Check --> Timer --> Set game token
    edges.addContent(new Edge(timeDelayNodeId, tokenCheckNodeId, "in", "OutValue").get());
    edges.addContent(new Edge(gameTokenSetNodeId, timeDelayNodeId, "Trigger", "out").get());

    for (StartItem s : archetypes) {
      String newItemNodeId = Integer.toString(idCounter++);
      // Node for adding an inventory item
      nodes.addContent(new Node(newItemNodeId, "Inventory:ItemAdd", ImmutableMap.of("entityId", "0", "archetype", s.getArchetype(),
          "quantity", Integer.toString(s.getQuantity()), "playfanfare", "0")).get());
      // Token Check --> Add item
      edges.addContent(new Edge(newItemNodeId, tokenCheckNodeId, "add", "OutValue").get());
      // Ensure item is added to player inventory
      edges.addContent(new Edge(newItemNodeId, playerNodeId, "entityId", "entityId").get());
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
   * Appends new entities into a level map. This is generally needed for custom flowgraph scripts.
   * @param objects Objects xml node of level map
   * @param levelDir levelDir representing the name of the map
   * @param settings Randomizer settings
   * @param zipHelper Handle to assist writing to zip
   * @param mimicEntities Entities to transform into hidden mimics
   * @param talosStartLocation Level to teleport to at the start of the game
   * @param startingDoor Door to teleport to at the start of the game
   * @param r seeded random number generator
   */
  public static void addEntities(Element objects, String levelDir, Settings settings,
      ZipHelper zipHelper, List<Element> mimicEntities, Door spawnLocation, Random r) {
    
    String startLocation = LevelConsts.NEUROMOD_DIVISION;
    if (settings.getGameStartSettings().getRandomStart()) {
      Level startLevel = StationConnectivityConsts.LEVELS_TO_DOORS.inverse().get(spawnLocation).asList().get(0);
      startLocation = StationConnectivityConsts.LEVELS_TO_NAMES.get(startLevel);
    }
    
    // Scripts that must be executed in the Neuromod Division.
    if (levelDir.equals(LevelConsts.NEUROMOD_DIVISION)) {
      // Kick off self-destruct for GOTS mode
      if (settings.getExpSettings().getStartSelfDestruct()) {
        objects.addContent(getNeuromodDivisionSelfDestructFlowgraph());
      }

      // If starting outside apartment, add the "day 2 start" script
      if (settings.getGameStartSettings().getStartOutsideApartment() || settings.getGameStartSettings().getRandomStart()) {
        try {
          Document document = zipHelper.getDocument(ZipHelper.NATURAL_DAY_2_START_FILE);
          Element root = document.getRootElement().clone();
          objects.addContent(root);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      if (settings.getGameStartSettings().getRandomStart()) {
        Level connectingLevel = StationConnectivityConsts.LEVELS_TO_CONNECTING_DOORS.inverse().get(spawnLocation).asList().get(0);
        // TODO: Use the destination from the randomized station bc this is just wrong
        String fromDestination = StationConnectivityConsts.LEVELS_TO_DESTINATIONS.get(connectingLevel);
        // Insert flowgraph script that grants player their starting kit and teleports them
        objects.addContent(getTeleportFlowgraph(startLocation, fromDestination));
      }
    }

    // Scripts that must be run in the start level (may or may not be ND)
    if (levelDir.equals(startLocation)) {
      // Initialize starting inventory for player: Add any items specified in settings
      List<StartItem> gameStartItems = Lists.newArrayList();
      gameStartItems.addAll(settings.getStartingItemsList());

      // If station randomization is also enabled, add two EMP grenades to bypass the GUTS fan, just in case.
      if (settings.getGameplaySettings().getRandomizeStation() || settings.getGameStartSettings().getRandomStart()) {
        gameStartItems.add(StartItem.newBuilder()
            .setArchetype("ArkPickups.Ammo.EMPGrenades")
            .setQuantity(2)
            .build());
      }
      
      // If starting outside apartment, grant a wrench
      if (settings.getGameStartSettings().getStartOutsideApartment() || settings.getGameStartSettings().getRandomStart()) {
        gameStartItems.add(StartItem.newBuilder()
            .setArchetype("ArkPickups.Weapons.Wrench")
            .setQuantity(1)
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
      objects.addContent(getAddItemsFlowgraph(gameStartItems));
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
