package randomizers.gameplay.filters.rules;

import java.util.Random;

import org.jdom2.Element;

/**
 * Overwrites initial spawn trigger to something invalid
 * @author Kida
 *
 */
public class StartOutsideLobbyRule implements Rule {

  @Override
  public boolean trigger(Element e, Random r, String filename) {
    if (!filename.equals("research/simulationlabs")) {
      return false;
    }
    return e.getAttributeValue("Name").equals("Spawn_From_Nowhere") || e.getAttributeValue("Name")
        .equals("SpawnPoint4");
  }

  @Override
  public void apply(Element e, Random r, String filename) {
    // Fudge the original spawn point so the game can't find it
    e.setAttribute("Pos", "752.26373,1593.2826,17.469378");
    e.setAttribute("Rotate", "-0.38268325,0,0,0.92387962");
  }
}
