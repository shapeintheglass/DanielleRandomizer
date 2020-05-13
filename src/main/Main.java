package main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

import randomizers.Installer;
import randomizers.gameplay.LootTableRandomizer;

public class Main {
  
  private static final String installDir = "C:\\Users\\cross_000\\Desktop\\temp";
  //private static final String installDir = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  public static void main(String[] args) {
    Random r = new Random();
    long seed = r.nextLong();
    r.setSeed(seed);
    System.out.println(seed);

    // Create a temp dir for level files
    Path tempLevelDir = createTempDir("level", r);
    Path tempPatchDir = createTempDir("patch", r);

    Installer installer = new Installer(Paths.get(installDir), tempPatchDir,
        tempLevelDir);



    LootTableRandomizer loot = new LootTableRandomizer(installDir);
    loot.randomize();
    
    installer.install();
    
    
  }

  private static Path createTempDir(String name, Random r) {
    // Create a temp dir for patch files
    Path tempDir = Paths.get(String.format("temp_%8d_%s",
        r.nextInt(Integer.MAX_VALUE), name));

    tempDir.toFile().mkdirs();

    return tempDir;
  }
}
