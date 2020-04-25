package randomizers.cosmetic;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import randomizers.BaseRandomizer;

public class BodyRandomizer extends BaseRandomizer {

  private static final String HUMANS_FINAL_DIR = "humansfinal\\";
  private static final String PATCH_NAME_ZIP = "patch_randombodies.zip";
  private static final String ZIP_FILE_NAME_PATTERN = "objects\\characters\\humansfinal\\%s";

  public BodyRandomizer(String installDir) {
    super(installDir, "randombodies");
  }

  public void install() {
    // Create a zip file to put patch changes into.
    File zipFile = tempDirPath.resolve(PATCH_NAME_ZIP).toFile();
    // zipFile.deleteOnExit();

    try {
      zipFile.createNewFile();
    } catch (Exception e) {
      e.printStackTrace();
    }

    try (ZipOutputStream zos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {
      // TODO: Modify player character model to match Morgan.
      for (File f : new File(HUMANS_FINAL_DIR).listFiles()) {
        randomizeBody(f, String.format(ZIP_FILE_NAME_PATTERN, f.getName()), zos);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    // Copy the generated zip to the given destination
    try {
      Files.copy(zipFile.toPath(), outPatchDirPath.resolve(patchName), StandardCopyOption.REPLACE_EXISTING);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private BodyConfig randomizeBody(File bodyCdf, String filename, ZipOutputStream zos)
      throws FileNotFoundException, IOException {
    BodyConfig bc = new BodyConfig();
    logger.info("Randomizing " + bodyCdf.getName());
    StringBuilder buffer = new StringBuilder();
    
    boolean keepHair = false;

    try (BufferedReader r = new BufferedReader(new FileReader(bodyCdf))) {
      String line = r.readLine();
      while (line != null) {
        String lowerCase = line.toLowerCase();

        // Look for the first line of the character def, which decides whether this is a
        // male or female body
        if (lowerCase.contains(BodyConfig.FEMALE_BODY_TYPE)) {
          bc.setGender(BodyConfig.Gender.FEMALE);
        } else if(lowerCase.contains(BodyConfig.MALE_BODY_TYPE)) {
          bc.setGender(BodyConfig.Gender.MALE);
        } else if (lowerCase.contains(BodyConfig.LARGE_MALE_BODY_TYPE)) {
          logger.info("Skipping because this body type is not supported yet");
          return null;
        }

        if (line.contains("CA_SKIN")) {
          if (lowerCase.contains("body_skin") || lowerCase.contains("aname=\"body\"")) {
            line = bc.generateRandomBody();
            logger.info("Chose body type " + bc.bodyModel);
          } else if (lowerCase.contains("head_skin")) {
            if (bc.generateRandomHeadAndHairForBody(line)) {
              line = bc.getHeadAndHair();
            } else {
              keepHair = true;
            }
            logger.info("Chose head type " + bc.headModel + ", hair type " + bc.hairModel + ", and hair color " + bc.hairColor);
          } else if (lowerCase.contains("hair_skin")) {
            if (!keepHair) {
              line = "";
            }
          }
          else {
            // If the type is not accounted for here, remove it
            line = "";
            logger.info("Ignoring line: " + line);
          }
        }

        buffer.append(line);
        if (!line.equals("")) {
          buffer.append('\n');
        }
        line = r.readLine();
      }
    }
    byte[] bytes = buffer.toString().getBytes();
    zos.putNextEntry(new ZipEntry(filename));
    zos.write(bytes, 0, bytes.length);
    zos.closeEntry();
    return bc;
  }

}
