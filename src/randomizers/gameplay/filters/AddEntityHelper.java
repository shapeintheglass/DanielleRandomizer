package randomizers.gameplay.filters;

import org.jdom2.Element;

import json.SettingsJson;

public class AddEntityHelper {

  private static Element getNeuromodDivisionSelfDestructFlowgraph() {
    Element entity = new Element("Entity").setAttribute("Name", "Shenanigans")
        .setAttribute("Pos", "0,0,0")
        .setAttribute("Rotate", "0,0,0,1")
        .setAttribute("EntityClass", "FlowgraphEntity")
        .setAttribute("EntityId", "5807")
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
        .addContent(new Element("Inputs").setAttribute("remoteEvent_Event", "14667999412552670772"));
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

  public static void addEntities(Element objects, String filename, SettingsJson settings) {
    if (settings.getGameplaySettings().getStartSelfDestruct() && filename.equals("research/simulationlabs")) {
      objects.addContent(getNeuromodDivisionSelfDestructFlowgraph());
    }
  }
}
