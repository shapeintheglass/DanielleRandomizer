package randomizers.cosmetic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.protobuf.util.JsonFormat;
import proto.RandomizerSettings.Settings;
import proto.Voice.ActorVoice;
import proto.Voice.Dialog;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

/**
 * 
 * Voice line randomizer.
 *
 */
public class VoiceRandomizer extends BaseRandomizer {

  private static final ImmutableList<String> EMOTIONS = ImmutableList.of("15659330456346423977",
      "15659330456346423989", "15659330456346424000", "15659330456346424008", "3152439162748317787",
      "3152439162748317984", "3152439162748317994", "3152439162748318010", "3152439162748318026",
      "3152439162748318033", "3152439162748318046", "3152439162748318053", "3152439162748318060",
      "3152439162748318066", "3152439162748318076", "3152439162748318081", "3152439162748318087");

  private Map<String, String> dialogIdToCharacterId = new HashMap<String, String>();
  private Map<String, List<String>> characterToDialog = new HashMap<String, List<String>>();
  private Map<String, String> swappedLinesMap = new HashMap<String, String>();

  private boolean randomizeVoice;
  private boolean randomizeEmotion;

  private Path tempPatchDir;

  public VoiceRandomizer(Settings s, Path tempPatchDir, ZipHelper zipHelper) {
    super(s, zipHelper);
    this.tempPatchDir = tempPatchDir;
    this.randomizeVoice = s.getCosmeticSettings()
        .getRandomizeVoicelines();
    this.randomizeEmotion = s.getCosmeticSettings()
        .getRandomizeEmotions();
  }

  /**
   * Randomizes voice lines and installs into temp directory
   */
  public void randomize() {
    Path outputDir = tempPatchDir.resolve("ark/dialog/dialoglogic");

    try {
      if (randomizeVoice) {
        getDialogIds();
      }
      randomizeAndWrite(ZipHelper.DIALOGIC_PATH, outputDir);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

  public Map<String, String> getSwappedLinesMap() {
    return swappedLinesMap;
  }

  // Randomizes the given file and adds it into the given output dir
  private void randomizeAndWrite(String srcDir, Path outDir)
      throws FileNotFoundException, IOException, JDOMException {
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
  private void randomizeDialog(String in, String out)
      throws FileNotFoundException, IOException, JDOMException {
    Document document = zipHelper.getDocument(in);
    for (Element responseSet : document.getRootElement()
        .getChildren("ResponseSet")) {
      for (Element response : responseSet.getChildren("Response")) {
        if (randomizeVoice) {
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

        if (randomizeEmotion) {
          int emotionIndex = r.nextInt(EMOTIONS.size());
          response.setAttribute("emotion", EMOTIONS.get(emotionIndex));
        }
      }
    }

    zipHelper.copyToPatch(document, out);
  }

  // Populates maps for dialog IDs.
  private void getDialogIds() throws FileNotFoundException, IOException, JDOMException {
    InputStream is = zipHelper.getInputStream(ZipHelper.PREPROCESSED_VOICES_PATH);
    Dialog.Builder builder = Dialog.newBuilder();

    byte[] bytes = ByteStreams.toByteArray(is);

    JsonFormat.parser()
        .ignoringUnknownFields()
        .merge(new String(bytes), builder);

    Dialog dialog = builder.build();

    for (ActorVoice actorVoice : dialog.getActorVoiceLinesList()) {
      String actorId = actorVoice.getActorId();
      List<String> voiceLineIds = Lists.newArrayList();
      for (String dialogId : actorVoice.getVoiceLinesList()) {
        dialogIdToCharacterId.put(dialogId, actorId);
        voiceLineIds.add(dialogId);
      }
      Collections.shuffle(voiceLineIds, r);
      characterToDialog.put(actorId, voiceLineIds);
    }
  }
}
