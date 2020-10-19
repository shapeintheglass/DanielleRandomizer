package randomizers.cosmetic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import randomizers.BaseRandomizer;
import settings.Settings;
import utils.BodyConfig;
import utils.BodyConsts;
import utils.FileConsts;

public class BodyRandomizer extends BaseRandomizer {

  public BodyRandomizer(Settings s) {
    super(s);
  }

  private static final String HUMANS_FINAL_OUT = "objects\\\\characters\\\\humansfinal";
  private static final String HUMANS_FILE_TEMPLATE = "objects\\characters\\humansfinal\\%s";
  
  public void randomize() {
    for (File f : new File(FileConsts.HUMANS_FINAL_DIR).listFiles()) {
      Path out = settings.getTempPatchDir()
          .resolve(String.format(HUMANS_FILE_TEMPLATE, f.getName()));
      try {
        settings.getTempPatchDir().resolve(HUMANS_FINAL_OUT).toFile().mkdirs();
        out.toFile().createNewFile();
        randomizeBody(f, out.toFile());
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return;
      }
    }
  }

  private void randomizeBody(File in, File out) throws FileNotFoundException, IOException {
    BodyConfig bc = new BodyConfig(r);
    logger.info("Body randomizer: Parsing " + in.getName());

    boolean keepHair = false;

    try (BufferedReader r = new BufferedReader(new FileReader(in));
        BufferedWriter w = new BufferedWriter(new FileWriter(out))) {
      String line = r.readLine();
      while (line != null) {
        String lowerCase = line.toLowerCase();

        // Look for the first line of the character def, which decides whether
        // this is a
        // male or female body
        if (lowerCase.contains(BodyConsts.FEMALE_BODY_TYPE)) {
          bc.setGender(BodyConfig.Gender.FEMALE);
        } else if (lowerCase.contains(BodyConsts.MALE_BODY_TYPE)) {
          bc.setGender(BodyConfig.Gender.MALE);
        } else if (lowerCase.contains(BodyConsts.LARGE_MALE_BODY_TYPE)) {
          //logger.info("Skipping because this body type is not supported yet");
        }

        if (line.contains("CA_SKIN")) {
          if (lowerCase.contains("body_skin")
              || lowerCase.contains("aname=\"body\"")) {
            line = bc.generateRandomBody();
            //logger.info("Chose body type " + bc.bodyModel);
          } else if (lowerCase.contains("head_skin")) {
            if (bc.generateRandomHeadAndHairForBody(line)) {
              line = bc.getHeadAndHair();
            } else {
              keepHair = true;
            }
            //logger.info("Chose head type " + bc.headModel + ", hair type "
            //    + bc.hairModel + ", and hair color " + bc.hairColor);
          } else if (lowerCase.contains("hair_skin")) {
            if (!keepHair) {
              line = "";
            }
          } else {
            // If the type is not accounted for here, remove it
            line = "";
            //logger.info("Ignoring line: " + line);
          }
        }

        w.append(line);
        if (!line.equals("")) {
          w.append('\n');
        }
        line = r.readLine();
      }
    }
  }

}
