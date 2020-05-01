package randomizers.cosmetic;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import randomizers.BaseRandomizer;
import utils.FileConsts;

/**
 * 
 * Voice line randomizer.
 *
 */
public class VoiceRandomizer extends BaseRandomizer {

  private static final String DIALOG_ID_PATTERN = "dialog=\"([0-9]*)\"";
  private static final String VOICES_ID_PATTERN = "id=\"([0-9]*)\"";

  private static final String PATCH_NAME_ZIP = "patch_randomvoicelines.zip";

  private static Map<String, String> DIALOG_TO_CHARACTER = new HashMap<String, String>();
  private static Map<String, List<String>> CHARACTER_TO_DIALOG = new HashMap<String, List<String>>();
  private static Map<String, String> SWAPPED_LINES_MAP = new HashMap<String, String>();

  public VoiceRandomizer(String installDir) {
    super(installDir, "randomvoicelines");
  }

  /**
   * Randomizes voice lines and installs into game directory
   */
  public void randomize() {
    // Zip file containing patch changes
    File zipFile = tempDirPath.resolve(PATCH_NAME_ZIP).toFile();
    zipFile.deleteOnExit();
    try {
      zipFile.createNewFile();
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }

    try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(
        new FileOutputStream(zipFile)))) {
      getDialogIds();
      randomizeAndAddToZip(new File(FileConsts.DIALOGIC_PATH),
          Paths.get("ark/dialog/dialoglogic"), zos);
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Copy the generated zip to the given destination
    try {
      Files.copy(zipFile.toPath(), outPatchDirPath.resolve(patchName),
          StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // Randomizes the given file and adds it into the given output stream
  private void randomizeAndAddToZip(File dir, Path parent, ZipOutputStream zos)
      throws FileNotFoundException, IOException {
    for (File file : dir.listFiles()) {
      String fileName = file.getName();
      System.out.printf("Parsing %s\n", fileName);
      if (file.isDirectory()) {
        // Call recursively
        randomizeAndAddToZip(file, parent.resolve(fileName), zos);
      } else {
        // Create and randomize new file
        randomizeDialog(file, parent.resolve(fileName).toString(), zos);
      }
    }
  }

  // Writes a randomized version of the in file to the out file.
  private void randomizeDialog(File in, String filename, ZipOutputStream zos)
      throws FileNotFoundException, IOException {
    StringBuilder buffer = new StringBuilder();
    try (BufferedReader r = new BufferedReader(new FileReader(in));) {
      String line = r.readLine();
      while (line != null) {
        Matcher m = Pattern.compile(DIALOG_ID_PATTERN).matcher(line);
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
            System.out.printf("Mapping %s --> %s, remaining IDs %s\n",
                oldDialog, newDialog, dialoglines.size());

            CHARACTER_TO_DIALOG.put(characterFile, dialoglines);
            SWAPPED_LINES_MAP.put(oldDialog, newDialog);
          }

          line = line.replaceFirst(DIALOG_ID_PATTERN,
              String.format("dialog=\"%s\"", SWAPPED_LINES_MAP.get(oldDialog)));
        }
        buffer.append(line);
        buffer.append('\n');
        line = r.readLine();
      }
    }
    byte[] bytes = buffer.toString().getBytes();
    zos.putNextEntry(new ZipEntry(filename));
    zos.write(bytes, 0, bytes.length);
    zos.closeEntry();
  }

  // Populates maps for dialog IDs.
  private void getDialogIds() throws FileNotFoundException, IOException {
    for (File file : new File(FileConsts.VOICES_PATH).listFiles()) {
      String fileName = file.getName();
      List<String> voiceLineIds = new ArrayList<>();
      try (BufferedReader r = new BufferedReader(new FileReader(file))) {
        String line = r.readLine();
        while (line != null) {
          Matcher m = Pattern.compile(VOICES_ID_PATTERN).matcher(line);
          if (m.find()) {
            String voiceLineId = m.group(1);
            DIALOG_TO_CHARACTER.put(voiceLineId, fileName);
            voiceLineIds.add(voiceLineId);
          }
          line = r.readLine();
        }
      }
      Collections.shuffle(voiceLineIds);
      CHARACTER_TO_DIALOG.put(fileName, voiceLineIds);
    }
  }
}
