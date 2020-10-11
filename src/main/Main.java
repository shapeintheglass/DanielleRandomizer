package main;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import filters.EnemyFilter;
import installers.Installer;
import randomizers.gameplay.LevelRandomizer;
import settings.EnemySettings;
import settings.EnemySettings.Preset;
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
            .setRandomizeMode(Preset.NO_LOGIC)
            .build())
        .build();

    Installer installer = new Installer(s);

    LevelRandomizer lr = new LevelRandomizer(s)
        .addFilter(new EnemyFilter(s));
    lr.randomize();
    
    try {
      installer.install();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
