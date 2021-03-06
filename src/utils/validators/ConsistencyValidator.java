package utils.validators;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.protobuf.util.JsonFormat;

import gui2.Gui2Consts;
import gui2.RandomizerService;
import proto.RandomizerSettings.AllPresets;
import proto.RandomizerSettings.CheatSettings;
import proto.RandomizerSettings.CosmeticSettings;
import proto.RandomizerSettings.GameStartSettings;
import proto.RandomizerSettings.GenericSpawnPresetFilter;
import proto.RandomizerSettings.ItemSettings;
import proto.RandomizerSettings.NeuromodSettings;
import proto.RandomizerSettings.NpcSettings;
import proto.RandomizerSettings.Settings;
import proto.RandomizerSettings.StoryProgressionSettings;
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

  private static Settings generateRandomSettings(long seed, String installDir) throws IOException {
    FileReader fr = new FileReader(Gui2Consts.ALL_PRESETS_FILE);
    AllPresets.Builder builder = AllPresets.newBuilder();
    JsonFormat.parser().ignoringUnknownFields().merge(fr, builder);
    AllPresets allPresets = builder.build();

    Random r = new Random(seed);

    CosmeticSettings cosmeticSettings = CosmeticSettings.newBuilder()
        .setRandomizeBodies(r.nextBoolean())
        .setRandomizeMusic(r.nextBoolean())
        .setRandomizePlayerModel(r.nextBoolean())
        .setRandomizeVoicelines(r.nextBoolean())
        .build();

    List<GenericSpawnPresetFilter> enemySettingsList = allPresets.getEnemySpawnSettingsList();
    GenericSpawnPresetFilter enemyFilter = enemySettingsList.get(r.nextInt(enemySettingsList.size()));

    List<GenericSpawnPresetFilter> itemSettingsList = allPresets.getItemSpawnSettingsList();
    GenericSpawnPresetFilter itemFilter = itemSettingsList.get(r.nextInt(itemSettingsList.size()));

    ItemSettings itemSettings = ItemSettings.newBuilder()
        .setMoreGuns(r.nextBoolean())
        .setRandomizeFabPlanCosts(r.nextBoolean())
        .setItemSpawnSettings(itemFilter)
        .build();
    NpcSettings npcSettings = NpcSettings.newBuilder()
        .setEnemySpawnSettings(enemyFilter)
        .build();
    NeuromodSettings neuromodSettings = NeuromodSettings.newBuilder().setRandomizeNeuromods(r.nextBoolean()).build();
    StoryProgressionSettings storySettings = StoryProgressionSettings.newBuilder()
        .setRandomizeStation(r.nextBoolean())
        .build();
    GameStartSettings startSettings = GameStartSettings.newBuilder()
        .setAddLootToApartment(r.nextBoolean())
        .setSkipJovanCutscene(r.nextBoolean())
        .setStartOnSecondDay(r.nextBoolean())
        .build();
    CheatSettings cheatSettings = CheatSettings.newBuilder()
        .setEnableGravityInExtAndGuts(r.nextBoolean())
        .setOpenStation(r.nextBoolean())
        .setStartSelfDestruct(r.nextBoolean())
        .setUnlockAllScans(r.nextBoolean())
        .setWanderingHumans(r.nextBoolean())
        .setZeroGravityEverywhere(r.nextBoolean())
        .setSelfDestructTimer(Float.toString(r.nextFloat()))
        .setSelfDestructShuttleTimer(Float.toString(r.nextFloat()))
        .build();

    return Settings.newBuilder()
        .setSeed(Long.toString(seed))
        .setInstallDir(installDir)
        .setReleaseVersion("Test")
        .setCosmeticSettings(cosmeticSettings)
        .setItemSettings(itemSettings)
        .setNpcSettings(npcSettings)
        .setNeuromodSettings(neuromodSettings)
        .setStoryProgressionSettings(storySettings)
        .setGameStartSettings(startSettings)
        .setCheatSettings(cheatSettings)
        .build();
  }

  private static void verifyDirsAreEqual(Path one, Path other) throws IOException {
    Files.walkFileTree(one, new SimpleFileVisitor<Path>() {
      @Override
      public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        FileVisitResult result = super.visitFile(file, attrs);

        // get the relative file name from path "one"
        Path relativize = one.relativize(file);
        // construct the path for the counterpart file in "other"
        Path fileInOther = other.resolve(relativize);
        // System.out.printf("=== comparing: {%s} to {%s}\n", file, fileInOther);

        byte[] otherBytes = Files.readAllBytes(fileInOther);
        byte[] theseBytes = Files.readAllBytes(file);
        if (!Arrays.equals(otherBytes, theseBytes)) {
          System.out.println(file + " is not equal to " + fileInOther);
        }
        return result;
      }
    });
  }

  private static Path createFakeInstallDirAndInstall(long seed, Path installDir, int numInstalls) throws IOException {
    setUpFakeInstallDir(installDir);
    Settings settings = generateRandomSettings(seed, installDir.toString());
    System.out.println(settings);
    Path tempDir = null;
    for (int i = 0; i < numInstalls; i++) {
      RandomizerService service = new RandomizerService(null, settings);
      service.doInstall(false);
      tempDir = service.getTempDir();
    }
    return tempDir;
  }

  public static void main(String[] args) {
    long seed = new Random().nextLong();
    Path p1 = OUTPUT_DIR.resolve("installDir1");
    Path p2 = OUTPUT_DIR.resolve("installDir2");

    try {
      Path tempDir1 = createFakeInstallDirAndInstall(seed, p1, 2);
      Path tempDir2 = createFakeInstallDirAndInstall(seed, p2, 1);
      verifyDirsAreEqual(tempDir1, tempDir2);
    } catch (IOException e) {
      System.out.println("dirs are equal!");
      e.printStackTrace();
    }
  }
}
