package utils.validators;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Random;

import com.google.common.collect.Lists;

import gui2.RandomizerService;
import json.CosmeticSettingsJson;
import json.GameplaySettingsJson;
import json.GenericRuleJson;
import json.SettingsJson;
import json.SpawnPresetJson;
import utils.LevelConsts;

public class ConsistencyValidator {

  private static final Path OUTPUT_DIR = Paths.get("testoutput");
  public static final String PREFIX = "gamesdk/levels/campaign";
  private static final String INSTALL_LOCATION = "GameSDK\\Precache";
  
  private static void setUpFakeInstallDir(Path fakeInstallDir) {
    // Create precache dir
    fakeInstallDir.resolve(INSTALL_LOCATION).toFile().mkdirs();
    // Create campaign dir
    fakeInstallDir.resolve(PREFIX).toFile().mkdirs();
    
    for (String s : LevelConsts.LEVEL_DIRS) {
      fakeInstallDir.resolve(PREFIX).resolve(s).toFile().mkdirs();
    }
  }

  private static SettingsJson generateRandomSettings(long seed, String installDir) {
    Random r = new Random(seed);

    GenericRuleJson itemRule = new GenericRuleJson(Lists.newArrayList("Neuromod_Calibration", "_MIMICABLE",
        "_CARRYABLE", "_USABLE", "_CONSUMABLE"), Lists.newArrayList("Bourbon", "UsedCigar"), null, null, null);
    SpawnPresetJson itemPreset = new SpawnPresetJson("Whiskey and cigars", "Whiskey and cigars", Lists.newArrayList(
        itemRule));

    GenericRuleJson enemyRule = new GenericRuleJson(Lists.newArrayList("ArkHumans", "ArkNpcs", "ArkRobots"), Lists
        .newArrayList("Alex Karl", "Luka Golubkin"), null, null, null);
    SpawnPresetJson enemyPreset = new SpawnPresetJson("Alex and Luka", "Alex and Luka", Lists.newArrayList(enemyRule));

    CosmeticSettingsJson cosmeticSettingsJson = new CosmeticSettingsJson(r.nextBoolean(), r.nextBoolean(), r
        .nextBoolean(), r.nextBoolean());
    GameplaySettingsJson gameplaySettingsJson = new GameplaySettingsJson(r.nextBoolean(), r.nextBoolean(), r
        .nextBoolean(), r.nextBoolean(), r.nextBoolean(), r.nextBoolean(), r.nextBoolean(), r.nextBoolean(), r
            .nextBoolean(), r.nextBoolean(), r.nextBoolean(), r.nextBoolean(), r.nextBoolean(), r.nextBoolean(), r
                .nextBoolean(), Float.toString(r.nextFloat()), Float.toString(r.nextFloat()), enemyPreset, itemPreset);

    return new SettingsJson("Test", installDir, seed, cosmeticSettingsJson, gameplaySettingsJson);
  }
  
  private static void verifyDirsAreEqual(Path one, Path other) throws IOException {
    Files.walkFileTree(one, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult visitFile(Path file,
                BasicFileAttributes attrs)
                throws IOException {
            FileVisitResult result = super.visitFile(file, attrs);

            // get the relative file name from path "one"
            Path relativize = one.relativize(file);
            // construct the path for the counterpart file in "other"
            Path fileInOther = other.resolve(relativize);
            //System.out.printf("=== comparing: {%s} to {%s}\n", file, fileInOther);

            byte[] otherBytes = Files.readAllBytes(fileInOther);
            byte[] theseBytes = Files.readAllBytes(file);
            if (!Arrays.equals(otherBytes, theseBytes)) {
                System.out.println(file + " is not equal to " + fileInOther);
            }  
            return result;
        }
    });
}
  
  private static Path createFakeInstallDirAndInstall(long seed, Path installDir) {
    setUpFakeInstallDir(installDir);
    SettingsJson settings = generateRandomSettings(seed, installDir.toString());
    System.out.println(settings);
    RandomizerService service = new RandomizerService(null, settings);
    service.doInstall(false);
    return service.getTempDir();
  }

  public static void main(String[] args) {
    long seed = new Random().nextLong();
    Path p1 = OUTPUT_DIR.resolve("installDir1");
    Path p2 = OUTPUT_DIR.resolve("installDir2");
    Path tempDir1 = createFakeInstallDirAndInstall(seed, p1);
    Path tempDir2 = createFakeInstallDirAndInstall(seed, p2);
    try {
      verifyDirsAreEqual(tempDir1, tempDir2);
    } catch (IOException e) {
      System.out.println("dirs are equal!");
      e.printStackTrace();
    }
  }
}
