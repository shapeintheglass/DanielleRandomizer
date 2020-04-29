package main;

import randomizers.gameplay.LevelRandomizer;

public class Main {
  private static final String installDir = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Prey";

  public static void main(String[] args) {
    LevelRandomizer lr = new LevelRandomizer(installDir);
    lr.install();
  }
}
