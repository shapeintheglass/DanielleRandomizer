package randomizers.generators;

import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

public class SelfDestructTimerHelper extends BaseRandomizer {

  public SelfDestructTimerHelper(Settings s, ZipHelper zipHelper) {
    super(s, zipHelper);
  }

  @Override
  public void randomize() {
    try {
      Document document = zipHelper.getDocument(ZipHelper.GLOBALACTIONS_SELFDESTRUCTTIMER);
      Element nodes = document.getRootElement().getChild("Nodes");

      float selfDestructTimer = Float.parseFloat(settings.getExpSettings().getSelfDestructTimer());
      float selfDestructShuttleTimer = Float.parseFloat(settings.getExpSettings().getSelfDestructShuttleTimer());
      float diff = selfDestructTimer - selfDestructShuttleTimer;
      String strDiff = String.format("%.6f", diff);

      for (Element node : nodes.getChildren()) {
        if (node.getAttributeValue("Id").equals("2450")) {
          node.getChild("Inputs").setAttribute("B", strDiff);
        }
        if (node.getAttributeValue("Id").equals("2553")) {
          node.getChild("Inputs").setAttribute("in", settings.getExpSettings().getSelfDestructTimer());
        }
      }

      zipHelper.copyToPatch(document, ZipHelper.GLOBALACTIONS_SELFDESTRUCTTIMER);
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }

  }
}
