package randomizers.gameplay;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import json.SettingsJson;
import randomizers.BaseRandomizer;

public class WorkstationRandomizer extends BaseRandomizer {

  private static final String WORKSTATION_FILE_IN = "data/ark/workstationlibrary.xml";
  private static final String WORKSTATION_FILE_OUT = "ark/campaign/workstationlibrary.xml";

  private static final String ALEX_WORKSTATION_ID = "14667999412530895137";

  private File workstationFileOut;

  public WorkstationRandomizer(SettingsJson s, Path tempPatchDir) {
    super(s);
    workstationFileOut = tempPatchDir.resolve(WORKSTATION_FILE_OUT).toFile();
  }

  @Override
  public void randomize() {
    if (!settings.getGameplaySettings().getOpenStation()
        && !settings.getGameplaySettings().getRandomizeStation()) {
      return;
    }

    try {
      SAXBuilder saxBuilder = new SAXBuilder();
      Document document = saxBuilder.build(WORKSTATION_FILE_IN);
      Element root = document.getRootElement().getChild("Workstations");

      for (Element arkWorkstation : root.getChildren()) {
        if (arkWorkstation.getAttributeValue("ID").equals(ALEX_WORKSTATION_ID)) {
          unlockAllUtilities(arkWorkstation);
        }
      }

      workstationFileOut.getParentFile().mkdirs();
      workstationFileOut.createNewFile();
      XMLOutputter xmlOutput = new XMLOutputter();
      xmlOutput.setFormat(Format.getPrettyFormat());
      xmlOutput.output(document, new FileOutputStream(workstationFileOut));
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }
  }

  private void unlockAllUtilities(Element arkWorkstation) {
    List<Element> utilities = arkWorkstation.getChild("Utilities").getChildren();
    for (Element utility : utilities) {
      utility.setAttribute("Hidden", "false");
    }
  }

}
