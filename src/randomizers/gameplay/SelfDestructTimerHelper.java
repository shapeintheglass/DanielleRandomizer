package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import json.SettingsJson;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

public class SelfDestructTimerHelper extends BaseRandomizer {

  private Path tempPatchDir;

  public SelfDestructTimerHelper(SettingsJson s, Path tempPatchDir, ZipHelper zipHelper) {
    super(s, zipHelper);
    this.tempPatchDir = tempPatchDir;
  }

  private static final String OUTPUT_FILE = "libs/globalactions/global_selfdestructsequence.xml";

  @Override
  public void randomize() {
    File out = tempPatchDir.resolve(OUTPUT_FILE).toFile();
    out.getParentFile().mkdirs();

    try {
      Document document = zipHelper.getDocument(ZipHelper.GLOBALACTIONS_SELFDESTRUCTTIMER);
      Element nodes = document.getRootElement().getChild("Nodes");

      float selfDestructTimer = Float.parseFloat(settings.getGameplaySettings().getSelfDestructTimer());
      float selfDestructShuttleTimer = Float.parseFloat(settings.getGameplaySettings().getSelfDestructShuttleTimer());
      float diff = selfDestructTimer - selfDestructShuttleTimer;
      String strDiff = String.format("%.6f", diff);

      for (Element node : nodes.getChildren()) {
        if (node.getAttributeValue("Id").equals("2450")) {
          node.getChild("Inputs").setAttribute("B", strDiff);
        }
        if (node.getAttributeValue("Id").equals("2553")) {
          node.getChild("Inputs").setAttribute("in", settings.getGameplaySettings().getSelfDestructTimer());
        }
      }

      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      xmlOutput.output(document, new FileOutputStream(out));
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }

  }
}