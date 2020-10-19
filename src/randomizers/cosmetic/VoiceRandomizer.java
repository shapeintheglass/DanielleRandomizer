package randomizers.cosmetic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import randomizers.BaseRandomizer;
import settings.Settings;
import utils.FileConsts;

/**
 * 
 * Voice line randomizer.
 *
 */
public class VoiceRandomizer extends BaseRandomizer {

  private static final String DIALOG_ID_PATTERN = "dialog=\"([0-9]*)\"";
  private static final String VOICES_ID_PATTERN = "id=\"([0-9]*)\"";

  private static Map<String, String> DIALOG_TO_CHARACTER = new HashMap<String, String>();
  private static Map<String, List<String>> CHARACTER_TO_DIALOG = new HashMap<String, List<String>>();
  private static Map<String, String> SWAPPED_LINES_MAP = new HashMap<String, String>();

  public VoiceRandomizer(Settings s) {
    super(s);
  }

  /**
   * Randomizes voice lines and installs into temp directory
   */
  public void randomize() {
    Path outputDir = settings.getTempPatchDir()
                             .resolve("ark/dialog/dialoglogic");

    try {
      getDialogIds();
      randomizeAndWrite(new File(FileConsts.DIALOGIC_PATH), outputDir);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // Randomizes the given file and adds it into the given output dir
  private void randomizeAndWrite(File srcDir, Path outDir) throws FileNotFoundException, IOException {
    outDir.toFile()
          .mkdirs();
    for (File file : srcDir.listFiles()) {
      String fileName = file.getName();
      logger.info(String.format("Voice randomizer: parsing %s\n", fileName));
      if (file.isDirectory()) {
        // Call recursively
        randomizeAndWrite(file, outDir.resolve(fileName));
      } else {
        // Create and randomize new file
        randomizeDialog(file, outDir.resolve(fileName)
                                    .toFile());
      }
    }
  }

  // Writes a randomized version of the in file to the out file.
  private void randomizeDialog(File in, File out) throws FileNotFoundException, IOException {
    out.createNewFile();
    try (BufferedReader br = new BufferedReader(new FileReader(in));
        BufferedWriter bw = new BufferedWriter(new FileWriter(out))) {
      String line = br.readLine();
      while (line != null) {
        Matcher m = Pattern.compile(DIALOG_ID_PATTERN)
                           .matcher(line);
        if (m.find()) {
          // Replace with a random dialog ID and remove from list
          String oldDialog = m.group(1);

          // If this line has not already been swapped with a random line of
          // dialogue, add
          // it
          if (!SWAPPED_LINES_MAP.containsKey(oldDialog)) {
            String characterFile = DIALOG_TO_CHARACTER.get(oldDialog);
            List<String> dialoglines = CHARACTER_TO_DIALOG.get(characterFile);

            String newDialog = dialoglines.remove(0);
            // logger.info(String.format("Mapping %s --> %s, remaining IDs %s\n",
            // oldDialog, newDialog, dialoglines.size()));

            CHARACTER_TO_DIALOG.put(characterFile, dialoglines);
            SWAPPED_LINES_MAP.put(oldDialog, newDialog);
          }

          line = line.replaceFirst(DIALOG_ID_PATTERN, String.format("dialog=\"%s\"", SWAPPED_LINES_MAP.get(oldDialog)));
        }

        bw.append(line);
        bw.append('\n');

        line = br.readLine();
      }
    }
  }

  // Populates maps for dialog IDs.
  private void getDialogIds() throws FileNotFoundException, IOException {
    for (File file : new File(FileConsts.VOICES_PATH).listFiles()) {
      String fileName = file.getName();
      List<String> voiceLineIds = new ArrayList<>();
      try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line = br.readLine();
        while (line != null) {
          Matcher m = Pattern.compile(VOICES_ID_PATTERN)
                             .matcher(line);
          if (m.find()) {
            String voiceLineId = m.group(1);
            DIALOG_TO_CHARACTER.put(voiceLineId, fileName);
            voiceLineIds.add(voiceLineId);
          }
          line = br.readLine();
        }
      }
      Collections.shuffle(voiceLineIds, settings.getRandom());
      CHARACTER_TO_DIALOG.put(fileName, voiceLineIds);
    }
  }
}
