package randomizers.generators;

import java.io.IOException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import proto.RandomizerSettings.Settings;
import utils.ZipHelper;

/*
 * Helper for generating the welcome note at the start of the game.
 */
public class WelcomeNoteHelper {
  public static final String WELCOME_NOTE_ID = "7193487293";  
  
  private static final String GREETING = "Thank you for trying the Prey Randomizer! For questions / bug reports, please join our discord server linked in the mod page.<br><br>";
  
  private static final String STATION_RANDO_LOADOUT_EXPLANATION = "Your starter equipment includes two EMPs. These are intended to break the large fan in GUTS in case that is the first room after the Neuromod Division.<br><br>";
  
  private static final String PREY_SOULS_HELP = "It looks like you enabled Prey For Death content. Please give all credit to coyote and try out the Prey For Death mod for the full experience!<br><br>";
  
  private static final String STATION_RANDOMIZER_HELP = "It looks like you enabled station or keycard randomization. Below is a guide on the new connectivity:<br><br>";
  
  private ZipHelper zipHelper;

  public WelcomeNoteHelper(ZipHelper zipHelper) {
    this.zipHelper = zipHelper;
  }

  public void installWelcomeNote(Settings settings, String stationConnectivity) {
    try {
      Document document = zipHelper.getDocument(ZipHelper.NOTES_XML);
      Element root = document.getRootElement();
      Element notes = root.getChild("Notes");
      
      StringBuilder content = new StringBuilder();
      content.append(GREETING);
      
      if (settings.getGameplaySettings().getRandomizeStation()) {
        content.append(STATION_RANDO_LOADOUT_EXPLANATION);
      }
      
      if (settings.getMoreSettings().getPreySoulsEnemies() || settings.getMoreSettings().getPreySoulsGuns() || settings.getMoreSettings().getPreySoulsTurrets()) {
        content.append(PREY_SOULS_HELP);
      }
      
      if (settings.getGameplaySettings().getRandomizeStation() || settings.getGameplaySettings().getRandomizeKeycards()) {
        content.append(STATION_RANDOMIZER_HELP);
        content.append(stationConnectivity);
      }
      
      Note b = new Note("RandomizerConnectivity", "Welcome to the Prey Randomizer",
          content.toString(), WelcomeNoteHelper.WELCOME_NOTE_ID);

      Element newNote= new Element("ArkNote")
          .setAttribute("ID", b.getUniqueId())
          .setAttribute("Name", b.getName())
          .setAttribute("Subject", b.getSubject())
          .setAttribute("Content", b.getContent());
      notes.addContent(newNote);
    
      zipHelper.copyToPatch(document, ZipHelper.NOTES_XML);
    } catch (IOException | JDOMException e) {
      e.printStackTrace();
    }
  }

  public static class Note {
    private String name;
    private String subject;
    private String content;
    private String uniqueId;

    public Note(String name, String subject, String content, String uniqueId) {
      this.name = name;
      this.subject = subject;
      this.content = content;
      this.uniqueId = uniqueId;
    }

    public String getName() {
      return name;
    }

    public String getSubject() {
      return subject;
    }

    public String getContent() {
      return content;
    }
    
    public String getUniqueId() {
      return uniqueId;
    }
  }
}
