package randomizers.cosmetic;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;

import com.google.common.collect.Lists;

import json.SettingsJson;
import randomizers.BaseRandomizer;
import utils.ZipHelper;

public class MusicRandomizer extends BaseRandomizer {
  private static final String OUT = "libs/gameaudio/music.xml";

  public MusicRandomizer(SettingsJson s, ZipHelper zipHelper) {
    super(s, zipHelper);
  }

  @Override
  public void randomize() {
    try {
      Document document = zipHelper.getDocument(ZipHelper.MUSIC_XML);
      Element root = document.getRootElement();

      List<String> validNames = Lists.newArrayList();
      for (Element atlTrigger : root.getChild("AudioTriggers").getChildren()) {
        Element wwiseTrigger = atlTrigger.getChild("WwiseEvent");
        if (wwiseTrigger == null) {
          continue;
        }
        String wwiseName = wwiseTrigger.getAttributeValue("wwise_name");
        if (wwiseName != null && wwiseName.startsWith("Set_State_MX_")) {
          validNames.add(wwiseName);
        }
      }
      Collections.shuffle(validNames, r);
      
      int index = 0;
      for (Element atlTrigger : root.getChild("AudioTriggers").getChildren()) {
        Element wwiseTrigger = atlTrigger.getChild("WwiseEvent");
        if (wwiseTrigger == null) {
          continue;
        }
        String wwiseName = wwiseTrigger.getAttributeValue("wwise_name");
        if (wwiseName != null && wwiseName.startsWith("Set_State_MX_")) {
          wwiseTrigger.setAttribute("wwise_name", validNames.get(index++));
        }
      }

      zipHelper.copyToPatch(document, OUT);
    } catch (JDOMException | IOException e) {
      e.printStackTrace();
    }
  }
}
