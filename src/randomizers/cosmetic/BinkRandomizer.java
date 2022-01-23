package randomizers.cosmetic;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

public class BinkRandomizer extends BaseRandomizer {

  private static final String DIALOG_ID = "dialogId";
  private Map<String, String> swappedLinesMap;

  public BinkRandomizer(Settings s, ZipHelper zipHelper, Map<String, String> swappedLinesMap) {
    super(s, zipHelper);
    this.swappedLinesMap = swappedLinesMap;
  }

  @Override
  public void randomize() {
    Collection<String> files = zipHelper.listFiles(ZipHelper.BINK_SUBTITLES_PATH);
    for (String f : files) {
      randomizeBinkSubtitleFile(f);
    }
  }

  private void randomizeBinkSubtitleFile(String path) {
    try {
      Document d = zipHelper.getDocument(path);
      Element root = d.getRootElement();
      for (Element child : root.getChildren()) {
        String originalDialogIdHex = child.getAttributeValue(DIALOG_ID);
        // Translate from hex to decimal, which the swapped lines map uses
        String originalDialogIdDec = new BigInteger(originalDialogIdHex, 16).toString();
        if (swappedLinesMap.containsKey(originalDialogIdDec)) {
          // Translate back to hex
          String newDialogIdHex =
              new BigInteger(swappedLinesMap.get(originalDialogIdDec)).toString(16)
                  .toUpperCase();
          child.setAttribute(DIALOG_ID, newDialogIdHex);
        }
      }
      zipHelper.copyToPatch(d, path);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }
}
