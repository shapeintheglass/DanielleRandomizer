package randomizers.cosmetic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.common.collect.Lists;

import json.SettingsJson;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

/**
 * 
 * Voice line randomizer.
 *
 */
public class VoiceRandomizer extends BaseRandomizer {

  private Map<String, String> dialogIdToCharacterId = new HashMap<String, String>();
  private Map<String, List<String>> characterToDialog = new HashMap<String, List<String>>();
  private Map<String, String> swappedLinesMap = new HashMap<String, String>();

  private Path tempPatchDir;

  public VoiceRandomizer(SettingsJson s, Path tempPatchDir, ZipHelper zipHelper) {
    super(s, zipHelper);
    this.tempPatchDir = tempPatchDir;
  }

  /**
   * Randomizes voice lines and installs into temp directory
   */
  public void randomize() {
    Path outputDir = tempPatchDir.resolve("ark/dialog/dialoglogic");

    try {
      getDialogIds();
      randomizeAndWrite(ZipHelper.DIALOGIC_PATH, outputDir);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }
  
  public Map<String, String> getSwappedLinesMap() {
    return swappedLinesMap;
  }

  // Randomizes the given file and adds it into the given output dir
  private void randomizeAndWrite(String srcDir, Path outDir) throws FileNotFoundException, IOException, JDOMException {
    for (String file : zipHelper.listFiles(srcDir)) {
      String name = ZipHelper.getFileName(file);
      if (zipHelper.isDirectory(file)) {
        // Call recursively
        randomizeAndWrite(file, outDir.resolve(name));
      } else {
        // Create and randomize new file
        randomizeDialog(file, file);
      }
    }
  }

  // Writes a randomized version of the in file to the out file.
  private void randomizeDialog(String in, String out) throws FileNotFoundException, IOException, JDOMException {
    Document document = zipHelper.getDocument(in);
    for (Element responseSet : document.getRootElement().getChildren("ResponseSet")) {
      Element response = responseSet.getChild("Response");
      String oldDialog = response.getAttributeValue("dialog");
      // If this line has not already been swapped, add it
      if (!swappedLinesMap.containsKey(oldDialog)) {
        String characterFile = dialogIdToCharacterId.get(oldDialog);
        List<String> dialogLines = characterToDialog.get(characterFile);
        String newDialog = dialogLines.remove(0);
        characterToDialog.put(characterFile, dialogLines);
        swappedLinesMap.put(oldDialog, newDialog);
      }

      response.setAttribute("dialog", swappedLinesMap.get(oldDialog));
    }

    zipHelper.copyToPatch(document, out);
  }

  // Populates maps for dialog IDs.
  private void getDialogIds() throws FileNotFoundException, IOException, JDOMException {
    Collection<String> files = zipHelper.listFiles(ZipHelper.VOICES_PATH);
    for (String file : files) {
      String fileName = ZipHelper.getFileName(file);
      List<String> voiceLineIds = Lists.newArrayList();
      Document document = zipHelper.getDocument(file);
      Element root = document.getRootElement();
      for (Element dialog : root.getChildren()) {
        String voiceLineId = dialog.getAttributeValue("id");
        dialogIdToCharacterId.put(voiceLineId, fileName);
        voiceLineIds.add(voiceLineId);
      }
      Collections.shuffle(voiceLineIds, r);
      characterToDialog.put(fileName, voiceLineIds);
    }
  }
}
