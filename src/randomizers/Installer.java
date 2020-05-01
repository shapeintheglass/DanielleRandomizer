package randomizers;

/**
 * 
 * Central place for installing all mod files
 *
 */
public class Installer {
  
  private static final String PATCH_NAME = "patch_randomizer.pak";
  
  // Prey install location
  private String installDir;
  // Temporary dir for holding patch files
  private String tempDirPatch;
  // Temporary dir for holding level files
  private String tempDirLevels;

  public Installer (String installDir, String tempDirPatch, String tempDirLevels) {
    this.installDir = installDir;
    this.tempDirPatch = tempDirPatch;
    this.tempDirLevels = tempDirLevels;
  }
  
  
  public void install() {
    
  }
  
  
  public void uninstall() {
    
  }
  
}
