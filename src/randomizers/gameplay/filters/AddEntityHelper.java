package randomizers.gameplay.filters;

import java.math.BigInteger;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;

import json.SettingsJson;
import utils.ZipHelper;

public class AddEntityHelper {

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

  private static Element gravityBox(double gravityVector) {

    // Used for generating IDs only
    Random r = new Random();
    BigInteger id = new BigInteger(Integer.toString(r.nextInt(Integer.MAX_VALUE)));
    Element entity = new Element("Entity").setAttribute("Name", String.format("GravityBox%s", id.toString()))
        .setAttribute("Pos", "0.0,0.0,0.0")
        .setAttribute("Rotate", "0.0,0,0,0")
        .setAttribute("EntityClass", "GravityBox")
        .setAttribute("EntityId", id.toString())
        .setAttribute("EntityGuid", id.toString(16).toUpperCase())
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
    properties.addContent(new Element("BoxMin").setAttribute("x", "0").setAttribute("y", "0").setAttribute("z", "0"));
    properties.addContent(new Element("Gravity").setAttribute("x", "0")
        .setAttribute("y", "0")
        .setAttribute("z", Double.toString(gravityVector)));

    entity.addContent(properties);
    return entity;
  }

  public static void addEntities(Element objects, String filename, SettingsJson settings, ZipHelper zipHelper) {
    if (settings.getGameplaySettings().getStartSelfDestruct() && filename.equals("research/simulationlabs")) {
      objects.addContent(getNeuromodDivisionSelfDestructFlowgraph());
    }

    if (settings.getGameplaySettings().getStartOn2ndDay() && filename.equals("research/simulationlabs")) {
      try {
        Document document = zipHelper.getDocument(ZipHelper.NATURAL_DAY_2_START_FILE);
        Element root = document.getRootElement().clone();

        objects.addContent(root);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (settings.getGameplaySettings().getRandomizeNightmare() && filename.equals("research/simulationlabs")) {
      try {
        Document document = zipHelper.getDocument(ZipHelper.ENABLE_NIGHTMARE_MANAGER);
        Element root = document.getRootElement().clone();

        objects.addContent(root);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (settings.getGameplaySettings().getDisableGravity() && !filename.equals("station/exterior") && !filename
        .equals("research/zerog_utilitytunnels")) {
      objects.addContent(gravityBox(0));
    }
  }
}
