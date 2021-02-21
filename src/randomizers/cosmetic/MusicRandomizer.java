package randomizers.cosmetic;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;

import proto.RandomizerSettings.Settings;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

public class MusicRandomizer extends BaseRandomizer {
  private static final String OUT = "libs/gameaudio/music.xml";

  private static final ImmutableList<String> MUSIC_STATES = new ImmutableList.Builder<String>().add("Zero_G",
      "Isolation", "Experiment", "Darkness", "Mimic_AttackPrisoner", "Combat_StateBased", "Dark_Science",
      "Space_Stranded", "Space_Explore_Variant", "Air_Low", "Air_Tense", "Cold", "Death", "EndGame", "Explore_Western",
      "Ghost", "Hacking", "IGC", "Intro_Music", "MainTheme", "Nightmare", "Poltergeist", "Progression", "Psychosis",
      "Psychosis_Theme", "Safety", "Search", "Silence", "Space_Explore_Variant", "Hunt", "Peace", "Go", "Unsettled",
      "Isolation_2", "Explore_Science", "Explore_Moody", "Science_Tense", "Anxiety", "Heli_Intro", "Heli_Ride", "Calm",
      "Sneak", "Hardware_ContainMimic", "SimLabs_Opening", "CrewFacilities_Memories", "DeepStorage", "GUTS",
      "Lobby_Intro", "Lobby_PostVideo", "Psychotronics_Memories", "Psychotronics_Psychoscope_Theme",
      "SimLabs_Day2_WakeUp", "SimLabs_EnterHallway", "SimLabs_PostWrench", "Arboretum_Apex_Arrival",
      "SimLabs_Heli_Main", "SimLabs_Heli_Intro").build();

  private Random r;

  public MusicRandomizer(Settings s, ZipHelper zipHelper) {
    super(s, zipHelper);
    r = new Random(Long.parseLong(s.getSeed()));
  }

  @Override
  public void randomize() {
    try {
      Document document = zipHelper.getDocument(ZipHelper.MUSIC_XML);
      Element root = document.getRootElement();

      ImmutableSet<String> musicSet = new ImmutableSet.Builder<String>().addAll(MUSIC_STATES).build();
      List<String> musicList = Lists.newArrayList(MUSIC_STATES);
      Collections.shuffle(musicList, r);

      int musicIndex = 0;
      for (Element atlSwitch : root.getChild("AudioSwitches").getChildren()) {
        for (Element switchState : atlSwitch.getChildren()) {
          Element wwiseState = switchState.getChild("WwiseState");
          if (wwiseState == null) {
            continue;
          }
          Element wwiseValue = wwiseState.getChild("WwiseValue");
          if (wwiseValue != null && musicSet.contains(wwiseValue.getAttributeValue("wwise_name"))) {
            wwiseValue.setAttribute("wwise_name", musicList.get(musicIndex++));
          }
        }
      }

      zipHelper.copyToPatch(document, OUT);
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }
  }
}