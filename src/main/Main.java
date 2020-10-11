package main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import filters.EnemyFilter;
import filters.ItemSpawnFilter;
import installers.Installer;
import randomizers.cosmetic.BodyRandomizer;
import randomizers.cosmetic.VoiceRandomizer;
import randomizers.gameplay.LevelRandomizer;
import settings.EnemySettings;
import settings.ItemSpawnSettings;
import settings.Settings;

public class Main {
  
  //private static final String installDir = "C:\\Users\\cross_000\\Desktop\\temp";
  private static final String installDir = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  public static void main(String[] args) {
    
    Path tempDir = Paths.get(".");
    
    Settings s = new Settings.Builder()
        .setInstallDir(Paths.get(installDir))
        .setTempDir(tempDir)
        .setEnemySettings(new EnemySettings.Builder()
            .setRandomizeMode(EnemySettings.Preset.ALL_MIMICS)
            .build())
        .setItemSpawnSettings(new ItemSpawnSettings.Builder()
            .setRandomizeMode(ItemSpawnSettings.Preset.WHISKEY_AND_CIGARS)
            .build())
        .build();

    Installer installer = new Installer(s);

    VoiceRandomizer vr = new VoiceRandomizer(s);
    vr.randomize();
    
    BodyRandomizer br = new BodyRandomizer(s);
    br.randomize();
    
    LevelRandomizer lr = new LevelRandomizer(s)
        .addFilter(new EnemyFilter(s))
        .addFilter(new ItemSpawnFilter(s));
    lr.randomize();
    
    try {
      installer.install();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
